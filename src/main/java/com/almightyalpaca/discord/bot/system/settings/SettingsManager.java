package com.almightyalpaca.discord.bot.system.settings;

import com.almightyalpaca.discord.bot.system.config.Config;

public class SettingsManager {
	private final Config	userConfig;
	private final Config	guildConfig;

	public SettingsManager(final Config userConfig, final Config guildConfig) {
		this.userConfig = userConfig;
		this.guildConfig = guildConfig;
	}

	public GuildSettings getGuildSettings(final String id) {
		return new GuildSettings(this.guildConfig, this.guildConfig.getJsonObject("guilds." + id));
	}

	public UserSettings getUserSettings(final String id) {
		return new UserSettings(this.userConfig, this.userConfig.getJsonObject("users." + id));
	}

	public void shutdown() {
		this.userConfig.save();
		this.guildConfig.save();
	}

}
