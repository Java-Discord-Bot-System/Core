package com.almightyalpaca.adbs4j;

public enum ExitCode {
	UNKNOWN(-2),
	FIRST_START(-1),
	SHUTDOWN(100),
	RESTART(101),
	ERROR(102),
	UPDATE(102);

	private final int code;

	private ExitCode(final int code) {
		this.code = code;
	}

	public static ExitCode get(final int c) {
		for (final ExitCode code : ExitCode.values()) {
			if (code.getCode() == c) {
				return code;
			}
		}
		return UNKNOWN;
	}

	public final int getCode() {
		return this.code;
	}

}
