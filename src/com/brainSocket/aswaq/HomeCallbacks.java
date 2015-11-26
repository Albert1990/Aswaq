package com.brainSocket.aswaq;

import java.util.HashMap;

import com.brainSocket.enums.FragmentType;

public interface HomeCallbacks {
	public void showProgress(boolean show, int msg);
	public void showToast (String msg);
	public void setTitle(String title);
	public void loadFragment(FragmentType fragmentType,HashMap<String, Object> params);
}
