package com.almightyalpaca.adbs4j.internal.extension;

import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.lang3.tuple.Triple;

import com.almightyalpaca.adbs4j.command.Command;
import com.almightyalpaca.adbs4j.command.CommandHandler;
import com.almightyalpaca.adbs4j.command.CommandInfo;
import com.almightyalpaca.adbs4j.events.commands.CommandEvent;

public class CommandExtension {

	final Command					command;

	final Set<CommandMethod>		commandMethods;

	final CommandExtensionManager	commandManager;

	public CommandExtension(final CommandExtensionManager commandManager, final Command command) {
		this.commandManager = commandManager;
		this.command = command;
		this.commandMethods = new HashSet<>();
		for (final Method method : command.getClass().getMethods()) {
			if (method.isAnnotationPresent(CommandHandler.class)) {
				try {
					final CommandMethod cMethod = new CommandMethod(this, method);
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
			if (event.getMessage().isPrivate() && !commandMethod.method.getAnnotation(CommandHandler.class).dm() || !event.getMessage().isPrivate() && !commandMethod.method.getAnnotation(
					CommandHandler.class).guild()) {
				continue;
			}
			try {
				list.add(commandMethod.parse(event));
			} catch (final CommandSyntaxException e) {
				continue;
			}
		}

		list.sort((t1, t2) -> Integer.compare(t1.getLeft(), t2.getLeft()));

		if (list.isEmpty()) {
			this.command.unknownSyntax(event);
			return;
		}

		final Triple<Integer, Method, Object[]> commandMethod = list.get(list.size() - 1);

		this.commandManager.executeAsync(this.command, commandMethod.getMiddle(), commandMethod.getRight());

	}

	public final Command getCommand() {
		return this.command;
	}

	public final CommandInfo getCommandInfo() {
		return this.command.getInfo();
	}

	public final Set<CommandMethod> getCommandMethods() {
		return Collections.unmodifiableSet(this.commandMethods);
	}

}