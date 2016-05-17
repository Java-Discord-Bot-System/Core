package com.almightyalpaca.discord.bot.system.command.arguments.parsers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import com.almightyalpaca.discord.bot.system.command.arguments.Arguments;
import com.almightyalpaca.discord.bot.system.command.arguments.ParseException;
import com.almightyalpaca.discord.bot.system.command.arguments.parsers.CommandAgumentParsers.Parser;

import net.dv8tion.jda.entities.Message;

public class OtherParsers {

	public static class BigDecimalParser extends Parser<BigDecimal> {
		public BigDecimalParser() {
			super(BigDecimal.class);
		}

		@Override
		public BigDecimal get(final Message msg, final Arguments args) {
			return new BigDecimal(args.removeNext());
		}
	}

	public static class BigIntegerParser extends Parser<BigInteger> {
		public BigIntegerParser() {
			super(BigInteger.class);
		}

		@Override
		public BigInteger get(final Message msg, final Arguments args) {
			return new BigInteger(args.removeNext());
		}
	}

	public static class URLParser extends Parser<URL> {
		public URLParser() {
			super(URL.class);
		}

		@Override
		public URL get(final Message msg, final Arguments args) throws ParseException {
			try {
				return new URL(args.removeNext());
			} catch (final MalformedURLException e) {
				throw new ParseException(e);
			}
		}
	}

	static void init() {
		CommandAgumentParsers.addParser(new OtherParsers.BigDecimalParser());
		CommandAgumentParsers.addParser(new OtherParsers.BigIntegerParser());
		CommandAgumentParsers.addParser(new OtherParsers.URLParser());

	}

}
