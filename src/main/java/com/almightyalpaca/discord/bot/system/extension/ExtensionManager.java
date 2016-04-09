package com.almightyalpaca.discord.bot.system.extension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.LoginException;

import com.almightyalpaca.discord.bot.system.config.Config;
import com.almightyalpaca.discord.bot.system.config.ConfigFactory;
import com.almightyalpaca.discord.bot.system.config.exception.ConfigSaveException;
import com.almightyalpaca.discord.bot.system.config.exception.KeyNotFoundException;
import com.almightyalpaca.discord.bot.system.config.exception.WrongTypeException;
import com.almightyalpaca.discord.bot.system.events.manager.EventManager;
import com.almightyalpaca.discord.bot.system.exception.PluginException;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;
import com.almightyalpaca.discord.bot.system.plugins.PluginInfo;
import com.almightyalpaca.discord.bot.system.plugins.PluginSelector;
import com.almightyalpaca.discord.bot.system.util.GCUtil;
import com.almightyalpaca.discord.bot.system.util.URLUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilderSettings;

public class ExtensionManager {

	final Set<PluginExtension> plugins;

	final Config	rootConfig;
	final JDA		api;

	final CommandExtensionManager commandManager;

	final File configFolder;

	final EventManager eventManager;

	final File cacheFolder;

	public ExtensionManager()
		throws JsonIOException, JsonSyntaxException, WrongTypeException, KeyNotFoundException, FileNotFoundException, IOException, LoginException, IllegalArgumentException, InterruptedException {
		this.plugins = new HashSet<>();

		this.cacheFolder = new File("cache");
		this.cacheFolder.mkdirs();

		this.configFolder = new File("config");
		this.configFolder.mkdirs();

		this.rootConfig = ConfigFactory.getConfig(new File(this.configFolder, "config.json"));

		MessageBuilderSettings.setNotehubPassword(this.rootConfig.getString("messages.upload.notehub.password", "Discord Bot"));

		this.commandManager = new CommandExtensionManager(this);

		this.eventManager = new EventManager(this);
		this.eventManager.register(this.commandManager);

		final JDABuilder builder = new JDABuilder(this.rootConfig.getString("shared.discord.email", "EMAIL"), this.rootConfig.getString("shared.discord.password", "PASSWORD"))
			.setEventManager(this.eventManager);

		final String proxyAdress = this.rootConfig.getString("proxy.host", "");
		final int proxyPort = this.rootConfig.getInt("proxy.port", 8080);
		if (this.rootConfig.getBoolean("proxy.use", false)) {
			builder.setProxy(proxyAdress, proxyPort);
		}

		this.rootConfig.save();

		this.api = builder.buildAsync();

	}

	public JDA getJDA() {
		return this.api;
	}

	public Config getPluginConfig(final Plugin plugin) {
		return this.rootConfig.getOrCreateConfig("plugins." + plugin.getPluginInfo().getName());
	}

	public final Set<PluginExtension> getPlugins() {
		return Collections.unmodifiableSet(this.plugins);
	}

	public boolean isLoaded(final PluginInfo info) {
		for (final PluginExtension plugin : this.plugins) {
			if (info.equals(plugin.plugin.getPluginInfo())) {
				return true;
			}
		}
		return false;
	}

	public void loadPlugin(final File pluginFolder) {
		PluginExtension extension = null;
		try {
			extension = new PluginExtension(this, pluginFolder);
			if (!this.isLoaded(extension.plugin.getPluginInfo())) {
				extension.load();
				this.plugins.add(extension);
			} else {
				GCUtil.runGC(10);
			}
		} catch (IOException | PluginException e) {
			this.plugins.remove(extension);
			e.printStackTrace();
		}
		try {
			this.rootConfig.save();
		} catch (final ConfigSaveException e) {
			e.printStackTrace();
		}
	}

	public void loadPlugins(final File pluginsFolder) {

		if (pluginsFolder == null || !pluginsFolder.exists() || !pluginsFolder.isDirectory()) {
			throw new IllegalArgumentException("Invalid folder");
		}

		for (final File pluginDir : pluginsFolder.listFiles()) {
			if (!pluginDir.isDirectory()) {
				continue; // Only folders for now, single jar and class files will come later
			} else {
				this.loadPlugin(pluginDir);
			}

		}
	}

	public void saveConfig() {
		this.rootConfig.save();
	}

	public void shutdown() {
		this.unloadPlugins();
		this.commandManager.shutdown();
		this.api.shutdown();
		this.eventManager.shutdown();
		URLUtils.shutdown();
	}

	public void unloadPlugin(final Class<? extends Plugin> clazz) {
		this.unloadPlugins(o -> o.plugin.getClass().equals(clazz));
	}

	public void unloadPlugin(final PluginExtension pluginExtension) {
		pluginExtension.unload();
		this.plugins.remove(pluginExtension);
		GCUtil.runGC(10);
	}

	public void unloadPlugins() {
		this.unloadPlugins(PluginSelector.ALL);
	}

	public void unloadPlugins(final PluginSelector selector) {
		final List<PluginExtension> unload = new ArrayList<>();

		for (final PluginExtension plugin : this.plugins) {
			if (selector.match(plugin)) {
				unload.add(plugin);
			}
		}
		for (final PluginExtension plugin : unload) {
			this.unloadPlugin(plugin);
		}

	}

}
