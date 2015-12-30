package com.brainSocket.aswaq;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brainSocket.aswaq.adapters.SliderAdapter;
import com.brainSocket.aswaq.data.DataCacheProvider;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.FacebookProvider;
import com.brainSocket.aswaq.data.PhotoProvider;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.data.FacebookProvider.STORY_TYPE;
import com.brainSocket.aswaq.dialogs.DiagRating;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.enums.SliderType;
import com.brainSocket.aswaq.models.AdvertiseModel;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.views.TextViewCustomFont;

public class AdvertiseDetailsActivity extends AppBaseActivity implements
		HomeCallbacks, OnClickListener {
	private Dialog dialogLoading;
	private TextViewCustomFont tvPaid;
	private ViewPager vpSlider;
	private ImageView btnFbPage;
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
	private Activity currentActivity;
	private boolean isFavourite = false;
	private Spinner spnrPhone;
	private TextViewCustomFont tvIsUsed;
	private Handler sliderHandler = null;
	private int currentSlide = 0;
	

	// actionbar
	private TextViewCustomFont tvFragTitle;
	private ImageView ivBackHome;
	private ImageView ivMenu;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertise_details);
		currentActivity = this;
		init();
		initCustomActionBar();
	}

	private void init() {
		try {
			sliderHandler=new Handler();
			tvPaid = (TextViewCustomFont) findViewById(R.id.tvPaid);
			vpSlider = (ViewPager) findViewById(R.id.vpSlider);
			btnFbPage = (ImageView) findViewById(R.id.btnFbPage);
			btnFbPage.setOnClickListener(this);
			ivUser = (ImageView) findViewById(R.id.ivUser);
			ivUser.setOnClickListener(this);
			tvUserName = (TextViewCustomFont) findViewById(R.id.tvUserName);
			tvUserName.setOnClickListener(this);
			tvUserRate = (TextViewCustomFont) findViewById(R.id.tvUserRate);
			rbUserRate = (RatingBar) findViewById(R.id.rbUserRate);
			tvPrice = (TextViewCustomFont) findViewById(R.id.tvPrice);
			tvDate = (TextViewCustomFont) findViewById(R.id.tvDate);
			tvCat = (TextViewCustomFont) findViewById(R.id.tvCat);
			tvPlace = (TextViewCustomFont) findViewById(R.id.tvPlace);
			tvDesc = (TextViewCustomFont) findViewById(R.id.tvDesc);
			ivFav = (ImageView) findViewById(R.id.ivFav);
			ivFav.setOnClickListener(this);
			View btnFacebookShare = findViewById(R.id.btnFacebookShare);
			btnFacebookShare.setOnClickListener(this);
			spnrPhone = (Spinner) findViewById(R.id.spnrPhone);
			findViewById(R.id.btnCall).setOnClickListener(this);
			tvIsUsed=(TextViewCustomFont)findViewById(R.id.tvIsUsed);

			int selectedAdId = getIntent().getIntExtra("selectedAdId", 0);
			showProgress(true);
			DataStore.getInstance().attemptGetAdvertiseDetails(selectedAdId,
					getAdvertiseDetailsCallback);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void initCustomActionBar() {
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setHomeAsUpIndicator(null);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		setActionBarColor(Color.WHITE);
		mActionBar.setDisplayShowCustomEnabled(true);
		View mCustomView = mActionBar.getCustomView();
		mCustomView.invalidate();

		tvFragTitle = (TextViewCustomFont) mCustomView
				.findViewById(R.id.tvFragTitle);
		ivBackHome = (ImageView) mCustomView.findViewById(R.id.ivBack);
		ivBackHome.setOnClickListener(this);
		ivMenu = (ImageView) mCustomView.findViewById(R.id.ivMenu);

		ivMenu.setVisibility(View.GONE);
		tvFragTitle.setText(getString(R.string.lbl_ad_details));
	}

	public void setActionBarColor(int color) {
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
	}

	private DataRequestCallback getAdvertiseDetailsCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			showProgress(false);
			if (success) {
				ad = (AdvertiseModel) data.getValue("adDetails");
				isFavourite = (Boolean) data.getValue("isFavourite");
				if (ad.IsPinned() == 0)
					tvPaid.setVisibility(View.INVISIBLE);

				if (isFavourite)
					ivFav.setBackgroundResource(R.drawable.ic_star_active);

				if (AswaqApp.isEmptyOrNull(ad.getFacebookPage())) {
					btnFbPage.setVisibility(View.GONE);
				}

				tvUserName.setText(ad.getUser().getName());
				tvPrice.setText(ad.getPriceWithUnit());
				tvPlace.setText(ad.getAddress());
				tvCat.setText(ad.getCategory().getName());
				tvDate.setText(ad.getDate());
				tvDesc.setText(ad.getDescription());
				String imgPath = AswaqApp.getImagePath(ImageType.User, ad
						.getUser().getPicture());
				rbUserRate.setRating(ad.getUser().getRate());
				PhotoProvider.getInstance().displayPhotoNormal(imgPath, ivUser);
				SliderAdapter adapter = new SliderAdapter(
						getApplicationContext(), ad.getImages(),
						SliderType.Advertise);
				vpSlider.setAdapter(adapter);
				
				if(ad.getImages().size()>1)
				{
					new Handler().postDelayed(SliderTransition,
							AswaqApp.SLIDER_TRANSITION_INTERVAL);
				}

				String rate = Float.toString(ad.getUser().getRate());
				tvUserRate.setText(rate);
				ArrayAdapter<String> telsAdapter=new ArrayAdapter<String>(getApplicationContext(), R.layout.item_spinner_phone,ad.getTelephones());
				telsAdapter
						.setDropDownViewResource(R.layout.item_spinner_phone);//android.R.layout.simple_spinner_dropdown_item
				spnrPhone.setAdapter(telsAdapter);
				
				if(ad.isUsed()==1)
				{
					tvIsUsed.setText(getString(R.string.advertise_details_lbl_used));
				}
				else
				{
					tvIsUsed.setText(getString(R.string.advertise_details_lbl_new));
				}
			} else
				showToast(getString(R.string.error_connection_error));
		}
	};
	
	private Runnable SliderTransition = new Runnable() {

		@Override
		public void run() {
			try {
				if (currentSlide >= ad.getImages().size())
					currentSlide = 0;
				vpSlider.setCurrentItem(currentSlide, true);
				currentSlide++;
				try {
					sliderHandler.removeCallbacks(SliderTransition);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				sliderHandler.postDelayed(SliderTransition,
						AswaqApp.SLIDER_TRANSITION_INTERVAL);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	private void addToFavourite() {
		showProgress(true);
		DataStore.getInstance().attemptAddAdvertiseToFavourite(ad.getId(),
				true, addToFavouriteCallback);
	}

	private void removeFromFavourite() {
		showProgress(true);
		DataStore.getInstance().attemptAddAdvertiseToFavourite(ad.getId(),
				false, removeFromFavouriteCallback);
	}

	private DataRequestCallback addToFavouriteCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			showProgress(false);
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					ivFav.setBackgroundResource(R.drawable.ic_star_active);
					isFavourite = true;
				}
			} else {
				showToast(getString(R.string.error_connection_error));
			}
		}
	};

	private DataRequestCallback removeFromFavouriteCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			showProgress(false);
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					ivFav.setBackgroundResource(R.drawable.ic_star);
					isFavourite = false;
				}
			} else {
				showToast(getString(R.string.error_connection_error));
			}
		}
	};

	@Override
	public void showProgress(boolean show) {
		if (dialogLoading == null) {
			dialogLoading = new Dialog(this);
			dialogLoading.setCancelable(false);
			dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogLoading.setContentView(R.layout.dialog_custom_loading);
			dialogLoading.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
		}
		if (show)
			dialogLoading.show();
		else
			dialogLoading.dismiss();

	}

	@Override
	public void showToast(String msg) {
		Toast.makeText(AswaqApp.getAppContext(), msg, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void setTitle(String title) {

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

	private void showUserPage() {
		Intent i = new Intent(AdvertiseDetailsActivity.this,
				UserPageActivity.class);
		i.putExtra("userId", ad.getUserId());
		startActivity(i);
	}

	private void handleFavouriteRequest() {
		AppUser me = DataStore.getInstance().getMe();
		if (me == null) {
			Intent i = new Intent(AdvertiseDetailsActivity.this,
					LoginActivity.class);
			startActivity(i);
		} else {
			if (isFavourite)
				removeFromFavourite();
			else
				addToFavourite();
		}

	}

	private void shareAdvertiseOnFacebook() {
		try {
			if (ad.getImages().size() > 0) {
				showProgress(true);
				String photoPath = AswaqApp.getImagePath(ImageType.Ad, ad
						.getImages().get(0).getPhoto_path());
				DataStore.getInstance().attemptDownloadPhoto(photoPath,
						downloadAdPhotoCallback);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private DataRequestCallback downloadAdPhotoCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {

			if (success) {
				Bitmap photo = (Bitmap) data.getValue("photo");
				FacebookProvider.getInstance().sharePhotoViaFacebook(
						currentActivity, photo, ad.getDescription());
				showProgress(false);
			} else {
				showProgress(false);
				showToast(getString(R.string.error_connection_error));
			}

		}
	};

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		Intent i = null;
		switch (viewId) {
		 case R.id.btnCall:
			 String selectedPhoneNumber=spnrPhone.getSelectedItem().toString();
		 i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
		 +selectedPhoneNumber));
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
			Uri uri = Uri.parse(ad.getFacebookPage()); // missing 'http://' will cause crashed
			i = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(i);
			break;
		case R.id.ivBack:
			finish();
			break;
		case R.id.btnFacebookShare:
			shareAdvertiseOnFacebook();
			break;
		}
	}

	@Override
	public void backToHome() {

	}

	public static class TelephonesAdapter extends ArrayAdapter<String> {
		private List<String> tels;

		public TelephonesAdapter(Context context, int resource,
				List<String> tels) {
			super(context, resource,tels);
			this.tels = tels;
		}

		@Override
		public int getCount() {
			return tels.size() - 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = null;
			try {
				v = super.getView(position, convertView, parent);
				if (position == getCount()) {
					((TextView) v.findViewById(android.R.id.text1)).setText("");
					((TextView) v.findViewById(android.R.id.text1))
							.setHint(getItem(getCount())); // "Hint to be displayed"
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return v;
		}

	}
	
	@Override
	public void onPause() {
		super.onPause();
		try {
			sliderHandler.removeCallbacks(SliderTransition);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			try {
				sliderHandler.removeCallbacks(SliderTransition);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			sliderHandler.postDelayed(SliderTransition,
					AswaqApp.SLIDER_TRANSITION_INTERVAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
