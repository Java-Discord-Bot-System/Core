package com.almightyalpaca.adbs4j.misc;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class ExpiringCache {

	private final Map<String, Pair<Long, Object>> map = new ConcurrentHashMap<>();

	public void clearUp() {
		final Iterator<Entry<String, Pair<Long, Object>>> iterator = this.map.entrySet().iterator();
		final long time = System.currentTimeMillis();
		while (iterator.hasNext()) {
			final Map.Entry<String, Pair<Long, Object>> entry = iterator.next();
			if (entry.getValue().getKey() < time) {
				iterator.remove();
			}
		}
	}

	public void empty() {
		this.map.clear();
	}

	public Object get(final String key) {
		final Pair<Long, Object> pair = this.map.get(key);
		if (pair != null && pair.getLeft() > System.currentTimeMillis()) {
			return pair.getRight();
		}
		return null;
	}

	public <V> V put(final String key, final V value, final long expireAfter) {
		this.map.put(key, new ImmutablePair<>(System.currentTimeMillis() + expireAfter, value));
		return value;
	}

	public void remove(final String key) {
		this.map.remove(key);
	}

}
