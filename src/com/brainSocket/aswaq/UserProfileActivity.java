package com.brainSocket.aswaq;

import java.util.HashMap;

import com.brainSocket.adapters.DrawerAdapter;
import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.models.AppUser;
import com.brainSocket.views.TextViewCustomFont;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class UserProfileActivity extends AppBaseActivity implements OnClickListener,HomeCallbacks{
	//slide drawer
		private ListView lvDrawer ;
		private DrawerAdapter adapter ;
		private DrawerLayout dlDrawer ;
		private View llLogout;
		
		//actionbar
		private ImageView ivMenu;
		private TextViewCustomFont tvFragTitle;
		private ImageView ivBackHome;
		private ImageView ivLogo;
		
		//view members
		private EditText txtUserNameRegister;
		private EditText txtMobileNumberRegister;
		private EditText txtAddressRegister;
		private EditText txtDescriptionRegister;
		private TextViewCustomFont btnEdit;
		private Dialog dialogLoading;
		

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_user_profile);
	    init();
	    initCustomActionBar();
	    //initSlideDrawer();
	}
	
	private void init()
	{
		try
		{
			AppUser me=DataCacheProvider.getInstance().getMe();
			if(me!=null)
			{
				txtUserNameRegister=(EditText)findViewById(R.id.txtUserNameRegister);
				txtUserNameRegister.setText(me.getName());
				
				txtMobileNumberRegister=(EditText)findViewById(R.id.txtMobileNumberRegister);
				txtMobileNumberRegister.setText(me.getPhoneNum());
				
				txtAddressRegister=(EditText)findViewById(R.id.txtAddressRegister);
				txtAddressRegister.setText(me.getAddress());
				
				txtDescriptionRegister=(EditText)findViewById(R.id.txtDescriptionRegister);
				txtDescriptionRegister.setText(me.getDescription());
				
				btnEdit=(TextViewCustomFont)findViewById(R.id.btnEditUserProfile);
				btnEdit.setOnClickListener(this);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	private void initSlideDrawer()
	{
		lvDrawer = (ListView) findViewById(R.id.lvDrawer);
		adapter = new DrawerAdapter(this, lvDrawer);
		lvDrawer.setAdapter(adapter);
		dlDrawer = (DrawerLayout) findViewById(R.id.dlDrawer);
		llLogout=findViewById(R.id.llLogout);
		llLogout.setOnClickListener(this);
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
		setActionBarColor(Color.argb(30, 0, 0, 0));
		mActionBar.setDisplayShowCustomEnabled(true);
		View mCustomView = mActionBar.getCustomView() ;
		mCustomView.invalidate();
		
		tvFragTitle = (TextViewCustomFont) mCustomView.findViewById(R.id.tvFragTitle) ;
		tvFragTitle.setText(getString(R.string.actionbar_edit_user_profile));
		tvFragTitle.setVisibility(View.VISIBLE);
		ivMenu = (ImageView) mCustomView.findViewById(R.id.ivMenu);
		ivBackHome = (ImageView) mCustomView.findViewById(R.id.ivBack);
		ivBackHome.setVisibility(View.VISIBLE);
		ivBackHome.setOnClickListener(this);
		ivLogo = (ImageView) mCustomView.findViewById(R.id.ivLogo);
		ivLogo.setVisibility(View.GONE);
		
		ivMenu.setOnClickListener(this);
	}

public void setActionBarColor(int color){
	getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
}
/**
 * update content of the actionBar accourding to the current fragment
 * @param section
 */
private void updateActionbar(FragmentType section) {
	switch(section) {
	case Main:
		setActionBarColor(Color.TRANSPARENT);
		break;
	default:
		setActionBarColor(getResources().getColor(R.color.app_theme));
		break;
	}

}
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

private void updateUserProfile()
{
	showProgress(true);
	String userName=txtUserNameRegister.getText().toString();
	String mobileNumber=txtMobileNumberRegister.getText().toString();
	String address=txtAddressRegister.getText().toString();
	String description=txtDescriptionRegister.getText().toString();
	DataStore.getInstance().attemptUpdateUserProfile(userName,
			mobileNumber,
			address,
			description,
			updateUserProfileCallback);
}

private DataRequestCallback updateUserProfileCallback=new DataRequestCallback() {
	
	@Override
	public void onDataReady(ServerResult data, boolean success) {
		// TODO Auto-generated method stub
		if(success)
		{
			if(data.getFlag()==ServerAccess.ERROR_CODE_done)
			{
				showProgress(false);
				AppUser recievedMe=(AppUser)data.getValue("me");
				if(recievedMe!=null)
				{
					AppUser originalMe=DataCacheProvider.getInstance().getMe();
					originalMe.setName(recievedMe.getName());
					originalMe.setPhoneNum(recievedMe.getPhoneNum());
					originalMe.setAddress(recievedMe.getAddress());
					originalMe.setDescription(recievedMe.getDescription());
					DataCacheProvider.getInstance().storeMe(originalMe);
				}
				finish();
			}
		}
	}
};

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	int viewId=v.getId();
	switch(viewId)
	{
	case R.id.btnEditUserProfile:
		updateUserProfile();
		break;
	case R.id.llLogout:
		DataCacheProvider.getInstance().removeStoredMe();
		break;
	case R.id.ivMenu:
		toggleDrawer();
		break;
	case R.id.ivBack:
		finish();
		break;
	}
}

@Override
public void showProgress(boolean show) {
	// TODO Auto-generated method stub
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
	
}

@Override
public void setTitle(String title) {
	// TODO Auto-generated method stub
	
}

@Override
public void loadFragment(FragmentType fragmentType,
		HashMap<String, Object> params) {
	// TODO Auto-generated method stub
	
}

@Override
public void openSlideDrawer() {
	// TODO Auto-generated method stub
	
}

@Override
public void closeSlideDrawer() {
	// TODO Auto-generated method stub
	
}
	

}
