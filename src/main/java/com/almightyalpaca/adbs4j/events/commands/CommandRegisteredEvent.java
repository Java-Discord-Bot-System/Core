package com.almightyalpaca.adbs4j.events.commands;

import com.almightyalpaca.adbs4j.command.CommandInfo;
import com.almightyalpaca.adbs4j.events.AsyncPluginEvent;
import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;

public class CommandRegisteredEvent extends AsyncPluginEvent {

	private final CommandInfo info;

	public CommandRegisteredEvent(final ExtensionManager manager, final CommandInfo info) {
		super(manager);
		this.info = info;
	}

	public final CommandInfo getInfo() {
		return this.info;
	}

}
