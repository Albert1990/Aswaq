package com.brainSocket.aswaq;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.brainSocket.adapters.SliderAdapter;
import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.PhotoProvider;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.dialogs.DiagRating;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.enums.ImageType;
import com.brainSocket.enums.SliderType;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.models.AppUser;
import com.brainSocket.views.TextViewCustomFont;

public class AdvertiseDetailsActivity extends AppBaseActivity implements HomeCallbacks,OnClickListener {
	private Dialog dialogLoading;
	private TextViewCustomFont tvPaid;
	private ViewPager vpSlider;
	private ImageView btnFbPage;
	private TextViewCustomFont btnCall;
	private ImageView ivUser;
	private TextViewCustomFont tvUserName;
	private TextViewCustomFont tvUserRate;
	private RatingBar rbUserRate;
	private TextViewCustomFont tvPrice;
	private TextViewCustomFont tvDate;
	private TextViewCustomFont tvCat;
	private TextViewCustomFont tvPlace;
	private TextViewCustomFont tvDesc;
	private ImageView ivFav;
	private AdvertiseModel ad;
	private boolean isFavourite=false;
	
	//actionbar
	private ImageView ivEditUserProfile;
	private TextViewCustomFont tvFragTitle;
	private ImageView ivBackHome;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_advertise_details);
	    init();
	    initCustomActionBar();
	}
	
	private void init()
	{
		try
		{
			
			tvPaid=(TextViewCustomFont)findViewById(R.id.tvPaid);
			vpSlider=(ViewPager)findViewById(R.id.vpSlider);
			btnFbPage=(ImageView)findViewById(R.id.btnFbPage);
			btnFbPage.setOnClickListener(this);
			btnCall=(TextViewCustomFont)findViewById(R.id.btnCall);
			btnCall.setOnClickListener(this);
			ivUser=(ImageView)findViewById(R.id.ivUser);
			ivUser.setOnClickListener(this);
			tvUserName=(TextViewCustomFont)findViewById(R.id.tvUserName);
			tvUserName.setOnClickListener(this);
			tvUserRate=(TextViewCustomFont)findViewById(R.id.tvUserRate);
			rbUserRate=(RatingBar)findViewById(R.id.rbUserRate);
			tvPrice=(TextViewCustomFont)findViewById(R.id.tvPrice);
			tvDate=(TextViewCustomFont)findViewById(R.id.tvDate);
			tvCat=(TextViewCustomFont)findViewById(R.id.tvCat);
			tvPlace=(TextViewCustomFont)findViewById(R.id.tvPlace);
			tvDesc=(TextViewCustomFont)findViewById(R.id.tvDesc);
			ivFav=(ImageView)findViewById(R.id.ivFav);
			ivFav.setOnClickListener(this);
			
			int selectedAdId=getIntent().getIntExtra("selectedAdId", 0);
			showProgress(true);
			DataStore.getInstance().attemptGetAdvertiseDetails(selectedAdId, getAdvertiseDetailsCallback);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
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
		ivBackHome = (ImageView) mCustomView.findViewById(R.id.ivBack);
	}
	
public void setActionBarColor(int color){
	getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
}


	
private DataRequestCallback getAdvertiseDetailsCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			showProgress(false);
			if(success)
			{
				ad=(AdvertiseModel)data.getValue("adDetails");
				isFavourite=(Boolean)data.getValue("isFavourite");
				if(ad.IsPinned()==0)
					tvPaid.setVisibility(View.INVISIBLE);
				
				if(isFavourite)
					ivFav.setBackgroundResource(R.drawable.ic_star_active);
					
				if(ad.getUser().getFacebookId().length()<=0)
				{
					btnFbPage.setVisibility(View.GONE);
				}
				
					tvUserName.setText(ad.getUser().getName());
					tvPrice.setText(ad.getPriceWithUnit());
					tvPlace.setText(ad.getAddress());
					tvCat.setText(ad.getCategory().getName());
					tvDate.setText(ad.getDate());
					tvDesc.setText(ad.getDescription());
					String imgPath=AswaqApp.getImagePath(ImageType.User, ad.getUser().getPicture());
//					if(ad.getUser().getPicture().length()==0)
//						imgPath= ServerAccess.IMAGE_SERVICE_URL+"users/"+AswaqApp.DEFAULT_USER_IMAGE;
//					else
//						imgPath=ServerAccess.IMAGE_SERVICE_URL+"users/"+ad.getUser().getPicture();
					rbUserRate.setRating(ad.getUser().getRate());
					//Picasso.with(getApplicationContext()).load(imgPath).into(ivUser);
					PhotoProvider.getInstance().displayPhotoNormal(imgPath, ivUser);
					SliderAdapter adapter=new SliderAdapter(getApplicationContext(), ad.getImages(),SliderType.Advertise);
					vpSlider.setAdapter(adapter);
					
					String rate=Float.toString(ad.getUser().getRate());
					tvUserRate.setText(rate);
			}
			else
				showToast(getString(R.string.error_connection_error));
		}
	};
	
	private void addToFavourite()
	{
		showProgress(true);
		DataStore.getInstance().attemptAddAdvertiseToFavourite(ad.getId(), true, addToFavouriteCallback);
	}
	
	private void removeFromFavourite()
	{
		showProgress(true);
		DataStore.getInstance().attemptAddAdvertiseToFavourite(ad.getId(), false, removeFromFavouriteCallback);
	}
	
	private DataRequestCallback addToFavouriteCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			showProgress(false);
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					ivFav.setBackgroundResource(R.drawable.ic_star_active);
					isFavourite=true;
				}
			}else
			{
				showToast(getString(R.string.error_connection_error));
			}
		}
	};
	
private DataRequestCallback removeFromFavouriteCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			showProgress(false);
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					ivFav.setBackgroundResource(R.drawable.ic_star);
					isFavourite=false;
				}
			}else
			{
				showToast(getString(R.string.error_connection_error));
			}
		}
	};

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
	
	private void showUserPage()
	{
		Intent i = new Intent(AdvertiseDetailsActivity.this,
				UserPageActivity.class);
		i.putExtra("userId", ad.getUserId());
		startActivity(i);
	}
	
	private void handleFavouriteRequest()
	{
		AppUser me=DataCacheProvider.getInstance().getMe();
		if(me==null)
		{
			Intent i=new Intent(AdvertiseDetailsActivity.this,LoginActivity.class);
			startActivity(i);
		}
		else
		{
			if(isFavourite)
				removeFromFavourite();
			else
				addToFavourite();
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId=v.getId();
		Intent i=null;
		switch(viewId)
		{
		case R.id.btnCall:
			i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +ad.getUser().getPhoneNum()));
			startActivity(i);
			break;
		case R.id.ivUser:
			showUserPage();
			break;
		case R.id.tvUserName:
			showUserPage();
			break;
		case R.id.ivFav:
			handleFavouriteRequest();
			break;
		case R.id.btnFbPage:
			String url="https://www.facebook.com/"+ad.getUser().getFacebookId();
			Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
			i = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(i);
			break;
		}
	}

	@Override
	public void backToHome() {
		// TODO Auto-generated method stub
		
	}

}
