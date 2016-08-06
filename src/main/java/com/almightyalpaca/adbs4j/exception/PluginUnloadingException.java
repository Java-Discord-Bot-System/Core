package com.almightyalpaca.adbs4j.exception;

public class PluginUnloadingException extends PluginException {

	private static final long serialVersionUID = 1225379880867199238L;

	public PluginUnloadingException() {
		super();
	}

	public PluginUnloadingException(final String s) {
		super(s);
	}

	public PluginUnloadingException(final String s, final Throwable t) {
		super(s, t);
	}

	public PluginUnloadingException(final Throwable t) {
		super(t);
	}
}
