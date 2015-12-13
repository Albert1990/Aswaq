package com.brainSocket.aswaq;

import java.util.HashMap;

import com.brainSocket.adapters.DrawerAdapter;
import com.brainSocket.adapters.SliderAdapter;
import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.PhotoProvider;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.enums.ImageType;
import com.brainSocket.enums.SliderType;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.views.TextViewCustomFont;
import com.squareup.picasso.Picasso;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

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
	private AdvertiseModel ad;
	
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
			showProgress(true);
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
			
			int selectedAdId=getIntent().getIntExtra("selectedAdId", 0);
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
			if(success)
			{
				ad=(AdvertiseModel)data.getValue("adDetails");
				if(ad.IsPinned()==0)
					tvPaid.setVisibility(View.INVISIBLE);
				
					tvUserName.setText(ad.getUser().getName());
					tvPrice.setText(ad.getPriceWithUnit());
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
					showProgress(false);
					String rate=Float.toString(ad.getUser().getRate());
					tvUserRate.setText(rate);
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
	
	private void showUserPage()
	{
		Intent i = new Intent(AdvertiseDetailsActivity.this,
				UserPageActivity.class);
		i.putExtra("userId", ad.getUserId());
		startActivity(i);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.btnCall:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +ad.getUser().getPhoneNum()));
			startActivity(intent);
			break;
		case R.id.ivUser:
			showUserPage();
			break;
		case R.id.tvUserName:
			showUserPage();
			break;
		}
	}

}
