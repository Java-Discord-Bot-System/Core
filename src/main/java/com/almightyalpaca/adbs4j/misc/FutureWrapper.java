package com.almightyalpaca.adbs4j.misc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class FutureWrapper<V, O> implements Future<V> {

	private final Future<O> future;

	public FutureWrapper(final Future<O> future) {
		this.future = future;
	}

	public static <V, O> Future<V> wrap(final Future<O> f, final Transformer<V, O> t) {
		return new FutureWrapper<V, O>(f) {
			@Override
			protected V transform(final O o) {
				return t.tramsform(o);
			}
		};
	}

	@Override
	public boolean cancel(final boolean mayInterruptIfRunning) {
		return this.future.cancel(mayInterruptIfRunning);
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		return this.transform(this.future.get());
	}

	@Override
	public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return this.transform(this.future.get(timeout, unit));
	}

	@Override
	public boolean isCancelled() {
		return this.future.isCancelled();
	}

	@Override
	public boolean isDone() {
		return this.future.isDone();
	}

	protected abstract V transform(O o);

}