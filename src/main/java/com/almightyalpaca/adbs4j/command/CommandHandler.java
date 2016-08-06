package com.almightyalpaca.adbs4j.command;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CommandHandler {

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
