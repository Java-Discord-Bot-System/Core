package com.almightyalpaca.adbs4j.exception;

public class CommandInitialisationException extends RuntimeException {

	private static final long serialVersionUID = -6243407699473779072L;

	public CommandInitialisationException() {
		super();
	}

	public CommandInitialisationException(final String s) {
		super(s);
	}

	public CommandInitialisationException(final String s, final Throwable t) {
		super(s, t);
	}

	public CommandInitialisationException(final Throwable t) {
		super(t);
	}

}
