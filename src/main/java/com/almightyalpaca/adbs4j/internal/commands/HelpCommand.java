package com.almightyalpaca.adbs4j.internal.commands;

import com.almightyalpaca.adbs4j.command.Command;
import com.almightyalpaca.adbs4j.command.CommandHandler;
import com.almightyalpaca.adbs4j.command.CommandInfo;
import com.almightyalpaca.adbs4j.events.commands.CommandEvent;
import com.almightyalpaca.adbs4j.internal.extension.CommandExtension;
import com.almightyalpaca.adbs4j.internal.extension.CommandExtensionManager;
import com.almightyalpaca.adbs4j.jda.AdvancedMessageBuilder;
import com.almightyalpaca.adbs4j.jda.AdvancedMessageBuilder.Formatting;

public abstract class HelpCommand extends Command {
	final CommandExtensionManager manager;

	HelpCommand(final CommandExtensionManager manager, final String name, final String category, final String description, final String help) {
		super(name, category, description, help);
		this.manager = manager;
	}

	public abstract void onCommand(CommandEvent event);

	@CommandHandler
	public void onCommand(final CommandEvent event, final String name) {
		System.out.println(name);
		final CommandExtension commandExtension = this.manager.commands.get(name);

		final Command command = commandExtension.getCommand();

		if (command instanceof CategoryHelpCommand) {
			((HelpCommand) command).onCommand(event);
		} else {

			final CommandInfo info = command.getInfo();
			final AdvancedMessageBuilder builder = new AdvancedMessageBuilder();

			builder.appendString("Help for command ", Formatting.BOLDSTART).appendString(info.getName().toUpperCase(), Formatting.BOLDEND).newLine();
			builder.appendString("Syntax: ", Formatting.BOLD).newLine();
			builder.appendString("```xml\n").appendString(event.getPrefix()).appendString(info.getName()).appendSpace().appendString(info.getSyntax()).appendString("\n```").newLine();
			builder.appendString(info.getHelp().replace("{$prefix}", event.getPrefix()));

			event.sendMessage(builder, null);
		}
	}
}