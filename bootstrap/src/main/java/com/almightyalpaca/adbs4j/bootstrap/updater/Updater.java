package com.almightyalpaca.adbs4j.bootstrap.updater;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.json.JSONArray;
import org.json.JSONObject;

import com.almightyalpaca.adbs4j.bootstrap.Bootstrap;

public class Updater {
	final JSONObject	object;
	final Bootstrap		bootstrap;

	public Updater(final Bootstrap bootstrap) throws IOException {
		this.bootstrap = bootstrap;

		final URL url = new URL("http://home.dv8tion.net:8080/job/JDA/lastBuild/api/json");
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

		final String displayPath = artifacts.getJSONObject(0).getString("displayPath");
		return displayPath.substring(0, displayPath.lastIndexOf("-")).substring(4);
	}

	public void update() throws IOException {

		final JSONArray artifacts = this.object.getJSONArray("artifacts");

		final String displayPath = artifacts.getJSONObject(0).getString("displayPath");
		final String version = displayPath.substring(0, displayPath.lastIndexOf("-")).substring(4);
		System.out.println("New version " + version + "found! Downloading files...");
		final int buildId = Integer.valueOf(this.object.getString("id"));

		final String build = "http://ci.dv8tion.net/job/JDA/" + buildId + "/artifact/build/";

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

		final URL dependenciesURL = new URL(build + "bot/dependencies.txt");

		final BufferedReader dependenciesReader = new BufferedReader(new InputStreamReader(dependenciesURL.openStream()));

		while ((line = dependenciesReader.readLine()) != null) {
			if (!line.isEmpty()) {
				final Dependency d = Dependency.ofId(line);
				Files.copy(searcher.get(d), new File(this.bootstrap.getWorkingDirectory(), "/libs/" + version + '/' + d.getAsId()).toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		}
		dependenciesReader.close();
	}
}
