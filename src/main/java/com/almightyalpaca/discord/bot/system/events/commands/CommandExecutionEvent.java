package com.almightyalpaca.discord.bot.system.events.commands;

import com.almightyalpaca.discord.bot.system.command.Command;
import com.almightyalpaca.discord.bot.system.events.PluginEvent;
import com.almightyalpaca.discord.bot.system.extension.ExtensionEvent;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public final class CommandExecutionEvent extends PluginEvent {

	private boolean cancelled = false;

	private final Command command;

	private final MessageReceivedEvent messageReceivedEvent;

	public CommandExecutionEvent(final ExtensionManager manager, final Command command, final MessageReceivedEvent messageReceivedEvent) {
		super(manager);
		this.command = command;
		this.messageReceivedEvent = messageReceivedEvent;
	}

	public CommandExecutionEvent(final Plugin plugin, final Command command, final MessageReceivedEvent messageReceivedEvent) {
		this(ExtensionEvent.getExtensionManager(plugin), command, messageReceivedEvent);
	}

	public User getAuthor() {
		return this.messageReceivedEvent.getMessage().getAuthor();
	}

	public MessageChannel getChannel() {
		return this.messageReceivedEvent.getChannel();
	}

	public String getCommandName() {
		return this.command.getInfo().getName();
	}

	public Guild getGuild() {
		return this.messageReceivedEvent.getGuild();
	}

	public PrivateChannel getPrivateChannel() {
		return this.messageReceivedEvent.getPrivateChannel();
	}

	public TextChannel getTextChannel() {
		return this.messageReceivedEvent.getTextChannel();
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public boolean isPrivate() {
		return this.messageReceivedEvent.getMessage().isPrivate();
	}

	public void setCancelled(final boolean cancel) {
		this.cancelled = cancel;
	}

}
