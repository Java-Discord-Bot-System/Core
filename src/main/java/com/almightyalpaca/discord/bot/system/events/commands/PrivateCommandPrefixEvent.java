package com.almightyalpaca.discord.bot.system.events.commands;

import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;

import net.dv8tion.jda.entities.User;

public class PrivateCommandPrefixEvent extends CommandPrefixEvent {
	private final User user;

	public PrivateCommandPrefixEvent(final ExtensionManager manager, final User user) {
		super(manager);
		this.user = user;
		this.prefixes.add("");
	}

	public final User getUser() {
		return this.user;
	}

}
