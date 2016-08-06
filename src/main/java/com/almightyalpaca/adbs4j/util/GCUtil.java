package com.almightyalpaca.adbs4j.util;

import java.util.concurrent.TimeUnit;

public class GCUtil {

	private static int		gcCounter	= 0;

	private static Thread	gcThread	= new Thread(() -> {
											boolean interrupted = false;
											while (!interrupted) {
												if (GCUtil.gcCounter > 0) {
													GCUtil.gcCounter--;
													System.gc();
												}
												try {
													TimeUnit.SECONDS.sleep(1);
												} catch (final InterruptedException e) {
													interrupted = true;
												}
											}
										}, "Garbade Collection Thread");

	static {
		GCUtil.gcThread.setPriority(Thread.MIN_PRIORITY);
		GCUtil.gcThread.setDaemon(true);
		GCUtil.gcThread.start();
	}

	public static void runFor(final int seconds) {
		GCUtil.gcCounter = Math.max(GCUtil.gcCounter, seconds);
	}

	public static void shutdown() {
		GCUtil.gcThread.interrupt();
	}
}
