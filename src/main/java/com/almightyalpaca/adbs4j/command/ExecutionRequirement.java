package com.almightyalpaca.adbs4j.command;

import com.almightyalpaca.adbs4j.internal.CommandExecutionManager;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;

public abstract class ExecutionRequirement {

	public static class ANDExecutionRequirement extends ExecutionRequirement {

		private final ExecutionRequirement[] requirements;

		public ANDExecutionRequirement(final ExecutionRequirement... requirements) {
			this.requirements = requirements;
		}

		@Override
		public boolean isSatisfied(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
			for (final ExecutionRequirement requirement : this.requirements) {
				if (!requirement.isSatisfied(manager, channel, user)) {
					return false;
				}
			}
			return true;
		}

	}

	public static class PermissionExecutionRequirement extends ExecutionRequirement {

		private final Permission[] perms;

		public PermissionExecutionRequirement(final Permission... perms) {
			this.perms = perms;
		}

		@Override
		public boolean isSatisfied(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
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

	public static final ExecutionRequirement	USER_IS_IN_VOICE_CHANNEL	= new ExecutionRequirement() {
																				@Override
																				public boolean isSatisfied(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
																					if (channel instanceof Channel) {
																						final Channel guildChannel = (Channel) channel;
																						final Guild guild = guildChannel.getGuild();
																						return guild.getVoiceStatusOfUser(user).getChannel() != null;
																					}
																					return false;
																				}
																			};

	public static final ExecutionRequirement	USER_IS_BOT_ADMIN			= new ExecutionRequirement() {
																				@Override
																				public boolean isSatisfied(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
																					return manager.isAdmin(user.getId());
																				}
																			};

	public static ExecutionRequirement hasDiscordPerms(final Permission... perms) {
		return new PermissionExecutionRequirement(perms);
	}

	public abstract boolean isSatisfied(CommandExecutionManager manager, MessageChannel channel, User user);

}
