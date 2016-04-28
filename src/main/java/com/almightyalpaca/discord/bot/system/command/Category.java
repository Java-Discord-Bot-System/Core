package com.almightyalpaca.discord.bot.system.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Category {

	private static Map<String, Category> categories = new HashMap<>();

	public static final Category	BOT			= Category.withName("Bot");
	public static final Category	BOT_ADMIN	= Category.withName("Admin");
	public static final Category	FUN			= Category.withName("Fun");
	public static final Category	GAMES		= Category.withName("Games");
	public static final Category	GUILD_ADMIN	= Category.withName("Guild");
	public static final Category	INFO		= Category.withName("Information");
	public static final Category	MISC		= Category.withName("Misc");
	public static final Category	MODERATION	= Category.withName("Moderation");
	public static final Category	MUSIC		= Category.withName("Music");
	public static final Category	USEFUL		= Category.withName("Useful");
	public static final Category	USELESS		= Category.withName("Useless");

	private final String name;

	private Category(final String name) {
		this.name = Objects.requireNonNull(name);
		if (Category.categories.containsKey(name)) {
			throw new RuntimeException("Two categories with the same name?");
		}
	}

	public static Category withName(final String name) {
		Category category = Category.categories.get(name);
		if (category == null) {
			category = new Category(name);
			Category.categories.put(name, category);
		}
		return category;
	}

	public boolean equals(final Category category) {
		if (category == null) {
			return false;
		}
		return this.name.equals(category.name);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Category) {
			return this.equals((Category) obj);
		} else {
			return false;
		}
	}

	public final String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return "Category [name=" + this.name + "]";
	}

}
