package com.almightyalpaca.adbs4j.internal.extension;

import com.almightyalpaca.adbs4j.events.manager.EventManager;
import com.almightyalpaca.adbs4j.plugins.Plugin;

public class ExtensionUtil {

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
