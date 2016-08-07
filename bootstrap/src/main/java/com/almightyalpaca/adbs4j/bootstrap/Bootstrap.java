package com.almightyalpaca.adbs4j.bootstrap;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bootstrap {
	public static Pattern	versionPattern	= Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+_([0-9]+)");
	private final File		workingDirectory;
	private final Launcher	launcher;

	public Bootstrap() throws IOException {
		final File workingDir = new File(System.getProperty("user.dir"));

		if (workingDir.getName().equals("bin")) {
			this.workingDirectory = workingDir.getParentFile();
		} else {
			this.workingDirectory = workingDir;
		}

		this.launcher = new Launcher(this);

		this.launcher.launchLatest(Code.FIRST_START);
	}

	public static void main(final String[] args) throws Exception {
		new Bootstrap();
	}

	public File getLatestVersion() {
		final File[] files = new File(this.workingDirectory, "libs/").listFiles();
		return files == null ? null : Arrays.stream(files).filter(f -> f.isFile()).filter(f -> {
			final Matcher m = Bootstrap.versionPattern.matcher(f.getName().replace(".version", ""));
			return m.matches();
		}).max((f1, f2) -> {
			final Matcher m1 = Bootstrap.versionPattern.matcher(f1.getName());
			m1.find();
			final int i1 = Integer.parseInt(m1.group(0));
			final Matcher m2 = Bootstrap.versionPattern.matcher(f2.getName());
			m2.find();
			final int i2 = Integer.parseInt(m2.group(0));
			return Integer.compare(i1, i2);
		}).orElseGet(() -> null);
	}

	public final Launcher getLauncher() {
		return this.launcher;
	}

	public File getWorkingDirectory() {
		return this.workingDirectory;
	}

	public void onExit(final Code code) {
		try {
			code.getHandler().handle(this);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
