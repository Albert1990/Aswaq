package com.brainSocket.aswaq;

import com.brainSocket.enums.FragmentType;
import com.brainSocket.fragments.FragAddAdvertise;
import com.brainSocket.fragments.FragAds;
import com.brainSocket.fragments.FragAdvertiseDetails;
import com.brainSocket.fragments.FragMain;
import com.brainSocket.fragments.FragSubCategories;
import com.brainSocket.fragments.FragVerification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

public class AppBaseActivity extends ActionBarActivity implements HomeCallbacks{

	//TODO this comment will delete later;
	protected boolean isVisible = false ;
	private FragmentType currentFragmentType;
	private FragmentManager fragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager=getSupportFragmentManager();
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
		//FacebookProvider.getInstance().onActiviyResult(arg0, arg1, arg2);
	}

	@Override
	public void showProgress(boolean show, int msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showToast(String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(AswaqApp.getAppContext(), msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFragment(FragmentType fragmentType) {
		// TODO Auto-generated method stub
		switch(fragmentType)
		{
		case Main:
			FragMain mainFrag = new FragMain();
			fragmentManager.beginTransaction()
				//.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,R.anim.slide_in_from_right,R.anim.slide_out_to_left)
				.replace(R.id.content_frame, mainFrag)
				.addToBackStack(FragmentType.Main.name())
				.commit();
			break;
		case AddAdvertise:
			FragAddAdvertise fragAddAdvertise = new FragAddAdvertise();
			fragmentManager.beginTransaction()
				//.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,R.anim.slide_in_from_right,R.anim.slide_out_to_left)
				.replace(R.id.content_frame, fragAddAdvertise)
				.addToBackStack(FragmentType.AddAdvertise.name())
				.commit();
			break;
		case Verification:
			FragVerification fragVerification=new FragVerification();
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragVerification)
			.commit();
			break;
		case SubCategories:
			FragSubCategories fragSubCategories=new FragSubCategories();
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragSubCategories)
			.addToBackStack(FragmentType.SubCategories.name())
			.commit();
			break;
		case ShowAds:
			FragAds fragAds=new FragAds();
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragAds)
			.addToBackStack(FragmentType.ShowAds.name())
			.commit();
			break;
		case AdvertiseDetails:
			FragAdvertiseDetails fragAdvertiseDetails=new FragAdvertiseDetails();
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragAdvertiseDetails)
			.addToBackStack(FragmentType.AdvertiseDetails.name())
			.commit();
			break;
		}
		currentFragmentType=fragmentType;
	}
	
}
