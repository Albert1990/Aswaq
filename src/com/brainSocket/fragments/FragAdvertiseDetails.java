package com.brainSocket.fragments;

import java.util.HashMap;

import com.brainSocket.aswaq.AswaqApp;
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
	
	private FragAdvertiseDetails()
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
		int selectedAdId=savedInstanceState.getInt("selectedAdId");
		init(selectedAdId);
	}
	
	private void init(int selectedAdId)
	{
		try
		{
			DataStore.getInstance().attemptGetAdvertiseDetails(selectedAdId, getAdvertiseDetailsCallback);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
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
	
	public static FragAdvertiseDetails newInstance(HashMap<String, Object> params)
	{
		FragAdvertiseDetails fragAdvertiseDetails=new FragAdvertiseDetails();
		try
		{
			Bundle extras=new Bundle();
			if(params.containsKey("selectedAdId"))
				extras.putInt("selectedAdId",(Integer) params.get("selectedAdId"));
			fragAdvertiseDetails.setArguments(extras);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return fragAdvertiseDetails;
	}
}
