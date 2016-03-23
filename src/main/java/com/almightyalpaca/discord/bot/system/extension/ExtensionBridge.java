package com.almightyalpaca.discord.bot.system.extension;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.almightyalpaca.discord.bot.system.command.ICommand;
import com.almightyalpaca.discord.bot.system.config.Config;
import com.almightyalpaca.discord.bot.system.exception.PluginLoadingException;
import com.almightyalpaca.discord.bot.system.exception.PluginUnloadingException;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;
import com.almightyalpaca.discord.bot.system.plugins.PluginInfo;
import com.almightyalpaca.discord.bot.system.settings.SettingsManager;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

public class ExtensionBridge {

	private final Set<CommandExtension>	commands;

	private final Plugin				plugin;

	PluginExtension						pluginExtension;

	private final Set<Object>			eventHandlers;

	public ExtensionBridge(final Plugin plugin) {
		this.plugin = plugin;
		this.commands = new HashSet<>();
		this.eventHandlers = new HashSet<>();
	}

	public File getCacheFolder() {
		return this.pluginExtension.getPluginManager().cacheFolder;
	}

	public Config getGuildConfig(final Guild guild) {
		return this.pluginExtension.getPluginManager().getGuildConfig(guild);
	}

	public JDA getJDA() {
		return this.pluginExtension.getPluginManager().getAPI();
	}

	public final Plugin getPlugin() {
		return this.plugin;
	}

	public final Config getPluginConfig() {
		return this.pluginExtension.getPluginManager().getPluginConfig(this.plugin);
	}

	public File getPluginFolder() {
		return this.pluginExtension.getFolder();
	}

	public final PluginInfo getPluginInfo() {
		return this.plugin.getPluginInfo();
	}

	public String getPrefix() {
		return this.pluginExtension.getPluginManager().getPrefix();
	}

	public final Config getSecureConfig(final String key) {
		return this.pluginExtension.getPluginManager().getSecureConfig(key, this);
	}

	public SettingsManager getSettingsManager() {
		return this.pluginExtension.getPluginManager().getSettingsManager();
	}

	public Config getUserConfig(final User user) {
		return this.pluginExtension.getPluginManager().getUserConfig(user);
	}

	public void initialize(final PluginExtension object) {
		this.pluginExtension = object;
	}

	public void load() throws PluginLoadingException {
		this.plugin.load();
	}

	public final void registerCommand(final ICommand command) {
		System.out.println("Registering command \"" + command.getClass().getName() + "\" as \"" + command.getName() + "\"");
		final CommandExtension commandExtension = new CommandExtension(this.pluginExtension.getPluginManager().getCommandManager(), command);
		this.commands.add(commandExtension);
		this.pluginExtension.registerCommand(commandExtension);
	}

	public void registerEventHandler(final Object o) {
		this.eventHandlers.add(o);
		this.pluginExtension.getPluginManager().getEventManager().register(o);
	}

	public void unload() throws PluginUnloadingException {
		this.plugin.unload();
		this.unregisterAllCommands();
		this.unregisterAllEventHandlers();
	}

	public final void unregisterAllCommands() {
		final Iterator<CommandExtension> iterator = this.commands.iterator();
		while (iterator.hasNext()) {
			final CommandExtension commandExtension = iterator.next();
			iterator.remove();
			this.unregisterCommand(commandExtension);
		}
		if (this.commands.size() != 0) {
			throw new RuntimeException("Could not remove all commands!");
		}
	}

	public void unregisterAllEventHandlers() {
		final Iterator<Object> iterator = this.eventHandlers.iterator();
		while (iterator.hasNext()) {
			final Object o = iterator.next();
			iterator.remove();
			this.unregisterEventHandler(o);
		}
		if (this.eventHandlers.size() != 0) {
			throw new RuntimeException("Could not remove all event handlers!");
		}
	}

	private final void unregisterCommand(final CommandExtension command) {
		this.pluginExtension.unregisterCommand(command);
		this.commands.remove(command);
	}

	public final boolean unregisterCommand(final ICommand command) {
		for (final CommandExtension commandExtension : this.commands) {
			if (commandExtension.getCommand() == command) {
				this.unregisterCommand(commandExtension);
				return true;
			}
		}
		return false;
	}

	public void unregisterEventHandler(final Object o) {
		this.eventHandlers.remove(o);
		this.pluginExtension.getPluginManager().getEventManager().unregister(o);
	}

}
