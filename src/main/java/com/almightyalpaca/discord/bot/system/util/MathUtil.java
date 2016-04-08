package com.almightyalpaca.discord.bot.system.util;

public class MathUtil {

	public static double limit(final double min, final double value, final double max) {
		return Math.max(min, Math.min(value, max));
	}

	public static float limit(final float min, final float value, final float max) {
		return Math.max(min, Math.min(value, max));
	}

	public static int limit(final int min, final int value, final int max) {
		return Math.max(min, Math.min(value, max));
	}

	public static long limit(final long min, final long value, final long max) {
		return Math.max(min, Math.min(value, max));
	}

}
