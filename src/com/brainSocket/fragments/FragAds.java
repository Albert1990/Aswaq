package com.brainSocket.fragments;

import java.util.List;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.models.SlideModel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragAds extends Fragment{
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
		homeCallbacks=(HomeCallbacks)getActivity();
		int categoryId=2;
		DataStore.getInstance().attemptGetCategoryAds(categoryId, getCategoryAdsCallback);
	}
	
	private DataRequestCallback getCategoryAdsCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				List<AdvertiseModel> ads=(List<AdvertiseModel>)data.getValue("ads");
				List<SlideModel> slides=(List<SlideModel>)data.getValue("slides");
				
				homeCallbacks.showToast("there is "+ads.size()+" ads");
			}
		}
	};
}
