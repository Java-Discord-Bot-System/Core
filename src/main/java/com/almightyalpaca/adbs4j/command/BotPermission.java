package com.almightyalpaca.adbs4j.command;

import com.almightyalpaca.adbs4j.internal.BotPermissionManager;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;

public abstract class BotPermission {

	public static class DiscordBotPermission extends BotPermission {

		private final Permission[] perms;

		public DiscordBotPermission(final Permission... perms) {
			this.perms = perms;
		}

		@Override
		public boolean isSatisfied(final BotPermissionManager manager, final MessageChannel channel, final User user) {
			if (channel instanceof Channel) {
				final Channel guildChannel = (Channel) channel;
				for (final Permission permission : this.perms) {
					if (!guildChannel.checkPermission(user, permission)) {
						return false;
					}
				}
				return true;
			} else {
				return true;
			}
		}

	}

	public static final BotPermission	IN_VOICE_CHANNEL	= new BotPermission() {
																@Override
																public boolean isSatisfied(final BotPermissionManager manager, final MessageChannel channel, final User user) {
																	if (channel instanceof Channel) {
																		final Channel guildChannel = (Channel) channel;
																		final Guild guild = guildChannel.getGuild();
																		return guild.getVoiceStatusOfUser(user).getChannel() != null;
																	}
																	return false;
																}
															};

	public static final BotPermission	BOT_ADMIN			= new BotPermission() {
																@Override
																public boolean isSatisfied(final BotPermissionManager manager, final MessageChannel channel, final User user) {
																	return manager.isAdmin(user.getId());
																}
															};

	public static BotPermission hasDiscordPerms(final Permission... perms) {
		return new DiscordBotPermission(perms);
	}

	public abstract boolean isSatisfied(BotPermissionManager manager, MessageChannel channel, User user);

}
