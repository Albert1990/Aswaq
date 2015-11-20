package com.brainSocket.fragments;

import java.util.List;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.models.SlideModel;

import enums.FragmentType;
import adapters.AdvertisesListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
		homeCallbacks=(HomeCallbacks)getActivity();
		lstAds=(ListView)getActivity().findViewById(R.id.lstAds);
		lstAds.setOnItemClickListener(this);
		int categoryId=4;
		DataStore.getInstance().attemptGetCategoryAds(categoryId, getCategoryAdsCallback);
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
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int adId=ads.get(position).getId();
		homeCallbacks.loadFragment(FragmentType.AdvertiseDetails);
	}
}
