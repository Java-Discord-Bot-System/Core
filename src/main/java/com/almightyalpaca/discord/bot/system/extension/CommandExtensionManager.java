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

import com.almightyalpaca.discord.bot.system.command.ICommand;
import com.almightyalpaca.discord.bot.system.events.CommandEvent;
import com.almightyalpaca.discord.bot.system.events.PermissionEvent;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandExtensionManager {
	private final ExtensionManager				manager;

	/**
	 * Mpa which holds all commands
	 */
	private final Map<String, CommandExtension>	commands;

	private final ExecutorService				executor;

	public CommandExtensionManager(final ExtensionManager manager) {
		this.manager = manager;
		this.commands = new HashMap<>();
		this.executor = new ThreadPoolExecutor(1, 10, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
			@Override
			public Thread newThread(final Runnable r) {
				final Thread thread = new Thread(r, "CommandExecution-Thread");
				thread.setPriority(Thread.NORM_PRIORITY + 1);
				return thread;
			}
		});
	}

	private boolean checkPermission(final CommandExtension command, final CommandEvent commandEvent) {
		final PermissionEvent event = new PermissionEvent(this.manager, command.command, commandEvent);
		event.fire();
		return event.getResult();
	}

	public void executeCommand(final ICommand command, final Method method, final Object[] args) {
		this.executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					method.invoke(command, args);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public final String getPrefix() {
		return this.manager.getPrefix();
	}

	public void onCommand(final MessageReceivedEvent event) {
		final CommandEvent commandEvent = new CommandEvent(this, event);

		final String commandName = commandEvent.getCommandWithoutPrefix().substring(0, commandEvent.getCommandWithoutPrefix().indexOf(" ") == -1 ? commandEvent.getCommandWithoutPrefix().length()
				: commandEvent.getCommandWithoutPrefix().indexOf(" "));

		final CommandExtension command = this.commands.get(commandName);

		if (command == null) {
			System.out.println("Command " + commandName + " not registered !");
		} else {
			if (this.checkPermission(command, commandEvent)) { // FIXME this doesn't work anymore !!!
				System.out.println("Access granted!");
				command.execute(commandEvent);
			} else {
				System.out.println("Access denied!");
			}
		}

	}

	public void register(final CommandExtension command) {
		if (this.commands.containsKey(command.getCommandName())) {
			System.out.println("Command " + command.getCommandName() + " already exists!");
		} else {
			this.commands.put(command.getCommandName(), command);
		}
	}

	public void shutdown() {
		this.executor.shutdownNow();
	}

	public void unregister(final CommandExtension command) {
		if (this.commands.remove(command.getCommandName(), command)) {
			System.out.println("Unregistered command " + command.getCommandName() + " !");
		} else {
			System.out.println("Command " + command.getCommandName() + " not registered !");
		}
	}

}
