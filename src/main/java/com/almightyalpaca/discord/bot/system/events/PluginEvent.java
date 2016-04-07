package com.almightyalpaca.discord.bot.system.events;

import com.almightyalpaca.discord.bot.system.extension.ExtensionEvent;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;

public abstract class PluginEvent extends ExtensionEvent {

	protected final ExtensionManager	extensionManager;
	private boolean						fired	= false;

	public PluginEvent(final ExtensionManager extensionManager) {
		this.extensionManager = extensionManager;
	}

	public final void fire() {
		if (this.fired) {
			throw new UnsupportedOperationException("You cannot reuse an event!");
		}
		this.fired = true;
		ExtensionEvent.getEventManager(this.extensionManager).handle(this);

	}

}
