package com.almightyalpaca.discord.bot.system.settings;

import com.almightyalpaca.discord.bot.system.config.Config;
import com.almightyalpaca.discord.bot.system.config.exception.KeyNotFoundException;
import com.almightyalpaca.discord.bot.system.config.exception.WrongTypeException;
import com.google.gson.JsonObject;

public class GuildSettings extends Config {

	protected GuildSettings(final Config parent, final JsonObject config) throws WrongTypeException, KeyNotFoundException {
		super(parent, config);
	}

}
