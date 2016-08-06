package com.almightyalpaca.adbs4j.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class StringUtil {

	public static final Pattern	MULTI_WHITESPACE_PATTERN	= Pattern.compile("\\s+");
	public static final Pattern	SINGLE_WHITESPACE_PATTERN	= Pattern.compile("\\s");

	/**
	 * Thanks StackOverflow
	 * 
	 * @see <a href=
	 *      "http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java">http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java</a>
	 */
	public static boolean isInteger(final String str) {
		if (str == null) {
			return false;
		}
		final int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			final char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public static void replaceAll(final StringBuilder builder, final String from, final String to) {
		int index;
		while ((index = builder.indexOf(from)) != -1) {
			builder.replace(index, index + from.length(), to);
		}

	}

	public static String replaceFirst(final String text, final String searchString, final String replacement) {
		return org.apache.commons.lang3.StringUtils.replaceOnce(text, searchString, replacement);
	}

	/**
	 * Thanks StackOverflow
	 * 
	 * @see <a href="http://stackoverflow.com/questions/2282728/java-replacelast">http://stackoverflow.com/questions/2282728/java-replacelast</a>
	 */
	public static String replaceLast(final String string, final String toReplace, final String replacement) {
		final int pos = string.lastIndexOf(toReplace);
		if (pos > -1) {
			return string.substring(0, pos) + replacement + string.substring(pos + toReplace.length(), string.length());
		} else {
			return string;
		}
	}

	public static String[] split(String string, final int lenth, final String split) {
		Objects.requireNonNull(string);
		if (string.length() == 0) {
			return new String[] {};
		} else if (string.length() == 1) {
			return new String[] { string };
		} else if (string.length() <= lenth) {
			return new String[] { string };
		}
		final List<String> strings = new ArrayList<>();

		while (string.length() > lenth) {
			final String current = string.substring(0, lenth + split.length());

			final int index = current.lastIndexOf(split);

			if (index == -1) {
				throw new UnsupportedOperationException("One or more substrings were too long!");
			}

			final String substring = current.substring(0, index);

			strings.add(substring);
			string = StringUtil.replaceFirst(string, substring + split, "");

		}

		return strings.toArray(new String[strings.size()]);
	}

	public static String toPrettyString(final Iterable<?> collection) {
		String string = "";

		for (final Object object : collection) {
			string += Objects.toString(object) + ", ";
		}
		return StringUtil.replaceLast(string, ", ", "");
	}

}
