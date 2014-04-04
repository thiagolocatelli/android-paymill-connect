package com.github.thiagolocatelli.paymill;

public interface PaymillConnectListener {
	
	public abstract void onConnected();
	
	public abstract void onDisconnected();

	public abstract void onError(String error);
	
}