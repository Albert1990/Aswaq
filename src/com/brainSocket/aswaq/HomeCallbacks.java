package com.brainSocket.aswaq;

import java.util.HashMap;

import android.app.Activity;

import com.brainSocket.aswaq.enums.FragmentType;

public interface HomeCallbacks {
	public void showProgress(boolean show);
	public void showToast (String msg);
	public void setTitle(String title);
	public void loadFragment(FragmentType fragmentType,HashMap<String, Object> params);
	public void openSlideDrawer();
	public void closeSlideDrawer();
	public void backToHome();
}
