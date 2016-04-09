package com.almightyalpaca.discord.bot.system.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.lang3.text.translate.UnicodeUnescaper;

import com.almightyalpaca.discord.bot.system.config.exception.ConfigSaveException;
import com.almightyalpaca.discord.bot.system.config.exception.KeyNotFoundException;
import com.almightyalpaca.discord.bot.system.config.exception.WrongTypeException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class RootConfig extends Config {

	private final File configFile;

	RootConfig(final File file) throws WrongTypeException, KeyNotFoundException, JsonIOException, JsonSyntaxException, FileNotFoundException {
		super(null, new JsonParser().parse(new FileReader(file)).getAsJsonObject());
		this.configFile = file;
	}

	@Override
	public File getConfigFile() {
		return this.configFile;
	}

	@Override
	public void save() throws ConfigSaveException {
		final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().serializeSpecialFloatingPointValues().create();
		final String json = gson.toJson(this.config);
		try {
			final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.configFile), "UTF-8"));
			new UnicodeUnescaper().translate(json, writer);
			writer.close();
		} catch (final IOException e) {
			throw new ConfigSaveException(e);
		}
	}

}
