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

public class BotSystem {

	private final ExtensionManager extensionManager;

	public BotSystem() throws JsonIOException, JsonSyntaxException, WrongTypeException, KeyNotFoundException, FileNotFoundException, IOException, InterruptedException, LoginException,
			IllegalArgumentException {

		this.extensionManager = new ExtensionManager();

		this.extensionManager.loadPlugins(new File("plugins"));

		this.extensionManager.saveConfig();

		GCUtil.fixedRate(60);

	}

	public ExtensionManager getExtensionManager() {
		return extensionManager;
	}

}
