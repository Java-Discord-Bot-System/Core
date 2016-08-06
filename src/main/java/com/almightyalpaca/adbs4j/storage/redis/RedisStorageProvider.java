package com.almightyalpaca.adbs4j.storage.redis;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

import com.almightyalpaca.adbs4j.events.manager.EventHandler;
import com.almightyalpaca.adbs4j.events.plugins.PluginUnloadedEvent;
import com.almightyalpaca.adbs4j.plugins.PluginInfo;
import com.almightyalpaca.adbs4j.storage.StorageProvider;
import com.almightyalpaca.adbs4j.storage.StorageProviderInstance;

public class RedisStorageProvider implements StorageProvider {

	final RedisClient									client;
	final StatefulRedisConnection<String, String>		connection;
	final RedisURI										uri;
	final RedisCommands<String, String>					sync;

	final Map<Integer, RedisStorageProviderInstance>	instances	= new ConcurrentHashMap<>();

	public RedisStorageProvider(final String hostname, final int port, final String password) {
		this.uri = new RedisURI();
		this.uri.setHost(hostname);
		this.uri.setPort(port);
		this.uri.setPassword(password);
		this.uri.setDatabase(0);
		this.client = RedisClient.create(this.uri);
		this.connection = this.client.connect();
		this.sync = this.connection.sync();
	}

	public void clearUpCache() {
		for (final RedisStorageProviderInstance instance : this.instances.values()) {
			instance.cached().cache.clearUp();
		}
	}

	public void emptyCache() {
		for (final RedisStorageProviderInstance instance : this.instances.values()) {
			instance.cached().cache.empty();
		}
	}

	@Override
	public StorageProviderInstance getGlobalInstance() {
		return this.getInstance(0);
	}

	private StorageProviderInstance getInstance(final int i) {
		synchronized (this.instances) {
			RedisStorageProviderInstance instance = this.instances.get(i);
			if (instance == null) {

				final RedisURI instanceURI = new RedisURI();
				instanceURI.setHost(this.uri.getHost());
				instanceURI.setPort(this.uri.getPort());
				instanceURI.setPassword(String.valueOf(this.uri.getPassword()));
				instanceURI.setDatabase(i);
				instance = new RedisStorageProviderInstance(instanceURI);

				this.instances.put(i, instance);
			}
			return instance;
		}
	}

	@Override
	public StorageProviderInstance getPluginInstance(final PluginInfo info) {
		int i = -1;

		final String id = info.getId();

		final Map<String, String> databases = this.sync.hgetall("databases");

		if (databases.isEmpty()) {
			this.sync.hset("databases", "0", "global");
		} else if (databases.size() > 1) {
			for (final Entry<String, String> database : databases.entrySet()) {
				if (database.getValue().equals(id)) {
					i = Integer.parseInt(database.getKey());
				}
			}
		}
		if (i == -1) {
			i = Math.toIntExact(this.sync.hlen("databases"));
			this.sync.hset("databases", String.valueOf(i), id);
		}

		return this.getInstance(i);
	}

	@EventHandler
	private void onPluginUnloaded(final PluginUnloadedEvent event) {
		this.unloadInstance(event.getInfo());
	}

	public void shutdown() {
		System.out.println("Shutting redis connection down.");
		for (final RedisStorageProviderInstance instance : this.instances.values()) {
			instance.shutdown();
		}
		this.client.shutdown();
	}

	@Override
	public void unloadInstance(final PluginInfo info) {
		int i = -1;

		final String id = info.getId();

		final Map<String, String> databases = this.sync.hgetall("databases");

		if (databases.size() > 1) {
			for (final Entry<String, String> database : databases.entrySet()) {
				if (database.getValue().equals(id)) {
					i = Integer.parseInt(database.getKey());
				}
			}
		}

		if (i != -1) {
			final RedisStorageProviderInstance instance = this.instances.remove(i);
			if (instance != null) {
				instance.shutdown();
			}
		}

	}

}
