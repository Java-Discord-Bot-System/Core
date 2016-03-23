package com.almightyalpaca.discord.bot.system.extension;

public class CommandSyntaxException extends Exception {

	private static final long serialVersionUID = -3999361682077473247L;

	public CommandSyntaxException() {
		super();
	}

	public CommandSyntaxException(final String message) {
		super(message);
	}

	public CommandSyntaxException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public CommandSyntaxException(final Throwable cause) {
		super(cause);
	}

}
