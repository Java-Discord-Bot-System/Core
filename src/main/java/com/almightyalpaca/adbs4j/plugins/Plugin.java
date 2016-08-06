package com.almightyalpaca.adbs4j.plugins;

import java.io.File;

import com.almightyalpaca.adbs4j.command.Command;
import com.almightyalpaca.adbs4j.exception.PluginLoadingException;
import com.almightyalpaca.adbs4j.exception.PluginUnloadingException;
import com.almightyalpaca.adbs4j.internal.extension.ExtensionBridge;
import com.almightyalpaca.adbs4j.storage.StorageProviderInstance;

import net.dv8tion.jda.JDA;

public abstract class Plugin {

	private final PluginInfo		info;

	private final ExtensionBridge	bridge;

	public Plugin(final PluginInfo info) {
		this.info = info;
		this.bridge = new ExtensionBridge(this);
	}

	public Plugin(final String id, final PluginInfo.Version version, final String author, final String name, final String description) {
		this(new PluginInfo(id, version, author, name, description));
	}

	public Plugin(final String id, final String version, final String author, final String name, final String description) {
		this(new PluginInfo(id, version, author, name, description));
	}

	public ExtensionBridge getBridge() {
		return this.bridge;
	}

	public File getCacheFolder() {
		return new File(this.bridge.getPluginFolder(), "cache");
	}

	protected File getChacheFolder() {
		return this.bridge.getChacheFolder();
	}

	public final StorageProviderInstance getGlobalStorageProvider() {
		return this.bridge.getGlobalStorageProvider();
	}

	public JDA getJDA() {
		return this.bridge.getJDA();
	}

	protected File getPluginFolder() {
		return this.bridge.getPluginFolder();
	}

	public final PluginInfo getPluginInfo() {
		return this.info;
	}

	public final StorageProviderInstance getPluginStorageProvider() {
		return this.bridge.getExtensionManager().getPluginStorageProvider(this);
	}

	public abstract void load() throws PluginLoadingException;

	protected boolean registerCommand(final Command command) {
		return this.bridge.registerCommand(command);
	}

	protected void registerEventHandler(final Object o) {
		this.bridge.registerEventHandler(o);
	}

	public abstract void unload() throws PluginUnloadingException;

	public void unloadPlugin() throws PluginUnloadingException {
		this.bridge.unloadPlugin();
	}

	protected boolean unregisterCommand(final Command command) {
		return this.bridge.unregisterCommand(command);
	}

	protected void unregisterEventHandler(final Object o) {
		this.bridge.unregisterEventHandler(o);
	}

}
