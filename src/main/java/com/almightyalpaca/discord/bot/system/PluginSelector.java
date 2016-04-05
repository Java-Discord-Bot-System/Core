package com.almightyalpaca.discord.bot.system;

import com.almightyalpaca.discord.bot.system.extension.PluginExtension;

@FunctionalInterface
public interface PluginSelector {

	public static final PluginSelector ALL = p -> true;

	public static final PluginSelector NONE = p -> false;

	public boolean match(PluginExtension plugin);

}
