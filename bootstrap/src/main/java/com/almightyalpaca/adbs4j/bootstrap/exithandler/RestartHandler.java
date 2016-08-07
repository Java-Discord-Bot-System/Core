package com.almightyalpaca.adbs4j.bootstrap.exithandler;

import com.almightyalpaca.adbs4j.bootstrap.Bootstrap;
import com.almightyalpaca.adbs4j.bootstrap.Code;

public class RestartHandler implements IExitHandler {

	@Override
	public void handle(final Bootstrap bootstrap) throws Exception {
		System.out.println("RESTART exit value received. Restarting...");
		bootstrap.getLauncher().launchLatest(Code.RESTART);
	}

}
