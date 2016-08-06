package com.almightyalpaca.adbs4j.internal;

import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;
import com.almightyalpaca.adbs4j.storage.CachedStorageProviderInstance;

public class BotPermissionManager {

	private final CachedStorageProviderInstance storage;

	public BotPermissionManager(final ExtensionManager manager) {
		this.storage = manager.getGlobalStorageProvider().cached();
	}

	public boolean isAdmin(final String id) {
		return this.storage.getSet(60, "admins").contains(id);
	}

}
