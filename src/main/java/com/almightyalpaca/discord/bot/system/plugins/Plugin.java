package com.almightyalpaca.discord.bot.system.plugins;

import java.io.File;

import com.almightyalpaca.discord.bot.system.command.ICommand;
import com.almightyalpaca.discord.bot.system.config.Config;
import com.almightyalpaca.discord.bot.system.exception.PluginLoadingException;
import com.almightyalpaca.discord.bot.system.exception.PluginUnloadingException;
import com.almightyalpaca.discord.bot.system.extension.ExtensionBridge;
import com.almightyalpaca.discord.bot.system.settings.GuildSettings;
import com.almightyalpaca.discord.bot.system.settings.UserSettings;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;

public abstract class Plugin {

	private final PluginInfo		info;

	private final ExtensionBridge	bridge;

	public Plugin(final PluginInfo info) {
		this.info = info;
		this.bridge = new ExtensionBridge(this);
	}

	public ExtensionBridge getBridge() {
		return this.bridge;
	}

	public final GuildSettings getGuildConfig(final Guild guild) {
		return this.getBridge().getSettingsManager().getGuildSettings(guild.getId());
	}

	public final Config getPluginConfig() {
		return this.getBridge().getPluginConfig();
	}

	protected File getPluginFolder() {
		return this.bridge.getPluginFolder();
	}

	public final PluginInfo getPluginInfo() {
		return this.info;
	}

	public final UserSettings getUserSettings(final User user) {
		return this.getBridge().getSettingsManager().getUserSettings(user.getId());
	}

	public abstract void load() throws PluginLoadingException;

	protected void registerCommand(final ICommand command) {
		this.getBridge().registerCommand(command);
	}

	protected void registerEventHandler(final Object o) {
		this.bridge.registerEventHandler(o);
	}

	public abstract void unload() throws PluginUnloadingException;

	protected void unregisterCommand(final ICommand command) {
		this.getBridge().unregisterCommand(command);
	}

	protected void unregisterEventHandler(final Object o) {
		this.bridge.unregisterEventHandler(o);
	}
}
