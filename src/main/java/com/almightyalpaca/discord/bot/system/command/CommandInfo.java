package com.almightyalpaca.discord.bot.system.command;

public class CommandInfo {

	private final String	name;
	private final String	description;
	private final String	help;
	private final Category	category;

	public CommandInfo(final String name, final Category category, final String description, final String help) {
		this.name = name.toLowerCase().trim();
		this.category = category;
		this.description = description;
		this.help = help;
	}

	public boolean equals(final CommandInfo info) {
		if (info == null) {
			return false;
		} else {
			return this.name.equalsIgnoreCase(info.name);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof CommandInfo) {
			return this.equals((CommandInfo) obj);
		} else {
			return false;
		}
	}

	public final Category getCategory() {
		return this.category;
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
