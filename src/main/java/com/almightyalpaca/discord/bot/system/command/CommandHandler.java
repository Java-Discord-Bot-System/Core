package com.almightyalpaca.discord.bot.system.command;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandHandler {

	/**
	 * Set to true if this method should be executed async. default is <b>false</b>.
	 */
	boolean async() default false;

	/**
	 * Weather this method can be executed from a pm. default is <b>true</b>.
	 */
	boolean dm() default true;

	/**
	 * Weather this method can be executed from a guild. default is <b>true</b>.
	 */
	boolean guild() default true;

	/**
	 * The priority of this method. Default is <b>0</b>.
	 */
	int priority() default 0;
}
