package com.almightyalpaca.discord.bot.system;

import java.io.IOException;
import java.time.ZonedDateTime;

import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import com.almightyalpaca.discord.bot.system.config.exception.KeyNotFoundException;
import com.almightyalpaca.discord.bot.system.config.exception.WrongTypeException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Main {

	public static void main(final String[] args) throws JsonIOException, JsonSyntaxException, WrongTypeException, KeyNotFoundException, IOException, InterruptedException, LoginException,
			IllegalArgumentException {
		final int hour = ZonedDateTime.now().getHour();
		if ((3 <= hour) && (hour < 7)) {
			JOptionPane.showMessageDialog(null, "Go to sleep!", "Reminder", JOptionPane.WARNING_MESSAGE);
			System.exit(9000);
		}

		new BotFramework();
	}
}
