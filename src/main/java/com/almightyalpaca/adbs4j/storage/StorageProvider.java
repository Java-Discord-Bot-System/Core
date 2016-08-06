package com.almightyalpaca.adbs4j.storage;

import com.almightyalpaca.adbs4j.plugins.PluginInfo;

public interface StorageProvider {

	public StorageProviderInstance getGlobalInstance();

	public StorageProviderInstance getPluginInstance(PluginInfo info);

	public void unloadInstance(PluginInfo info);

}
