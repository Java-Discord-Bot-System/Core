package com.almightyalpaca.discord.bot.system.command;

import com.almightyalpaca.discord.bot.system.events.commands.CommandEvent;

public abstract class Command {

	private final CommandInfo info;

	public Command(final CommandInfo info) {
		this.info = info;
	}

	public Command(final String name, final Category category, final String description, final String help) {
		this(new CommandInfo(name, category, description, help));
	}

	public boolean equals(final Command command) {
		if (command == null) {
			return false;
		} else {
			return this.info.equals(command.info);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof Command) {
			return this.equals((Command) obj);
		} else {
			return false;
		}
	}

	public final CommandInfo getInfo() {
		return this.info;
	}

	public void unknownSyntax(final CommandEvent event) {}

}
