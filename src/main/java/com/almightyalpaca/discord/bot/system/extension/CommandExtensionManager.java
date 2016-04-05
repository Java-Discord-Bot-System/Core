package com.almightyalpaca.discord.bot.system.extension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.almightyalpaca.discord.bot.system.command.Command;
import com.almightyalpaca.discord.bot.system.events.CommandEvent;
import com.almightyalpaca.discord.bot.system.events.PermissionEvent;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandExtensionManager {

	final ExtensionManager extensionManager;

	/**
	 * Map which holds all commands
	 */
	final Map<String, CommandExtension> commands;

	final ExecutorService executor;

	public CommandExtensionManager(final ExtensionManager extensionManager) {
		this.extensionManager = extensionManager;
		this.commands = new HashMap<>();
		this.executor = new ThreadPoolExecutor(1, 10, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), (ThreadFactory) r -> {
			final Thread thread = new Thread(r, "CommandExecution-Thread");
			thread.setPriority(Thread.NORM_PRIORITY + 1);
			return thread;
		});
	}

	private boolean checkPermission(final CommandExtension command, final CommandEvent commandEvent) {
		final PermissionEvent event = new PermissionEvent(this.extensionManager, command.command, commandEvent);
		event.fire();
		return event.getResult();
	}

	public void executeAsync(final Command command, final Method method, final Object[] args) {
		this.executor.submit(() -> {
			try {
				method.invoke(command, args);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		});
	}

	public void onCommand(final MessageReceivedEvent event) {
		final CommandEvent commandEvent = new CommandEvent(this, event);

		final String commandName = commandEvent.getCommandWithoutPrefix().substring(0,
			commandEvent.getCommandWithoutPrefix().indexOf(" ") == -1 ? commandEvent.getCommandWithoutPrefix().length() : commandEvent.getCommandWithoutPrefix().indexOf(" "));

		final CommandExtension command = this.commands.get(commandName);

		if (command == null) {
			// TODO fire event
		} else {
			if (this.checkPermission(command, commandEvent)) {
				// TODO Fire acess granted event
				command.execute(commandEvent);
			} else {
				// TODO Fire acess denied event
			}
		}

	}

	public boolean register(final CommandExtension command) {
		if (this.commands.containsKey(command.getCommandInfo().getName())) {
			// TODO fire event
			return false;
		} else {
			this.commands.put(command.getCommandInfo().getName(), command);
			// TODO fire event
			return true;
		}
	}

	public void shutdown() {
		this.executor.shutdownNow();
	}

	public boolean unregister(final CommandExtension command) {
		if (this.commands.remove(command.getCommandInfo().getName(), command)) {
			// TODO fire event
			return true;
		} else {
			// TODO fire event
			return false;
		}
	}

}
