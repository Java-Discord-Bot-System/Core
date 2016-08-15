package com.almightyalpaca.adbs4j.misc;

import java.util.Queue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class RateLimit {

	private final long			period;
	private final int			times;
	private final Queue<Long>	queue;

	public RateLimit(final int times, final long period, final TimeUnit unit) {
		this.times = times;
		this.period = unit.toMillis(period);
		this.queue = new CircularFifoQueue<>(times);
	}

	public boolean action() {
		if (this.isLimited()) {
			return false;
		} else {
			this.queue.add(System.currentTimeMillis());
			return true;
		}
	}

	public boolean isLimited() {
		if (this.queue.size() < this.times) {
			return false;
		} else {
			final long l = this.queue.peek();
			return l > System.currentTimeMillis() - this.period;
		}
	}
}
