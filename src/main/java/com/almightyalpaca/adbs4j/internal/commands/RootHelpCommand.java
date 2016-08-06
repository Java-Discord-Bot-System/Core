package com.almightyalpaca.adbs4j.internal.commands;

import java.util.HashSet;
import java.util.Set;

import com.almightyalpaca.adbs4j.command.CommandHandler;
import com.almightyalpaca.adbs4j.command.CommandInfo;
import com.almightyalpaca.adbs4j.events.commands.CommandEvent;
import com.almightyalpaca.adbs4j.internal.extension.CommandExtension;
import com.almightyalpaca.adbs4j.internal.extension.CommandExtensionManager;
import com.almightyalpaca.adbs4j.jda.AdvancedMessageBuilder;
import com.almightyalpaca.adbs4j.jda.AdvancedMessageBuilder.Formatting;

public class RootHelpCommand extends HelpCommand {

	public RootHelpCommand(final CommandExtensionManager manager) {
		super(manager, "help", null, "<category/command>", "Shows this help page");
	}

	@Override
	@CommandHandler
	public void onCommand(final CommandEvent event) {
		final Set<String> categories = new HashSet<>();
		final Set<CommandExtension> uncategorized = new HashSet<>();
		for (final CommandExtension extension : this.manager.commands.values()) {
			if (!(extension.getCommand() instanceof HelpCommand)) {
				final String category = extension.getCommandInfo().getCategory();
				if (category == null) {
					uncategorized.add(extension);
				} else {
					categories.add(category);
				}
			}
		}

		final AdvancedMessageBuilder builder = new AdvancedMessageBuilder();
		if (!uncategorized.isEmpty()) {
			builder.appendString("Commands", Formatting.BOLD).newLine().appendString("```xml");
			for (final CommandExtension extension : uncategorized) {
				final CommandInfo info = extension.getCommandInfo();
				builder.newLine().appendString("> ").appendString(info.getName()).appendSpace().appendString(info.getSyntax());
			}
			builder.appendString("```");
		}

		if (!categories.isEmpty()) {
			builder.ensureNewLine().appendString("Categorized Commands", Formatting.BOLD).newLine();
			builder.appendString("```xml");
			for (final String category : categories) {
				builder.newLine().appendString("> ").appendString(category);
			}
			builder.appendString("```");
		}
		event.sendMessage(builder, null);

	}

}