package com.almightyalpaca.adbs4j.bootstrap.exithandler;

import com.almightyalpaca.adbs4j.bootstrap.Bootstrap;
import com.almightyalpaca.adbs4j.bootstrap.Code;

public class UnknownHandler extends AbstractExitHandler {

	@Override
	public void handle(final Bootstrap bootstrap) throws Exception {
		System.out.println("Unknown exit value received. Restarting...");
		bootstrap.getLauncher().launchLatest(Code.UNKNOWN);
	}

}
