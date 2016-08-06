package com.almightyalpaca.adbs4j.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.almightyalpaca.adbs4j.util.StringUtil;

public class CommandBuffer {

	private final StringBuffer buffer;

	public CommandBuffer(final String text) {
		this.buffer = new StringBuffer(text.length());
		this.buffer.replace(0, text.length(), text);
	}

	public String get(final int part) {
		return StringUtil.SINGLE_WHITESPACE_PATTERN.split(this.buffer)[part];
	}

	public String getNext() {
		final int index = this.indexOfPattern(StringUtil.SINGLE_WHITESPACE_PATTERN);
		final String string = this.buffer.substring(0, index);
		return string;
	}

	public int indexOfPattern(final Pattern pattern) {
		final Matcher matcher = pattern.matcher(this.buffer);
		if (matcher.find()) {
			return matcher.start();
		} else {
			return -1;
		}
	}

	public boolean isEmpty() {
		return this.buffer.length() == 0;
	}

	public int lenght() {
		return this.buffer.length();
	}

	public int parts() {
		return this.buffer.toString().split("\\s").length;
	}

	public String remove(final int lenght) {
		final String string = this.buffer.substring(0, lenght);
		this.buffer.delete(0, lenght);
		return string;
	}

	public String removeNext() {
		int index = this.indexOfPattern(StringUtil.SINGLE_WHITESPACE_PATTERN);
		if (index == -1) {
			index = this.lenght();
		}
		final String string = this.buffer.substring(0, index);
		this.buffer.delete(0, index + 1);
		return string;
	}

	public boolean startsWith(final String string) {
		return this.buffer.toString().startsWith(string);
	}

	@Override
	public String toString() {
		return this.buffer.toString();
	}

}
