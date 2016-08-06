package com.almightyalpaca.adbs4j.bootstrap.exithandler;

import com.almightyalpaca.adbs4j.bootstrap.Bootstrap;
import com.almightyalpaca.adbs4j.bootstrap.Code;

public class RestartHandler extends AbstractExitHandler {

	@Override
	public void handle(final Bootstrap bootstrap) throws Exception {
		System.out.println("Restart exit value received. Restarting...");
		bootstrap.getLauncher().launchLatest(Code.RESTART);
	}

}
