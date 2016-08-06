package com.almightyalpaca.adbs4j.exception;

public class PluginLoadingException extends PluginException {

	private static final long serialVersionUID = 5572125208284064063L;

	public PluginLoadingException() {
		super();
	}

	public PluginLoadingException(final String s) {
		super(s);
	}

	public PluginLoadingException(final String s, final Throwable t) {
		super(s, t);
	}

	public PluginLoadingException(final Throwable t) {
		super(t);
	}
}
