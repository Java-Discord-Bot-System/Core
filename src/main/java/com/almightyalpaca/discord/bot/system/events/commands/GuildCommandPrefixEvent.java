package com.almightyalpaca.discord.bot.system.events.commands;

import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;
import com.almightyalpaca.discord.bot.system.extension.ExtensionUtils;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;

import net.dv8tion.jda.entities.Guild;

public class GuildCommandPrefixEvent extends CommandPrefixEvent {
	private final Guild guild;

	public GuildCommandPrefixEvent(final ExtensionManager manager, final Guild guild) {
		super(manager);
		this.guild = guild;
		this.prefixes.add(manager.getJDA().getSelfInfo().getAsMention() + " ");
	}

	public GuildCommandPrefixEvent(final Plugin plugin, final Guild guild) {
		this(ExtensionUtils.getExtensionManager(plugin), guild);
	}

	public final Guild getGuild() {
		return this.guild;
	}

}
