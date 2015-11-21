package com.brainSocket.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Switch;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.views.EditTextCustomFont;
import com.brainSocket.views.TextViewCustomFont;

import enums.FragmentType;

public class FragAddAdvertise extends Fragment implements OnClickListener{
	private HomeCallbacks homeCallback;
	private EditTextCustomFont txtProductDescription;
	private EditTextCustomFont tvPrice;
	private EditTextCustomFont tvPhone;
	private Switch swhNew;
	private TextViewCustomFont btnSubmit;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_add_advertise, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		homeCallback=(HomeCallbacks)getActivity();
		txtProductDescription=(EditTextCustomFont)getActivity().findViewById(R.id.txtProductDescription);
		tvPrice=(EditTextCustomFont)getActivity().findViewById(R.id.tvPrice);
		tvPhone=(EditTextCustomFont)getActivity().findViewById(R.id.tvPhone);
		swhNew=(Switch)getActivity().findViewById(R.id.swhNew);
		btnSubmit=(TextViewCustomFont)getActivity().findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
	}
	
	private void addNewAdvertise()
	{
		boolean cancel=false;
		View focusView=null;
		//check description textbox
		String description=txtProductDescription.getText().toString();
		int selectedCategoryId=2;
		boolean isUsed=swhNew.isActivated();
		int price=0;
		String phone=tvPhone.getText().toString();
		JSONArray telephones=new JSONArray();
		
		if(AswaqApp.isEmptyOrNull(description))
		{
			txtProductDescription.setError(getString(R.string.error_description_required));
			focusView=txtProductDescription;
			cancel=true;
		}
		
		if(AswaqApp.isEmptyOrNull(tvPrice.getText().toString()))
		{
			tvPrice.setError(getString(R.string.error_price_required));
			focusView=tvPrice;
			cancel=true;
		}
		else
		{
			price=Integer.parseInt(tvPrice.getText().toString());
		}
		
		if(AswaqApp.isEmptyOrNull(phone))
		{
			tvPhone.setError(getString(R.string.error_phone_required));
			focusView=tvPhone;
			cancel=true;
		}
		
		
		
		if(cancel){
			focusView.requestFocus();
		}else{
			telephones.put(phone);
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
					//homeCallback.showToast("the new advertise have been added successfully");
					homeCallback.loadFragment(FragmentType.Main);
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		int viewId=v.getId();
		switch(viewId){
		case R.id.btnSubmit:
			addNewAdvertise();
			break;
		}
	}

}
