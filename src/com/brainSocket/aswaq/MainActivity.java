package com.brainSocket.aswaq;

import java.util.HashMap;
import java.util.List;

import com.brainSocket.adapters.DrawerAdapter;
import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.fragments.FragAddAdvertise;
import com.brainSocket.fragments.FragAds;
import com.brainSocket.fragments.FragAdvertiseDetails;
import com.brainSocket.fragments.FragMain;
import com.brainSocket.fragments.FragSubCategories;
import com.brainSocket.fragments.FragVerification;
import com.brainSocket.models.AppUser;
import com.brainSocket.models.CategoryModel;
import com.brainSocket.views.TextViewCustomFont;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppBaseActivity implements OnClickListener,HomeCallbacks{
	private ListView lvDrawer ;
	private DrawerAdapter adapter ;
	private DrawerLayout dlDrawer ;
	private View llLogout;
	private FragmentManager fragmentManager;
	private FragmentType currentFragmentType;
	private Dialog dialogLoading;
	private ImageView ivMenu;
	private TextViewCustomFont tvFragTitle;
	private ImageView ivBackHome;
	private ImageView ivLogo;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initCustomActionBar();
	}
	
	private void init()
	{
		fragmentManager=getSupportFragmentManager();
		AppUser me=DataCacheProvider.getInstance().getMe();
		if(!me.isVerified())
		{
			loadFragment(FragmentType.Verification,null);
		}
		else
		{
			lvDrawer = (ListView) findViewById(R.id.lvDrawer);
			adapter = new DrawerAdapter(this, lvDrawer);
			lvDrawer.setAdapter(adapter);
			dlDrawer = (DrawerLayout) findViewById(R.id.dlDrawer);
			llLogout=findViewById(R.id.llLogout);
			llLogout.setOnClickListener(this);
			loadFragment(FragmentType.Main,null);
		}
	}
	
private void initCustomActionBar() {
		
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false) ;
		mActionBar.setHomeAsUpIndicator(null);
		//LayoutInflater mInflater = LayoutInflater.from(this); 
		mActionBar.setCustomView(R.layout.custom_actionbar);
		mActionBar.setDisplayShowCustomEnabled(true);
		View mCustomView = mActionBar.getCustomView() ;
		mCustomView.invalidate();
		
		tvFragTitle = (TextViewCustomFont) mCustomView.findViewById(R.id.tvFragTitle) ;
		ivMenu = (ImageView) mCustomView.findViewById(R.id.ivMenu);
		ivBackHome = (ImageView) mCustomView.findViewById(R.id.ivBack);
		ivLogo = (ImageView) mCustomView.findViewById(R.id.ivLogo);
		//btnGroup = findViewById(R.id.btnGroup);
		
		ivMenu.setOnClickListener(this);
		//btnGroup.setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
//	@Override
//	public void onBackPressed() {

//		//super.onBackPressed();
//		int  entrys = getSupportFragmentManager().getBackStackEntryCount() ;
//		
//		try {
//			FragmentType backFragmentType;
//			if(entrys>1)
//			{
//				backFragmentType = FragmentType.valueOf(getSupportFragmentManager().getBackStackEntryAt(entrys-2).getName());
//				if(backFragmentType!=FragmentType.Main)
//				{
//					getSupportFragmentManager().popBackStackImmediate();
//					getSupportFragmentManager().popBackStackImmediate();
//					loadFragment(backFragmentType);
//				}
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	private void toggleDrawer(){
		try{
			if(dlDrawer.isDrawerOpen(Gravity.RIGHT)){
				dlDrawer.closeDrawer(Gravity.RIGHT);
			}else{
				dlDrawer.openDrawer(Gravity.RIGHT);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void closeDrawer(){
		try{
			if(dlDrawer.isDrawerOpen(Gravity.RIGHT)){
				dlDrawer.closeDrawer(Gravity.RIGHT);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.llLogout:
			DataCacheProvider.getInstance().removeStoredMe();
			break;
		case R.id.ivMenu:
			toggleDrawer();
			break;
		}
	}
	
	@Override
	public void showProgress(boolean show) {
		if(dialogLoading==null)
		{
			dialogLoading = new Dialog(this);
			dialogLoading.setCancelable(false);
			dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogLoading.setContentView(R.layout.dialog_custom_loading);
			dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		}
		if(show)
			dialogLoading.show();
		else
			dialogLoading.dismiss();
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
	public void loadFragment(FragmentType fragmentType,HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		switch(fragmentType)
		{
		case Main:
			FragMain mainFrag = FragMain.newInstance();
			fragmentManager.beginTransaction()
				//.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,R.anim.slide_in_from_right,R.anim.slide_out_to_left)
				.replace(R.id.content_frame, mainFrag)
				//.addToBackStack(FragmentType.Main.name())
				.commit();
			break;
		case AddAdvertise:
			FragAddAdvertise fragAddAdvertise = FragAddAdvertise.newInstance(params);
			fragmentManager.beginTransaction()
				//.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,R.anim.slide_in_from_right,R.anim.slide_out_to_left)
				.replace(R.id.content_frame, fragAddAdvertise)
				.addToBackStack(FragmentType.AddAdvertise.name())
				.commit();
			break;
		case Verification:
			FragVerification fragVerification=FragVerification.newInstance();
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragVerification)
			.commit();
			break;
		case SubCategories:
			FragSubCategories fragSubCategories=FragSubCategories.newInstance(params);
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragSubCategories)
			.addToBackStack(FragmentType.SubCategories.name())
			.commit();
			break;
		case ShowAds:
			FragAds fragAds=FragAds.newInstance(params);
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragAds)
			.addToBackStack(FragmentType.ShowAds.name())
			.commit();
			break;
		case AdvertiseDetails:
			FragAdvertiseDetails fragAdvertiseDetails=FragAdvertiseDetails.newInstance(params);
			fragmentManager.beginTransaction()
			.replace(R.id.content_frame, fragAdvertiseDetails)
			.addToBackStack(FragmentType.AdvertiseDetails.name())
			.commit();
			break;
		}
		currentFragmentType=fragmentType;
	}
}
