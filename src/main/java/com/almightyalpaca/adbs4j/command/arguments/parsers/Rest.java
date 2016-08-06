package com.almightyalpaca.adbs4j.command.arguments.parsers;

import java.util.Objects;
import java.util.stream.IntStream;

public class Rest implements CharSequence {

	private final String text;

	public Rest(final String text) {
		this.text = Objects.requireNonNull(text);
	}

	@Override
	public char charAt(final int index) {
		return this.text.charAt(index);
	}

	@Override
	public IntStream chars() {
		return this.text.chars();
	}

	@Override
	public Rest clone() {
		return new Rest(this.text);
	}

	@Override
	public IntStream codePoints() {
		return this.text.codePoints();
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Rest)) {
			return false;
		} else {
			return this.text.equals(((Rest) obj).text);
		}
	}

	@Override
	public int hashCode() {
		return this.text.hashCode();
	}

	@Override
	public int length() {
		return this.text.length();
	}

	@Override
	public CharSequence subSequence(final int beginIndex, final int endIndex) {
		return this.text.subSequence(beginIndex, endIndex);
	}

	@Override
	public String toString() {
		return this.text;
	}
}
