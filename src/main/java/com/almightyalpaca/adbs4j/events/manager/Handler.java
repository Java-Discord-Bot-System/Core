package com.almightyalpaca.adbs4j.events.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import net.dv8tion.jda.hooks.SubscribeEvent;

public class Handler {

	private final Object						object;

	private final Set<Pair<Class<?>, Method>>	methods;

	public Handler(final EventManager eventManager, final Object object) {
		this.object = object;

		this.methods = new HashSet<>();

		final Class<? extends Object> clazz = object.getClass();

		for (final Method method : clazz.getDeclaredMethods()) {
			if (method.getParameterCount() == 1) {
				if (method.isAnnotationPresent(SubscribeEvent.class) || method.isAnnotationPresent(EventHandler.class)) {
					method.setAccessible(true);
					this.methods.add(new ImmutablePair<>(method.getParameterTypes()[0], method));
				}
			}
		}
	}

	public void handle(final Object event) {
		for (final Pair<Class<?>, Method> entry : this.methods) {
			if (entry.getLeft().isAssignableFrom(event.getClass())) {
				try {
					entry.getRight().invoke(this.object, event);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
