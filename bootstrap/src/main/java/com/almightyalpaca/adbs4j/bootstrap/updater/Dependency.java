package com.almightyalpaca.adbs4j.bootstrap.updater;

public class Dependency {
	private final String group, artifactId, version;

	public Dependency(final String group, final String artifactId, final String version) {
		this.group = group;
		this.artifactId = artifactId;
		this.version = version;
	}

	public static Dependency ofId(final String id) {
		final String[] parts = id.split(":");
		return new Dependency(parts[0], parts[1], parts[2]);
	}

	public final String getArtifactId() {
		return this.artifactId;
	}

	public String getAsId() {
		return new StringBuilder(this.artifactId.length() + this.group.length() + this.version.length() + 2).append(this.group).append(':').append(this.artifactId).append(':').append(this.version)
				.toString();
	}

	public String getAsPath() {
		return new StringBuilder(this.group.length() + this.artifactId.length() * 2 + this.version.length() * 2 + 8).append(this.group.replace('.', '/')).append('/').append(this.artifactId).append(
				'/').append(this.version).append('/').append(this.artifactId).append('-').append(this.version).append(".jar").toString();
	}

	public final String getGroup() {
		return this.group;
	}

	public final String getVersion() {
		return this.version;
	}
}
