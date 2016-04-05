package com.almightyalpaca.discord.bot.system.extension;

import java.lang.reflect.Method;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import com.almightyalpaca.discord.bot.system.command.annotation.CommandHandler;
import com.almightyalpaca.discord.bot.system.command.arguments.Arguments;
import com.almightyalpaca.discord.bot.system.command.arguments.parsers.CommandAgumentParsers;
import com.almightyalpaca.discord.bot.system.events.CommandEvent;

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

	public Triple<Integer, Method, Object[]> parse(final CommandEvent event) throws CommandSyntaxException { // TODO make handling for unknown syntax better
		try {
			final Object[] arguments = new Object[this.method.getParameterTypes().length];
			arguments[0] = event;
			String commandWithoutName = event.getCommandWithoutPrefix().replaceFirst(this.commandExtension.getCommandInfo().getName(), "");
			if (commandWithoutName.startsWith(" ")) {
				commandWithoutName = commandWithoutName.replaceFirst(" ", "");
			}
			final Arguments args = Arguments.create(commandWithoutName);
			for (int i = 1; i < this.method.getParameterTypes().length; i++) {
				if (args.isEmpty()) {
					throw new CommandSyntaxException();
				}
				final Class<?> clazz = this.method.getParameterTypes()[i];
				arguments[i] = CommandAgumentParsers.getParser(clazz).get(event.getMessage(), args);
			}
			if (args.hasNext()) {
				throw new CommandSyntaxException();
			}

			return new ImmutableTriple<Integer, Method, Object[]>(this.method.getAnnotation(CommandHandler.class).priority(), this.method, arguments);

		} catch (final Exception e) {
			throw new CommandSyntaxException(e);
		}
	}

}