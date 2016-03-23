package com.almightyalpaca.discord.bot.system.events;

import java.util.HashMap;
import java.util.Map;

import com.almightyalpaca.discord.bot.system.events.manager.Handler;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;

import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.IEventManager;

public class EventManager implements IEventManager {

	private final Map<Object, Handler>	handlers;

	private final ExtensionManager		manager;

	public EventManager(final ExtensionManager manager) {
		this.manager = manager;
		this.handlers = new HashMap<>();
	}

	@Override
	public void handle(final Event event) {
		this.handle((Object) event);
	}

	public void handle(final Object event) {
		for (final Handler handler : this.handlers.values()) {
			handler.handle(event);
		}
		if (event instanceof MessageReceivedEvent) {
			final MessageReceivedEvent msgEvent = (MessageReceivedEvent) event;
			if (msgEvent.getMessage().getRawContent().startsWith(this.manager.getPrefix())) {
				this.manager.getCommandManager().onCommand(msgEvent);
			}
		}
	}

	@Override
	public void register(final Object o) {
		this.handlers.put(o, new Handler(o));
	}

	@Override
	public void unregister(final Object o) {
		this.handlers.remove(o);
	}
}
