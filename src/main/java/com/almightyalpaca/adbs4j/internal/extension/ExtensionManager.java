package com.almightyalpaca.adbs4j.internal.extension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.security.auth.login.LoginException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;

import com.google.common.collect.Sets;

import com.almightyalpaca.adbs4j.ExitCode;
import com.almightyalpaca.adbs4j.events.manager.EventManager;
import com.almightyalpaca.adbs4j.events.plugins.PluginLoadedEvent;
import com.almightyalpaca.adbs4j.events.plugins.PluginUnloadedEvent;
import com.almightyalpaca.adbs4j.exception.PluginException;
import com.almightyalpaca.adbs4j.internal.BotPermissionManager;
import com.almightyalpaca.adbs4j.plugins.Plugin;
import com.almightyalpaca.adbs4j.plugins.PluginInfo;
import com.almightyalpaca.adbs4j.storage.StorageProviderInstance;
import com.almightyalpaca.adbs4j.storage.redis.RedisStorageProvider;
import com.almightyalpaca.adbs4j.util.GCUtil;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

public class ExtensionManager {

	final Set<PluginExtension>		plugins;

	final JDA						api;

	final CommandExtensionManager	commandManager;

	final EventManager				eventManager;

	final File						cacheDir;
	final File						rootDir;
	final File						pluginDir;

	final RedisStorageProvider		storageProvider;

	final BotPermissionManager		botPermissionManager;

	private final ExitCode			previousExitCode;

	private final AtomicBoolean		shutdown;

	public ExtensionManager() throws Exception {
		try {
			this.shutdown = new AtomicBoolean(false);

			this.previousExitCode = ExitCode.get(Integer.parseInt(System.getProperty("bot.previousExitCode")));

			this.rootDir = new File(System.getProperty("user.dir"));
			this.rootDir.mkdirs();

			this.plugins = Sets.newConcurrentHashSet();

			this.cacheDir = new File(System.getProperty("bot.cachedir"));
			this.cacheDir.mkdirs();

			this.pluginDir = new File(System.getProperty("bot.plugindir"));
			this.pluginDir.mkdirs();

			final File configFile = new File(System.getProperty("bot.configfile"));
			if (!configFile.exists()) {
				final JSONObject object = new JSONObject().put("redis", new JSONObject().put("hostname", "localhost").put("port", 0000).put("password", RandomStringUtils.random(32))).put("sharding",
						new JSONObject().put("id", 0).put("num", 0));
				Files.write(configFile.toPath(), object.toString(4).getBytes("UTF-8"), StandardOpenOption.CREATE_NEW);
				this.shutdown(ExitCode.SHUTDOWN);
				throw new Exception("Config file not found! Created a template file.");
			}

			final JSONObject config = new JSONObject(new String(Files.readAllBytes(configFile.toPath()), "UTF-8"));
			final JSONObject redis = config.getJSONObject("redis");
			this.storageProvider = new RedisStorageProvider(redis.getString("hostname"), redis.getInt("port"), redis.getString("password"));

			this.eventManager = new EventManager(this);
			this.eventManager.register(this.storageProvider);

			this.commandManager = new CommandExtensionManager(this);
			this.eventManager.register(this.commandManager);

			final JDABuilder builder = new JDABuilder().setEventManager(this.eventManager);

			builder.setBulkDeleteSplittingEnabled(false);

			final JSONObject sharding = config.getJSONObject("sharding");

			if (sharding.getInt("num") > 1) {
				builder.useSharding(sharding.getInt("id"), sharding.getInt("num"));
			}

			final Map<String, String> discordConfig = this.getGlobalStorageProvider().getStrings("discord.token", "proxy");

			final String token = discordConfig.get("discord.token");
			if (token == null) {
				this.getGlobalStorageProvider().putString("discord.token", "The bot's token");
				throw new LoginException("Token not found.");
			}
			builder.setBotToken(token);

			final String proxyName = discordConfig.get("global.proxy.host");
			if (proxyName != null) {
				try {
					final String proxyAdress = proxyName.substring(0, proxyName.indexOf(':'));
					final int proxyPort = Integer.parseInt(proxyName.substring(proxyName.indexOf(':'), proxyName.length()));
					builder.setProxy(proxyAdress, proxyPort);
				} catch (final Exception e) {
					System.err.println("Invalid proxy!");
				}
			}
			this.api = builder.buildBlocking();

			this.botPermissionManager = new BotPermissionManager(this);

			this.loadPlugins();

		} catch (final Exception e) {
			this.shutdown(ExitCode.ERROR_DO_NOT_RESTART);
			throw e;
		}
	}

	public final File getCacheDir() {
		return this.cacheDir;
	}

	public StorageProviderInstance getGlobalStorageProvider() {
		return this.storageProvider.getGlobalInstance();
	}

	public JDA getJDA() {
		return this.api;
	}

	public final File getPluginDir() {
		return this.pluginDir;
	}

	public final Set<PluginExtension> getPluginExtensions() {
		return Collections.unmodifiableSet(this.plugins);
	}

	public final Set<PluginInfo> getPluginInfos() {
		return this.plugins.stream().map(p -> p.plugin.getPluginInfo()).collect(Collectors.toSet());
	}

	public final Set<Plugin> getPlugins() {
		return this.plugins.stream().map(p -> p.plugin).collect(Collectors.toSet());
	}

	public StorageProviderInstance getPluginStorageProvider(final Plugin plugin) {
		return this.storageProvider.getPluginInstance(plugin.getPluginInfo());
	}

	public ExitCode getPreviousExitCode() {
		return this.previousExitCode;
	}

	public BotPermissionManager getRequirmentManager() {
		return this.botPermissionManager;
	}

	public final File getRootDir() {
		return this.rootDir;
	}

	public boolean isLoaded(final PluginInfo info) {
		for (final PluginExtension plugin : this.plugins) {
			if (info.equals(plugin.plugin.getPluginInfo())) {
				return true;
			}
		}
		return false;
	}

	public void loadPlugin(final String name) {
		PluginExtension extension = null;
		try {
			extension = new PluginExtension(this, name);
			if (!this.isLoaded(extension.plugin.getPluginInfo())) {
				extension.load();
				this.plugins.add(extension);
				new PluginLoadedEvent(this, extension.getPluginInfo()).fire();
			} else {
				GCUtil.runFor(5);
			}
		} catch (IOException | PluginException e) {
			if (extension != null) {
				this.plugins.remove(extension);
			}
			e.printStackTrace();
		}
	}

	public void loadPlugins() {
		Arrays.stream(this.pluginDir.listFiles()).map(f -> f.isDirectory() ? f.getName() : FilenameUtils.getBaseName(f.getName())).distinct().forEach(s -> this.loadPlugin(s));
	}

	public void shutdown(final ExitCode code) {
		if (!this.shutdown.getAndSet(true)) {
			new Thread(() -> {
				if (this.plugins != null) {
					ExtensionManager.this.unloadPlugins();
				}
				if (ExtensionManager.this.commandManager != null) {
					ExtensionManager.this.commandManager.shutdown();
				}
				if (ExtensionManager.this.api != null) {
					ExtensionManager.this.api.shutdown();
				}
				if (ExtensionManager.this.eventManager != null) {
					ExtensionManager.this.eventManager.shutdown();
				}
				if (ExtensionManager.this.storageProvider != null) {
					ExtensionManager.this.storageProvider.shutdown();
				}

				final long time = System.currentTimeMillis();

				for (final Thread thread : Thread.getAllStackTraces().keySet()) {
					if (!thread.isDaemon() && thread != Thread.currentThread() && !thread.getName().equals("DestroyJavaVM")) {
						final long joinTime = time + 10000 - System.currentTimeMillis();
						if (joinTime > 0) {
							try {
								thread.join(joinTime);
							} catch (final InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}

				for (final Thread thread : Thread.getAllStackTraces().keySet()) {
					if (!thread.isDaemon() && thread != Thread.currentThread() && !thread.getName().equals("DestroyJavaVM")) {
						System.out.println("Thread \"" + thread.getName() + "\" is still running.");
						System.out.println("Terminating now.");
					}
				}

				System.exit(code.getCode());

			}, "Shutdown Thread").start();
		}
	}

	public void unloadPlugin(final Class<? extends Plugin> clazz) {
		this.unloadPlugins(o -> o.plugin.getClass().equals(clazz));
	}

	public void unloadPlugin(final PluginExtension pluginExtension) {
		pluginExtension.unload();
		this.plugins.remove(pluginExtension);
		GCUtil.runFor(5);
		new PluginUnloadedEvent(this, pluginExtension.getPluginInfo()).fire();
	}

	public void unloadPlugins() {
		this.unloadPlugins(i -> true);
	}

	public void unloadPlugins(final Predicate<PluginExtension> selector) {
		for (final PluginExtension plugin : this.plugins) {
			if (selector.test(plugin)) {
				this.unloadPlugin(plugin);
			}
		}
	}

}
