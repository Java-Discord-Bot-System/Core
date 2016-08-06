package com.almightyalpaca.adbs4j.storage.redis;

import java.util.*;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

import com.almightyalpaca.adbs4j.storage.AsyncStorageProviderInstance;
import com.almightyalpaca.adbs4j.storage.StorageProviderInstance;

public class RedisStorageProviderInstance implements StorageProviderInstance {

	final RedisClient								client;
	final StatefulRedisConnection<String, String>	connection;
	final RedisCommands<String, String>				sync;
	AsyncRedisStorageProviderInstance				asyncInstance;
	CachedRedisStorageProviderInstance				cachedInstance;

	public RedisStorageProviderInstance(final RedisURI uri) {
		this.client = RedisClient.create(uri);
		this.connection = this.client.connect();
		this.sync = this.connection.sync();
	}

	@Override
	public AsyncStorageProviderInstance async() {
		if (this.asyncInstance == null) {
			this.asyncInstance = new AsyncRedisStorageProviderInstance(this);
		}
		return this.asyncInstance;
	}

	@Override
	public CachedRedisStorageProviderInstance cached() {
		if (this.cachedInstance == null) {
			this.cachedInstance = new CachedRedisStorageProviderInstance(this);
		}
		return this.cachedInstance;
	}

	@Override
	public void delete(final String... keys) {
		for (final String key : keys) {
			this.valueChanged(key);
		}
		this.sync.del(keys);
	}

	@Override
	public List<String> getList(final String key) {
		return this.getList(key, 0, -1);
	}

	@Override
	public List<String> getList(final String key, final int start, final int stop) {
		return this.sync.lrange(key, start, stop);
	}

	@Override
	public int getListLenght(final String key) {
		return Math.toIntExact(this.sync.llen(key));
	}

	@Override
	public Map<String, String> getMap(final String key) {
		return this.sync.hgetall(key);
	}

	@Override
	public boolean getMapHasKey(final String key, final String mapKey) {
		return this.sync.hexists(key, mapKey);
	}

	@Override
	public Collection<String> getMapKeys(final String key) {
		return this.sync.hkeys(key);
	}

	@Override
	public int getMapLenght(final String key) {
		return Math.toIntExact(this.sync.hlen(key));
	}

	@Override
	public String getMapValue(final String key, final String field) {
		return this.sync.hget(key, field);
	}

	@Override
	public Collection<String> getMapValues(final String key) {
		return this.sync.hvals(key);
	}

	@Override
	public Map<String, String> getMapValues(final String key, final String... fields) {
		final Map<String, String> map = new HashMap<>();
		final List<String> values = this.sync.hmget(key, fields);
		for (int i = 0; i < fields.length; i++) {
			map.put(fields[i], values.get(i));
		}
		return map;
	}

	@Override
	public Set<String> getSet(final String key) {
		return this.sync.smembers(key);
	}

	@Override
	public boolean getSetContains(final String key, final String value) {
		return this.sync.sismember(key, value);
	}

	@Override
	public int getSetLenght(final String key) {
		return Math.toIntExact(this.sync.scard(key));
	}

	@Override
	public String getString(final String key) {
		return this.sync.get(key);
	}

	@Override
	public Map<String, String> getStrings(final String... keys) {
		final List<String> values = this.sync.mget(keys);
		final Map<String, String> map = new HashMap<>();
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i], values.get(i));
		}
		return map;
	}

	@Override
	public void listAppend(final String key, final String... values) {
		this.valueChanged(key);
		this.sync.rpush(key, values);
	}

	@Override
	public void listPrepend(final String key, final String... values) {
		this.valueChanged(key);
		this.sync.lpush(key, values);
	}

	@Override
	public void listRemove(final String key, final int... indexes) {
		this.valueChanged(key);
		this.sync.watch(key);
		this.sync.multi();
		for (final int i : indexes) {
			this.listSet(key, i, "_t_o_b_e_d_e_l_e_t_e_d_");
		}
		this.sync.lrem(key, 0, "_t_o_b_e_d_e_l_e_t_e_d_");
		this.sync.exec();
	}

	@Override
	public void listSet(final String key, final int index, final String value) {
		this.valueChanged(key);
		this.sync.lset(key, index, value);
	}

	@Override
	public void mapAdd(final String key, final Map<String, String> map) {
		this.valueChanged(key);
		this.sync.hmset(key, map);
	}

	@Override
	public void mapAdd(final String key, final String mapKey, final String value) {
		this.valueChanged(key);
		this.sync.hmset(key, Collections.singletonMap(mapKey, value));
	}

	@Override
	public void mapRemove(final String key, final String... fields) {
		this.valueChanged(key);
		this.sync.hdel(key, fields);
	}

	@Override
	public void putString(final String key, final String value) {
		this.valueChanged(key);
		this.sync.set(key, value);
	}

	@Override
	public void setAdd(final String key, final String... values) {
		this.valueChanged(key);
		this.sync.sadd(key, values);
	}

	@Override
	public void setRemove(final String key, final String... values) {
		this.valueChanged(key);
		this.sync.srem(key, values);
	}

	public void shutdown() {
		this.client.shutdown();
	}

	public void valueChanged(final String key) {
		if (this.cachedInstance != null) {
			this.cachedInstance.cache.remove(key);
		}
	}

}
