package com.almightyalpaca.discord.bot.system.command;

import com.almightyalpaca.discord.bot.system.events.CommandEvent;

public abstract class AbstractCommand implements ICommand {

	private final String	name;
	private final String	description;
	private final String	help;

	/**
	 * @param name
	 * @param description
	 * @param help
	 */
	public AbstractCommand(final String name, final String description, final String help) {
		this.name = name;
		this.description = description;
		this.help = help;
	}

	@Override
	public final String getDescription() {
		return this.description;
	}

	@Override
	public final String getHelp() {
		return this.help;
	}

	@Override
	public final String getName() {
		return this.name;
	}

	@Override
	public void unknownSyntax(final CommandEvent event) {
		System.out.println("Wrong usage:" + event.getMessage().getContent());
	}

}
