package com.almightyalpaca.discord.bot.system.command.arguments.parsers;

import java.util.NoSuchElementException;

import com.almightyalpaca.discord.bot.system.command.arguments.Arguments;
import com.almightyalpaca.discord.bot.system.command.arguments.parsers.CommandAgumentParsers.Parser;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;

public class JDAParsers {

	public static class ChannelParser extends Parser<Channel> {
		public ChannelParser() {
			super(Channel.class);
		}

		@Override
		public Channel get(final Message msg, final Arguments args) {
			Channel channel;
			final String arg = args.removeNext();
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
		public Guild get(final Message msg, final Arguments args) {
			Guild guild;
			final String arg = args.removeNext();
			if (arg.equalsIgnoreCase("this") || arg.equalsIgnoreCase("guild") || arg.equalsIgnoreCase("server")) {
				guild = msg.getJDA().getTextChannelById(msg.getChannelId()).getGuild();
			} else {
				guild = msg.getJDA().getGuildById(args.removeNext());
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
		public TextChannel get(final Message msg, final Arguments args) {
			TextChannel channel;
			final String arg = args.removeNext();
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

		private User get(final JDA api, final String tag) {
			System.out.println("tag: " + tag);
			final int i = tag.lastIndexOf("#");
			if (i != -1 && i != 0 && i != tag.length() - 1) {
				for (final User user : api.getUsers()) {
					if ((user.getUsername() + "#" + user.getDiscriminator()).contentEquals(tag)) {
						return user;
					}
				}
			}
			return null;
		}

		@Override
		public User get(final Message msg, final Arguments args) {
			User user;
			final String arg = args.removeNext();
			if (arg.equalsIgnoreCase("me")) {
				user = msg.getAuthor();
			} else if (arg.equalsIgnoreCase("bot")) {
				user = msg.getJDA().getSelfInfo();
			} else {
				user = msg.getJDA().getUserById(arg.replace("<@", "").replace(">", ""));
			}

			for (final String string : args) {
				System.out.println(string);
			}

			if (user == null) {
				int parts = -1;
				String temp = arg;
				while ((user = this.get(msg.getJDA(), temp)) == null) {
					parts++;
					if (parts == args.size()) {
						break;
					}
					try {
						temp = arg + " " + args.get(parts);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}

				if (user == null) {
					throw new NoSuchElementException();
				} else {
					for (int i = 0; i <= parts; i++) {
						args.removeNext();
					}
				}
			}
			return user;
		}
	}

	public static class VoiceChannelParser extends Parser<VoiceChannel> {
		public VoiceChannelParser() {
			super(VoiceChannel.class);
		}

		@Override
		public VoiceChannel get(final Message msg, final Arguments args) {
			VoiceChannel channel;
			final String arg = args.removeNext();

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
