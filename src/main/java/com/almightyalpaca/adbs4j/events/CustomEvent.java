package com.almightyalpaca.adbs4j.events;

import com.almightyalpaca.adbs4j.internal.extension.ExtensionUtil;
import com.almightyalpaca.adbs4j.plugins.Plugin;
import com.almightyalpaca.adbs4j.plugins.PluginInfo;

public final class CustomEvent extends PluginEvent {

	private final String		key;
	private final PluginInfo	pluginInfo;

	private Object				value	= null;

	public CustomEvent(final Plugin plugin, final String key) {
		super(ExtensionUtil.getExtensionManager(plugin));
		this.pluginInfo = plugin.getPluginInfo();
		this.key = key;
	}

	public CustomEvent(final Plugin plugin, final String key, final Object defaultValue) {
		this(plugin, key);
		this.value = defaultValue;
	}

	public final String getKey() {
		return this.key;
	}

	public final PluginInfo getPluginInfo() {
		return this.pluginInfo;
	}

	public final Object getValue() {
		return this.value;
	}

	public final void setValue(final Object value) {
		this.value = value;
	}

}
