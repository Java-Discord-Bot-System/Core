package com.almightyalpaca.discord.bot.system.extension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.almightyalpaca.discord.bot.system.command.Command;
import com.almightyalpaca.discord.bot.system.command.CommandInfo;
import com.almightyalpaca.discord.bot.system.events.commands.CommandEvent;
import com.almightyalpaca.discord.bot.system.events.commands.CommandExecutionEvent;
import com.almightyalpaca.discord.bot.system.events.commands.CommandPrefixEvent;
import com.almightyalpaca.discord.bot.system.events.commands.GuildCommandPrefixEvent;
import com.almightyalpaca.discord.bot.system.events.commands.PrivateCommandPrefixEvent;
import com.almightyalpaca.discord.bot.system.events.manager.EventHandler;

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
		this.executor = new ThreadPoolExecutor(1, 10, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), r -> {
			final Thread thread = new Thread(r, "CommandExecution-Thread");
			thread.setPriority(Thread.NORM_PRIORITY + 1);
			return thread;
		});
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

	public final Set<CommandExtension> getCommandExtensions() {
		return new HashSet<>(this.commands.values());
	}

	public final Set<CommandInfo> getCommandInfos() {
		return this.commands.values().stream().map(e -> e.command.getInfo()).collect(Collectors.toSet());
	}

	public final Set<Command> getCommands() {
		return this.commands.values().stream().map(e -> e.command).collect(Collectors.toSet());
	}

	@EventHandler
	private void onMessageReceived(final MessageReceivedEvent event) {

		final CommandPrefixEvent commandPrefixEvent;
		if (event.isPrivate()) {
			commandPrefixEvent = new PrivateCommandPrefixEvent(this.extensionManager, event.getAuthor());
		} else {
			commandPrefixEvent = new GuildCommandPrefixEvent(this.extensionManager, event.getGuild());
		}
		commandPrefixEvent.fire();

		for (final String prefix : commandPrefixEvent.getPrefixes()) {
			if (event.getMessage().getRawContent().startsWith(prefix)) {

				final String commandWithoutPrefix = event.getMessage().getRawContent().replaceFirst(prefix, "");

				final String commandName = commandWithoutPrefix.substring(0, commandWithoutPrefix.indexOf(" ") == -1 ? commandWithoutPrefix.length() : commandWithoutPrefix.indexOf(" "));

				final CommandExtension commandExtension = this.commands.get(commandName);

				final CommandExecutionEvent commandExecutionEvent = new CommandExecutionEvent(this.extensionManager, commandExtension.command, event);
				commandExecutionEvent.fire();

				if (!commandExecutionEvent.isCancelled()) {
					final CommandEvent commandEvent = new CommandEvent(this.extensionManager, event, prefix);
					commandEvent.fire();
					commandExtension.execute(commandEvent);
				}

				break;
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
