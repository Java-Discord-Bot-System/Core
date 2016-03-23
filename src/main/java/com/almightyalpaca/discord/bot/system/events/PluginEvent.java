package com.almightyalpaca.discord.bot.system.events;

import com.almightyalpaca.discord.bot.system.extension.ExtensionEvent;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;

abstract class PluginEvent extends ExtensionEvent {

	private final ExtensionManager	extensionManager;
	private boolean					fired	= false;

	public PluginEvent(final ExtensionManager extensionManager) {
		this.extensionManager = extensionManager;
	}

	public void fire() {
		if (this.fired) {
			throw new UnsupportedOperationException("You cannot reuse an event!");
		}
		this.fired = true;
		this.extensionManager.getEventManager().handle(this);

	}

}
