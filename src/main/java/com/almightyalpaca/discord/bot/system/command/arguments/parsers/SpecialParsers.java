package com.almightyalpaca.discord.bot.system.command.arguments.parsers;

import com.almightyalpaca.discord.bot.system.command.arguments.Arguments;
import com.almightyalpaca.discord.bot.system.command.arguments.parsers.CommandAgumentParsers.Parser;
import com.almightyalpaca.discord.bot.system.command.arguments.special.Rest;

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

	static void init() {
		CommandAgumentParsers.addParser(new RestParser());
	}

}
