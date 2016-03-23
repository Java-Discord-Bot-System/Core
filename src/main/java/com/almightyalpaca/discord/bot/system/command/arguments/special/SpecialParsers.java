package com.almightyalpaca.discord.bot.system.command.arguments.special;

import com.almightyalpaca.discord.bot.system.command.arguments.Arguments;
import com.almightyalpaca.discord.bot.system.command.arguments.parsers.CommandAgumentParsers;
import com.almightyalpaca.discord.bot.system.command.arguments.parsers.CommandAgumentParsers.Parser;

import net.dv8tion.jda.entities.Message;

public class SpecialParsers {

	public static class RestParser extends Parser<Rest> {

		public RestParser() {
			super(Rest.class);
		}

		@Override
		public Rest get(final Message msg, final Arguments args) {

			return new Rest(args);
		}
	}

	private static boolean initialized = false;

	public static void init() {
		if (!SpecialParsers.initialized) {
			SpecialParsers.initialized = true;
			CommandAgumentParsers.addParser(new RestParser());
		}
	}

}
