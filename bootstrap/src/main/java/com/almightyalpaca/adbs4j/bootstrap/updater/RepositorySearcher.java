package com.almightyalpaca.adbs4j.bootstrap.updater;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RepositorySearcher {

	private final Set<Repository> repositories;

	public RepositorySearcher() {
		this.repositories = Collections.newSetFromMap(new ConcurrentHashMap<>());
	}

	public void addRepository(final Repository repository) {
		this.repositories.add(repository);
	}

	public InputStream get(final Dependency dependency) {
		System.out.println("Searching " + dependency.getAsId() + " ...");
		for (final Repository repository : this.repositories) {
			System.out.println("In " + repository.getURL() + " ...");
			final InputStream stream = repository.get(dependency);
			if (stream != null) {
				System.out.println("Found!");
				return stream;
			}
		}
		System.out.println("Not found!");
		return null;
	}

	public boolean has(final Dependency dependency) {
		for (final Repository repository : this.repositories) {
			if (repository.has(dependency)) {
				return true;
			}
		}
		return false;
	}

}
