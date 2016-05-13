package com.almightyalpaca.discord.bot.system.events.manager;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import net.dv8tion.jda.hooks.SubscribeEvent;

public class Handler {

	private final EventManager eventManager;

	private final Object object;

	private final Set<Triple<Class<?>, Method, Boolean>> methods;

	/**
	 * @param object
	 * @param methods
	 */
	public Handler(final EventManager eventManager, final Object object) {
		this.eventManager = eventManager;
		this.object = object;

		this.methods = new HashSet<>();

		final Class<? extends Object> clazz = object.getClass();

		for (final Method method : clazz.getDeclaredMethods()) {
			if (method.getParameterCount() == 1) {
				if (method.isAnnotationPresent(SubscribeEvent.class)) {
					method.setAccessible(true);
					this.methods.add(new ImmutableTriple<>(method.getParameterTypes()[0], method, false));
				} else if (method.isAnnotationPresent(EventHandler.class)) {
					method.setAccessible(true);
					final boolean async = method.getAnnotation(EventHandler.class).async();
					this.methods.add(new ImmutableTriple<>(method.getParameterTypes()[0], method, async));
				}
			}
		}
	}

	public void handle(final Object event) {
		for (final Triple<Class<?>, Method, Boolean> entry : this.methods) {
			if (entry.getLeft().isAssignableFrom(event.getClass())) {
				if (entry.getRight()) {
					this.eventManager.executeAsync(this.object, entry.getMiddle(), event);
				} else {
					this.eventManager.executeSync(this.object, entry.getMiddle(), event);

				}
			}
		}
	}
}
