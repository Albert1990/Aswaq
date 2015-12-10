package com.brainSocket.fragments;

import java.util.HashMap;
import java.util.List;

import com.brainSocket.adapters.AdvertisesListAdapter;
import com.brainSocket.adapters.SliderAdapter;
import com.brainSocket.aswaq.AdvertiseDetailsActivity;
import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.MainActivity;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.models.SlideModel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FragAds extends Fragment implements OnItemClickListener{
	private HomeCallbacks homeCallbacks;
	private ListView lstAds;
	private List<AdvertiseModel> ads;
	private List<SlideModel> slides;
	private ViewPager vpSliderAds;
	private int currentSlide=0;
	private boolean stopSliderTransition;
	
	private FragAds()
	{
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.frag_category_ads, container, false);
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
			lstAds=(ListView)getActivity().findViewById(R.id.lstAds);
			lstAds.setOnItemClickListener(this);
			//vpSliderAds=(ViewPager)getActivity().findViewById(R.id.vpSliderAds);
			
			vpSliderAds = (ViewPager) getActivity().getLayoutInflater().inflate(R.layout.layout_slider, lstAds, false);

			lstAds.addHeaderView(vpSliderAds, null, false);
			
			homeCallbacks.showProgress(true);
			DataStore.getInstance().attemptGetCategoryAds(getArguments().getInt("selectedSubCategoryId"), getCategoryAdsCallback);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private DataRequestCallback getCategoryAdsCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				ads=(List<AdvertiseModel>)data.getValue("ads");
				slides=(List<SlideModel>)data.getValue("slides");
				
				AdvertisesListAdapter advertisesListAdapter=new AdvertisesListAdapter(getActivity(), ads);
				
				lstAds.setAdapter(advertisesListAdapter);
				
				SliderAdapter sliderAdapter=new SliderAdapter(getActivity(), slides);
				vpSliderAds.setAdapter(sliderAdapter);
				if(slides.size() > 0)
				{
					stopSliderTransition=false;
					new Handler().postDelayed(SliderTransition,AswaqApp.SLIDER_TRANSITION_INTERVAL);
				}
				homeCallbacks.showProgress(false);
			}
		}
	};
	
private Runnable SliderTransition=new Runnable() {
		
		@Override
		public void run() {
			try
			{
				if(currentSlide >= slides.size())
					currentSlide=0;
				vpSliderAds.setCurrentItem(currentSlide, true);
				currentSlide++;
				if(!stopSliderTransition)
					new Handler().postDelayed(SliderTransition,AswaqApp.SLIDER_TRANSITION_INTERVAL);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int adId=ads.get(position).getId();
		//homeCallbacks.showToast(Integer.toString(adId));
		//homeCallbacks.loadFragment(FragmentType.AdvertiseDetails,params);
		//Intent i=new Intent(MainActivity.this,AdvertiseDetailsActivity.class);
		//homeCallbacks.loadActivity(AdvertiseDetailsActivity.class, params);
		Intent i=new Intent(getActivity(), AdvertiseDetailsActivity.class);
		i.putExtra("selectedAdId", adId);
		startActivity(i);
		
	}
	
	public static FragAds newInstance(HashMap<String, Object> params)
	{
		FragAds fragAds=new FragAds();
		try
		{
			Bundle extras=new Bundle();
			if(params.containsKey("selectedSubCategoryId"))
				extras.putInt("selectedSubCategoryId", (Integer)params.get("selectedSubCategoryId"));
			fragAds.setArguments(extras);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return fragAds;
	}
}
