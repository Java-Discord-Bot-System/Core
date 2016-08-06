package com.almightyalpaca.adbs4j.bootstrap.exithandler;

import com.almightyalpaca.adbs4j.bootstrap.Bootstrap;

public class ShutdownHandler extends AbstractExitHandler {

	@Override
	public void handle(final Bootstrap bootstrap) {
		System.out.println("Shutdown exit value received. Won't restart!");
	}

}
