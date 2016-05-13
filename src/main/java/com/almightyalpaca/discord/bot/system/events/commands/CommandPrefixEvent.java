package com.almightyalpaca.discord.bot.system.events.commands;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.almightyalpaca.discord.bot.system.events.PluginEvent;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;
import com.almightyalpaca.discord.bot.system.extension.ExtensionUtils;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;

public abstract class CommandPrefixEvent extends PluginEvent {

	protected final Set<String> prefixes;

	public CommandPrefixEvent(final ExtensionManager manager) {
		super(manager);
		this.prefixes = new HashSet<>();
	}

	public CommandPrefixEvent(final Plugin plugin) {
		this(ExtensionUtils.getExtensionManager(plugin));
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
