package com.almightyalpaca.discord.bot.system.events;

import com.almightyalpaca.discord.bot.system.command.Command;
import com.almightyalpaca.discord.bot.system.extension.ExtensionEvent;
import com.almightyalpaca.discord.bot.system.extension.ExtensionManager;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

public final class PermissionEvent extends PluginEvent {

	private final Command		command;
	private final CommandEvent	commandEvent;
	private boolean				result	= true;

	public PermissionEvent(final ExtensionManager manager, final Command command, final CommandEvent commandEvent) {
		super(manager);
		this.command = command;
		this.commandEvent = commandEvent;
	}

	public PermissionEvent(final Plugin plugin, final Command command, final CommandEvent commandEvent) {
		this(ExtensionEvent.getExtensionManager(plugin), command, commandEvent);
	}

	public User getAuthor() {
		return this.commandEvent.getMessage().getAuthor();
	}

	public MessageChannel getChannel() {
		return this.commandEvent.getChannel();
	}

	public CommandEvent getCommandEvent() {
		return this.commandEvent;
	}

	public String getCommandName() {
		return this.command.getInfo().getName();
	}

	public Guild getGuild() {
		return this.commandEvent.getGuild();
	}

	public PrivateChannel getPrivateChannel() {
		return this.commandEvent.getPrivateChannel();
	}

	public boolean getResult() {
		return this.result;
	}

	public TextChannel getTextChannel() {
		return this.commandEvent.getTextChannel();
	}

	public boolean isPrivate() {
		return this.commandEvent.getMessage().isPrivate();
	}

	public void setResult(final boolean result) {
		this.result = result;
	}

}
