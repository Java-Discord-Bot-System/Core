package com.almightyalpaca.adbs4j.storage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CachedStorageProviderInstance {

	public AsyncCachedStorageProviderInstance async();

	public void delete(String... keys);

	public List<String> getList(int cacheTime, String key);

	public List<String> getList(int cacheTime, String key, int start, int stop);

	public int getListLenght(int cacheTime, String key);

	public Map<String, String> getMap(int cacheTime, String key);

	public boolean getMapHasKey(int cacheTime, String key, String mapKey);

	public Collection<String> getMapKeys(int cacheTime, String key);

	public int getMapLenght(int cacheTime, String key);

	public String getMapValue(String key, String field);

	public Collection<String> getMapValues(int cacheTime, String key);

	public Map<String, String> getMapValues(String key, String... fields);

	public Set<String> getSet(int cacheTime, String key);

	public boolean getSetContains(int cacheTime, String key, String value);

	public int getSetLenght(int cacheTime, String key);

	public String getString(int cacheTime, String key);

	public Map<String, String> getStrings(int cacheTime, String... key);

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
