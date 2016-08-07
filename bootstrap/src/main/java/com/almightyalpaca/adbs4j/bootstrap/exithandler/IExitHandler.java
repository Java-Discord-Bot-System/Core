package com.almightyalpaca.adbs4j.bootstrap.exithandler;

import com.almightyalpaca.adbs4j.bootstrap.Bootstrap;

@FunctionalInterface
public interface IExitHandler {

	public void handle(Bootstrap bootstrap) throws Exception;
}
