package com.almightyalpaca.discord.bot.system.command.arguments;

import java.util.ArrayList;

public final class Arguments extends ArrayList<String> {

	private static final long serialVersionUID = 1475565314952824077L;

	private Arguments() {
		super();
	}

	private Arguments(final String message) {
		super();
		for (final String string : message.split(" ")) {
			if (!string.trim().isEmpty()) {
				this.add(string);
			}
		}
	}

	public static Arguments create(final String commandWithoutName) {
		if (commandWithoutName.isEmpty()) {
			return new Arguments();
		} else {
			return new Arguments(commandWithoutName);
		}
	}

	public String getNext() {
		return this.get(0);
	}

	public boolean hasNext() {
		return !this.isEmpty();
	}

	public String removeNext() {
		return this.remove(0);
	}

}