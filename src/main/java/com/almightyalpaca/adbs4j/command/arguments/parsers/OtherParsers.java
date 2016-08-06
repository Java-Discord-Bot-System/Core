package com.almightyalpaca.adbs4j.command.arguments.parsers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import com.almightyalpaca.adbs4j.command.arguments.ParseException;
import com.almightyalpaca.adbs4j.command.arguments.parsers.CommandAgumentParsers.Parser;
import com.almightyalpaca.adbs4j.internal.CommandBuffer;

import net.dv8tion.jda.entities.Message;

public class OtherParsers {

	public static class BigDecimalParser extends Parser<BigDecimal> {
		public BigDecimalParser() {
			super(BigDecimal.class);
		}

		@Override
		public BigDecimal get(final Message msg, final CommandBuffer buffer) {
			return new BigDecimal(buffer.removeNext());
		}

	}

	public static class BigIntegerParser extends Parser<BigInteger> {
		public BigIntegerParser() {
			super(BigInteger.class);
		}

		@Override
		public BigInteger get(final Message msg, final CommandBuffer buffer) {
			return new BigInteger(buffer.removeNext());
		}

	}

	public static class RestParser extends Parser<Rest> {

		public RestParser() {
			super(Rest.class);
		}

		@Override
		public Rest get(final Message msg, final CommandBuffer buffer) {
			return new Rest(buffer.remove(buffer.lenght()));
		}
	}

	public static class URLParser extends Parser<URL> {
		public URLParser() {
			super(URL.class);
		}

		@Override
		public URL get(final Message msg, final CommandBuffer buffer) throws ParseException {
			try {
				return new URL(buffer.removeNext());
			} catch (final MalformedURLException e) {
				throw new ParseException(e);
			}
		}

	}

	static void init() {
		CommandAgumentParsers.addParser(new OtherParsers.BigDecimalParser());
		CommandAgumentParsers.addParser(new OtherParsers.BigIntegerParser());
		CommandAgumentParsers.addParser(new OtherParsers.URLParser());
		CommandAgumentParsers.addParser(new RestParser());

	}

}
