package com.almightyalpaca.adbs4j.internal.extension;

public class EventManagerExtension {
	protected final ExtensionManager extensionManager;

	public EventManagerExtension(final ExtensionManager extensionManager) {
		this.extensionManager = extensionManager;
	}

	protected CommandExtensionManager getCommandManager() {
		return this.extensionManager.commandManager;
	}
}
