package com.almightyalpaca.adbs4j.bootstrap.exithandler;

import com.almightyalpaca.adbs4j.bootstrap.Bootstrap;
import com.almightyalpaca.adbs4j.bootstrap.Code;
import com.almightyalpaca.adbs4j.bootstrap.updater.Updater;

public class UpdateHandler implements IExitHandler {

	@Override
	public void handle(final Bootstrap bootstrap) {
		try {
			System.out.println("UPDATE exit value received. Searching for updates...");

			final Updater updater = new Updater(bootstrap);

			final String installedVersion = bootstrap.getLatestVersion().getName();

			final String version = updater.getLatestVersion();
			if (!installedVersion.equals(version)) {

				System.out.println("New version " + version + "found! Downloading files...");
				updater.update();
			} else {
				System.out.println("No new version found!");
			}
			System.out.println("Restarting now...");
			bootstrap.getLauncher().launch(Code.UPDATE, version);
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println("Encountered an exception while updating! Won't restart!");
		}

	}

}
