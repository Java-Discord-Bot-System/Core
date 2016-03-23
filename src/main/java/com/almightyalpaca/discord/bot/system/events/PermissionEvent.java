package com.almightyalpaca.discord.bot.system.events;

import com.almightyalpaca.discord.bot.system.command.ICommand;
import com.almightyalpaca.discord.bot.system.extension.ExtensionEvent;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

public final class PermissionEvent extends PluginEvent {

	private final ICommand		command;
	private final CommandEvent	commandEvent;
	private boolean				result	= true;

	public PermissionEvent(final ExtensionManager manager, final ICommand command, final CommandEvent commandEvent) {
		super(manager);
		this.command = command;
		this.commandEvent = commandEvent;
	}

	public PermissionEvent(final Plugin plugin, final ICommand command, final CommandEvent commandEvent) {
		this(ExtensionEvent.getExtensionManager(plugin), command, commandEvent);
	}

	public User getAuthor() {
		return this.commandEvent.getMessage().getAuthor();
	}

	public MessageChannel getChannel() {
		return this.commandEvent.receivedEvent.getChannel();
	}

	public CommandEvent getCommandEvent() {
		return this.commandEvent;
	}

	public String getCommandName() {
		return this.command.getName();
	}

	public Guild getGuild() {
		return this.commandEvent.receivedEvent.getGuild();
	}

	public PrivateChannel getPrivateChannel() {
		return this.commandEvent.receivedEvent.getPrivateChannel();
	}

	public boolean getResult() {
		return this.result;
	}

	public TextChannel getTextChannel() {
		return this.commandEvent.receivedEvent.getTextChannel();
	}

	public boolean isPrivate() {
		return this.commandEvent.getMessage().isPrivate();
	}

	public void setResult(final boolean result) {
		this.result = result;
	}

}
