package com.almightyalpaca.discord.bot.system.extension;

import java.net.URL;
import java.net.URLClassLoader;

public class ExtensionClassLoader extends URLClassLoader {

	class JarClassLoader extends URLClassLoader {

		private final ExtensionClassLoader loader;

		public JarClassLoader(final URL url, final ExtensionClassLoader loader) {
			super(new URL[] { url });
			this.loader = loader;
		}

		@Override
		public URL getResource(final String name) {
			final URL url = this.loader.getRezource(name);
			return url != null ? url : super.getResource(name);
		}

		@Override
		public Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
			try {
				return this.loader.loadClazz(name, resolve);
			} catch (final ClassNotFoundException ignored) {}
			return super.loadClass(name, resolve);
		}

	}

	private final JarClassLoader pluginLoader;

	public ExtensionClassLoader(final URL url, final URL[] urls) {
		super(urls);
		this.pluginLoader = new JarClassLoader(url, this);
	}

	@Override
	public URL getResource(final String name) {
		final URL url = this.pluginLoader.getResource(name);
		return url != null ? url : super.getResource(name);
	}

	public URL getRezource(final String name) {
		return super.getResource(name);
	}

	@Override
	public Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
		try {
			return this.pluginLoader.loadClass(name, resolve);
		} catch (final ClassNotFoundException ignored) {}
		return super.loadClass(name, resolve);
	}

	public Class<?> loadClazz(final String name, final boolean resolve) throws ClassNotFoundException {
		return super.loadClass(name, resolve);
	}

}
