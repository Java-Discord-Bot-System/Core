package com.almightyalpaca.adbs4j.internal.extension;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.util.concurrent.MoreExecutors;

import com.almightyalpaca.adbs4j.command.Command;
import com.almightyalpaca.adbs4j.command.CommandInfo;
import com.almightyalpaca.adbs4j.events.commands.CommandEvent;
import com.almightyalpaca.adbs4j.events.commands.CommandExecutionEvent;
import com.almightyalpaca.adbs4j.events.commands.CommandRegisteredEvent;
import com.almightyalpaca.adbs4j.events.commands.CommandUnregisteredEvent;
import com.almightyalpaca.adbs4j.events.manager.EventHandler;
import com.almightyalpaca.adbs4j.internal.commands.CategoryHelpCommand;
import com.almightyalpaca.adbs4j.internal.commands.HelpCommand;
import com.almightyalpaca.adbs4j.internal.commands.RootHelpCommand;
import com.almightyalpaca.adbs4j.storage.CachedStorageProviderInstance;

import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandExtensionManager {

	final ExtensionManager						extensionManager;
	/**
	 * Map which holds all commands
	 */
	public final Map<String, CommandExtension>	commands;

	public final Map<String, Integer>			categories;

	final ExecutorService						executor;

	private final HelpCommand					helpCommand;
	private String								mention;

	public CommandExtensionManager(final ExtensionManager extensionManager) {
		this.extensionManager = Objects.requireNonNull(extensionManager);
		this.commands = new ConcurrentHashMap<>();
		this.categories = new ConcurrentHashMap<>();
		this.executor = new ThreadPoolExecutor(1, 10, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(), r -> {
			final Thread thread = new Thread(r, "CommandExecution-Thread");
			thread.setPriority(Thread.NORM_PRIORITY + 1);
			return thread;
		});

		this.helpCommand = new RootHelpCommand(this);
		this.register(new CommandExtension(this, this.helpCommand));
	}

	public void executeAsync(final Command command, final Method method, final Object[] args) {
		this.executor.submit(() -> {
			this.executeSync(command, method, args);
		});
	}

	public void executeSync(final Command command, final Method method, final Object[] args) {
		try {
			method.invoke(command, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
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

	private List<String> getPrefix(final MessageReceivedEvent event) {
		final List<String> prefixes = new ArrayList<>();
		final CachedStorageProviderInstance storage = this.extensionManager.getGlobalStorageProvider().cached();
		String prefix = storage.getString(300, "prefix");
		final String msg = event.getMessage().getRawContent();
		if (prefix == null) {
			prefix = event.getJDA().getSelfInfo().getAsMention() + " ";
			storage.putString("prefix", prefix);
		}
		if (msg.startsWith(prefix)) {
			prefixes.add(prefix);
		} else if (msg.startsWith(this.mention)) {
			prefixes.add(this.mention);
		} else if (!event.isPrivate()) {
			final Set<String> guildPrefixes = storage.getSet(300, "prefixes.guild." + event.getGuild().getId());
			if (!guildPrefixes.isEmpty()) {
				for (final String s : guildPrefixes) {
					if (msg.startsWith(s)) {
						prefixes.add(s);
					}
				}
			}
		}

		return prefixes;
	}

	private int nullTo0(final Integer integer) {
		return integer == null ? 0 : integer;
	}

	@EventHandler
	private void onMessageReceived(final MessageReceivedEvent event) {
		if (!event.getAuthor().isBot() && !event.getAuthor().equals(event.getJDA().getSelfInfo())) {
			final List<String> prefixes = this.getPrefix(event);
			if (prefixes != null) {

				String commandWithoutPrefix = null;
				String commandName = null;
				CommandExtension commandExtension = null;
				String prefix = null;
				final Iterator<String> iterator = prefixes.iterator();
				while (iterator.hasNext()) {
					prefix = iterator.next();
					commandWithoutPrefix = StringUtils.replaceOnce(event.getMessage().getRawContent(), prefix, "");
					commandName = commandWithoutPrefix.substring(0, commandWithoutPrefix.indexOf(" ") == -1 ? commandWithoutPrefix.length() : commandWithoutPrefix.indexOf(" "));
					commandExtension = this.commands.get(commandName);
					if (commandExtension != null) {
						break;
					}
				}
				if (commandExtension != null && commandExtension.command.canExecute(this.extensionManager.getRequirmentManager(), event.getChannel(), event.getAuthor())) {
					final CommandExecutionEvent commandExecutionEvent = new CommandExecutionEvent(this.extensionManager, commandExtension.command, event);
					commandExecutionEvent.fire();
					if (!commandExecutionEvent.isCancelled()) {
						final CommandEvent commandEvent = new CommandEvent(this.extensionManager, event, prefix);
						commandEvent.fire();
						commandExtension.execute(commandEvent);
					}
				}
			}
		}
	}

	@EventHandler
	private void onReady(final ReadyEvent event) {
		this.mention = event.getJDA().getSelfInfo().getAsMention() + " ";
	}

	public boolean register(final CommandExtension command) {
		Objects.requireNonNull(command);
		System.out.println("Registering " + command.command.getClass().getName() + " as " + command.getCommandInfo().getName());
		if (this.commands.containsKey(command.getCommandInfo().getName())) {
			return false;
		} else {
			final String category = command.getCommandInfo().getCategory();
			if (category != null) {
				this.categories.put(category, this.nullTo0(this.categories.get(category)) + 1);
				CommandExtension extension = this.commands.get(category);
				if (extension == null) {
					extension = new CommandExtension(this, new CategoryHelpCommand(this, category));
					this.register(extension);
				} else if (!(extension.command instanceof HelpCommand)) {
					System.out.println("Could not register help command! Name already blocked by " + extension.command.getClass());
				}
			}
			this.commands.put(command.getCommandInfo().getName(), command);
			new CommandRegisteredEvent(this.extensionManager, command.getCommandInfo()).fire();
			return true;
		}
	}

	public void shutdown() {
		MoreExecutors.shutdownAndAwaitTermination(this.executor, 10, TimeUnit.SECONDS);
	}

	public boolean unregister(final CommandExtension command) {
		Objects.requireNonNull(command);
		if (this.commands.remove(command.getCommandInfo().getName(), command)) {
			final String category = command.getCommandInfo().getCategory();
			if (category != null) {
				final int entries = this.categories.get(category) - 1;
				this.categories.put(category, entries);
				if (entries == 0) {
					this.unregister(this.commands.get(category));
				}
			}
			new CommandUnregisteredEvent(this.extensionManager, command.getCommandInfo()).fire();
			return true;
		} else {
			return false;
		}
	}
}
