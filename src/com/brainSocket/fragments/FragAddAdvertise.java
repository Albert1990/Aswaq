package com.brainSocket.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;

public class FragAddAdvertise extends Fragment implements OnClickListener{
	private HomeCallbacks homeCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_add_advertise, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
	}
	
	private void addNewAdvertise()
	{
		boolean cancel=false;
		//check description textbox
		String description="Acer extensa 4320 in a very good state";
		int selectedCategoryId=2;
		boolean isUsed=true;
		int price=30000;
		JSONArray telephones=new JSONArray();
		telephones.put("0932525649");
		telephones.put("4460467");
		
		if(cancel){
			
		}else{
			DataStore.getInstance().attemptAddNewAdvertise(description,
					selectedCategoryId,
					isUsed,price,telephones,addNewAdvertiseCallback);
		}
	}
	
	private DataRequestCallback addNewAdvertiseCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			if(success){
				if(data.getFlag()==ServerAccess.ERROR_CODE_done){
					homeCallback.showToast("the new advertise have been added successfully");
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		int viewId=v.getId();
		switch(viewId){
		case 1:
			addNewAdvertise();
			break;
		}
	}

}
