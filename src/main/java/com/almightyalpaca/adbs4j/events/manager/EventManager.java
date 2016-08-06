package com.almightyalpaca.adbs4j.events.manager;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import com.google.common.util.concurrent.MoreExecutors;

import com.almightyalpaca.adbs4j.events.AsyncPluginEvent;
import com.almightyalpaca.adbs4j.internal.extension.EventManagerExtension;
import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;

import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.hooks.IEventManager;

public class EventManager extends EventManagerExtension implements IEventManager {

	private final Map<Object, Handler>	handlers;

	private final ExecutorService		executor	= new ThreadPoolExecutor(1, 10, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), (ThreadFactory) r -> {
														final Thread thread = new Thread(r, "EventExecution-Thread");
														thread.setPriority(Thread.NORM_PRIORITY + 1);
														return thread;
													});

	public EventManager(final ExtensionManager extensionManager) {
		super(extensionManager);
		this.handlers = new ConcurrentHashMap<>();
	}

	@Override
	public List<Object> getRegisteredListeners() {
		return Collections.emptyList();
	}

	@Override
	public void handle(final Event event) {
		this.handle((Object) event);
	}

	public void handle(final Object event) {
		if (event instanceof AsyncPluginEvent || event instanceof Event) {
			this.executor.submit(() -> EventManager.this.handleEvent(event));
		} else {
			this.handleEvent(event);
		}
	}

	private void handleEvent(final Object event) {
		for (final Handler handler : this.handlers.values()) {
			handler.handle(event);
		}
	}

	@Override
	public void register(final Object o) {
		if (!this.handlers.keySet().contains(o)) {
			this.handlers.put(o, new Handler(this, o));
		}
	}

	public void shutdown() {
		MoreExecutors.shutdownAndAwaitTermination(this.executor, 10, TimeUnit.SECONDS);
	}

	@Override
	public void unregister(final Object o) {
		this.handlers.remove(o);
	}

}
