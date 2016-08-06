package com.almightyalpaca.adbs4j.events.commands;

import java.io.File;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.almightyalpaca.adbs4j.events.AsyncPluginEvent;
import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.*;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandEvent extends AsyncPluginEvent {
	final MessageReceivedEvent	receivedEvent;
	private final String		prefix;

	public CommandEvent(final ExtensionManager extensionManager, final MessageReceivedEvent receivedEvent, final String prefix) {
		super(extensionManager);
		this.receivedEvent = receivedEvent;
		this.prefix = prefix;
	}

	public final User getAuthor() {
		return this.receivedEvent.getAuthor();
	}

	public final MessageChannel getChannel() {
		return this.receivedEvent.getChannel();
	}

	public final String getCommandWithoutPrefix() {
		return StringUtils.replaceOnce(this.getMessage().getRawContent(), this.prefix, "");
	}

	public final Guild getGuild() {
		return this.receivedEvent.getGuild();
	}

	public final JDA getJDA() {
		return this.receivedEvent.getJDA();
	}

	public final Message getMessage() {
		return this.receivedEvent.getMessage();
	}

	public final MessageReceivedEvent getMessageReceivedEvent() {
		return this.receivedEvent;
	}

	public final String getPrefix() {
		return this.prefix;
	}

	public final PrivateChannel getPrivateChannel() {
		return this.receivedEvent.getPrivateChannel();
	}

	public final TextChannel getTextChannel() {
		return this.receivedEvent.getTextChannel();
	}

	public final boolean isGuild() {
		return !this.isPrivate();
	}

	public final boolean isPrivate() {
		return this.receivedEvent.isPrivate();
	}

	public final void sendFile(final File file, final MessageBuilder builder, final Consumer<Message> callback) {
		this.receivedEvent.getChannel().sendFileAsync(file, builder.build(), callback);
	}

	public final void sendMessage(final MessageBuilder builder, final Consumer<Message> callback) { // TODO: add handling for messages longer than 2k chars
		this.receivedEvent.getChannel().sendMessageAsync(builder.build(), callback);
	}

}
