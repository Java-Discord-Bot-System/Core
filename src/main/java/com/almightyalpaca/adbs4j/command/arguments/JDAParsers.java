package com.almightyalpaca.adbs4j.command.arguments;

import java.lang.annotation.Annotation;
import java.util.NoSuchElementException;

import com.almightyalpaca.adbs4j.command.arguments.CommandAgumentParsers.Parser;
import com.almightyalpaca.adbs4j.internal.CommandBuffer;

import net.dv8tion.jda.entities.*;

public class JDAParsers {

	public static class ChannelParser extends Parser<Channel> {
		public ChannelParser() {
			super(Channel.class);
		}

		@Override
		public Channel get(final Message msg, final CommandBuffer buffer, final Annotation[] annotations) {
			Channel channel;
			final String arg = buffer.removeNext();
			if (arg.equalsIgnoreCase("this")) {
				channel = msg.getJDA().getTextChannelById(msg.getChannelId());
			} else {
				channel = msg.getJDA().getTextChannelById(arg.replace("<@", "").replace(">", ""));
				if (channel == null) {
					channel = msg.getJDA().getVoiceChannelById(arg);
				}
			}
			if (channel == null) {
				throw new NoSuchElementException();
			}
			return channel;
		}

	}

	public static class GuildParser extends Parser<Guild> {
		public GuildParser() {
			super(Guild.class);
		}

		@Override
		public Guild get(final Message msg, final CommandBuffer buffer, final Annotation[] annotations) {
			Guild guild;
			final String arg = buffer.removeNext();
			if (arg.equalsIgnoreCase("this") || arg.equalsIgnoreCase("guild") || arg.equalsIgnoreCase("server")) {
				guild = msg.getJDA().getTextChannelById(msg.getChannelId()).getGuild();
			} else {
				guild = msg.getJDA().getGuildById(buffer.removeNext());
			}
			if (guild == null) {
				throw new NoSuchElementException();
			}
			return guild;
		}

	}

	public static class TextChannelParser extends Parser<TextChannel> {
		public TextChannelParser() {
			super(TextChannel.class);
		}

		@Override
		public TextChannel get(final Message msg, final CommandBuffer buffer, final Annotation[] annotations) {
			TextChannel channel;
			final String arg = buffer.removeNext();
			if (arg.equalsIgnoreCase("this") || arg.equalsIgnoreCase("channel")) {
				channel = msg.getJDA().getTextChannelById(msg.getChannelId());
			} else {
				channel = msg.getJDA().getTextChannelById(arg.replace("<#", "").replace(">", ""));
			}
			if (channel == null) {
				throw new NoSuchElementException();
			}
			return channel;
		}

	}

	public static class UserParser extends Parser<User> {
		public UserParser() {
			super(User.class);
		}

		@Override
		public User get(final Message msg, final CommandBuffer buffer, final Annotation[] annotations) {
			User user;
			final String arg = buffer.removeNext();
			if (arg.equalsIgnoreCase("me")) {
				user = msg.getAuthor();
			} else if (arg.equalsIgnoreCase("bot")) {
				user = msg.getJDA().getSelfInfo();
			} else {
				user = msg.getJDA().getUserById(arg.replace("<@", "").replace(">", ""));
			}

			// if (user == null) {
			// int parts = -1;
			// String temp = arg;
			// while ((user = this.get(msg.getJDA(), temp)) == null) {
			// parts++;
			// if (parts == buffer.parts()) {
			// break;
			// }
			// try {
			// temp = arg + " " + buffer.get(parts);
			// } catch (final Exception e) {
			// e.printStackTrace();
			// }
			// }
			//
			// if (user == null) {
			// throw new NoSuchElementException();
			// } else {
			// for (int i = 0; i <= parts; i++) {
			// buffer.removeNext();
			// }
			// }
			// }
			if (user == null) {
				throw new NoSuchElementException();
			}
			return user;
		}

	}

	public static class VoiceChannelParser extends Parser<VoiceChannel> {
		public VoiceChannelParser() {
			super(VoiceChannel.class);
		}

		@Override
		public VoiceChannel get(final Message msg, final CommandBuffer buffer, final Annotation[] annotations) {
			VoiceChannel channel;
			final String arg = buffer.removeNext();

			channel = msg.getJDA().getVoiceChannelById(arg.replace("<@", "").replace(">", ""));

			if (channel == null) {
				msg.getJDA().getTextChannelById(msg.getChannelId()).getGuild().getVoiceChannels().parallelStream().filter(c -> c.getName().contentEquals(arg)).findFirst().get();
			}
			if (channel == null) {
				throw new NoSuchElementException();
			}
			return channel;
		}

	}

	static void init() {
		CommandAgumentParsers.addParser(new JDAParsers.ChannelParser());
		CommandAgumentParsers.addParser(new JDAParsers.GuildParser());
		CommandAgumentParsers.addParser(new JDAParsers.TextChannelParser());
		CommandAgumentParsers.addParser(new JDAParsers.UserParser());
		CommandAgumentParsers.addParser(new JDAParsers.VoiceChannelParser());
	}

}
