package com.dudev.util;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public final class BusProvider extends Bus {
	private static BusProvider instance;

	public static synchronized BusProvider getInstance() {
		if (instance == null) {
			instance = new BusProvider();
		}
		
		return instance;
	}

	private BusProvider() {
		super(ThreadEnforcer.ANY);
	}
}
