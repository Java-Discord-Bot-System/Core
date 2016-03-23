package com.almightyalpaca.discord.bot.system.extension;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.almightyalpaca.discord.bot.system.config.Config;
import com.almightyalpaca.discord.bot.system.exception.InvalidPluginException;
import com.almightyalpaca.discord.bot.system.exception.PluginInitializingException;
import com.almightyalpaca.discord.bot.system.exception.PluginLoadingException;
import com.almightyalpaca.discord.bot.system.exception.PluginUnloadingException;
import com.almightyalpaca.discord.bot.system.plugins.Plugin;
import com.almightyalpaca.discord.bot.system.plugins.PluginInfo;
import com.almightyalpaca.discord.bot.system.util.FileUtils;
import com.almightyalpaca.discord.bot.system.util.StringUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class PluginExtension {

	private final File						folder;
	private final File						jar;

	private final ExtensionClassLoader		loader;

	private Plugin							plugin;

	private final ExtensionManager			extensionManager;
	private final ExtensionBridge			bridge;
	private final Class<? extends Plugin>	pluginClass;

	@SuppressWarnings("unchecked")
	public PluginExtension(final ExtensionManager manager, final File folder) throws InvalidPluginException, IOException, PluginInitializingException, PluginLoadingException {
		System.out.println("Begin initializing plugin: " + folder);
		this.extensionManager = manager;
		this.folder = folder;
		final File libs = new File(folder, "lib");
		libs.mkdirs();
		this.jar = new File(folder, "Plugin.jar");
		if (!this.jar.exists()) {
			throw new InvalidPluginException("Plugin.jar does not exist !");
		}

		JarFile jarFile = new JarFile(this.jar);
		if (jarFile.getManifest() != null) {
			jarFile.close();
			try {
				final ZipFile zip = new ZipFile(this.jar);

				zip.removeFile("META-INF/MANIFEST.MF");

			} catch (final ZipException e) {
				e.printStackTrace();
			}
			jarFile = new JarFile(this.jar);
		}

		final List<URL> urls = new ArrayList<>();
		for (final File file : FileUtils.listfiles(libs)) {
			urls.add(file.toURI().toURL());
		}

		this.loader = new ExtensionClassLoader(this.jar.toURI().toURL(), urls.toArray(new URL[urls.size()]));

		final List<Class<? extends Plugin>> list = new ArrayList<>();

		final Enumeration<JarEntry> enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			final JarEntry jarEntry = enumeration.nextElement();
			if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
				continue;
			}

			try {
				final Class<?> clazz = this.loader.loadClass(StringUtils.replaceLast(jarEntry.getName().replace("/", "."), ".class", ""), true);
				if (Plugin.class.isAssignableFrom(clazz)) {
					System.out.println("Found plugin main class: " + clazz.getName());
					list.add((Class<? extends Plugin>) clazz);
				}
			} catch (final ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		jarFile.close();

		if (list.size() > 1) {
			throw new InvalidPluginException("Too many Plugin classes found!");
		} else if (list.size() < 1) {
			throw new InvalidPluginException("No Plugin class found!");
		} else {
			this.pluginClass = list.get(0);
			try {
				Constructor<? extends Plugin> constructor;
				try {
					constructor = this.pluginClass.getConstructor();
					constructor.setAccessible(true);
					this.plugin = constructor.newInstance();
				} catch (final Exception e) {
					throw new PluginInitializingException(e);
				}

			} catch (final Exception e) {
				throw new PluginInitializingException(e);
			}

			this.bridge = this.plugin.getBridge();
			this.bridge.initialize(this);

		}

		System.out.println("Finished initializing plugin: " + folder);
	}

	@Override
	protected void finalize() throws Throwable {
		System.out.println("FINALIZING PLUGINOBJECT");
	}

	public final ExtensionClassLoader getClassLoader() {
		return this.loader;
	}

	public final File getFolder() {
		return this.folder;
	}

	public final File getJarFile() {
		return this.jar;
	}

	public final Class<? extends Plugin> getPluginClass() {
		return this.pluginClass;
	}

	public final Config getPluginConfig() {
		return this.plugin.getBridge().getPluginConfig();
	}

	public final PluginInfo getPluginInfo() {
		return this.plugin.getPluginInfo();
	}

	public final ExtensionManager getPluginManager() {
		return this.extensionManager;
	}

	public Plugin getPluginObject() {
		return this.plugin;
	}

	public void load() {
		try {
			this.bridge.load();
		} catch (final PluginLoadingException e) {
			e.printStackTrace();
		}
	}

	public void registerCommand(final CommandExtension command) {
		this.extensionManager.getCommandManager().register(command);
	}

	public void unload() {
		try {
			this.bridge.unload();
		} catch (final PluginUnloadingException e) {
			e.printStackTrace();
		}
		try {
			this.loader.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	public void unregisterCommand(final CommandExtension command) {
		this.extensionManager.getCommandManager().unregister(command);
	}

}