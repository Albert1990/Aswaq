package com.brainSocket.aswaq;

import android.support.v4.app.Fragment;
import enums.FragmentType;

public interface HomeCallbacks {
	public void showProgress(boolean show, int msg);
	public void showToast (String msg);
	public void setTitle(String title);
	public void loadFragment(FragmentType fragmentType);
}
