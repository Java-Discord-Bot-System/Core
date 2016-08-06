package com.almightyalpaca.adbs4j.storage.redis;

import java.util.*;
import java.util.concurrent.Future;

import com.lambdaworks.redis.api.async.RedisAsyncCommands;

import com.almightyalpaca.adbs4j.misc.FutureWrapper;
import com.almightyalpaca.adbs4j.storage.AsyncCachedStorageProviderInstance;
import com.almightyalpaca.adbs4j.storage.AsyncStorageProviderInstance;

public class AsyncRedisStorageProviderInstance implements AsyncStorageProviderInstance {

	final RedisAsyncCommands<String, String>	async;
	final RedisStorageProviderInstance			syncInstance;
	AsyncCachedRedisStorageProviderInstance		cachedInstance;

	public AsyncRedisStorageProviderInstance(final RedisStorageProviderInstance syncInstance) {
		this.syncInstance = syncInstance;
		this.async = syncInstance.connection.async();
	}

	@Override
	public AsyncCachedStorageProviderInstance cached() {
		if (this.cachedInstance == null) {
			this.cachedInstance = new AsyncCachedRedisStorageProviderInstance(this);
		}
		return this.cachedInstance;
	}

	@Override
	public void delete(final String... keys) {
		for (final String key : keys) {
			this.syncInstance.valueChanged(key);
		}
		this.async.del(keys);
	}

	@Override
	public Future<List<String>> getList(final String key) {
		return this.getList(key, 0, -1);
	}

	@Override
	public Future<List<String>> getList(final String key, final int start, final int stop) {
		return this.async.lrange(key, start, stop);
	}

	@Override
	public Future<Integer> getListLenght(final String key) {
		return new FutureWrapper<Integer, Long>(this.async.llen(key)) {
			@Override
			protected Integer transform(final Long l) {
				return Math.toIntExact(l);
			}
		};
	}

	@Override
	public Future<Map<String, String>> getMap(final String key) {
		return this.async.hgetall(key);
	}

	@Override
	public Future<Boolean> getMapHasKey(final String key, final String mapKey) {
		return this.async.hexists(key, mapKey);
	}

	@Override
	public Future<? extends Collection<String>> getMapKeys(final String key) {
		return this.async.hkeys(key);
	}

	@Override
	public Future<Integer> getMapLenght(final String key) {
		return new FutureWrapper<Integer, Long>(this.async.hlen(key)) {
			@Override
			protected Integer transform(final Long l) {
				return Math.toIntExact(l);
			}
		};
	}

	@Override
	public Future<String> getMapValue(final String key, final String field) {
		return this.async.hget(key, field);
	}

	@Override
	public Future<? extends Collection<String>> getMapValues(final String key) {
		return this.async.hvals(key);
	}

	@Override
	public Future<Map<String, String>> getMapValues(final String key, final String... fields) {
		return FutureWrapper.wrap(this.async.hmget(key, fields), values -> {
			final Map<String, String> map = new HashMap<>();
			for (int i = 0; i < fields.length; i++) {
				map.put(fields[i], values.get(i));
			}
			return map;
		});

	}

	@Override
	public Future<Set<String>> getSet(final String key) {
		return this.async.smembers(key);
	}

	@Override
	public Future<Boolean> getSetContains(final String key, final String value) {
		return this.async.sismember(key, value);
	}

	@Override
	public Future<Integer> getSetLenght(final String key) {
		return new FutureWrapper<Integer, Long>(this.async.scard(key)) {
			@Override
			protected Integer transform(final Long l) {
				return Math.toIntExact(l);
			}
		};
	}

	@Override
	public Future<String> getString(final String key) {
		return this.async.get(key);
	}

	@Override
	public Future<Map<String, String>> getStrings(final String... keys) {
		return new FutureWrapper<Map<String, String>, List<String>>(this.async.mget(keys)) {
			@Override
			protected Map<String, String> transform(final List<String> values) {
				final Map<String, String> map = new HashMap<>();
				for (int i = 0; i < keys.length; i++) {
					map.put(keys[i], values.get(i));
				}
				return map;
			}
		};
	}

	@Override
	public void listAppend(final String key, final String... values) {
		this.syncInstance.valueChanged(key);
		this.async.rpush(key, values);
	}

	@Override
	public void listPrepend(final String key, final String... values) {
		this.syncInstance.valueChanged(key);
		this.async.lpush(key, values);
	}

	@Override
	public void listRemove(final String key, final int... indexes) {
		this.syncInstance.valueChanged(key);
		this.async.watch(key);
		this.async.multi();
		for (final int i : indexes) {
			this.listSet(key, i, "_t_o_b_e_d_e_l_e_t_e_d_");
		}
		this.async.lrem(key, 0, "_t_o_b_e_d_e_l_e_t_e_d_");
		this.async.exec();
	}

	@Override
	public void listSet(final String key, final int index, final String value) {
		this.syncInstance.valueChanged(key);
		this.async.lset(key, index, value);
	}

	@Override
	public void mapAdd(final String key, final Map<String, String> map) {
		this.syncInstance.valueChanged(key);
		this.async.hmset(key, map);
	}

	@Override
	public void mapAdd(final String key, final String mapKey, final String value) {
		this.syncInstance.valueChanged(key);
		this.async.hmset(key, Collections.singletonMap(mapKey, value));

	}

	@Override
	public void mapRemove(final String key, final String... fields) {
		this.syncInstance.valueChanged(key);

	}

	@Override
	public void putString(final String key, final String value) {
		this.syncInstance.valueChanged(key);
		this.async.set(key, value);
	}

	@Override
	public void setAdd(final String key, final String... values) {
		this.syncInstance.valueChanged(key);
		this.async.sadd(key, values);
	}

	@Override
	public void setRemove(final String key, final String... values) {
		this.syncInstance.valueChanged(key);
		this.async.srem(key, values);
	}

}
