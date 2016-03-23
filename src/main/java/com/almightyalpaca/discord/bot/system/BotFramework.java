package com.almightyalpaca.discord.bot.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import com.almightyalpaca.discord.bot.system.config.exception.KeyNotFoundException;
import com.almightyalpaca.discord.bot.system.config.exception.WrongTypeException;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;
import com.almightyalpaca.discord.bot.system.util.GCUtil;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class BotFramework {

	private final ExtensionManager pm;

	public BotFramework() throws JsonIOException, JsonSyntaxException, WrongTypeException, KeyNotFoundException, FileNotFoundException, IOException, InterruptedException, LoginException,
			IllegalArgumentException {

		this.pm = new ExtensionManager();

		this.pm.loadPlugins(new File("plugins"));

		this.pm.saveConfig();

		GCUtil.fixedRate(30);

	}
}
