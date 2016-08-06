package com.almightyalpaca.adbs4j.bootstrap.updater;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Repository {
	private final URL baseURL;

	public Repository(final URL baseURL) {
		this.baseURL = baseURL;
	}

	public InputStream get(final Dependency dependency) {

		try {
			final URL url = new URL(this.baseURL, '/' + dependency.getGroup().replace('.', '/') + '/' + dependency.getArtifactId() + '/' + dependency.getVersion() + '/' + dependency.getArtifactId()
					+ '-' + dependency.getVersion() + ".jar");

			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.setInstanceFollowRedirects(true);
			connection.connect();

			if (connection.getResponseCode() == 200) {
				return connection.getInputStream();
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean has(final Dependency dependency) {

		try {
			final URL url = new URL(this.baseURL, '/' + dependency.getGroup().replace('.', '/') + '/' + dependency.getArtifactId() + '/' + dependency.getVersion() + '/' + dependency.getArtifactId()
					+ '-' + dependency.getVersion() + ".jar");

			final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("HEAD");
			connection.setInstanceFollowRedirects(true);
			connection.connect();

			if (connection.getResponseCode() == 200) {
				return true;
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}

		return false;
	}
}
