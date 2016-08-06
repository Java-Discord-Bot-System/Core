package com.almightyalpaca.adbs4j.events;

import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;

public class AsyncPluginEvent extends PluginEvent {

	public AsyncPluginEvent(final ExtensionManager extensionManager) {
		super(extensionManager);
	}

}
