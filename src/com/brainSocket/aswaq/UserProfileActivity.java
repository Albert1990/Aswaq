package com.brainSocket.aswaq;

import java.util.HashMap;

import com.brainSocket.aswaq.adapters.DrawerAdapter;
import com.brainSocket.aswaq.data.DataCacheProvider;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.enums.PhoneNumberCheckResult;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.views.TextViewCustomFont;

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
import android.widget.Toast;

public class UserProfileActivity extends AppBaseActivity implements OnClickListener,HomeCallbacks{
	//slide drawer
		private ListView lvDrawer ;
		private DrawerAdapter adapter ;
		private DrawerLayout dlDrawer ;
		private View llLogout;
		
		//actionbar
		private TextViewCustomFont tvFragTitle;
		private ImageView ivBackHome;
		private ImageView ivMenu;
		
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
			AppUser me=DataStore.getInstance().getMe();
			if(me!=null)
			{
				txtUserNameRegister=(EditText)findViewById(R.id.txtUserNameRegister);
				txtMobileNumberRegister=(EditText)findViewById(R.id.txtMobileNumberRegister);
				txtAddressRegister=(EditText)findViewById(R.id.txtAddressRegister);
				txtDescriptionRegister=(EditText)findViewById(R.id.txtDescriptionRegister);
				btnEdit=(TextViewCustomFont)findViewById(R.id.btnEditUserProfile);
				btnEdit.setOnClickListener(this);
				bindUiData(me);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void bindUiData(AppUser me)
	{
		txtUserNameRegister.setText(me.getName());
		txtMobileNumberRegister.setText(me.getPhoneNum());
		txtAddressRegister.setText(me.getAddress());
		txtDescriptionRegister.setText(me.getDescription());
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
		mActionBar.setCustomView(R.layout.custom_actionbar);
		setActionBarColor(Color.argb(30, 0, 0, 0));
		mActionBar.setDisplayShowCustomEnabled(true);
		View mCustomView = mActionBar.getCustomView() ;
		mCustomView.invalidate();
		
		tvFragTitle = (TextViewCustomFont) mCustomView.findViewById(R.id.tvFragTitle) ;
		tvFragTitle.setText(getString(R.string.actionbar_edit_user_profile));
		ivBackHome = (ImageView) mCustomView.findViewById(R.id.ivBack);
		ivBackHome.setOnClickListener(this);
		ivMenu=(ImageView)mCustomView.findViewById(R.id.ivMenu);
		
		ivMenu.setVisibility(View.GONE);
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
	
	boolean cancel=false;
	View focusView=null;
	
	String userName=txtUserNameRegister.getText().toString();
	String mobileNumber=txtMobileNumberRegister.getText().toString();
	String address=txtAddressRegister.getText().toString();
	String description=txtDescriptionRegister.getText().toString();
	
	if(AswaqApp.isEmptyOrNull(userName))
	{
		txtUserNameRegister.setError(getString(R.string.login_error_user_name_empty));
		focusView=txtUserNameRegister;
		cancel=true;
	}
	
//	if(!AswaqApp.isEmptyOrNull(mobileNumber))
//	{
//		String attemptingPhoneNum = txtMobileNumberRegister.getText().toString().replaceAll("\\s+","");
//		PhoneNumberCheckResult numValid = AswaqApp.validatePhoneNum(attemptingPhoneNum);
//		switch (numValid) {
//		case SHORT:
//			txtMobileNumberRegister.setError(getString(R.string.error_short_phone_num));
//			focusView = txtMobileNumberRegister;
//			cancel = true;
//			break;
//		case WRONG:
//			txtMobileNumberRegister.setError(getString(R.string.error_incorrect_phone_num));
//			focusView = txtMobileNumberRegister;
//			cancel = true;
//			break;
//		}
//	}
	
	
	if(cancel)
		focusView.requestFocus();
	else
	{
		showProgress(true);
		DataStore.getInstance().attemptUpdateUserProfile(userName,
				mobileNumber,
				address,
				description,
				updateUserProfileCallback);
	}
}

private DataRequestCallback updateUserProfileCallback=new DataRequestCallback() {
	
	@Override
	public void onDataReady(ServerResult data, boolean success) {
		showProgress(false);
		if(success)
		{
			if(data.getFlag()==ServerAccess.ERROR_CODE_done)
			{
				AppUser recievedMe=(AppUser)data.getValue("me");
				if(recievedMe!=null)
				{
					AppUser originalMe=DataStore.getInstance().getMe();
					originalMe.setName(recievedMe.getName());
					originalMe.setPhoneNum(recievedMe.getPhoneNum());
					originalMe.setAddress(recievedMe.getAddress());
					originalMe.setDescription(recievedMe.getDescription());
					DataCacheProvider.getInstance().storeMe(originalMe);
				}
				finish();
			}
			else if(data.getFlag()==ServerAccess.ERROR_CODE_error_in_mobile_number_format)
			{
				txtMobileNumberRegister.setError(getString(R.string.error_incorrect_phone_num));
				txtMobileNumberRegister.requestFocus();
			}
			else
			{
				showToast(getString(R.string.error_server_error));
			}
		}
		else
		{
			showToast(getString(R.string.error_connection_error));
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

@Override
public void backToHome() {
	// TODO Auto-generated method stub
	
}
	

}
