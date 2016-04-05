package com.almightyalpaca.discord.bot.system.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.almightyalpaca.discord.bot.system.config.exception.KeyNotFoundException;
import com.almightyalpaca.discord.bot.system.config.exception.WrongTypeException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ConfigTest {

	public static void main(final String[] args)
		throws JsonIOException, JsonSyntaxException, WrongTypeException, KeyNotFoundException, FileNotFoundException, IOException, InterruptedException {

		final Config config = ConfigFactory.getConfig(File.createTempFile("config test file", ".json"));
		System.out.println(config.getConfigFile().getAbsolutePath());

		Config current = config;
		for (int i = 0; i < 100; i++) {
			current = current.getOrCreateConfig("Current" + i);
			System.out.println();
			System.out.println("Current" + i);
		}

		config.save();
	}

}
