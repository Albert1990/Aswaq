package com.brainSocket.fragments;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragClients extends Fragment{
	private HomeCallbacks homeCallbacks;
	
	private FragClients()
	{
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		DataStore.getInstance().attemptGetClients(getClientsCallback);
	}
	
	private DataRequestCallback getClientsCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public static FragClients newInstance()
	{
		return new FragClients();
	}

}
