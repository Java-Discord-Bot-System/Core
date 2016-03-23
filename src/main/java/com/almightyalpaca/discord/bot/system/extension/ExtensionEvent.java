package com.almightyalpaca.discord.bot.system.extension;

import com.almightyalpaca.discord.bot.system.plugins.Plugin;

public class ExtensionEvent {

	protected static ExtensionManager getExtensionManager(final Plugin plugin) {
		return plugin.getBridge().pluginExtension.getPluginManager();
	}
}
