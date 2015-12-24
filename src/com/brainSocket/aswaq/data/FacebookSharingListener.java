package com.brainSocket.aswaq.data;

public interface FacebookSharingListener
{
	void onShareResult(boolean success);
	
	void onShareError(String error);
	
	void onShareCancelled();
}
