package com.almightyalpaca.adbs4j.bootstrap.updater;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

import com.almightyalpaca.adbs4j.bootstrap.Bootstrap;

public class Updater {
	final JSONObject	object;
	final Bootstrap		bootstrap;

	public Updater(final Bootstrap bootstrap) throws IOException {
		this.bootstrap = bootstrap;

		final URL url = new URL("https://ci-almightyalpaca.rhcloud.com/job/adbs4j/lastSuccessfulBuild/api/json");
		final InputStream is = url.openStream();
		final BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		final StringBuilder builder = new StringBuilder();
		while ((line = br.readLine()) != null) {
			builder.append(line);
		}
		br.close();
		is.close();

		this.object = new JSONObject(builder.toString());
	}

	public String getLatestVersion() {
		final JSONArray artifacts = this.object.getJSONArray("artifacts");

		String displayPath;
		int i = 0;
		do {
			displayPath = artifacts.getJSONObject(i++).getString("displayPath");
		} while (!displayPath.contains("-"));
		return displayPath.substring(0, displayPath.lastIndexOf("-")).substring(7);
	}

	public void update() throws IOException {

		final File libs = new File(this.bootstrap.getWorkingDirectory(), "/libs/");
		libs.mkdirs();

		final JSONArray artifacts = this.object.getJSONArray("artifacts");

		String displayPath;
		int i = 0;
		do {
			displayPath = artifacts.getJSONObject(i++).getString("displayPath");
		} while (!displayPath.contains("-"));

		final String version = displayPath.substring(0, displayPath.lastIndexOf("-")).substring(7);
		System.out.println("New version " + version + " found! Downloading files...");
		final int buildId = Integer.valueOf(this.object.getString("id"));

		final String build = "https://ci-almightyalpaca.rhcloud.com/job/adbs4j/" + buildId + "/artifact/build/";

		final RepositorySearcher searcher = new RepositorySearcher();

		final URL repositoriesURL = new URL(build + "bot/repositories.txt");

		final BufferedReader repositoriesReader = new BufferedReader(new InputStreamReader(repositoriesURL.openStream()));

		String line;
		while ((line = repositoriesReader.readLine()) != null) {
			if (!line.isEmpty()) {
				searcher.addRepository(new Repository(new URL(line)));
			}
		}
		repositoriesReader.close();

		searcher.addRepository(new Repository(new URL("http://dl.bintray.com/almightyalpaca/maven/")));

		final File versionFile = new File(libs, version + ".version");
		if (versionFile.exists()) {
			versionFile.delete();
		}
		System.out.println(versionFile);
		try {
			versionFile.createNewFile();
			final Writer writer = new FileWriter(versionFile);

			final URL dependenciesURL = new URL(build + "bot/dependencies.txt");

			final BufferedReader dependenciesReader = new BufferedReader(new InputStreamReader(dependenciesURL.openStream()));

			while ((line = dependenciesReader.readLine()) != null) {
				if (!line.isEmpty()) {
					writer.write(line);
					writer.write("\n");
					final Dependency d = Dependency.ofId(line);
					final File file = new File(this.bootstrap.getWorkingDirectory(), "/libs/" + d.getAsPath());
					if (!file.exists()) {
						file.getParentFile().mkdirs();
						Files.copy(searcher.get(d), file.toPath());
					}
				}
			}
			dependenciesReader.close();

			final Dependency d = new Dependency("com.almightyalpaca", "adbs4j", version);

			writer.write(d.getAsId());
			writer.write("\n");

			final File file = new File(libs, d.getAsPath());
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				Files.copy(searcher.get(d), file.toPath());
			}
			writer.close();
		} catch (final Exception e) {
			versionFile.deleteOnExit();
			versionFile.delete();
			throw e;
		}

	}
}
