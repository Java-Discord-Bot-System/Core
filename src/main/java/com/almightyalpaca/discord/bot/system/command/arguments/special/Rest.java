package com.almightyalpaca.discord.bot.system.command.arguments.special;

import com.almightyalpaca.discord.bot.system.command.arguments.Arguments;

public class Rest {

	private final String text;

	public Rest(final Arguments args) {
		String string = "";
		while (args.hasNext()) {
			string += " " + args.removeNext();
		}
		this.text = string.replaceFirst(" ", "");
	}

	public Rest(final String string) {
		this.text = string;
	}

	public final String getString() {
		return this.text;
	}

}
