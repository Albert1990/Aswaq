package com.brainSocket.aswaq;


import com.brainSocket.aswaq.data.FacebookProvider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class AppBaseActivity extends ActionBarActivity {

	//TODO this comment will delete later;
	protected boolean isVisible = false ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//RosaryApp.setCurrentAcivity(null);
		isVisible = false ;
	}
	@Override
	protected void onResume() {
		super.onResume();
		//RosaryApp.setCurrentAcivity(this);
		isVisible = true ;
	}
	
	@Override
	protected void onStop() {
		//TrackingMgr.getInstance().onAcitivityStop(this);
		super.onStop();
	}
	@Override
	protected void onStart() {
		//TrackingMgr.getInstance().onAcitivityStart(this);
		super.onStart();
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		FacebookProvider.getInstance().onActiviyResult(arg0, arg1, arg2);
	}
	
}
