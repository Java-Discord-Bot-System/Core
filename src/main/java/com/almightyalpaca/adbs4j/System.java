package com.almightyalpaca.adbs4j;

import com.almightyalpaca.adbs4j.internal.extension.ExtensionManager;

public class System {

	public System() {
		try {
			new ExtensionManager();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) {
		new System();
	}
}
