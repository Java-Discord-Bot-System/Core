package com.almightyalpaca.discord.bot.system.events.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class Handler {
	private final Object						object;

	private final Set<Pair<Class<?>, Method>>	methods;

	/**
	 * @param object
	 * @param methods
	 */
	public Handler(final Object object) {
		this.object = object;

		this.methods = new HashSet<>();

		final Class<? extends Object> clazz = object.getClass();

		for (final Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(EventHandler.class) && (method.getParameterCount() == 1)) {
				this.methods.add(new ImmutablePair<>(method.getParameterTypes()[0], method));
			}
		}

	}

	public Pair<Object, Set<Method>> getMethods(final Class<?> clazz) {
		final Set<Method> temp = new HashSet<Method>();

		for (final Pair<Class<?>, Method> entry : this.methods) {
			if (entry.getKey().isAssignableFrom(clazz)) {
				temp.add(entry.getValue());
			}
		}

		return new ImmutablePair<Object, Set<Method>>(this.object, temp);

	}

	public Object getObject() {
		return this.object;
	}

	public void handle(final Object event) {
		for (final Pair<Class<?>, Method> entry : this.methods) {
			if (entry.getKey().isAssignableFrom(event.getClass())) {
				try {
					entry.getValue().invoke(this.object, event);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
