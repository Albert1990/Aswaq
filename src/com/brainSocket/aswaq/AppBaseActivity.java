package com.brainSocket.aswaq;

import com.brainSocket.aswaq.data.FacebookProvider;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class AppBaseActivity extends ActionBarActivity {
	protected boolean isVisible = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		isVisible = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isVisible = true;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		FacebookProvider.getInstance().onActiviyResult(arg0, arg1, arg2);
	}

}
