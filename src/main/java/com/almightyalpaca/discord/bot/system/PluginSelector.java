package com.almightyalpaca.discord.bot.system;

import com.almightyalpaca.discord.bot.system.extension.PluginExtension;

@FunctionalInterface
public interface PluginSelector {

	public static final PluginSelector	ALL		= new PluginSelector() {
													@Override
													public boolean match(final PluginExtension p) {
														return true;
													}
												};

	public static final PluginSelector	NONE	= new PluginSelector() {
													@Override
													public boolean match(final PluginExtension p) {
														return false;
													}
												};

	public boolean match(PluginExtension plugin);

}
