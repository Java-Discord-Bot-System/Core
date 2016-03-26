package com.almightyalpaca.discord.bot.system.events;

import java.io.File;
import java.util.function.Consumer;

import com.almightyalpaca.discord.bot.system.extension.CommandExtensionManager;
import com.almightyalpaca.discord.bot.system.extension.ExtensionEvent;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.MessageBuilder.SplitMode;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandEvent extends ExtensionEvent {
	final MessageReceivedEvent				receivedEvent;
	private final CommandExtensionManager	manager;

	public CommandEvent(final CommandExtensionManager manager, final MessageReceivedEvent receivedEvent) {
		this.manager = manager;
		this.receivedEvent = receivedEvent;
	}

	public final User getAuthor() {
		return this.receivedEvent.getAuthor();
	}

	public final MessageChannel getChannel() {
		return this.receivedEvent.getChannel();
	}

	public final String getCommandWithoutPrefix() {
		return this.getMessage().getRawContent().replaceFirst(this.manager.getPrefix(), "");
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

	/**
	 * Uploads a file to the Discord servers and sends it to this {@link net.dv8tion.jda.entities.TextChannel TextChannel}. Sends the provided {@link net.dv8tion.jda.entities.Message Message} with the
	 * uploaded file.<br>
	 * If you do not wish to send a Message with the uploaded file, you can provide <code>null</code> for the <code>message</code> parameter.
	 * <p>
	 * <b>Note:</b> This method is blocking, which can cause problems when uploading large files.<br>
	 * Consider {@link #sendFileAsync(java.io.File, java.util.function.Consumer) sendFileAsync(File, Consumer)} for an alternative.
	 *
	 * @param file
	 *			The file to upload to the {@link net.dv8tion.jda.entities.TextChannel TextChannel}.
	 * @param message
	 *			The message to be sent along with the uploaded file. This value can be <code>null</code>.
	 * @return The {@link net.dv8tion.jda.entities.Message Message} created from this upload.
	 */
	public final Message sendFile(final File file, final Message message) {
		return this.receivedEvent.getChannel().sendFile(file, message);
	}

	/**
	 * Asynchronously uploads a file to the Discord servers and sends it to this {@link net.dv8tion.jda.entities.TextChannel TextChannel}. Sends the provided {@link net.dv8tion.jda.entities.Message
	 * Message} with the uploaded file.<br>
	 * If you do not wish to send a Message with the uploaded file, you can provide <code>null</code> for the <code>message</code> parameter.
	 *
	 * @param file
	 *			The file to upload to the {@link net.dv8tion.jda.entities.TextChannel TextChannel}.
	 * @param message
	 *			The message to be sent along with the uploaded file. This value can be <code>null</code>.
	 * @param callback
	 *			Function to deal with the returned {@link net.dv8tion.jda.entities.Message Message} after asynchronous uploading completes.
	 */
	public final void sendFileAsync(final File file, final Message message, final Consumer<Message> callback) {
		this.receivedEvent.getChannel().sendFileAsync(file, message, callback);
	}

	/**
	 * Sends a given {@link net.dv8tion.jda.entities.Message Message} to this Channel This method only extracts the mentions, text and tts status out of the given Message-Object Therefore this can
	 * also be used to resend already received Messages To allow above behaviour, this method returns a new {@link net.dv8tion.jda.entities.Message Message} instance. The passed one is not modified!
	 * If the sending of the Message failed (probably Permissions), this method returns null. When the Rate-limit is reached (30 Messages in 30 secs), a
	 * {@link net.dv8tion.jda.exceptions.RateLimitedException RateLimitedException} is thrown
	 *
	 * @param msg
	 *			the {@link net.dv8tion.jda.entities.Message Message} to send
	 * @return The created {@link net.dv8tion.jda.entities.Message Message} object or null if it failed
	 * @throws net.dv8tion.jda.exceptions.RateLimitedException
	 *			 when rate-imit is reached
	 */
	public final Message sendMessage(final Message msg) {
		return this.receivedEvent.getChannel().sendMessage(msg);
	}

	public final void sendMessage(final MessageBuilder msgBuilder) throws RuntimeException {
		msgBuilder.send(this.getChannel());
	}

	public final void sendMessage(final MessageBuilder msgBuilder, final SplitMode mode) throws RuntimeException {
		msgBuilder.send(this.getChannel(), mode);
	}

	/**
	 * Sents a plain text {@link net.dv8tion.jda.entities.Message Message} to this channel. This will fail if the account of the api does not have the {@link net.dv8tion.jda.Permission#MESSAGE_WRITE
	 * Write-Permission} for this channel set After the Message has been sent, the created {@link net.dv8tion.jda.entities.Message Message} object is returned This Object will be null, if the sending
	 * failed. When the Rate-limit is reached (5 Messages in 5 secs), a {@link net.dv8tion.jda.exceptions.RateLimitedException RateLimitedException} is thrown
	 *
	 * @param text
	 *			the text to send
	 * @return the Message created by this function
	 * @throws net.dv8tion.jda.exceptions.RateLimitedException
	 *			 when rate-imit is reached
	 */
	public final Message sendMessage(final String text) {
		return this.receivedEvent.getChannel().sendMessage(text);
	}

	/**
	 * Sends a given {@link net.dv8tion.jda.entities.Message Message} to this Channel This method only extracts the mentions, text and tts status out of the given Message-Object Therefore this can
	 * also be used to resend already received Messages. To allow above behaviour, this method calls the callback with a new {@link net.dv8tion.jda.entities.Message Message} instance. The passed one
	 * is not modified! This method will wait, and send later, if a Rate-Limit occurs.
	 *
	 * @param msg
	 *			the {@link net.dv8tion.jda.entities.Message Message} to send
	 * @param callback
	 *			the Callback-function that is called upon successful sending
	 */
	public final void sendMessageAsync(final Message msg, final Consumer<Message> callback) {
		this.receivedEvent.getChannel().sendMessageAsync(msg, callback);
	}

	/**
	 * Sents a plain text {@link net.dv8tion.jda.entities.Message Message} to this channel. After the message has been sent, the corresponding {@link net.dv8tion.jda.entities.Message Message} object
	 * is passed to the callback-function This method will wait, and send later, if a Rate-Limit occurs.
	 *
	 * @param msg
	 *			the text to send
	 * @param callback
	 *			the Callback-function that is called upon successful sending
	 */
	public final void sendMessageAsync(final String msg, final Consumer<Message> callback) {
		this.receivedEvent.getChannel().sendMessageAsync(msg, callback);
	}

	/**
	 * Sends the typing status to discord. This is what is used to make the message "X is typing..." appear.<br>
	 * The typing status only lasts for 5 seconds, so if you wish to show continuous typing you will need to call this method once every 5 seconds.
	 */
	/**
	 * Sends the typing status to discord. This is what is used to make the message "X is typing..." appear.<br>
	 * The typing status only lasts for 5 seconds, so if you wish to show continuous typing you will need to call this method once every 5 seconds.
	 */
	public final void sendTyping() {
		this.receivedEvent.getChannel().sendTyping();
	}

}
