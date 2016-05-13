package com.almightyalpaca.discord.bot.system.extension;

import com.almightyalpaca.discord.bot.system.events.manager.EventManager;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;

public class ExtensionUtils {

	public static EventManager getEventManager(final ExtensionManager extensionManager) {
		return extensionManager.eventManager;
	}

	public static ExtensionManager getExtensionManager(final CommandExtensionManager commandExtensionManager) {
		return commandExtensionManager.extensionManager;
	}

	public static ExtensionManager getExtensionManager(final Plugin plugin) {
		return plugin.getBridge().pluginExtension.extensionManager;
	}
}
