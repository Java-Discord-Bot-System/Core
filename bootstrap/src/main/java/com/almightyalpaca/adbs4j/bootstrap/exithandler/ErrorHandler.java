package com.almightyalpaca.adbs4j.bootstrap.exithandler;

import com.almightyalpaca.adbs4j.bootstrap.Bootstrap;
import com.almightyalpaca.adbs4j.bootstrap.Code;

public class ErrorHandler implements IExitHandler {

	int	errors		= 0;
	int	maxErrors	= 5;

	@Override
	public void handle(final Bootstrap bootstrap) throws Exception {
		System.out.println("ERROR_RESTART exit value received. " + ++this.errors + '/' + this.maxErrors + ' ');
		if (this.errors < this.maxErrors) {
			System.out.print("Restarting...");
			bootstrap.getLauncher().launchLatest(Code.ERROR_RESTART);
		} else {
			System.out.print("Won't restart!");
		}
	}

}
