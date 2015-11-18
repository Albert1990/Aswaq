package com.brainSocket.fragments;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.models.AdvertiseModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragAdvertiseDetails extends Fragment {
	private HomeCallbacks homeCallbacks;
	
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
		int adId=1;
		DataStore.getInstance().attemptGetAdvertiseDetails(adId, getAdvertiseDetailsCallback);
	}
	
	private DataRequestCallback getAdvertiseDetailsCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				AdvertiseModel ad=(AdvertiseModel)data.getValue("adDetails");
			}
		}
	};
}
