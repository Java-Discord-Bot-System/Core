package com.almightyalpaca.adbs4j.internal.extension;

import java.io.File;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

import com.almightyalpaca.adbs4j.command.Command;
import com.almightyalpaca.adbs4j.exception.PluginLoadingException;
import com.almightyalpaca.adbs4j.exception.PluginUnloadingException;
import com.almightyalpaca.adbs4j.plugins.Plugin;
import com.almightyalpaca.adbs4j.plugins.PluginState;
import com.almightyalpaca.adbs4j.storage.StorageProviderInstance;

import net.dv8tion.jda.JDA;

public class ExtensionBridge {

	private PluginState			state	= PluginState.NOTLOADED;

	final Plugin				plugin;

	PluginExtension				pluginExtension;

	final Set<CommandExtension>	commands;

	final Set<Object>			eventHandlers;

	public ExtensionBridge(final Plugin plugin) {
		this.plugin = plugin;
		this.commands = Sets.newConcurrentHashSet();
		this.eventHandlers = Sets.newConcurrentHashSet();
	}

	public final File getChacheFolder() {
		return this.pluginExtension.extensionManager.cacheDir;
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

	public final StorageProviderInstance getGlobalStorageProvider() {
		return this.pluginExtension.extensionManager.getGlobalStorageProvider();
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

	public final PluginState getState() {
		return this.state;
	}

	final void initialize(final PluginExtension pluginExtension) {
		this.pluginExtension = pluginExtension;
	}

	final void loadPlugin() throws PluginLoadingException {
		if (this.state == PluginState.UNLOADED) {
			throw new PluginLoadingException("A plugin instance cannot be reused!");
		} else if (this.state == PluginState.LOADED) {
			throw new PluginLoadingException("THe plugin instance was already loaded!");
		}
		this.state = PluginState.LOADED;

		this.plugin.load();
	}

	public final boolean registerCommand(final Command command) {
		Objects.requireNonNull(command);
		final CommandExtension commandExtension = new CommandExtension(this.pluginExtension.extensionManager.commandManager, command);
		this.commands.add(commandExtension);
		return this.pluginExtension.extensionManager.commandManager.register(commandExtension);
	}

	public final void registerEventHandler(final Object o) {
		Objects.requireNonNull(o);
		this.eventHandlers.add(o);
		this.pluginExtension.extensionManager.eventManager.register(o);
	}

	public final void unloadPlugin() throws PluginUnloadingException {
		if (this.state == PluginState.NOTLOADED) {
			throw new PluginUnloadingException("A plugin instance cannot unloaded before it has been loaded!");
		} else if (this.state == PluginState.UNLOADED) {
			throw new PluginUnloadingException("The plugin instance was already unloaded!");
		}
		this.state = PluginState.UNLOADED;

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
		Objects.requireNonNull(command);
		for (final CommandExtension commandExtension : this.commands) {
			if (commandExtension.getCommand() == command) {
				this.unregisterCommand(commandExtension);
				return true;
			}
		}
		return false;
	}

	private final boolean unregisterCommand(final CommandExtension command) {
		Objects.requireNonNull(command);
		this.commands.remove(command);
		return this.pluginExtension.extensionManager.commandManager.unregister(command);
	}

	public final void unregisterEventHandler(final Object o) {
		Objects.requireNonNull(o);
		this.eventHandlers.remove(o);
		this.pluginExtension.extensionManager.eventManager.unregister(o);
	}

}
