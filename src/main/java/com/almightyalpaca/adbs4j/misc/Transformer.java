package com.almightyalpaca.adbs4j.misc;

@FunctionalInterface
public interface Transformer<V, O> {
	public V tramsform(O o);
}
