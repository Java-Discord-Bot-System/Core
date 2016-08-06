package com.almightyalpaca.adbs4j.bootstrap;

import com.almightyalpaca.adbs4j.bootstrap.exithandler.*;

public enum Code {
	UNKNOWN(-2, new UnknownHandler()),
	FIRST_START(-1, null),
	SHUTDOWN(100, new ShutdownHandler()),
	RESTART(101, new RestartHandler()),
	ERROR(102, new ErrorHandler()),
	UPDATE(102, new UpdateHandler());

	private final int					code;

	private final AbstractExitHandler	handler;

	private Code(final int code, final AbstractExitHandler handler) {
		this.code = code;
		this.handler = handler;
	}

	public static Code get(final int c) {
		for (final Code code : Code.values()) {
			if (code.getCode() == c) {
				return code;
			}
		}
		return UNKNOWN;
	}

	public final int getCode() {
		return this.code;
	}

	public final AbstractExitHandler getHandler() {
		return this.handler;
	}

}
