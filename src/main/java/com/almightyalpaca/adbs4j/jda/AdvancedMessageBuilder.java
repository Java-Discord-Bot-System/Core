/*
 * Copyright 2015-2016 Austin Keener & Michael Ritter Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.almightyalpaca.adbs4j.jda;

import com.almightyalpaca.adbs4j.util.StringUtil;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.impl.MessageImpl;

public class AdvancedMessageBuilder extends MessageBuilder {

	/**
	 * Holds the Available formatting used in {@link #appendString(String, Formatting...)}
	 */
	public enum Formatting {
		ITALICS("*", "*"),
		BOLD("**", "**"),
		STRIKETHROUGH("~~", "~~"),
		UNDERLINE("__", "__"),
		BLOCK("`", "`"),

		ITALICSSTART("*", ""),
		BOLDSTART("**", ""),
		STRIKETHROUGHSTART("~~", ""),
		UNDERLINESTART("__", ""),
		BLOCKSTART("`", ""),

		ITALICSEND("", "*"),
		BOLDEND("", "**"),
		STRIKETHROUGHEND("", "~~"),
		UNDERLINEEND("", "__"),
		BLOCKEND("", "`");

		private final String	begin;
		private final String	end;

		Formatting(final String begin, final String end) {
			this.begin = begin;
			this.end = end;
		}

		public final String getBegin() {
			return this.begin;
		}

		public final String getEnd() {
			return this.end;
		}

	}

	// public abstract class SplitMode {
	// protected abstract void send(MessageChannel channel) throws RuntimeException;
	// }

	// public SplitMode SPIT = new SplitMode() {
	//
	// @Override
	// protected void send(final MessageChannel channel) {
	// final String message = AdvancedMessageBuilder.this.builder.toString();
	// if (message.isEmpty()) {
	// throw new UnsupportedOperationException("Cannot build a Message with no content. (You never added any content to the message)");
	// }
	//
	// final String[] messages = StringUtil.split(message, 2000, "\n");
	//
	// final List<MessageImpl> msgs = new ArrayList<>(messages.length);
	//
	// for (final String string : messages) {
	// msgs.add(new MessageImpl("", null).setContent(string).setTTS(AdvancedMessageBuilder.this.isTTS).setMentionedUsers(AdvancedMessageBuilder.this.mentioned)
	// .setMentionedChannels(AdvancedMessageBuilder.this.mentionedTextChannels).setMentionsEveryone(AdvancedMessageBuilder.this.mentionEveryone));
	// }
	//
	// for (final MessageImpl msg : msgs) {
	// channel.sendMessage(msg);
	// }
	// }
	// };
	//
	// public SplitMode UPLOAD = new SplitMode() {
	//
	// @Override
	// protected void send(final MessageChannel channel) throws RuntimeException {
	// this.withMessage("The Message was too long, you can view it here: $url$").send(channel);
	// }
	//
	// public SplitMode withMessage(final String message) {
	// return new SplitMode() {
	// @Override
	// protected void send(final MessageChannel channel) throws RuntimeException {
	// try {
	// final URL url = NoteHubUploader.upload(AdvancedMessageBuilder.this.getMessage(), AdvancedMessageBuilderSettings.notehubPassword);
	// channel.sendMessage(message.replace("$url$", url.toString()));
	// } catch (IOException | UnirestException e) {
	// throw new RuntimeException(e);
	// }
	// }
	// };
	// }
	// };
	//
	// public SplitMode AUTO = new SplitMode() {
	//
	// @Override
	// protected void send(final MessageChannel channel) throws RuntimeException {
	// try {
	// if (StringUtil.split(AdvancedMessageBuilder.this.getMessage(), 2000, "\n").length <= 4) {
	// AdvancedMessageBuilder.this.SPIT.send(channel);
	// } else {
	// AdvancedMessageBuilder.this.UPLOAD.send(channel);
	// }
	// } catch (final Exception e) {
	// AdvancedMessageBuilder.this.UPLOAD.send(channel);
	// }
	// }
	// };
	//
	// public SplitMode TRUNCATE = new SplitMode() {
	// @Override
	// protected void send(final MessageChannel channel) {
	// channel.sendMessage(new MessageImpl("", null).setContent(AdvancedMessageBuilder.this.getMessage().substring(0, Math.min(AdvancedMessageBuilder.this.getLength(), 1997)) + "...")
	// .setTTS(AdvancedMessageBuilder.this.isTTS).setMentionedUsers(AdvancedMessageBuilder.this.mentioned).setMentionedChannels(AdvancedMessageBuilder.this.mentionedTextChannels)
	// .setMentionsEveryone(AdvancedMessageBuilder.this.mentionEveryone));
	//
	// }
	//
	// };

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdvancedMessageBuilder appendCodeBlock(final String text, final String language) {
		super.appendCodeBlock(text, language);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdvancedMessageBuilder appendEveryoneMention() {
		super.appendEveryoneMention();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdvancedMessageBuilder appendFormat(final String format, final Object... args) {
		super.appendFormat(format, args);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdvancedMessageBuilder appendHereMention() {
		super.appendHereMention();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdvancedMessageBuilder appendMention(final Role role) {
		super.appendMention(role);
		return this;
	}

	// public void send(final MessageChannel channel) throws RuntimeException {
	// this.send(channel, this.AUTO);
	// }
	//
	// public void send(final MessageChannel channel, final SplitMode mode) throws RuntimeException {
	// mode.send(channel);
	// }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdvancedMessageBuilder appendMention(final TextChannel channel) {
		super.appendMention(channel);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdvancedMessageBuilder appendMention(final User user) {
		super.appendMention(user);
		return this;
	}

	/**
	 * Appends a space char (" ") to the Message
	 *
	 * @return this instance
	 */
	public AdvancedMessageBuilder appendSpace() {
		return this.appendString(" ");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdvancedMessageBuilder appendString(final String text) {
		super.appendString(text);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	public AdvancedMessageBuilder appendString(final String text, final Formatting... format) {
		boolean blockPresent = false;
		for (final Formatting formatting : format) {
			if (formatting == Formatting.BLOCK) {
				blockPresent = true;
				continue;
			}
			this.builder.append(formatting.getBegin());
		}
		if (blockPresent) {
			this.builder.append(Formatting.BLOCK.getBegin());
		}

		this.builder.append(text);

		if (blockPresent) {
			this.builder.append(Formatting.BLOCK.getEnd());
		}
		for (int i = format.length - 1; i >= 0; i--) {
			if (format[i] == Formatting.BLOCK) {
				continue;
			}
			this.builder.append(format[i].getEnd());
		}
		return this;
	}

	/**
	 * Creates a {@link net.dv8tion.jda.entities.Message Message} object from this Builder
	 *
	 * @return the created {@link net.dv8tion.jda.entities.Message Message}
	 * @throws java.lang.UnsupportedOperationException
	 *             <ul>
	 *             <li>If you attempt to build() an empty Message (no content added to the Message)</li>
	 *             <li>If you attempt to build() a Message with more than 2000 characters of content.</li>
	 *             </ul>
	 */
	@Override
	public Message build() {
		final String message = this.builder.toString();
		if (message.isEmpty()) {
			throw new UnsupportedOperationException("Cannot build a Message with no content. (You never added any content to the message)");
		}
		if (message.length() > 2000) {
			throw new UnsupportedOperationException("Cannot build a Message with more than 2000 characters. Please limit your input.");
		}

		return new MessageImpl("", null).setContent(message).setTTS(this.isTTS).setMentionedUsers(this.mentioned).setMentionedChannels(this.mentionedTextChannels).setMentionsEveryone(
				this.mentionEveryone);
	}

	/**
	 * Appends a new line char ( <b>\n</b> ) if and only if the last char isn't already a new line char
	 *
	 * @return this instance
	 */
	public AdvancedMessageBuilder ensureNewLine() {
		if (this.builder.length() == 0 || this.builder.charAt(this.builder.length() - 1) == '\n') {
			return this;
		}
		return this.newLine();
	}

	/**
	 * Returns the current length of the content that will be built into a {@link net.dv8tion.jda.entities.Message Message} when {@link #build()} is called.<br>
	 * If this value is <code>0</code> or greater than <code>2000</code> when {@link #build()} is called, an exception will be raised.
	 *
	 * @return The currently length of the content that will be built into a Message.
	 */
	@Override
	public int getLength() {
		return this.builder.length();
	}

	/**
	 * Appends a new line char ( <b>\n</b> ) to the Message
	 *
	 * @return this instance
	 */
	public AdvancedMessageBuilder newLine() {
		return this.appendString("\n");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AdvancedMessageBuilder setTTS(final boolean tts) {
		super.setTTS(tts);
		return this;
	}

	public AdvancedMessageBuilder stripEveryoneMention() {
		StringUtil.replaceAll(this.builder, "@everyone", "@\u180Eeveryone");
		return this;
	}

	public AdvancedMessageBuilder stripGlobalMentions() {
		this.stripEveryoneMention();
		this.stripHereMention();
		return this;
	}

	public AdvancedMessageBuilder stripHereMention() {
		StringUtil.replaceAll(this.builder, "@here", "@\u180Ehere");
		return this;
	}

	@Override
	public String toString() {
		return this.builder.toString();
	}

}