package com.almightyalpaca.adbs4j.bootstrap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		final ProcessBuilder builder = new ProcessBuilder();
		builder.directory(this.bootstrap.getWorkingDirectory());

		final List<String> command = new ArrayList<>();

		command.add(System.getProperty("java.home"));
		command.add("-cp");

		final File[] jars = Arrays.stream(new File(this.bootstrap.getWorkingDirectory(), "bin/" + version + "/").listFiles()).filter(f -> f.isFile()).toArray(value -> new File[value]);

		final StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < jars.length - 1; i++) {
			sBuilder.append(jars[i].getAbsolutePath()).append(';');
		}
		sBuilder.append(jars[jars.length - 1].getAbsolutePath());

		command.add(sBuilder.toString());

		command.add("com.almightyalpaca.adbs4j.System");

		command.add("-Djava.io.tmpdir=" + new File(this.bootstrap.getWorkingDirectory(), "cache/").getAbsolutePath());
		command.add("-Djuser.dir=" + this.bootstrap.getWorkingDirectory().getAbsolutePath());
		command.add("-Dbot.cachedir=" + new File(this.bootstrap.getWorkingDirectory(), "cache/").getAbsolutePath());
		command.add("-Dbot.configfile=" + new File(this.bootstrap.getWorkingDirectory(), "config.json").getAbsolutePath());
		command.add("-Dbot.plugindir=" + new File(this.bootstrap.getWorkingDirectory(), "plugins/").getAbsolutePath());

		command.add("-Dbot.previousExitCode=" + previousExitCode.getCode());

		builder.command(command);

		builder.inheritIO();

		final Process process = builder.start();

		while (process.isAlive()) {
			try {
				process.waitFor();
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

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
			version = file.getName();
		}
		this.launch(previousExitCode, version);
	}
}
