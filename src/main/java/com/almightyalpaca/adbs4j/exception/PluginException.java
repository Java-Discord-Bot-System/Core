package com.almightyalpaca.adbs4j.exception;

public class PluginException extends Exception {

	private static final long serialVersionUID = -3733583680854582146L;

	public PluginException() {
		super();
	}

	public PluginException(final String s) {
		super(s);
	}

	public PluginException(final String s, final Throwable t) {
		super(s, t);
	}

	public PluginException(final Throwable t) {
		super(t);
	}

}
