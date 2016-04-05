package com.almightyalpaca.discord.bot.system.command;

public class CommandInfo {

	private final String	name;
	private final String	description;
	private final String	help;

	public CommandInfo(final String name, final String description, final String help) {
		this.name = name.toLowerCase().trim();
		this.description = description;
		this.help = help;
	}

	public boolean equals(final CommandInfo info) {
		return info.name.equalsIgnoreCase(this.name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof CommandInfo) {
			return this.equals((CommandInfo) obj);
		} else {
			return false;
		}
	}

	public final String getDescription() {
		return this.description;
	}

	public final String getHelp() {
		return this.help;
	}

	public final String getName() {
		return this.name;
	}

}
