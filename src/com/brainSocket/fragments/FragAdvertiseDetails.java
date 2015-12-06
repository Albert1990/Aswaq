package com.brainSocket.fragments;

import java.util.HashMap;

import com.brainSocket.adapters.SliderAdapter;
import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.views.TextViewCustomFont;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

public class FragAdvertiseDetails extends Fragment implements OnClickListener{
	private HomeCallbacks homeCallbacks;
	private TextViewCustomFont tvPaid;
	private ViewPager vpSlider;
	private ImageView btnFbPage;
	private TextViewCustomFont btnCall;
	private ImageView ivUser;
	private TextViewCustomFont tvUserName;
	private TextViewCustomFont tvUserRate;
	private RatingBar rbUserRate;
	private TextViewCustomFont tvPrice;
	private TextViewCustomFont tvDate;
	private TextViewCustomFont tvCat;
	private TextViewCustomFont tvPlace;
	private TextViewCustomFont tvDesc;
	private AdvertiseModel ad;
	
	
	private FragAdvertiseDetails()
	{
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_advertise_details, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		try
		{
			homeCallbacks=(HomeCallbacks)getActivity();
			homeCallbacks.showProgress(true);
			tvPaid=(TextViewCustomFont)getActivity().findViewById(R.id.tvPaid);
			vpSlider=(ViewPager)getActivity().findViewById(R.id.vpSlider);
			btnFbPage=(ImageView)getActivity().findViewById(R.id.btnFbPage);
			btnFbPage.setOnClickListener(this);
			btnCall=(TextViewCustomFont)getActivity().findViewById(R.id.btnCall);
			btnCall.setOnClickListener(this);
			ivUser=(ImageView)getActivity().findViewById(R.id.ivUser);
			ivUser.setOnClickListener(this);
			tvUserName=(TextViewCustomFont)getActivity().findViewById(R.id.tvUserName);
			tvUserName.setOnClickListener(this);
			tvUserRate=(TextViewCustomFont)getActivity().findViewById(R.id.tvUserRate);
			rbUserRate=(RatingBar)getActivity().findViewById(R.id.rbUserRate);
			tvPrice=(TextViewCustomFont)getActivity().findViewById(R.id.tvPrice);
			tvDate=(TextViewCustomFont)getActivity().findViewById(R.id.tvDate);
			tvCat=(TextViewCustomFont)getActivity().findViewById(R.id.tvCat);
			tvPlace=(TextViewCustomFont)getActivity().findViewById(R.id.tvPlace);
			tvDesc=(TextViewCustomFont)getActivity().findViewById(R.id.tvDesc);
			
			DataStore.getInstance().attemptGetAdvertiseDetails(getArguments().getInt("selectedAdId"), getAdvertiseDetailsCallback);
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
				ad=(AdvertiseModel)data.getValue("adDetails");
				if(ad.IsPinned()==0)
					tvPaid.setVisibility(View.INVISIBLE);
				
					tvUserName.setText(ad.getUser().getName());
					tvPrice.setText(Integer.toString(ad.getPrice()));
					tvCat.setText(ad.getCategory().getName());
					tvDate.setText(ad.getDate());
					tvDesc.setText(ad.getDescription());
					String imgPath=null;
					if(ad.getUser().getPicture().length()==0)
						imgPath= ServerAccess.IMAGE_SERVICE_URL+"users/"+AswaqApp.DEFAULT_USER_IMAGE;
					else
						imgPath=ServerAccess.IMAGE_SERVICE_URL+"users/"+ad.getUser().getPicture();
					rbUserRate.setRating(ad.getUser().getRate());
					Picasso.with(getActivity()).load(imgPath).into(ivUser);
					SliderAdapter adapter=new SliderAdapter(getActivity(), ad.getImages());
					vpSlider.setAdapter(adapter);
					homeCallbacks.showProgress(false);
					String rate=Float.toString(ad.getUser().getRate());
					tvUserRate.setText(rate);
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
	
	private void showUserPage()
	{
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("userId", ad.getUserId());
		homeCallbacks.loadFragment(FragmentType.UserPage, params);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.btnCall:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +ad.getUser().getPhoneNum()));
			startActivity(intent);
			break;
		case R.id.ivUser:
			showUserPage();
			break;
		case R.id.tvUserName:
			showUserPage();
			break;
		}
	}
}
