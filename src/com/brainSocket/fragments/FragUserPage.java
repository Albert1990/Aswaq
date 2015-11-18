package com.brainSocket.fragments;

import java.util.List;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.models.AppUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class FragUserPage extends Fragment implements OnClickListener{
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
		int userId=1;
		DataStore.getInstance().attemptGetUserPage(userId,getUserPageCallback);
	}
	
	private DataRequestCallback getUserPageCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				AppUser user=(AppUser)data.getValue("user");
				List<AdvertiseModel> userAds=(List<AdvertiseModel>)data.getValue("userAds");
				
			}
		}
	};
	
	private void follow(int userId)
	{
		
		DataStore.getInstance().attemptFollowUser(userId,followUserCallback);
	}
	
	private DataRequestCallback followUserCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId=v.getId();
		switch(viewId)
		{
		case 1:
			
			break;
		}
	}

}
