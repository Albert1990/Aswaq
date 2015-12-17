package com.brainSocket.fragments;

import java.util.List;

import com.brainSocket.adapters.AdvertisesListAdapter;
import com.brainSocket.aswaq.AdvertiseDetailsActivity;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.MainActivity;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.models.AdvertiseModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragFavourites extends Fragment implements OnItemClickListener{
	private ListView lvFavouritesAds;
	private View vNoDataPlaceHolder;
	private HomeCallbacks homeCallbacks;
	private List<AdvertiseModel> ads;
	
	private FragFavourites()
	{
		//constructer
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_favourites, container, false);
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
		lvFavouritesAds=(ListView)getActivity().findViewById(R.id.lvFavouritesAds);
		lvFavouritesAds.setOnItemClickListener(this);
		vNoDataPlaceHolder=getActivity().findViewById(R.id.vNoDataPlaceHolder);
		homeCallbacks.closeSlideDrawer();
		homeCallbacks.showProgress(true);
		DataStore.getInstance().attemptGetMyFavourites(getMyFavouritesCallback);
	}
	
	private DataRequestCallback getMyFavouritesCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			homeCallbacks.showProgress(false);
			if(success)
			{
				ads=(List<AdvertiseModel>) data.getValue("ads");
				if(ads.size()>0)
				{
					vNoDataPlaceHolder.setVisibility(View.GONE);
					AdvertisesListAdapter adapter=new AdvertisesListAdapter(getActivity(), ads);
					lvFavouritesAds.setAdapter(adapter);
				}
			}
			else
			{
				homeCallbacks.showToast(getString(R.string.error_connection_error));
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i=new Intent(getActivity().getApplicationContext(),AdvertiseDetailsActivity.class);
		i.putExtra("selectedAdId", ads.get(position).getId());
		getActivity().startActivity(i);
	}
	
	public static FragFavourites newInstance()
	{
		return new FragFavourites();
	}

}
