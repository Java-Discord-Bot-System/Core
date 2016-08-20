package com.almightyalpaca.adbs4j.command;

import java.util.Set;

import com.google.common.collect.Sets;

import com.almightyalpaca.adbs4j.events.commands.CommandEvent;
import com.almightyalpaca.adbs4j.internal.CommandExecutionManager;

import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;

public abstract class Command {

	private final CommandInfo				info;
	private final Set<ExecutionRequirement>	permissions;
	private final Set<BotRateLimit>			rateLimits;

	public Command(final CommandInfo info, final ExecutionRequirement... perms) {
		this.info = info;
		this.permissions = Sets.newConcurrentHashSet();
		this.rateLimits = Sets.newConcurrentHashSet();
		this.addPermissions(perms);
	}

	public Command(final String name, final String category, final String syntax, final String help, final ExecutionRequirement... perms) {
		this(new CommandInfo(name, category, syntax, help), perms);
	}

	protected void addPermissions(final ExecutionRequirement... perms) {
		if (perms != null && perms.length > 0) {
			for (final ExecutionRequirement perm : perms) {
				this.permissions.add(perm);
			}
		}
	}

	protected void addRateLimit(final BotRateLimit... rateLimits) {
		if (rateLimits != null && rateLimits.length > 0) {
			for (final BotRateLimit rateLimit : rateLimits) {
				this.rateLimits.add(rateLimit);
			}
		}
	}

	public final boolean canExecute(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
		for (final ExecutionRequirement permission : this.permissions) {
			if (!permission.isSatisfied(manager, channel, user)) {
				return false;
			}
		}
		for (final BotRateLimit rateLimit : this.rateLimits) {
			if (rateLimit.isLimited(manager, channel, user)) {
				return false;
			}
		}
		for (final BotRateLimit rateLimit : this.rateLimits) {
			rateLimit.action(manager, channel, user);
		}
		return true;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Command) {
			return this.info.equals(((Command) obj).info);
		} else {
			return false;
		}
	}

	public final CommandInfo getInfo() {
		return this.info;
	}

	public void unknownSyntax(final CommandEvent event) {}

}
