package com.brainSocket.aswaq;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.brainSocket.aswaq.adapters.DrawerAdapter;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.PhotoProvider;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.fragments.FragAbout;
import com.brainSocket.aswaq.fragments.FragAddAdvertise;
import com.brainSocket.aswaq.fragments.FragAds;
import com.brainSocket.aswaq.fragments.FragClients;
import com.brainSocket.aswaq.fragments.FragFavourites;
import com.brainSocket.aswaq.fragments.FragMain;
import com.brainSocket.aswaq.fragments.FragSubCategories;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.views.TextViewCustomFont;

public class MainActivity extends AppBaseActivity implements OnClickListener,
		HomeCallbacks, OnBackStackChangedListener {
	// slide drawer
	private ListView lvDrawer;
	private DrawerAdapter adapter;
	private DrawerLayout dlDrawer;
	//private View llLogout;
	private TextViewCustomFont tvUserName;

	private FragmentManager fragmentManager;
	private FragmentType currentFragmentType;
	private Fragment currentFragment;
	private Dialog dialogLoading;
	private ImageView ivUserImage;

	// actionbar
	private ImageView ivMenu;
	private TextViewCustomFont tvFragTitle;
	private ImageView ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		setContentView(R.layout.activity_main);
		initCustomActionBar();
		initSlideDrawer();
		init();

	}

	private void init() {
		fragmentManager = getSupportFragmentManager();
		fragmentManager.addOnBackStackChangedListener(this);
		loadFragment(FragmentType.Main, null);
	}

	private void initSlideDrawer() {
		lvDrawer = (ListView) findViewById(R.id.lvDrawer);
		adapter = new DrawerAdapter(this, lvDrawer);
		lvDrawer.setAdapter(adapter);
		dlDrawer = (DrawerLayout) findViewById(R.id.dlDrawer);
//		llLogout = findViewById(R.id.llLogout);
//		llLogout.setOnClickListener(this);
		tvUserName = (TextViewCustomFont) findViewById(R.id.tvUserName);
		ivUserImage=(ImageView)findViewById(R.id.ivUserImage);
		AppUser me = DataStore.getInstance().getMe();
		if (me != null)
		{
			tvUserName.setText(me.getName());
			String photoPath=AswaqApp.getImagePath(ImageType.User, me.getPicture());
			PhotoProvider.getInstance().displayPhotoNormal(photoPath, ivUserImage);
		}
	}

	private void initCustomActionBar() {

		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setHomeAsUpIndicator(null);
		// LayoutInflater mInflater = LayoutInflater.from(this);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		setActionBarColor(Color.argb(0, 0, 0, 0));
		mActionBar.setDisplayShowCustomEnabled(true);
		View mCustomView = mActionBar.getCustomView();
		mCustomView.invalidate();

		ivMenu = (ImageView) mCustomView.findViewById(R.id.ivMenu);
		ivMenu.setOnClickListener(this);
		tvFragTitle = (TextViewCustomFont) mCustomView
				.findViewById(R.id.tvFragTitle);
		ivBack = (ImageView) mCustomView.findViewById(R.id.ivBack);
		ivBack.setOnClickListener(this);
	}

	public void setActionBarColor(int color) {
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
	}

	/**
	 * update content of the actionBar accourding to the current fragment
	 * 
	 * @param section
	 */
	private void updateActionbar(FragmentType section) {
		setActionBarColor(Color.WHITE);
		ivMenu.setVisibility(View.INVISIBLE);
		ivBack.setVisibility(View.VISIBLE);
		tvFragTitle.setVisibility(View.VISIBLE);

		switch (section) {
		case Main:
			setActionBarColor(Color.TRANSPARENT);
			ivBack.setVisibility(View.GONE);
			ivMenu.setVisibility(View.VISIBLE);
			tvFragTitle.setVisibility(View.GONE);
			break;
		case AddAdvertise:
			//ivMenu.setVisibility(View.GONE);
			break;
		case SubCategories:
			//ivMenu.setVisibility(View.GONE);
			break;
		case MyClients:
			//ivMenu.setVisibility(View.GONE);
			tvFragTitle.setText(getString(R.string.drawer_agents));
			break;
		case Favourites:
			//ivMenu.setVisibility(View.GONE);
			tvFragTitle.setText(getString(R.string.drawer_favourites));
			break;
		case ShowAds:
			//ivMenu.setVisibility(View.GONE);
			break;
		default:
			// setActionBarColor(getResources().getColor(R.color.app_theme));
			break;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	public void onBackStackChanged() {
		try {
			int entrys = getSupportFragmentManager().getBackStackEntryCount();
			String fragmentName = getSupportFragmentManager()
					.getBackStackEntryAt(entrys - 1).getName();
			FragmentType backFrag = FragmentType.valueOf(fragmentName);
			if (entrys > 1) {
				updateActionbar(backFrag);
			} else {
				updateActionbar(FragmentType.Main);
			}
		} catch (Exception e) {

		}

	}

	private void toggleDrawer() {
		try {
			if (dlDrawer.isDrawerOpen(Gravity.RIGHT)) {
				dlDrawer.closeDrawer(Gravity.RIGHT);
			} else {
				dlDrawer.openDrawer(Gravity.RIGHT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeDrawer() {
		try {
			if (dlDrawer.isDrawerOpen(Gravity.RIGHT)) {
				dlDrawer.closeDrawer(Gravity.RIGHT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void finishCurrentFragment() {
		getSupportFragmentManager().popBackStackImmediate();
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
//		case R.id.llLogout:
//			DataStore.getInstance().removeAllStoredData();
//			Intent i = new Intent(MainActivity.this, SplashScreen.class);
//			startActivity(i);
//			break;
		case R.id.ivMenu:
			toggleDrawer();
			break;
		case R.id.ivBack:
			finishCurrentFragment();
			break;
		}
	}

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
		tvFragTitle.setText(title);
	}

	@Override
	public void loadFragment(FragmentType fragmentType,
			HashMap<String, Object> params) {
		switch (fragmentType) {
		case Main:
			currentFragment = FragMain.newInstance();
			fragmentManager.beginTransaction()
			// .setCustomAnimations(R.anim.slide_in_from_left,
			// R.anim.slide_out_to_right,R.anim.slide_in_from_right,R.anim.slide_out_to_left)
					.replace(R.id.content_frame, currentFragment)
					// .addToBackStack(FragmentType.Main.name())
					.commit();
			break;
		case AddAdvertise:
			currentFragment = FragAddAdvertise.newInstance(params);
			fragmentManager.beginTransaction()
					// .setCustomAnimations(R.anim.slide_in_from_left,
					// R.anim.slide_out_to_right,R.anim.slide_in_from_right,R.anim.slide_out_to_left)
					.replace(R.id.content_frame, currentFragment)
					.addToBackStack(FragmentType.AddAdvertise.name()).commit();
			break;
		case SubCategories:
			currentFragment = FragSubCategories.newInstance(params);
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, currentFragment)
					.addToBackStack(FragmentType.SubCategories.name()).commit();
			break;
		case ShowAds:
			currentFragment = FragAds.newInstance(params);
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, currentFragment)
					.addToBackStack(FragmentType.ShowAds.name()).commit();
			break;
		case MyClients:
			currentFragment = FragClients.newInstance();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, currentFragment)
					.addToBackStack(FragmentType.MyClients.name()).commit();
			break;
		case Favourites:
			currentFragment = FragFavourites.newInstance();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, currentFragment)
					.addToBackStack(FragmentType.Favourites.name()).commit();
			break;
		case About:
			currentFragment = FragAbout.newInstance();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, currentFragment)
					.addToBackStack(FragmentType.About.name()).commit();
			break;
		}
		currentFragmentType = fragmentType;
		updateActionbar(currentFragmentType);
	}

	@Override
	public void openSlideDrawer() {

	}

	@Override
	public void closeSlideDrawer() {
		closeDrawer();
	}

	@Override
	public void backToHome() {
		try {
			final FragmentManager fm = getSupportFragmentManager();
			while (fm.getBackStackEntryCount() > 0) {
				fm.popBackStackImmediate();
			}

			adapter.onFragmentChange(FragmentType.Main);
			updateActionbar(FragmentType.Main);
			closeDrawer();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
