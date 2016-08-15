package com.almightyalpaca.adbs4j.command.arguments;

import java.util.Objects;
import java.util.stream.IntStream;

public class Text implements CharSequence {

	private final String text;

	public Text(final String text) {
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
	public Text clone() {
		return new Text(this.text);
	}

	@Override
	public IntStream codePoints() {
		return this.text.codePoints();
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Text)) {
			return false;
		} else {
			return this.text.equals(((Text) obj).text);
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
