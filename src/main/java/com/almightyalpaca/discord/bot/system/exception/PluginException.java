package com.almightyalpaca.discord.bot.system.exception;

public class PluginException extends Exception {

	private static final long serialVersionUID = -3733583680854582146L;

	public PluginException(final String string) {
		super(string);
	}

	public PluginException(final Throwable throwable) {
		super(throwable);
	}

}
