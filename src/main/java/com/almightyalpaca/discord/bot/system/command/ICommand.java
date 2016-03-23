package com.almightyalpaca.discord.bot.system.command;

import com.almightyalpaca.discord.bot.system.events.CommandEvent;

public interface ICommand {

	public String getDescription();

	public String getHelp();

	public String getName();

	void unknownSyntax(CommandEvent event);

}
