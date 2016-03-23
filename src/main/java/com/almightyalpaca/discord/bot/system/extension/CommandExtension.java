package com.almightyalpaca.discord.bot.system.extension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import com.almightyalpaca.discord.bot.system.command.ICommand;
import com.almightyalpaca.discord.bot.system.command.annotation.Command;
import com.almightyalpaca.discord.bot.system.command.arguments.Arguments;
import com.almightyalpaca.discord.bot.system.command.arguments.parsers.CommandAgumentParsers;
import com.almightyalpaca.discord.bot.system.events.CommandEvent;

public class CommandExtension {

	private class CommandMethod {
		private final Method method;

		public CommandMethod(final Method method) throws InvalidCommandMethodException {
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
				String commandWithoutName = event.getCommandWithoutPrefix().replaceFirst(CommandExtension.this.getCommandName(), "");
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

				return new ImmutableTriple<Integer, Method, Object[]>(this.method.getAnnotation(Command.class).priority(), this.method, arguments);

			} catch (final Exception e) {
				throw new CommandSyntaxException(e);
			}
		}

	}

	final ICommand							command;

	private final List<CommandMethod>		commandMethods;

	private final CommandExtensionManager	commandManager;

	public CommandExtension(final CommandExtensionManager commandManager, final ICommand command) {
		this.commandManager = commandManager;
		this.command = command;
		this.commandMethods = new ArrayList<>();
		for (final Method method : command.getClass().getDeclaredMethods()) {
			if (method.isAnnotationPresent(Command.class)) {
				try {
					final CommandMethod cMethod = new CommandMethod(method);
					this.commandMethods.add(cMethod);
				} catch (final InvalidCommandMethodException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void execute(final CommandEvent event) {
		final List<Triple<Integer, Method, Object[]>> list = new ArrayList<>();
		for (final CommandMethod commandMethod : this.commandMethods) {
			if ((event.getMessage().isPrivate() && !commandMethod.method.getAnnotation(Command.class).dm()) || (!event.getMessage().isPrivate() && !commandMethod.method.getAnnotation(Command.class)
					.guild())) {
				continue;
			}
			try {
				list.add(commandMethod.parse(event));
			} catch (final CommandSyntaxException e) {
				continue;
			}
		}

		list.sort(new Comparator<Triple<Integer, Method, Object[]>>() {

			@Override
			public int compare(final Triple<Integer, Method, Object[]> t1, final Triple<Integer, Method, Object[]> t2) {
				return Integer.compare(t1.getLeft(), t2.getLeft());
			}
		});

		if (list.isEmpty()) {
			this.command.unknownSyntax(event);
			return;
		}

		final Triple<Integer, Method, Object[]> commandMethod = list.get(list.size() - 1);

		if (commandMethod.getMiddle().getAnnotation(Command.class).async()) {
			this.commandManager.executeCommand(this.command, commandMethod.getMiddle(), commandMethod.getRight());
		} else {
			try {
				commandMethod.getMiddle().invoke(this.command, commandMethod.getRight());
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

	public final ICommand getCommand() {
		return this.command;
	}

	public final String getCommandDescription() {
		return this.command.getDescription();
	}

	public final String getCommandHelp() {
		return this.command.getHelp();
	}

	public final String getCommandName() {
		return this.command.getName();
	}
}