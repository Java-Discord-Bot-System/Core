package com.almightyalpaca.discord.bot.system.command.arguments.parsers;

import java.util.HashMap;
import java.util.Map.Entry;

import com.almightyalpaca.discord.bot.system.command.arguments.Arguments;
import com.almightyalpaca.discord.bot.system.command.arguments.ParseException;

import net.dv8tion.jda.entities.Message;

public class CommandAgumentParsers {

	public static abstract class Parser<T> {

		private final Class<? extends T> parserClass;

		public Parser(final Class<? extends T> parserClass) {
			this.parserClass = parserClass;
		}

		public abstract T get(Message msg, Arguments args) throws ParseException;

		public final Class<? extends T> getParserClass() {
			return this.parserClass;
		}
	}

	static {
		parsers = new HashMap<>();

		PrimitiveParsers.init();
		JDAParsers.init();
		OtherParsers.init();
		SpecialParsers.init();
	}

	private static final HashMap<Class<?>, Parser<?>> parsers;

	public static void addParser(final Parser<?> parser) {
		CommandAgumentParsers.parsers.put(parser.getParserClass(), parser);
	}

	public final static Parser<?> getParser(final Class<?> clazz) {
		for (final Entry<Class<?>, Parser<?>> entry : CommandAgumentParsers.parsers.entrySet()) {
			if (clazz.isAssignableFrom(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	public static boolean isSupported(final Class<?> clazz) {
		return CommandAgumentParsers.getParser(clazz) != null;
	}

}
