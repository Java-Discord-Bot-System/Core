package com.almightyalpaca.adbs4j.events.commands;

import com.almightyalpaca.adbs4j.command.Command;
import com.almightyalpaca.adbs4j.command.CommandInfo;
import com.almightyalpaca.adbs4j.events.PluginEvent;
import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;
import com.almightyalpaca.adbs4j.internal.extension.ExtensionUtil;
import com.almightyalpaca.adbs4j.plugins.Plugin;

import net.dv8tion.jda.entities.*;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public final class CommandExecutionEvent extends PluginEvent {

	private boolean						cancelled	= false;

	private final Command				command;

	private final MessageReceivedEvent	messageReceivedEvent;

	public CommandExecutionEvent(final ExtensionManager manager, final Command command, final MessageReceivedEvent messageReceivedEvent) {
		super(manager);
		this.command = command;
		this.messageReceivedEvent = messageReceivedEvent;
	}

	public CommandExecutionEvent(final Plugin plugin, final Command command, final MessageReceivedEvent messageReceivedEvent) {
		this(ExtensionUtil.getExtensionManager(plugin), command, messageReceivedEvent);
	}

	public User getAuthor() {
		return this.messageReceivedEvent.getMessage().getAuthor();
	}

	public MessageChannel getChannel() {
		return this.messageReceivedEvent.getChannel();
	}

	public CommandInfo getCommandInfo() {
		return this.command.getInfo();
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
