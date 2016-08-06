package com.almightyalpaca.adbs4j.command.arguments.parsers;

import org.apache.commons.lang3.StringUtils;

import com.almightyalpaca.adbs4j.command.arguments.ParseException;
import com.almightyalpaca.adbs4j.command.arguments.parsers.CommandAgumentParsers.Parser;
import com.almightyalpaca.adbs4j.internal.CommandBuffer;
import com.almightyalpaca.adbs4j.util.StringUtil;

import net.dv8tion.jda.entities.Message;

public class PrimitiveParsers {
	public static class BooleanParser extends Parser<Boolean> {
		public BooleanParser() {
			super(Boolean.class);
		}

		@Override
		public Boolean get(final Message msg, final CommandBuffer buffer) {
			return Boolean.parseBoolean(buffer.removeNext());
		}

	}

	public static class ByteParser extends Parser<Byte> {
		public ByteParser() {
			super(Byte.class);
		}

		@Override
		public Byte get(final Message msg, final CommandBuffer buffer) {
			return Byte.parseByte(buffer.removeNext());
		}

	}

	public static class CharacterParser extends Parser<Character> {
		public CharacterParser() {
			super(Character.class);
		}

		@Override
		public Character get(final Message msg, final CommandBuffer buffer) {
			if (buffer.getNext().length() != 1) {
				throw new ParseException("Invalid char!");
			}
			return buffer.removeNext().charAt(0);
		}

	}

	public static class DoubleParser extends Parser<Double> {
		public DoubleParser() {
			super(Double.class);
		}

		@Override
		public Double get(final Message msg, final CommandBuffer buffer) {
			return Double.parseDouble(buffer.removeNext());
		}

	}

	public static class FloatParser extends Parser<Float> {
		public FloatParser() {
			super(Float.class);
		}

		@Override
		public Float get(final Message msg, final CommandBuffer buffer) {
			return Float.parseFloat(buffer.removeNext());
		}
	}

	public static class IntegerParser extends Parser<Integer> {

		public IntegerParser() {
			super(Integer.class);
		}

		@Override
		public Integer get(final Message msg, final CommandBuffer buffer) {
			return Integer.parseInt(buffer.removeNext());
		}

	}

	public static class LongParser extends Parser<Long> {
		public LongParser() {
			super(Long.class);
		}

		@Override
		public Long get(final Message msg, final CommandBuffer buffer) {
			return Long.parseLong(buffer.removeNext());
		}

	}

	public static class PrimitiveBooleanParser extends Parser<Boolean> {
		public PrimitiveBooleanParser() {
			super(boolean.class);
		}

		@Override
		public Boolean get(final Message msg, final CommandBuffer buffer) {
			return Boolean.parseBoolean(buffer.removeNext());
		}

	}

	public static class PrimitiveByteParser extends Parser<Byte> {
		public PrimitiveByteParser() {
			super(byte.class);
		}

		@Override
		public Byte get(final Message msg, final CommandBuffer buffer) {
			return Byte.parseByte(buffer.removeNext());
		}

	}

	public static class PrimitiveCharacterParser extends Parser<Character> {
		public PrimitiveCharacterParser() {
			super(char.class);
		}

		@Override
		public Character get(final Message msg, final CommandBuffer buffer) {
			if (buffer.getNext().length() != 1) {
				throw new ParseException("Invalid char!");
			}
			return buffer.removeNext().charAt(0);
		}

	}

	public static class PrimitiveDoubleParser extends Parser<Double> {
		public PrimitiveDoubleParser() {
			super(double.class);
		}

		@Override
		public Double get(final Message msg, final CommandBuffer buffer) {
			return Double.parseDouble(buffer.removeNext());
		}

	}

	public static class PrimitiveFloatParser extends Parser<Float> {
		public PrimitiveFloatParser() {
			super(float.class);
		}

		@Override
		public Float get(final Message msg, final CommandBuffer buffer) {
			return Float.parseFloat(buffer.removeNext());
		}

	}

	public static class PrimitiveIntegerParser extends Parser<Integer> {

		public PrimitiveIntegerParser() {
			super(int.class);
		}

		@Override
		public Integer get(final Message msg, final CommandBuffer buffer) {
			return Integer.parseInt(buffer.removeNext());
		}

	}

	public static class PrimitiveLongParser extends Parser<Long> {
		public PrimitiveLongParser() {
			super(long.class);
		}

		@Override
		public Long get(final Message msg, final CommandBuffer buffer) {
			return Long.parseLong(buffer.removeNext());
		}

	}

	public static class PrimitiveShortParser extends Parser<Short> {
		public PrimitiveShortParser() {
			super(short.class);
		}

		@Override
		public Short get(final Message msg, final CommandBuffer buffer) {
			return Short.parseShort(buffer.removeNext());
		}

	}

	public static class ShortParser extends Parser<Short> {
		public ShortParser() {
			super(Short.class);
		}

		@Override
		public Short get(final Message msg, final CommandBuffer buffer) {
			return Short.parseShort(buffer.removeNext());
		}

	}

	public static class StringParser extends Parser<String> {
		public StringParser() {
			super(String.class);
		}

		@Override
		public String get(final Message msg, final CommandBuffer buffer) {
			String string = buffer.removeNext();
			if (string.startsWith("\"")) {
				do {
					string += buffer.removeNext();
				} while (!string.endsWith("\"") || buffer.isEmpty());
				string = StringUtil.replaceLast(StringUtils.replaceOnce(string, "\"", ""), "\"", "");
			}
			return string;
		}

	}

	static void init() {
		CommandAgumentParsers.addParser(new PrimitiveParsers.PrimitiveBooleanParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.PrimitiveByteParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.PrimitiveCharacterParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.PrimitiveDoubleParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.PrimitiveFloatParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.PrimitiveIntegerParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.PrimitiveLongParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.PrimitiveShortParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.BooleanParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.ByteParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.CharacterParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.DoubleParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.FloatParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.IntegerParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.LongParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.ShortParser());
		CommandAgumentParsers.addParser(new PrimitiveParsers.StringParser());
	}

}
