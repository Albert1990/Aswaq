package com.brainSocket.aswaq;

import java.util.HashMap;

import com.brainSocket.aswaq.data.DataCacheProvider;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.fragments.FragAddAdvertise;
import com.brainSocket.aswaq.fragments.FragAds;
import com.brainSocket.aswaq.fragments.FragClients;
import com.brainSocket.aswaq.fragments.FragMain;
import com.brainSocket.aswaq.fragments.FragSubCategories;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.views.TextViewCustomFont;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerificationActivity extends AppBaseActivity implements
		OnClickListener, HomeCallbacks {
	private TextViewCustomFont btnVerifyCode;
	private TextViewCustomFont btnResendVerificationCode;
	private EditText txtVerificationCode;
	private Dialog dialogLoading;
	private FragmentType currentFragmentType;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		init();
	}

	private void init() {
		btnVerifyCode = (TextViewCustomFont) findViewById(R.id.btnVerifyCode);
		btnVerifyCode.setOnClickListener(this);
		btnResendVerificationCode = (TextViewCustomFont) findViewById(R.id.btnResendVerificationCode);
		btnResendVerificationCode.setOnClickListener(this);
		txtVerificationCode = (EditText) findViewById(R.id.txtVerificationCode);
	}

	private void initCustomActionBar() {
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.hide();
	}

	private void resendVerificationCode() {
		showProgress(true);
		DataStore.getInstance().attemptSendVerificationCode(
				resendVerificationCodeCallback);
	}

	private DataRequestCallback resendVerificationCodeCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			showProgress(false);
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					showToast(getString(R.string.toast_verification_sent));
				} else if (data.getFlag() == ServerAccess.ERROR_CODE_verification_attempts_exceeded) {
					showToast(getString(R.string.error_verification_attempts_exceeded));
				}
			} else
				showToast(getString(R.string.error_connection_error));
		}
	};

	private DataRequestCallback verifyUserCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			showProgress(false);
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					Intent i = new Intent(VerificationActivity.this,
							MainActivity.class);
					startActivity(i);
					finish();
				} else if (data.getFlag() == ServerAccess.ERROR_CODE_wrong_verification_code) {
					txtVerificationCode
							.setError(getString(R.string.verif_error_verification_message_wrong));
					txtVerificationCode.requestFocus();
				} else {
					showToast("unknown error:" + data.getFlag());
				}
			} else {
				showToast(getString(R.string.error_connection_error));
			}
		}
	};

	private void verifyVerificationCode() {
		boolean cancel = false;
		String verificationCode = txtVerificationCode.getText().toString();
		if (AswaqApp.isEmptyOrNull(verificationCode)) {
			txtVerificationCode
					.setError(getString(R.string.error_verification_code_required));
			txtVerificationCode.requestFocus();
			cancel = true;
		}
		if (!cancel) {
			showProgress(true);
			DataStore.getInstance().attempVerifyUser(verificationCode,
					verifyUserCallback);
		}
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.btnVerifyCode:
			verifyVerificationCode();
			break;
		case R.id.btnResendVerificationCode:
			resendVerificationCode();
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

}
