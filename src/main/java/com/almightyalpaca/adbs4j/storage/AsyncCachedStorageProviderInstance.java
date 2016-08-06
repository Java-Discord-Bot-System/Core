package com.almightyalpaca.adbs4j.storage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public interface AsyncCachedStorageProviderInstance {

	public void delete(String... keys);

	public Future<List<String>> getList(int cacheTime, String key);

	public Future<List<String>> getList(int cacheTime, String key, int start, int stop);

	public Future<Integer> getListLenght(int cacheTime, String key);

	public Future<Map<String, String>> getMap(int cacheTime, String key);

	public Future<Boolean> getMapHasKey(int cacheTime, String key, String mapKey);

	public Future<? extends Collection<String>> getMapKeys(int cacheTime, String key);

	public Future<Integer> getMapLenght(int cacheTime, String key);

	public Future<String> getMapValue(String key, String field);

	public Future<? extends Collection<String>> getMapValues(int cacheTime, String key);

	public Future<Map<String, String>> getMapValues(String key, String... fields);

	public Future<Set<String>> getSet(int cacheTime, String key);

	public Future<Boolean> getSetContains(int cacheTime, String key, String value);

	public Future<Integer> getSetLenght(int cacheTime, String key);

	public Future<String> getString(int cacheTime, String key);

	public Future<Map<String, String>> getStrings(int cacheTime, String... keys);

	public void listAppend(String key, String... values);

	public void listPrepend(String key, String... values);

	public void listRemove(String key, int... indexes);

	public void listSet(String key, int position, String value);

	public void mapAdd(String key, Map<String, String> map);

	public void mapAdd(String key, String mapKey, String value);

	public void mapRemove(String key, String... fields);

	public void putString(String key, String value);

	public void setAdd(String key, String... values);

	public void setRemove(String key, String... values);

}
