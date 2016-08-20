package com.almightyalpaca.adbs4j.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.almightyalpaca.adbs4j.internal.CommandExecutionManager;
import com.almightyalpaca.adbs4j.misc.RateLimit;

import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.User;

public abstract class BotRateLimit {

	public static class GlobalRateLimit extends BotRateLimit {

		private final RateLimit	rateLimit;

		private final boolean	ignoreAdmins;

		public GlobalRateLimit(final int times, final long period, final TimeUnit unit, final boolean ignoreAdmins) {
			this.ignoreAdmins = ignoreAdmins;
			this.rateLimit = new RateLimit(times, period, unit);
		}

		@Override
		public void action(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
			if (!(this.ignoreAdmins && manager.isAdmin(user.getId()))) {
				this.rateLimit.action();
			}
		}

		@Override
		public boolean isLimited(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
			if (this.ignoreAdmins && manager.isAdmin(user.getId())) {
				return true;
			} else {
				return this.rateLimit.isLimited();
			}
		}
	}

	public static class PerGuildRateLimit extends BotRateLimit {

		private final Map<String, RateLimit>	rateLimits;

		private final boolean					ignoreAdmins;

		private final long						period;
		private final int						times;
		private final TimeUnit					unit;

		public PerGuildRateLimit(final int times, final long period, final TimeUnit unit, final boolean ignoreAdmins) {
			this.times = times;
			this.period = period;
			this.unit = unit;
			this.ignoreAdmins = ignoreAdmins;
			this.rateLimits = new ConcurrentHashMap<>();
		}

		@Override
		public void action(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
			if (!(this.ignoreAdmins && manager.isAdmin(user.getId()))) {
				if (channel instanceof Channel) {
					this.getRateLimit(((Channel) channel).getGuild().getId()).action();
				}
			}
		}

		private RateLimit getRateLimit(final String id) {
			RateLimit rateLimit = this.rateLimits.get(id);
			if (rateLimit == null) {
				rateLimit = new RateLimit(this.times, this.period, this.unit);
				this.rateLimits.put(id, rateLimit);
			}
			return rateLimit;
		}

		@Override
		public boolean isLimited(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
			if (this.ignoreAdmins && manager.isAdmin(user.getId())) {
				return true;
			} else {
				if (channel instanceof Channel) {
					return this.getRateLimit(((Channel) channel).getGuild().getId()).isLimited();
				}
				return true;
			}
		}

	}

	public static class PerUserRateLimit extends BotRateLimit {

		private final Map<String, RateLimit>	rateLimits;

		private final boolean					ignoreAdmins;

		private final long						period;
		private final int						times;
		private final TimeUnit					unit;

		public PerUserRateLimit(final int times, final long period, final TimeUnit unit, final boolean ignoreAdmins) {
			this.times = times;
			this.period = period;
			this.unit = unit;
			this.ignoreAdmins = ignoreAdmins;
			this.rateLimits = new ConcurrentHashMap<>();
		}

		@Override
		public void action(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
			if (!(this.ignoreAdmins && manager.isAdmin(user.getId()))) {
				this.getRateLimit(user.getId()).action();
			}

		}

		private RateLimit getRateLimit(final String id) {
			RateLimit rateLimit = this.rateLimits.get(id);
			if (rateLimit == null) {
				rateLimit = new RateLimit(this.times, this.period, this.unit);
				this.rateLimits.put(id, rateLimit);
			}
			return rateLimit;
		}

		@Override
		public boolean isLimited(final CommandExecutionManager manager, final MessageChannel channel, final User user) {
			if (this.ignoreAdmins && manager.isAdmin(user.getId())) {
				return true;
			} else {
				return this.getRateLimit(user.getId()).isLimited();
			}
		}
	}

	public static BotRateLimit newGlobalRateLimit(final int times, final long period, final TimeUnit unit, final boolean ignoreAdmins) {
		return new GlobalRateLimit(times, period, unit, ignoreAdmins);
	}

	public static BotRateLimit newGuildRateLimit(final int times, final long period, final TimeUnit unit, final boolean ignoreAdmins) {
		return new PerGuildRateLimit(times, period, unit, ignoreAdmins);
	}

	public static BotRateLimit newUserRateLimit(final int times, final long period, final TimeUnit unit, final boolean ignoreAdmins) {
		return new PerUserRateLimit(times, period, unit, ignoreAdmins);
	}

	public abstract void action(CommandExecutionManager manager, MessageChannel channel, User user);

	public abstract boolean isLimited(CommandExecutionManager manager, MessageChannel channel, User user);
}
