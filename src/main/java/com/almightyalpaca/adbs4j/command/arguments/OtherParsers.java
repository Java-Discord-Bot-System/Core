package com.almightyalpaca.adbs4j.command.arguments;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

import com.almightyalpaca.adbs4j.command.arguments.CommandAgumentParsers.Parser;
import com.almightyalpaca.adbs4j.internal.CommandBuffer;

import net.dv8tion.jda.entities.Message;

public class OtherParsers {

	public static class BigDecimalParser extends Parser<BigDecimal> {
		public BigDecimalParser() {
			super(BigDecimal.class);
		}

		@Override
		public BigDecimal get(final Message msg, final CommandBuffer buffer, final Annotation[] annotations) {
			return new BigDecimal(buffer.removeNext());
		}

	}

	public static class BigIntegerParser extends Parser<BigInteger> {
		public BigIntegerParser() {
			super(BigInteger.class);
		}

		@Override
		public BigInteger get(final Message msg, final CommandBuffer buffer, final Annotation[] annotations) {
			return new BigInteger(buffer.removeNext());
		}

	}

	public static class TextParser extends Parser<Text> {

		public TextParser() {
			super(Text.class);
		}

		@Override
		public Text get(final Message msg, final CommandBuffer buffer, final Annotation[] annotations) {
			return new Text(buffer.remove(buffer.lenght()));
		}
	}

	public static class URLParser extends Parser<URL> {
		public URLParser() {
			super(URL.class);
		}

		@Override
		public URL get(final Message msg, final CommandBuffer buffer, final Annotation[] annotations) throws ParseException {
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
		CommandAgumentParsers.addParser(new TextParser());

	}

}
