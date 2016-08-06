package com.almightyalpaca.adbs4j.storage.redis;

import java.util.*;
import java.util.Map.Entry;

import com.almightyalpaca.adbs4j.misc.ExpiringCache;
import com.almightyalpaca.adbs4j.storage.AsyncCachedStorageProviderInstance;
import com.almightyalpaca.adbs4j.storage.CachedStorageProviderInstance;

public class CachedRedisStorageProviderInstance implements CachedStorageProviderInstance {

	final RedisStorageProviderInstance	uncachedInstance;

	final ExpiringCache					cache	= new ExpiringCache();

	public CachedRedisStorageProviderInstance(final RedisStorageProviderInstance uncachedInstance) {
		this.uncachedInstance = uncachedInstance;
	}

	@Override
	public AsyncCachedStorageProviderInstance async() {
		return this.uncachedInstance.async().cached();
	}

	@Override
	public void delete(final String... keys) {
		this.uncachedInstance.delete(keys);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getList(final int cacheTime, final String key) {
		Object object = this.cache.get(key);
		if (!(object instanceof List)) {
			object = this.cache.put(key, this.uncachedInstance.getList(key), cacheTime);
		}
		return (List<String>) object;
	}

	@Override
	public List<String> getList(final int cacheTime, final String key, final int start, final int stop) {
		return this.uncachedInstance.getList(key, start, stop);
	}

	@Override
	public int getListLenght(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof List)) {
			return this.uncachedInstance.getListLenght(key);
		}
		return ((List<?>) object).size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getMap(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.cache.put(key, this.uncachedInstance.getMap(key), cacheTime);
		}
		return (Map<String, String>) object;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean getMapHasKey(final int cacheTime, final String key, final String mapKey) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapHasKey(key, mapKey);
		}
		return ((Map<String, String>) object).containsKey(mapKey);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> getMapKeys(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapKeys(key);
		}
		return ((Map<String, String>) object).keySet();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getMapLenght(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapLenght(key);
		}
		return ((Map<String, String>) object).size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getMapValue(final String key, final String field) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapValue(key, field);
		}
		return ((Map<String, String>) object).get(field);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> getMapValues(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapValues(key);
		}
		return ((Map<String, String>) object).values();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getMapValues(final String key, final String... fields) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Map)) {
			return this.uncachedInstance.getMapValues(key, fields);
		}
		final Map<String, String> original = (Map<String, String>) object;

		final Map<String, String> map = new HashMap<>();
		for (final String field : fields) {
			map.put(field, original.get(field));
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getSet(final int cacheTime, final String key) {
		Object object = this.cache.get(key);
		if (!(object instanceof Set)) {
			object = this.cache.put(key, this.uncachedInstance.getSet(key), cacheTime);
		}
		return (Set<String>) object;
	}

	@Override
	public boolean getSetContains(final int cacheTime, final String key, final String value) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Set)) {
			return this.uncachedInstance.getSetContains(key, value);
		}
		return ((Set<?>) object).contains(value);
	}

	@Override
	public int getSetLenght(final int cacheTime, final String key) {
		final Object object = this.cache.get(key);
		if (!(object instanceof Set)) {
			return this.uncachedInstance.getSetLenght(key);
		}
		return ((Set<?>) object).size();
	}

	@Override
	public String getString(final int cacheTime, final String key) {
		Object object = this.cache.get(key);
		if (!(object instanceof String)) {
			object = this.cache.put(key, this.uncachedInstance.getString(key), cacheTime);
		}
		return (String) object;
	}

	@Override
	public Map<String, String> getStrings(final int cacheTime, final String... keys) {
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
			final Map<String, String> values = this.uncachedInstance.getStrings(list.toArray(new String[list.size()]));
			for (final Entry<String, String> entry : values.entrySet()) {
				this.cache.put(entry.getKey(), entry.getValue(), cacheTime);
			}
			map.putAll(values);
		}
		return map;
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
