package com.brainSocket.aswaq;

import java.util.HashMap;
import java.util.List;

import com.brainSocket.aswaq.adapters.AdvertisesListAdapter;
import com.brainSocket.aswaq.adapters.DrawerAdapter;
import com.brainSocket.aswaq.data.DataCacheProvider;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.PhotoProvider;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.dialogs.DiagRating;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.models.AdvertiseModel;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.views.TextViewCustomFont;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

public class UserPageActivity extends AppBaseActivity implements OnClickListener,HomeCallbacks,OnItemClickListener{
	private Dialog dialogLoading;
	private ImageView ivUser;
	private TextViewCustomFont tvUserName;
	private TextViewCustomFont tvFollowers;
	private ImageView btnFbPage;
	private TextViewCustomFont tvUserRating;
	private RatingBar rbUserRate;
	private TextViewCustomFont tvFollow;
	private TextViewCustomFont tvDesc;
	private ListView lvAds;
	private int isFollowedByMe; 
	private int userId;
	private AppUser user;
	private List<AdvertiseModel> userAds;
	
	////actionbar
		private ImageView ivEditUserProfile;
		private TextViewCustomFont tvFragTitle;
		private ImageView ivBackHome;
		private ImageView ivMenu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_user_page);
	    init();
	    initCustomActionBar();
	}
	
	private void init()
	{
		showProgress(true);
		userId=getIntent().getIntExtra("userId", 0);//getArguments().getInt("userId");
		if(userId==0)
		{
			AppUser me=DataStore.getInstance().getMe();
			if(me==null)
				return;
			userId=me.getId();
		}
		ivUser=(ImageView)findViewById(R.id.ivUser);
		tvUserName=(TextViewCustomFont)findViewById(R.id.tvUserName);
		tvFollowers=(TextViewCustomFont)findViewById(R.id.tvFollowers);
		btnFbPage=(ImageView)findViewById(R.id.btnFbPage);
		btnFbPage.setOnClickListener(this);
		tvUserRating=(TextViewCustomFont)findViewById(R.id.tvUserRating);
		rbUserRate=(RatingBar)findViewById(R.id.rbUserRate);
		tvFollow=(TextViewCustomFont)findViewById(R.id.tvFollow);
		tvDesc=(TextViewCustomFont)findViewById(R.id.tvDesc);
		lvAds=(ListView)findViewById(R.id.lvAds);
		lvAds.setOnItemClickListener(this);
		
		AppUser me=DataStore.getInstance().getMe();
		if(me!=null)
		{
			if(userId==me.getId())
			{
				tvFollow.setVisibility(View.GONE);
				
			}
			else
			{
				findViewById(R.id.llUserRating).setOnClickListener(this);
				tvFollow.setOnClickListener(this);
			}
		}
		else
		{
			findViewById(R.id.llUserRating).setOnClickListener(this);
			tvFollow.setOnClickListener(this);
		}
		DataStore.getInstance().attemptGetUserPage(userId,getUserPageCallback);
	}
	
private void initCustomActionBar() {
		
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false) ;
		mActionBar.setHomeAsUpIndicator(null);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		setActionBarColor(Color.WHITE);
		mActionBar.setDisplayShowCustomEnabled(true);
		View mCustomView = mActionBar.getCustomView() ;
		mCustomView.invalidate();
		
		tvFragTitle = (TextViewCustomFont) mCustomView.findViewById(R.id.tvFragTitle) ;
		ivEditUserProfile=(ImageView)mCustomView.findViewById(R.id.ivEditUserProfile);
		ivMenu=(ImageView)mCustomView.findViewById(R.id.ivMenu);
		ivBackHome = (ImageView) mCustomView.findViewById(R.id.ivBack);
		ivBackHome.setOnClickListener(this);
		
		ivMenu.setVisibility(View.GONE);
		
		AppUser me=DataStore.getInstance().getMe();
		if(me!=null)
		{
			if(userId==me.getId())
			{
				ivEditUserProfile.setVisibility(View.VISIBLE);
				ivEditUserProfile.setOnClickListener(this);
			}
		}
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
	
private DataRequestCallback getUserPageCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			try
			{
				showProgress(false);
			if(success)
			{
				user=(AppUser)data.getValue("user");
				userAds=(List<AdvertiseModel>)data.getValue("userAds");
				AdvertisesListAdapter adsAdapter=new AdvertisesListAdapter(getApplicationContext(), userAds);
				lvAds.setAdapter(adsAdapter);
				int followersCount=(Integer)data.getValue("followersCount");
				isFollowedByMe=(Integer)data.getValue("isFollowedByMe");
				tvFragTitle.setText(user.getName());
				String photoPath=AswaqApp.getImagePath(ImageType.User, user.getPicture());
				PhotoProvider.getInstance().displayPhotoNormal(photoPath, ivUser);
				tvUserName.setText(user.getName());
				tvFollowers.setText(Integer.toString(followersCount));
				if(user.getFacebookId().length()<=1)
					btnFbPage.setVisibility(View.GONE);
				tvUserRating.setText(Float.toString(user.getRate()));
				rbUserRate.setRating(user.getRate());
				tvDesc.setText(user.getDescription());
				if(isFollowedByMe==0)
				{
					tvFollow.setText(getString(R.string.user_list_follow));
				}
				else
				{
					tvFollow.setText(getString(R.string.user_list_unfollow));
				}
			}
			else
			{
				showToast(getString(R.string.error_connection_error));
			}
			
		}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	};
	
	private void follow(int userId)
	{
		
		DataStore.getInstance().attemptFollowUser(userId,true,followUserCallback);
	}
	
	private DataRequestCallback followUserCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			showProgress(false);
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					tvFollow.setText(getString(R.string.user_list_unfollow));
					isFollowedByMe=1;
					
				}
			}
			else
				showToast(getString(R.string.error_connection_error));
		}
	};
	
private DataRequestCallback unfollowUserCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			showProgress(false);
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					tvFollow.setText(getString(R.string.user_list_follow));
					isFollowedByMe=0;
					
				}
			}
			else
				showToast(getString(R.string.error_connection_error));
		}
	};
	
private DataRequestCallback onRatingCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			String strRating=(String)data.getValue("rating");
			//float rating=Float.parseFloat((String)data.getValue("rate"));
			showProgress(true);
			DataStore.getInstance().attemptRateUser(user.getId(),strRating,userRateCallback);
			
		}
	};
	
	private DataRequestCallback userRateCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			showProgress(false);
			if(success)
			{
				String strNewRate=(String)data.getValue("newRate");
				float newRate=Float.parseFloat(strNewRate);
				user.setRate(newRate);
				rbUserRate.setRating(newRate);
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
	Intent i=null;
	AppUser me=null;
	switch(viewId)
	{
	case R.id.tvFollow:
		me=DataStore.getInstance().getMe();
		if(me!=null)
		{
			showProgress(true);
			if(isFollowedByMe==0)
				DataStore.getInstance().attemptFollowUser(userId, true, followUserCallback);
			else
				DataStore.getInstance().attemptFollowUser(userId, false, unfollowUserCallback);
		}
		else
		{
			i=new Intent(UserPageActivity.this, LoginActivity.class);
			startActivity(i);
		}
		break;
	case R.id.ivEditUserProfile:
		i=new Intent(UserPageActivity.this, UserProfileActivity.class);
		startActivity(i);
		break;
	case R.id.ivBack:
		finish();
		break;
	case R.id.llUserRating:
		me=DataStore.getInstance().getMe();
		if(me!=null)
		{
			DiagRating ratingDialog=new DiagRating(user.getRate(), onRatingCallback);
			ratingDialog.show(getSupportFragmentManager(), "DiagRating");
		}
		else
		{
			i= new Intent(UserPageActivity.this,LoginActivity.class);
			startActivity(i);
		}
		
		break;
	case R.id.btnFbPage:
		String url="https://www.facebook.com/"+user.getFacebookId();
		Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
		i = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(i);
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
	Toast.makeText(AswaqApp.getAppContext(), msg, Toast.LENGTH_SHORT).show();
}

@Override
public void setTitle(String title) {
	tvFragTitle.setText(title);
}

@Override
public void loadFragment(FragmentType fragmentType,
		HashMap<String, Object> params) {
	
}

@Override
public void openSlideDrawer() {
	
}

@Override
public void closeSlideDrawer() {
}

@Override
public void backToHome() {
	
}

@Override
protected void onResume() {
	super.onResume();
	AppUser me=DataStore.getInstance().getMe();
	if(me.getId()==userId)
	{
		tvUserName.setText(me.getName());
		tvDesc.setText(me.getDescription());
		setTitle(me.getName());
	}
}

@Override
public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	if(position==0)
		position=1;
	if(userAds!=null)
	{
	int adId=userAds.get(position).getId();
	Intent i=new Intent(UserPageActivity.this, AdvertiseDetailsActivity.class);
	i.putExtra("selectedAdId", adId);
	startActivity(i);
	}
}

}
