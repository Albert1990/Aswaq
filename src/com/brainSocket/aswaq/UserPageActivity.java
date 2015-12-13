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
import com.brainSocket.enums.ImageType;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.models.AppUser;
import com.brainSocket.views.TextViewCustomFont;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

public class UserPageActivity extends AppBaseActivity implements OnClickListener,HomeCallbacks{
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
	
	////actionbar
		private ImageView ivEditUserProfile;
		private TextViewCustomFont tvFragTitle;
		private ImageView ivBackHome;

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
			AppUser me=DataCacheProvider.getInstance().getMe();
			if(me==null)
				return;
			userId=me.getId();
		}
		ivUser=(ImageView)findViewById(R.id.ivUser);
		tvUserName=(TextViewCustomFont)findViewById(R.id.tvUserName);
		tvFollowers=(TextViewCustomFont)findViewById(R.id.tvFollowers);
		btnFbPage=(ImageView)findViewById(R.id.btnFbPage);
		//btnFbPage.setOnClickListener(this);
		tvUserRating=(TextViewCustomFont)findViewById(R.id.tvUserRating);
		rbUserRate=(RatingBar)findViewById(R.id.rbUserRate);
		tvFollow=(TextViewCustomFont)findViewById(R.id.tvFollow);
		tvFollow.setOnClickListener(this);
		tvDesc=(TextViewCustomFont)findViewById(R.id.tvDesc);
		lvAds=(ListView)findViewById(R.id.lvAds);
		
		DataStore.getInstance().attemptGetUserPage(userId,getUserPageCallback);
	}
	
private void initCustomActionBar() {
		
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false) ;
		mActionBar.setHomeAsUpIndicator(null);
		mActionBar.setCustomView(R.layout.custom_actionbar1);
		setActionBarColor(Color.argb(30, 0, 0, 0));
		mActionBar.setDisplayShowCustomEnabled(true);
		View mCustomView = mActionBar.getCustomView() ;
		mCustomView.invalidate();
		
		tvFragTitle = (TextViewCustomFont) mCustomView.findViewById(R.id.tvFragTitle) ;
		ivEditUserProfile=(ImageView)mCustomView.findViewById(R.id.ivEditUserProfile);
		ivBackHome = (ImageView) mCustomView.findViewById(R.id.ivBack);
		ivBackHome.setOnClickListener(this);
		
		AppUser me=DataCacheProvider.getInstance().getMe();
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
			// TODO Auto-generated method stub
			try
			{
			if(success)
			{
				AppUser user=(AppUser)data.getValue("user");
				List<AdvertiseModel> userAds=(List<AdvertiseModel>)data.getValue("userAds");
				int followersCount=(Integer)data.getValue("followersCount");
				isFollowedByMe=(Integer)data.getValue("isFollowedByMe");
				
				tvFragTitle.setText(user.getName());
				tvFragTitle.setVisibility(View.VISIBLE);
				ivBackHome.setVisibility(View.VISIBLE);
				String photoPath=AswaqApp.getImagePath(ImageType.User, user.getPicture());
				Picasso.with(getApplicationContext()).load(photoPath).into(ivUser);
				tvUserName.setText(user.getName());
				tvFollowers.setText(Integer.toString(followersCount));
				if(user.getFacebookId()==-1)
					btnFbPage.setVisibility(View.INVISIBLE);
				tvUserRating.setText(Float.toString(user.getRate()));
				rbUserRate.setRating(user.getRate());
				tvDesc.setText(user.getDescription());
				//AdvertisesListAdapter advertisesListAdapter=new AdvertisesListAdapter(getActivity(), userAds);
				//lvAds.setAdapter(advertisesListAdapter);
				if(isFollowedByMe==0)
				{
					tvFollow.setText(getString(R.string.user_list_follow));
				}
				else
				{
					tvFollow.setText(getString(R.string.user_list_unfollow));
				}
				showProgress(false);
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
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					tvFollow.setText(getString(R.string.user_list_unfollow));
					isFollowedByMe=1;
					showProgress(false);
				}
			}
		}
	};
	
private DataRequestCallback unfollowUserCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					tvFollow.setText(getString(R.string.user_list_follow));
					isFollowedByMe=0;
					showProgress(false);
				}
			}
		}
	};

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	int viewId=v.getId();
	Intent i=null;
	switch(viewId)
	{
	case R.id.tvFollow:
		AppUser me=DataCacheProvider.getInstance().getMe();
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
