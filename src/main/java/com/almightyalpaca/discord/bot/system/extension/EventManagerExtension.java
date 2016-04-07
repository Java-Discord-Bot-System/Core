package com.almightyalpaca.discord.bot.system.extension;

public class EventManagerExtension {
	protected final ExtensionManager extensionManager;

	public EventManagerExtension(final ExtensionManager extensionManager) {
		this.extensionManager = extensionManager;
	}

	protected CommandExtensionManager getCommandManager() {
		return this.extensionManager.commandManager;
	}
}
