package com.brainSocket.aswaq;

import java.util.ArrayList;
import java.util.HashMap;

import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.FacebookProvider;
import com.brainSocket.aswaq.data.FacebookProviderListener;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.dialogs.DiagChangePassword;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.enums.PhoneNumberCheckResult;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.views.TextViewCustomFont;
import com.facebook.AccessToken;
import com.facebook.FacebookAuthorizationException;
import com.facebook.login.LoginManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppBaseActivity implements OnClickListener,
		HomeCallbacks {
	//private EditText txtEmail;
	//private EditText txtPassword;
	private TextViewCustomFont btnLogin;
	//private TextViewCustomFont btnDoesntHaveAccount;
	//private TextViewCustomFont btnLoginFB;
	private Dialog dialogLoading;
	//private View btnForgetPassword;
	private EditText txtMobileNumber;
	private String mobileNumber="";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initComponents();
		AswaqApp.setAppContext(getApplicationContext());

	}

	private void initComponents() {
		//txtEmail = (EditText) findViewById(R.id.txtEmail);
		//txtPassword = (EditText) findViewById(R.id.txtPassword);
		btnLogin = (TextViewCustomFont) findViewById(R.id.btnLogin);
		//btnDoesntHaveAccount = (TextViewCustomFont) findViewById(R.id.btnDoesntHaveAccount);
		//btnLoginFB = (TextViewCustomFont) findViewById(R.id.btnLoginFB);
		btnLogin.setOnClickListener(this);
		//btnDoesntHaveAccount.setOnClickListener(this);
		//btnLoginFB.setOnClickListener(this);
		//btnForgetPassword=findViewById(R.id.btnForgetPassword);
		//btnForgetPassword.setOnClickListener(this);
		
		txtMobileNumber=(EditText)findViewById(R.id.txtMobileNumber);
	}


	
	private void login() {
		boolean cancel = false;
		View focusView = null;

		mobileNumber = txtMobileNumber.getText().toString();
		//check mobile number
		PhoneNumberCheckResult mobileNumberCheckResult= AswaqApp.validatePhoneNum(mobileNumber);
		
		
		if(mobileNumberCheckResult!=PhoneNumberCheckResult.OK)
		{
			cancel=true;
		switch(mobileNumberCheckResult)
		{
		case WRONG:
			txtMobileNumber.setError(getString(R.string.error_mobile_number_wrong));
			break;
		case SHORT:
			txtMobileNumber.setError(getString(R.string.error_mobile_number_short));
			break;
		case EMPTY:
			txtMobileNumber.setError(getString(R.string.error_mobile_number_empty));
			break;
		}
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			// every thing is good
			showProgress(true);
			DataStore.getInstance()
					.attemptLogin(mobileNumber, loginCallback);
		}
	}

	private DataRequestCallback loginCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			showProgress(false);
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					AppUser me = DataStore.getInstance().getMe();
					Intent i = null;
					if (me.isVerified()) {
						i = new Intent(LoginActivity.this, MainActivity.class);
					} else {
						i = new Intent(LoginActivity.this,
								VerificationActivity.class);
					}
					startActivity(i);
					finish();
				} else if (data.getFlag() == ServerAccess.ERROR_CODE_user_not_exists) {
					//txtEmail.setError(getString(R.string.login_error_email_or_password_wrong));
					//txtEmail.requestFocus();
					Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
					i.putExtra("mobileNumber", mobileNumber);
					startActivity(i);
					
				}
			} else {
				showToast(getString(R.string.login_error_connection_failed));
			}
		}
	};

//	private DataRequestCallback registerCallback = new DataRequestCallback() {
//		@Override
//		public void onDataReady(ServerResult data, boolean success) {
//			if (success) {
//				switch (data.getFlag()) {
//				case ServerAccess.ERROR_CODE_done:
//					AppUser me = DataStore.getInstance().getMe();
//					Intent i = null;
//					if (me.isVerified()) {
//						i = new Intent(LoginActivity.this, MainActivity.class);
//					} else {
//						i = new Intent(LoginActivity.this,
//								VerificationActivity.class);
//					}
//
//					startActivity(i);
//					finish();
//					break;
//				case ServerAccess.ERROR_CODE_user_exists_before:
//					showToast(getString(R.string.login_error_user_exists_before));
//					break;
//				}
//			} else {
//				showToast(getString(R.string.error_connection_error));
//			}
//			showProgress(false);
//		}
//	};

	@Override
	public void onClick(View v) {
		try {
			int viewId = v.getId();
			switch (viewId) {
			case R.id.btnLogin:
				login();
				break;
//			case R.id.btnDoesntHaveAccount:
//				Intent registerActivityIntent = new Intent(LoginActivity.this,
//						RegisterActivity.class);
//				startActivity(registerActivityIntent);
//				finish();
//				break;
//			case R.id.btnLoginFB:
//				attempFBtLogin();
//				break;
//			case R.id.btnForgetPassword:
//				DiagChangePassword diagChangePassword=new DiagChangePassword();
//				diagChangePassword.show(getSupportFragmentManager(), "");
//				break;
			default:
				break;
			}
		} catch (Exception ex) {

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
