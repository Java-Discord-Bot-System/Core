package com.almightyalpaca.adbs4j.internal.commands;

import com.almightyalpaca.adbs4j.command.CommandHandler;
import com.almightyalpaca.adbs4j.command.CommandInfo;
import com.almightyalpaca.adbs4j.events.commands.CommandEvent;
import com.almightyalpaca.adbs4j.internal.extension.CommandExtension;
import com.almightyalpaca.adbs4j.internal.extension.CommandExtensionManager;
import com.almightyalpaca.adbs4j.jda.AdvancedMessageBuilder;
import com.almightyalpaca.adbs4j.jda.AdvancedMessageBuilder.Formatting;

public class CategoryHelpCommand extends HelpCommand {

	public CategoryHelpCommand(final CommandExtensionManager manager, final String category) {
		super(manager, category, "help", "Shows help for all " + category + "commands", "Help me with help");
	}

	@Override
	@CommandHandler
	public void onCommand(final CommandEvent event) {
		final AdvancedMessageBuilder builder = new AdvancedMessageBuilder();

		builder.appendString(this.getInfo().getName().toUpperCase(), Formatting.BOLD).newLine();
		builder.appendString("```xml");
		for (final CommandExtension extension : this.manager.commands.values()) {
			final CommandInfo info = extension.getCommandInfo();
			if (this.getInfo().getName().equals(info.getCategory())) {
				builder.newLine().appendString("> ").appendString(info.getName()).appendSpace().appendString(info.getSyntax());
			}
		}
		builder.appendString("```");

		event.sendMessage(builder, null);

	}

}