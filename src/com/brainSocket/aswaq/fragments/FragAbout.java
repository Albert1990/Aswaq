package com.brainSocket.aswaq.fragments;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragAbout extends Fragment{
	private HomeCallbacks homeCallbacks;
	
	private FragAbout()
	{
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_about, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		homeCallbacks=(HomeCallbacks)getActivity();
		homeCallbacks.closeSlideDrawer();
	}
	
	public static FragAbout newInstance()
	{
		return new FragAbout();
	}

}
