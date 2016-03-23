package com.almightyalpaca.discord.bot.system.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.almightyalpaca.discord.bot.system.config.exception.KeyNotFoundException;
import com.almightyalpaca.discord.bot.system.config.exception.WrongTypeException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ConfigFactory {

	public static Config getConfig(final File file) throws IOException, WrongTypeException, KeyNotFoundException, JsonIOException, JsonSyntaxException {
		if (!file.exists() || com.almightyalpaca.discord.bot.system.util.FileUtils.isEmpty(file)) {
			file.createNewFile();
			final FileWriter writer = new FileWriter(file);
			writer.write("{}");
			writer.close();
		}
		return new RootConfig(file);
	}

	public static Config getConfig(final String data, final File file) throws IOException {
		FileUtils.writeStringToFile(file, data, "UTF-8");
		return ConfigFactory.getConfig(file);
	}

	public static Config getConfig(final URL url, final File file) throws JsonIOException, JsonSyntaxException, WrongTypeException, KeyNotFoundException, IOException {
		FileUtils.copyURLToFile(url, file);
		return ConfigFactory.getConfig(file);
	}

	public static Config getDefaultConfig() throws IOException, WrongTypeException, KeyNotFoundException, JsonIOException, JsonSyntaxException {
		final File file = new File("config.json");
		return ConfigFactory.getConfig(file);
	}

}
