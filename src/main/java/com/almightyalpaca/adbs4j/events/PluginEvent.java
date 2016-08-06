package com.almightyalpaca.adbs4j.events;

import java.util.Objects;

import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;
import com.almightyalpaca.adbs4j.internal.extension.ExtensionUtil;

public abstract class PluginEvent {

	protected final ExtensionManager	extensionManager;
	private boolean						fired	= false;

	public PluginEvent(final ExtensionManager extensionManager) {
		this.extensionManager = Objects.requireNonNull(extensionManager);
	}

	public final PluginEvent fire() {
		if (this.fired) {
			throw new UnsupportedOperationException("You cannot reuse an event!");
		}
		this.fired = true;
		ExtensionUtil.getEventManager(this.extensionManager).handle(this);
		return this;
	}

}
