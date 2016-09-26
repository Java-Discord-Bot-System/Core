package com.almightyalpaca.adbs4j.bootstrap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.almightyalpaca.adbs4j.bootstrap.updater.Dependency;
import com.almightyalpaca.adbs4j.bootstrap.updater.Updater;

public class Launcher {

	private final Bootstrap bootstrap;

	public Launcher(final Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
	}

	public final Bootstrap getBootstrap() {
		return this.bootstrap;
	}

	public void launch(final Code previousExitCode, final String version) throws IOException {
		System.out.println("Launching version " + version + " ...");
		final ProcessBuilder builder = new ProcessBuilder();
		builder.directory(this.bootstrap.getWorkingDirectory());

		final List<String> command = new ArrayList<>();

		command.add(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");

		command.add("-Djava.io.tmpdir=" + new File(this.bootstrap.getWorkingDirectory(), "cache/").getAbsolutePath());
		command.add("-Djuser.dir=" + this.bootstrap.getWorkingDirectory().getAbsolutePath());
		command.add("-Dbot.cachedir=" + new File(this.bootstrap.getWorkingDirectory(), "cache/").getAbsolutePath());
		command.add("-Dbot.configfile=" + new File(this.bootstrap.getWorkingDirectory(), "config.json").getAbsolutePath());
		command.add("-Dbot.plugindir=" + new File(this.bootstrap.getWorkingDirectory(), "plugins/").getAbsolutePath());

		command.add("-Dbot.previousExitCode=" + previousExitCode.getCode());

		command.add("-cp");

		final File versionFile = new File(this.bootstrap.getWorkingDirectory(), "libs/" + version + ".version");

		final BufferedReader dependenciesReader = new BufferedReader(new FileReader(versionFile));

		String line;

		final StringBuilder sBuilder = new StringBuilder();

		char seperator;
		String osname = System.getProperty("os.name");
		if (osname == null) {
			dependenciesReader.close();
			throw new Error("Cannot determine operation system! Property 'os.name' is null!");
		} else if (osname.toLowerCase().startsWith("linux")) {
			seperator = ':';
		} else {
			seperator = ';';
		}

		while ((line = dependenciesReader.readLine()) != null) {
			if (!line.isEmpty()) {
				final Dependency d = Dependency.ofId(line);
				final File dependencyFile = new File(this.bootstrap.getWorkingDirectory(), "libs/" + d.getAsPath());
				sBuilder.append(dependencyFile.getAbsolutePath()).append(seperator);
			}
		}
		sBuilder.deleteCharAt(sBuilder.length() - 1);

		dependenciesReader.close();

		command.add(sBuilder.toString());

		command.add("com.almightyalpaca.adbs4j.System");

		builder.command(command);

		builder.inheritIO();

		final Process process = builder.start();

		final Thread hook = new Thread(() -> {
			System.out.println("Bootstrap has been requested to close! Trying to shutdown the bot...");
			process.destroy();
		}, "Shutdown hook");

		Runtime.getRuntime().addShutdownHook(hook);

		while (process.isAlive()) {
			try {
				process.waitFor();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		Runtime.getRuntime().removeShutdownHook(hook);

		this.bootstrap.onExit(Code.get(process.exitValue()));
	}

	public void launchLatest(final Code previousExitCode) throws IOException {
		final File file = this.bootstrap.getLatestVersion();
		String version;
		if (file == null) {
			System.out.println("No installed version found! Searching latest...");
			final Updater updater = new Updater(this.bootstrap);

			System.out.println("Found version " + updater.getLatestVersion() + "! Downloading...");

			updater.update();

			version = updater.getLatestVersion();

		} else {
			version = file.getName().replace(".version", "");
		}
		this.launch(previousExitCode, version);
	}
}
