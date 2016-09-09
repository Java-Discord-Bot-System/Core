package com.almightyalpaca.adbs4j.command;

import java.util.Set;

import com.google.common.collect.Sets;

import com.almightyalpaca.adbs4j.events.commands.CommandEvent;
import com.almightyalpaca.adbs4j.internal.CommandExecutionManager;

import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;

public abstract class Command {

	private final CommandInfo				info;
	private final Set<ExecutionRequirement>	requirements;
	private final Set<BotRateLimit>			rateLimits;

	public Command(final CommandInfo info, final ExecutionRequirement... perms) {
		this.info = info;
		this.requirements = Sets.newConcurrentHashSet();
		this.rateLimits = Sets.newConcurrentHashSet();
		this.addRequirements(perms);
	}

	public Command(final String name, final String category, final String syntax, final String help, final ExecutionRequirement... perms) {
		this(new CommandInfo(name, category, syntax, help), perms);
	}

	protected void addRateLimit(final BotRateLimit... rateLimits) {
		if (rateLimits != null && rateLimits.length > 0) {
			for (final BotRateLimit rateLimit : rateLimits) {
				this.rateLimits.add(rateLimit);
			}
		}
	}

	protected void addRequirements(final ExecutionRequirement... requirements) {
		if (requirements != null && requirements.length > 0) {
			for (final ExecutionRequirement req : requirements) {
				this.requirements.add(req);
			}
		}
	}

	public final boolean canExecute(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
		for (final ExecutionRequirement requirement : this.requirements) {
			if (!requirement.isSatisfied(manager, channel, user)) {
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
