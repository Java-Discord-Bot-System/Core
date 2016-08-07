package com.almightyalpaca.adbs4j.bootstrap;

import com.almightyalpaca.adbs4j.bootstrap.exithandler.ErrorHandler;
import com.almightyalpaca.adbs4j.bootstrap.exithandler.IExitHandler;
import com.almightyalpaca.adbs4j.bootstrap.exithandler.RestartHandler;
import com.almightyalpaca.adbs4j.bootstrap.exithandler.UpdateHandler;

public enum Code {
	UNKNOWN(-2, bootstrap -> System.out.println("UNKNOWN exit value received. Maybe the process was killed? Won't restart!")) {},

	FIRST_START(-1, null),

	SHUTDOWN(20, bootstrap -> System.out.println("SHUTDOWN exit value received. Won't restart!")),
	RESTART(21, new RestartHandler()),
	UPDATE(22, new UpdateHandler()),

	ERROR_RESTART(30, new ErrorHandler()),
	ERROR_DO_NOT_RESTART(31, bootstrap -> System.out.println("ERROR_DO_NOT_RESTART exit value received. Won't restart!"));

	private final int			code;

	private final IExitHandler	handler;

	private Code(final int code, final IExitHandler handler) {
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

	public final IExitHandler getHandler() {
		return this.handler;
	}

}
