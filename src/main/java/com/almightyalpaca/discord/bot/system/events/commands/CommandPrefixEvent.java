package com.almightyalpaca.discord.bot.system.events.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.almightyalpaca.discord.bot.system.events.PluginEvent;
import com.almightyalpaca.discord.bot.system.extension.ExtensionEvent;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;

import net.dv8tion.jda.entities.Guild;

public final class CommandPrefixEvent extends PluginEvent {

	private final Set<String>	prefixes;
	private final Guild			guild;

	public CommandPrefixEvent(final ExtensionManager manager, final Guild guild) {
		super(manager);
		this.guild = guild;
		this.prefixes = new HashSet<>();
		this.prefixes.add(manager.getJDA().getSelfInfo().getAsMention() + " ");
	}

	public CommandPrefixEvent(final Plugin plugin, final Guild guild) {
		this(ExtensionEvent.getExtensionManager(plugin), guild);
	}

	public boolean addPrefix(final String prefix) {
		return this.prefixes.add(prefix);
	}

	public boolean addPrefixes(final Collection<String> prefixes) {
		return this.prefixes.addAll(prefixes);
	}

	private void ensureMention() {
		this.prefixes.add(this.extensionManager.getJDA().getSelfInfo().getAsMention());
	}

	public final Guild getGuild() {
		return this.guild;
	}

	public Set<String> getPrefixes() {
		return Collections.unmodifiableSet(this.prefixes);
	}

	public boolean removePrefix(final String prefix) {
		final boolean result = this.prefixes.remove(prefix);
		this.ensureMention();
		return result;
	}

	public boolean removePrefixes(final Collection<String> prefixes) {
		final boolean result = this.prefixes.removeAll(prefixes);
		this.ensureMention();
		return result;
	}

}
