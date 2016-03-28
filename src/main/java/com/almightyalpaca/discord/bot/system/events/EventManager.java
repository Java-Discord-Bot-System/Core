package com.almightyalpaca.discord.bot.system.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.almightyalpaca.discord.bot.system.events.manager.Handler;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;

import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.IEventManager;

public class EventManager implements IEventManager {

	private final Map<Object, Handler>	handlers;

	private final ExtensionManager		manager;

	ExecutorService						executor	= new ThreadPoolExecutor(1, 10, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), (ThreadFactory) r -> {
														final Thread thread = new Thread(r, "EventExecution-Thread");
														thread.setPriority(Thread.NORM_PRIORITY + 2);
														return thread;
													});

	public EventManager(final ExtensionManager manager) {
		this.manager = manager;
		this.handlers = new HashMap<>();
	}

	public void executeAsync(final Object object, final Method method, final Object event) {
		this.executor.submit(() -> {
			try {
				method.invoke(object, event);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void handle(final Event event) {
		this.handle((Object) event);
	}

	public void handle(final Object event) {
		for (final Handler handler : new ArrayList<>(this.handlers.values())) {
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
		this.handlers.put(o, new Handler(this, o));
	}

	public void shutdown() {
		this.executor.shutdownNow();
	}

	@Override
	public void unregister(final Object o) {
		this.handlers.remove(o);
	}

}
