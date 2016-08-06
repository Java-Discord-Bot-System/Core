package com.almightyalpaca.adbs4j.command;

import com.almightyalpaca.adbs4j.exception.CommandInitialisationException;

public class CommandInfo {

	private final String	name;
	private final String	syntax;
	private final String	help;
	private final String	category;

	public CommandInfo(final String name, final String category, final String syntax, final String help) throws CommandInitialisationException {
		this.name = name.toLowerCase().replaceAll("\\s+", "");
		if (!name.matches("\\p{L}+")) {
			throw new CommandInitialisationException("Command name must only have latin characters");
		}
		this.category = category == null ? null : category.toLowerCase().replaceAll("\\s+", "");
		if (this.name.equals(this.category)) {
			throw new CommandInitialisationException("Command name and category can't be the same !");
		} else if (!name.matches("\\p{L}+")) {
			throw new CommandInitialisationException("Command category must only have latin characters");
		}
		this.syntax = syntax;
		this.help = help;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof CommandInfo) {
			return this.name.equalsIgnoreCase(((CommandInfo) obj).name);
		} else {
			return false;
		}
	}

	public final String getCategory() {
		return this.category;
	}

	public final String getHelp() {
		return this.help;
	}

	public final String getName() {
		return this.name;
	}

	public final String getSyntax() {
		return this.syntax;
	}

}
