package com.almightyalpaca.discord.bot.system.extension;

public class InvalidCommandMethodException extends Exception {

	private static final long serialVersionUID = -3528936025909025067L;

	public InvalidCommandMethodException() {
		super();
	}

	public InvalidCommandMethodException(final String message) {
		super(message);
	}

	public InvalidCommandMethodException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public InvalidCommandMethodException(final Throwable cause) {
		super(cause);
	}

}
