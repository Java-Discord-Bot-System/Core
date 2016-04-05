package com.almightyalpaca.discord.bot.system.plugins;

import java.io.File;

import com.almightyalpaca.discord.bot.system.command.Command;
import com.almightyalpaca.discord.bot.system.config.Config;
import com.almightyalpaca.discord.bot.system.exception.PluginLoadingException;
import com.almightyalpaca.discord.bot.system.exception.PluginUnloadingException;
import com.almightyalpaca.discord.bot.system.extension.ExtensionBridge;

import net.dv8tion.jda.JDA;

public abstract class Plugin {

	private final PluginInfo info;

	private final ExtensionBridge bridge;

	public Plugin(final PluginInfo info) {
		this.info = info;
		this.bridge = new ExtensionBridge(this);
	}

	public ExtensionBridge getBridge() {
		return this.bridge;
	}

	public File getCacheFolder() {
		return new File(this.bridge.getPluginFolder(), "cache");
	}

	public JDA getJDA() {
		return this.bridge.getJDA();
	}

	public final Config getPluginConfig() {
		return this.bridge.getExtensionManager().getPluginConfig(this);
	}

	protected File getPluginFolder() {
		return this.bridge.getPluginFolder();
	}

	public final PluginInfo getPluginInfo() {
		return this.info;
	}

	public final Config getSharedConfig(final String key) {
		return this.bridge.getSharedConfig(key);
	}

	public abstract void load() throws PluginLoadingException;

	protected boolean registerCommand(final Command command) {
		return this.bridge.registerCommand(command);
	}

	protected boolean registerEventHandler(final Object o) {
		return this.bridge.registerEventHandler(o);
	}

	public abstract void unload() throws PluginUnloadingException;

	public void unloadPlugin() throws PluginUnloadingException {
		this.bridge.unloadPlugin();
	}

	protected boolean unregisterCommand(final Command command) {
		return this.bridge.unregisterCommand(command);
	}

	protected boolean unregisterEventHandler(final Object o) {
		return this.bridge.unregisterEventHandler(o);
	}

}
