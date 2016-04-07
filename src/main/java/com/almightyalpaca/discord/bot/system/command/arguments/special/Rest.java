package com.almightyalpaca.discord.bot.system.command.arguments.special;

import com.almightyalpaca.discord.bot.system.command.arguments.Arguments;

public class Rest implements CharSequence {

	private final String text;

	public Rest(final Arguments args) {
		String string = "";
		while (args.hasNext()) {
			string += " " + args.removeNext();
		}
		this.text = string.replaceFirst(" ", "");
	}

	public Rest(final String string) {
		this.text = string;
	}

	@Override
	public char charAt(final int index) {
		return this.text.charAt(index);
	}

	public final String getString() {
		return this.text;
	}

	@Override
	public int length() {
		return this.text.length();
	}

	@Override
	public CharSequence subSequence(final int beginIndex, final int endIndex) {
		return this.text.subSequence(beginIndex, endIndex);
	}

}
