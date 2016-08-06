package com.almightyalpaca.adbs4j.storage.redis;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import com.almightyalpaca.adbs4j.misc.ExpiringCache;
import com.almightyalpaca.adbs4j.misc.FutureWrapper;
import com.almightyalpaca.adbs4j.storage.AsyncCachedStorageProviderInstance;

public class AsyncCachedRedisStorageProviderInstance implements AsyncCachedStorageProviderInstance {

	final AsyncRedisStorageProviderInstance	uncachedInstance;
	final ExpiringCache						cache;

	public AsyncCachedRedisStorageProviderInstance(final AsyncRedisStorageProviderInstance uncachedInstance) {
		this.uncachedInstance = uncachedInstance;
		this.cache = uncachedInstance.syncInstance.cached().cache;
	}

	@Override
	public void delete(final String... keys) {
		this.uncachedInstance.delete(keys);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Future<List<String>> getList(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		final long time = System.currentTimeMillis();
		if (!(object instanceof List)) {
			return FutureWrapper.wrap(this.uncachedInstance.getList(key), o -> AsyncCachedRedisStorageProviderInstance.this.cache.put(key, o, cacheTime - (System.currentTimeMillis() - time)));
		}
		return CompletableFuture.completedFuture((List<String>) object);
	}

	@Override
	public Future<List<String>> getList(final int cacheTime, final String key, final int start, final int stop) {
		return this.uncachedInstance.getList(key, start, stop);
	}

	@Override
	public Future<Integer> getListLenght(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof List)) {
			return this.uncachedInstance.getListLenght(key);
		}
		return CompletableFuture.completedFuture(((List<?>) object).size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Future<Map<String, String>> getMap(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		final long time = System.currentTimeMillis();
		if (!(object instanceof Map)) {
			return FutureWrapper.wrap(this.uncachedInstance.getMap(key), o -> AsyncCachedRedisStorageProviderInstance.this.cache.put(key, o, cacheTime - (System.currentTimeMillis() - time)));
		}
		return CompletableFuture.completedFuture((Map<String, String>) object);
	}

	@Override
	public Future<Boolean> getMapHasKey(final int cacheTime, final String key, final String mapKey) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapHasKey(key, mapKey);
		}
		return CompletableFuture.completedFuture(((Map<?, ?>) object).containsKey(mapKey));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Future<? extends Collection<String>> getMapKeys(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapKeys(key);
		}
		return CompletableFuture.completedFuture(((Map<String, String>) object).keySet());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Future<Integer> getMapLenght(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapLenght(key);
		}
		return CompletableFuture.completedFuture(((Map<String, String>) object).size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Future<String> getMapValue(final String key, final String field) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapValue(key, field);
		}
		return CompletableFuture.completedFuture(((Map<String, String>) object).get(field));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Future<? extends Collection<String>> getMapValues(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapValues(key);
		}
		return CompletableFuture.completedFuture(((Map<String, String>) object).values());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Future<Map<String, String>> getMapValues(final String key, final String... fields) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapValues(key, fields);
		}
		final Map<String, String> original = (Map<String, String>) object;
		final Map<String, String> map = new HashMap<>();
		for (int i = 0; i < fields.length; i++) {
			map.put(fields[i], original.get(i));
		}
		return CompletableFuture.completedFuture(map);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Future<Set<String>> getSet(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		final long time = System.currentTimeMillis();
		if (!(object instanceof Set)) {
			return FutureWrapper.wrap(this.uncachedInstance.getSet(key), o -> AsyncCachedRedisStorageProviderInstance.this.cache.put(key, o, cacheTime - (System.currentTimeMillis() - time)));
		}
		return CompletableFuture.completedFuture((Set<String>) object);
	}

	@Override
	public Future<Boolean> getSetContains(final int cacheTime, final String key, final String value) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Set)) {
			return this.uncachedInstance.getSetContains(key, value);
		}
		return CompletableFuture.completedFuture(((Set<?>) object).contains(value));
	}

	@Override
	public Future<Integer> getSetLenght(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Set)) {
			return this.uncachedInstance.getSetLenght(key);
		}
		return CompletableFuture.completedFuture(((Set<?>) object).size());
	}

	@Override
	public Future<String> getString(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		final long time = System.currentTimeMillis();
		if (!(object instanceof String)) {
			return FutureWrapper.wrap(this.uncachedInstance.getString(key), o -> AsyncCachedRedisStorageProviderInstance.this.cache.put(key, o, cacheTime - (System.currentTimeMillis() - time)));
		}
		return CompletableFuture.completedFuture((String) object);
	}

	@Override
	public Future<Map<String, String>> getStrings(final int cacheTime, final String... keys) {
		final Map<String, String> map = new HashMap<>(keys.length);
		final List<String> list = new ArrayList<>();
		for (final String key : keys) {
			final Object object = this.cache.get(key);
			if (object instanceof String) {
				map.put(key, (String) object);
			} else {
				list.add((String) object);
			}
		}
		if (list.size() > 0) {
			final long time = System.currentTimeMillis();
			return FutureWrapper.wrap(this.uncachedInstance.getStrings(list.toArray(new String[list.size()])), o -> {
				for (final Entry<String, String> entry : o.entrySet()) {
					AsyncCachedRedisStorageProviderInstance.this.cache.put(entry.getKey(), entry.getValue(), cacheTime - (System.currentTimeMillis() - time));
				}
				map.putAll(o);
				return map;
			});
		} else {
			return CompletableFuture.completedFuture(map);
		}
	}

	@Override
	public void listAppend(final String key, final String... values) {
		this.uncachedInstance.listAppend(key, values);
	}

	@Override
	public void listPrepend(final String key, final String... values) {
		this.uncachedInstance.listPrepend(key, values);
	}

	@Override
	public void listRemove(final String key, final int... indexes) {
		this.uncachedInstance.listRemove(key, indexes);
	}

	@Override
	public void listSet(final String key, final int index, final String value) {
		this.uncachedInstance.listSet(key, index, value);
	}

	@Override
	public void mapAdd(final String key, final Map<String, String> map) {
		this.uncachedInstance.mapAdd(key, map);
	}

	@Override
	public void mapAdd(final String key, final String mapKey, final String value) {
		this.uncachedInstance.mapAdd(key, mapKey, value);
	}

	@Override
	public void mapRemove(final String key, final String... fields) {
		this.uncachedInstance.mapRemove(key, fields);
	}

	@Override
	public void putString(final String key, final String value) {
		this.uncachedInstance.putString(key, value);
	}

	@Override
	public void setAdd(final String key, final String... values) {
		this.uncachedInstance.setAdd(key, values);
	}

	@Override
	public void setRemove(final String key, final String... values) {
		this.uncachedInstance.setRemove(key, values);
	}

}
