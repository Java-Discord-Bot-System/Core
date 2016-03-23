package com.almightyalpaca.discord.bot.system.command.arguments;

public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 2012413614738028339L;

	public ParseException() {
		super();
	}

	public ParseException(final String s) {
		super(s);
	}

	public ParseException(final String s, final Throwable t) {
		super(s, t);
	}

	public ParseException(final Throwable t) {
		super(t);
	}
}
