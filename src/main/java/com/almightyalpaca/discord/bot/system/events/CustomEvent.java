package com.almightyalpaca.discord.bot.system.events;

import com.almightyalpaca.discord.bot.system.extension.ExtensionUtils;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;
import com.almightyalpaca.discord.bot.system.plugins.PluginInfo;

public final class CustomEvent extends PluginEvent {

	private final String		key;
	private final PluginInfo	pluginInfo;

	private Object value = null;

	public CustomEvent(final Plugin plugin, final String key) {
		super(ExtensionUtils.getExtensionManager(plugin));
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
