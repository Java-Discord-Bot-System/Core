package com.almightyalpaca.discord.bot.system.util;

import java.util.concurrent.TimeUnit;

public class GCUtil {

	private static int		gcCounter		= 0;
	private static int		gcFixed			= 0;
	private static int		gcFixedCurrent	= 0;

	private static Thread	gcThread		= new Thread(() -> {
												boolean interrupted = false;
												while (!interrupted) {
													if (GCUtil.gcCounter > 0) {
														GCUtil.gcCounter--;
														System.gc();
													}
													if (GCUtil.gcFixed > 0) {
														GCUtil.gcFixedCurrent--;
													}
													if (GCUtil.gcFixedCurrent == 0) {
														GCUtil.gcFixedCurrent = GCUtil.gcFixed;
														if (GCUtil.gcCounter == 0) {
															System.gc();
														}
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

	public static void fixedRate(final int seconds) { // FIXME
		GCUtil.gcFixed = seconds;
		GCUtil.gcFixedCurrent = 0;
	}

	public static void runGC(final int seconds) {
		GCUtil.gcCounter = Math.max(GCUtil.gcCounter, seconds);
	}

	public static void shutdown() {
		gcThread.interrupt();
	}
}
