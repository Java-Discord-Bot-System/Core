package com.almightyalpaca.adbs4j.internal.extension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import com.almightyalpaca.adbs4j.command.CommandHandler;
import com.almightyalpaca.adbs4j.command.arguments.parsers.CommandAgumentParsers;
import com.almightyalpaca.adbs4j.events.commands.CommandEvent;
import com.almightyalpaca.adbs4j.internal.CommandBuffer;

public class CommandMethod {

	final CommandExtension	commandExtension;
	final Method			method;

	public CommandMethod(final CommandExtension commandExtension, final Method method) throws InvalidCommandMethodException {
		this.commandExtension = commandExtension;
		this.method = method;
		if (method.getParameterCount() < 1) {
			throw new InvalidCommandMethodException("Invalid number arguments");
		}
		for (int i = 0; i < method.getParameterTypes().length; i++) {
			final Class<?> clazz = method.getParameterTypes()[i];
			if (i == 0) {
				if (!clazz.isAssignableFrom(CommandEvent.class)) {
					throw new InvalidCommandMethodException("CommandEvent has to be the first argumant!");
				}
			} else {
				if (!CommandAgumentParsers.isSupported(clazz)) {
					throw new InvalidCommandMethodException("Invalid command argument type: " + clazz.getName());
				}
			}
		}
		method.setAccessible(true);
	}

	public final CommandExtension getCommandExtension() {
		return this.commandExtension;
	}

	public final Method getMethod() {
		return this.method;
	}

	public Triple<Integer, Method, Object[]> parse(final CommandEvent event) throws CommandSyntaxException {
		try {
			final List<Object> arguments = new ArrayList<>(this.method.getParameterTypes().length);
			arguments.add(event);

			String commandWithoutName = StringUtils.replaceOnce(event.getCommandWithoutPrefix(), this.commandExtension.getCommandInfo().getName(), "");
			if (commandWithoutName.startsWith(" ")) {
				commandWithoutName = commandWithoutName.substring(1);
			}

			final CommandBuffer buffer = new CommandBuffer(commandWithoutName);

			final Class<?>[] parameters = this.method.getParameterTypes();

			for (int i = 1; i < parameters.length; i++) {
				if (buffer.isEmpty()) {
					throw new CommandSyntaxException();
				}
				final Class<?> clazz = parameters[i];
				arguments.add(CommandAgumentParsers.getParser(clazz).get(event.getMessage(), buffer));
			}
			if (this.method.isVarArgs()) {
				final Class<?> clazz = parameters[parameters.length - 1];
				while (!buffer.isEmpty()) {
					arguments.add(CommandAgumentParsers.getParser(clazz).get(event.getMessage(), buffer));
				}
			}
			if (!buffer.isEmpty()) {
				throw new CommandSyntaxException();
			}

			return new ImmutableTriple<>(this.method.getAnnotation(CommandHandler.class).priority(), this.method, arguments.toArray());

		} catch (final Exception e) {
			throw new CommandSyntaxException(e);
		}
	}

}