package com.brainSocket.aswaq.fragments;

import java.util.HashMap;
import java.util.List;

import com.brainSocket.aswaq.AdvertiseDetailsActivity;
import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.MainActivity;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.adapters.AdvertisesListAdapter;
import com.brainSocket.aswaq.adapters.CirclePageIndicator;
import com.brainSocket.aswaq.adapters.SliderAdapter;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.enums.SliderType;
import com.brainSocket.aswaq.models.AdvertiseModel;
import com.brainSocket.aswaq.models.SlideModel;

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
	private Handler sliderHandler=null;
	private View vNoDataPlaceHolder;
	private CirclePageIndicator circleIndicator;
	
	private FragAds()
	{
		sliderHandler=new Handler();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_category_ads, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		try
		{
			homeCallbacks=(HomeCallbacks)getActivity();
			lstAds=(ListView)getView().findViewById(R.id.lstAds);
			lstAds.setOnItemClickListener(this);
			vNoDataPlaceHolder=getView().findViewById(R.id.vNoDataPlaceHolder);
			
			View sliderHeader=getActivity().getLayoutInflater().inflate(R.layout.layout_slider, lstAds, false);
			
			vpSliderAds = (ViewPager) sliderHeader.findViewById(R.id.vpSliderAds);
			circleIndicator=(CirclePageIndicator)sliderHeader.findViewById(R.id.titles);
			lstAds.addHeaderView(sliderHeader, null, true);
			
			homeCallbacks.showProgress(true);
			String selectedSubCategoryName=getArguments().getString("selectedSubCategoryName");
			homeCallbacks.setTitle(selectedSubCategoryName);
			
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
			if(success)
			{
				ads=(List<AdvertiseModel>)data.getValue("ads");
				
				if(ads.size()>0)
				{
					vNoDataPlaceHolder.setVisibility(View.GONE);
					AdvertisesListAdapter advertisesListAdapter=new AdvertisesListAdapter(getActivity(), ads);
					lstAds.setAdapter(advertisesListAdapter);
				}
				
				slides=(List<SlideModel>)data.getValue("slides");
				SliderAdapter sliderAdapter=new SliderAdapter(getActivity(), slides,SliderType.Banner);
				vpSliderAds.setAdapter(sliderAdapter);
				circleIndicator.setViewPager(vpSliderAds);
				if(slides.size() > 1)
				{
					try
					{
						sliderHandler.removeCallbacks(SliderTransition);
					}catch(Exception ex){ex.printStackTrace();}
					sliderHandler.postDelayed(SliderTransition,AswaqApp.SLIDER_TRANSITION_INTERVAL);
				}				
				
			}
			else
			{
				homeCallbacks.showToast(getString(R.string.error_connection_error));
			}
			homeCallbacks.showProgress(false);
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
				sliderHandler.postDelayed(SliderTransition,AswaqApp.SLIDER_TRANSITION_INTERVAL);
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
		if(position==0)
			position=1;
		int adId=ads.get(position-1).getId();
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
			if(params.containsKey("selectedSubCategoryName"))
				extras.putString("selectedSubCategoryName", (String)params.get("selectedSubCategoryName"));
			fragAds.setArguments(extras);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return fragAds;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		try
		{
			sliderHandler.removeCallbacks(SliderTransition);
		}catch(Exception ex){ex.printStackTrace();}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		try
		{
			try
			{
				sliderHandler.removeCallbacks(SliderTransition);
			}catch(Exception ex){ex.printStackTrace();}
			sliderHandler.postDelayed(SliderTransition,AswaqApp.SLIDER_TRANSITION_INTERVAL);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
