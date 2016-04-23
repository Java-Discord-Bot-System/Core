package com.almightyalpaca.discord.bot.system.extension;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.almightyalpaca.discord.bot.system.command.Command;
import com.almightyalpaca.discord.bot.system.config.Config;
import com.almightyalpaca.discord.bot.system.exception.PluginLoadingException;
import com.almightyalpaca.discord.bot.system.exception.PluginUnloadingException;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;

import net.dv8tion.jda.JDA;

public class ExtensionBridge {

	final Plugin plugin;

	PluginExtension pluginExtension;

	final Set<CommandExtension> commands;

	final Set<Object> eventHandlers;

	public ExtensionBridge(final Plugin plugin) {
		this.plugin = plugin;
		this.commands = new HashSet<>();
		this.eventHandlers = new HashSet<>();
	}

	public final File getChacheFolder() {
		return this.pluginExtension.extensionManager.cacheFolder;
	}

	public final ExtensionClassLoader getClassLoader() {
		return this.pluginExtension.loader;
	}

	public final CommandExtensionManager getCommandExtensionManager() {
		return this.pluginExtension.extensionManager.commandManager;
	}

	public final ExtensionManager getExtensionManager() {
		return this.pluginExtension.extensionManager;
	}

	public final JDA getJDA() {
		return this.pluginExtension.extensionManager.api;
	}

	public final PluginExtension getPluginExtension() {
		return this.pluginExtension;
	}

	public final File getPluginFolder() {
		return this.pluginExtension.folder;
	}

	public final Config getSharedConfig(final String key) {
		return this.pluginExtension.extensionManager.rootConfig.getConfig("shared." + key.replace(".", "/"));
	}

	final void initialize(final PluginExtension pluginExtension) {
		this.pluginExtension = pluginExtension;
	}

	public final void loadPlugin() throws PluginLoadingException {
		this.plugin.load();
	}

	public final boolean registerCommand(final Command command) {
		final CommandExtension commandExtension = new CommandExtension(this.pluginExtension.extensionManager.commandManager, command);
		this.commands.add(commandExtension);
		return this.pluginExtension.extensionManager.commandManager.register(commandExtension);
	}

	public final boolean registerEventHandler(final Object o) {
		this.eventHandlers.add(o);
		return this.pluginExtension.extensionManager.eventManager.register(o);
	}

	public final void unloadPlugin() throws PluginUnloadingException {
		PluginUnloadingException exception = null;
		try {
			this.plugin.unload();
		} catch (final PluginUnloadingException e) {
			exception = e;
		}
		this.unregisterAllCommands();
		this.unregisterAllEventHandlers();
		if (exception != null) {
			throw exception;
		}

	}

	public final void unregisterAllCommands() {
		final Iterator<CommandExtension> iterator = this.commands.iterator();
		while (iterator.hasNext()) {
			final CommandExtension commandExtension = iterator.next();
			iterator.remove();
			this.unregisterCommand(commandExtension);
		}
	}

	public final void unregisterAllEventHandlers() {
		final Iterator<Object> iterator = this.eventHandlers.iterator();
		while (iterator.hasNext()) {
			final Object o = iterator.next();
			iterator.remove();
			this.unregisterEventHandler(o);
		}
	}

	public final boolean unregisterCommand(final Command command) {
		for (final CommandExtension commandExtension : this.commands) {
			if (commandExtension.getCommand() == command) {
				this.unregisterCommand(commandExtension);
				return true;
			}
		}
		return false;
	}

	private final boolean unregisterCommand(final CommandExtension command) {
		this.commands.remove(command);
		return this.pluginExtension.extensionManager.commandManager.unregister(command);
	}

	public final boolean unregisterEventHandler(final Object o) {
		this.eventHandlers.remove(o);
		return this.pluginExtension.extensionManager.eventManager.unregister(o);
	}

}
