package com.almightyalpaca.discord.bot.system.extension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.LoginException;

import com.almightyalpaca.discord.bot.system.PluginSelector;
import com.almightyalpaca.discord.bot.system.config.Config;
import com.almightyalpaca.discord.bot.system.config.ConfigFactory;
import com.almightyalpaca.discord.bot.system.config.exception.KeyNotFoundException;
import com.almightyalpaca.discord.bot.system.config.exception.WrongTypeException;
import com.almightyalpaca.discord.bot.system.events.EventManager;
import com.almightyalpaca.discord.bot.system.exception.PluginException;
import com.almightyalpaca.discord.bot.system.exception.PluginLoadingException;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;
import com.almightyalpaca.discord.bot.system.plugins.PluginInfo;
import com.almightyalpaca.discord.bot.system.settings.SettingsManager;
import com.almightyalpaca.discord.bot.system.util.GCUtil;
import com.almightyalpaca.discord.bot.system.util.URLUtils;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilderSettings;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

public class ExtensionManager {

	public Set<PluginExtension>				plugins;

	private final Config					rootConfig;
	private final JDA						api;

	private final CommandExtensionManager	commandManager;

	private final SettingsManager			settingsManager;

	private final File						configFolder;

	private final EventManager				eventManager;

	final File								cacheFolder;

	public ExtensionManager() throws JsonIOException, JsonSyntaxException, WrongTypeException, KeyNotFoundException, FileNotFoundException, IOException, LoginException, IllegalArgumentException,
			InterruptedException {
		this.plugins = new HashSet<>();

		this.cacheFolder = new File("cache");

		this.configFolder = new File("config");
		this.configFolder.mkdirs();

		this.rootConfig = ConfigFactory.getConfig(new File(this.configFolder, "config.json"));

		MessageBuilderSettings.setNodehubPassword(this.rootConfig.getOrPutString("messages.upload.nodehub.password", "Discord Bot"));

		this.settingsManager = new SettingsManager(ConfigFactory.getConfig(new File(this.configFolder, "users.json")), ConfigFactory.getConfig(new File(this.configFolder, "guilds.json")));

		this.eventManager = new EventManager(this);

		this.commandManager = new CommandExtensionManager(this);

		final JDABuilder builder = new JDABuilder(this.rootConfig.getString("secure.discord.email"), this.rootConfig.getString("secure.discord.password")).setEventManager(this.eventManager);

		if (this.rootConfig.getBoolean("proxy.use")) {
			builder.setProxy(this.rootConfig.getString("proxy.host"), this.rootConfig.getInt("proxy.port"));
		}

		this.api = builder.buildAsync();

	}

	public JDA getAPI() {
		return this.api;
	}

	public final CommandExtensionManager getCommandManager() {
		return this.commandManager;
	}

	public Config getConfig(final String string) {
		return this.rootConfig.getOrCreateConfig(string);
	}

	public EventManager getEventManager() {
		return this.eventManager;
	}

	public Config getGuildConfig(final Guild guild) {
		return this.getConfig("guilds." + guild.getId());
	}

	public Config getPluginConfig(final Plugin plugin) {
		return this.getConfig("plugins." + plugin.getPluginInfo().getId().replace(".", "/"));
	}

	public Config getPluginConfig(final PluginExtension plugin) {
		return this.getPluginConfig(plugin.getPluginObject());
	}

	public String getPrefix() {
		final String prefix = this.getConfig("commands").getString("prefix");
		if (prefix.equalsIgnoreCase("@mention")) {
			return this.api.getSelfInfo().getAsMention() + " ";
		}
		return prefix;
	}

	public Config getSecureConfig(final String name, final ExtensionBridge bridge) {
		System.out.println("WARN: \"" + bridge.getPlugin().getPluginInfo().getName() + "\" Plugin requested access to secure config: " + name);// TODO
		return this.getConfig("secure." + name);
	}

	public SettingsManager getSettingsManager() {
		return this.settingsManager;
	}

	public Config getUserConfig(final User user) {
		final String key = "users." + user.getId();
		if (!this.rootConfig.hasKey(key)) {
			this.rootConfig.put(key + ".id", user.getId());
		}
		return this.getConfig(key);
	}

	public boolean isLoaded(final PluginInfo info) {
		for (final PluginExtension plugin : this.plugins) {
			if (info.equals(plugin.getPluginInfo())) {
				return true;
			}
		}
		return false;
	}

	public void loadPlugin(final File pluginFolder) {
		try {
			PluginExtension extension = new PluginExtension(this, pluginFolder);
			if (!this.isLoaded(extension.getPluginInfo())) {
				extension.load();
				this.plugins.add(extension);
			} else {
				extension = null;
				GCUtil.runGC(10);
			}
		} catch (IOException | PluginException | PluginLoadingException e) {
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
		this.unloadPlugins(o -> o.getPluginObject().getClass().equals(clazz));
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
