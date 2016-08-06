package com.almightyalpaca.adbs4j.storage;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StorageProviderInstance {

	public AsyncStorageProviderInstance async();

	public CachedStorageProviderInstance cached();

	public void delete(String... keys);

	public List<String> getList(String key);

	public List<String> getList(String key, int start, int stop);

	public int getListLenght(String key);

	public Map<String, String> getMap(String key);

	public boolean getMapHasKey(String key, String mapKey);

	public Collection<String> getMapKeys(String key);

	public int getMapLenght(String key);

	public String getMapValue(String key, String field);

	public Collection<String> getMapValues(String key);

	public Map<String, String> getMapValues(String key, String... fields);

	public Set<String> getSet(String key);

	public boolean getSetContains(String key, String value);

	public int getSetLenght(String key);

	public String getString(String key);

	public Map<String, String> getStrings(String... keys);

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
