package com.almightyalpaca.adbs4j.events.commands;

import com.almightyalpaca.adbs4j.command.CommandInfo;
import com.almightyalpaca.adbs4j.events.AsyncPluginEvent;
import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;

public class CommandUnregisteredEvent extends AsyncPluginEvent {

	private final CommandInfo info;

	public CommandUnregisteredEvent(final ExtensionManager manager, final CommandInfo info) {
		super(manager);
		this.info = info;
	}

	public final CommandInfo getInfo() {
		return this.info;
	}

}
