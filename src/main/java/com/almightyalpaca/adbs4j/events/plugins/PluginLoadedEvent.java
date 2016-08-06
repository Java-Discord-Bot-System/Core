package com.almightyalpaca.adbs4j.events.plugins;

import com.almightyalpaca.adbs4j.events.AsyncPluginEvent;
import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;
import com.almightyalpaca.adbs4j.plugins.PluginInfo;

public class PluginLoadedEvent extends AsyncPluginEvent {

	private final PluginInfo info;

	public PluginLoadedEvent(final ExtensionManager manager, final PluginInfo info) {
		super(manager);
		this.info = info;
	}

	public final PluginInfo getInfo() {
		return this.info;
	}

}
