package com.brainSocket.aswaq;

import java.util.ArrayList;
import java.util.HashMap;

import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.FacebookProvider;
import com.brainSocket.data.FacebookProviderListener;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.fragments.FragAddAdvertise;
import com.brainSocket.fragments.FragAds;
import com.brainSocket.fragments.FragMain;
import com.brainSocket.fragments.FragSubCategories;
import com.brainSocket.fragments.FragVerification;
import com.brainSocket.views.TextViewCustomFont;
import com.facebook.Profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppBaseActivity implements OnClickListener,HomeCallbacks{
	private EditText txtEmail;
	private EditText txtPassword;
	private TextViewCustomFont btnLogin;
	private TextViewCustomFont btnDoesntHaveAccount;
	private TextViewCustomFont btnLoginFB;
	private Dialog dialogLoading;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initComponents();
		AswaqApp.setAppContext(getApplicationContext());
		
	}

	private void initComponents() {
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		btnLogin = (TextViewCustomFont) findViewById(R.id.btnLogin);
		btnDoesntHaveAccount = (TextViewCustomFont) findViewById(R.id.btnDoesntHaveAccount);
		btnLoginFB=(TextViewCustomFont)findViewById(R.id.btnLoginFB);
		btnLogin.setOnClickListener(this);
		btnDoesntHaveAccount.setOnClickListener(this);
		btnLoginFB.setOnClickListener(this);
	}

	private void login() {
		boolean cancel = false;
		View focusView = null;

		String email = txtEmail.getText().toString();
		String password = txtPassword.getText().toString();

		// check email if valid
		if (AswaqApp.isEmptyOrNull(email)) {
			txtEmail.setError(getString(R.string.login_error_email_empty));
			cancel = true;
			focusView = txtEmail;
		} else {
			if (!AswaqApp.isEmailValid(email)) {
				txtEmail.setError(getString(R.string.login_error_email_invalid));
				cancel = true;
				focusView = txtEmail;
			}
		}
		if (AswaqApp.isEmptyOrNull(password)) {
			txtPassword.setError(getString(R.string.login_error_password_empty));
			cancel = true;
			focusView = txtPassword;
		} else {
			if (password.length() < 4) {
				txtPassword.setError(getString(R.string.login_error_password_length));
				cancel = true;
				focusView = txtPassword;
			}
		}
		if (cancel) {
			focusView.requestFocus();
		} else {
			// every thing is good
			showProgress(true);
			DataStore.getInstance()
					.attemptLogin(email, password, loginCallback);
		}
	}

	private DataRequestCallback loginCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			showProgress(false);
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					Intent i = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(i);
				} else if (data.getFlag() == ServerAccess.ERROR_CODE_user_not_exists) {
					txtEmail.setError(getString(R.string.login_error_email_or_password_wrong));
					txtEmail.requestFocus();
				}
			} else {
				showToast(getString(R.string.login_error_connection_failed));
			}
		}
	};
	

	
FacebookProviderListener facebookLoginListner = new FacebookProviderListener() {
		
		@Override
		public void onFacebookSessionOpened(String accessToken, String userId, HashMap<String, Object> map) {
			
		  //Profile profile = com.facebook.Profile.getCurrentProfile();
		  //String name = profile.getName(); 
		  String email = (String) map.get("email");
		  String name = (String) map.get("name");
		  String password="5982";
		  String facebookAccessToken="";
		  DataStore.getInstance().attemptSignUp(email, name,
					 password, userId, accessToken,
					registerCallback);
		  
		  //linkWithFB = true ;
		  FacebookProvider.getInstance().unregisterListener();
		}
		
		@Override
		public void onFacebookSessionClosed() {
			showToast(getString(R.string.error_facebook_permissions_rejected));
		}
		
		@Override
		public void onFacebookException(Exception exception) {
			showToast(getString(R.string.error_facebook_exception));
		}
	};
	
	/**
	 * try login first using facebook if success then singning up to the API Server using the 
	 * facebook Id and phone number entered in the previous stage 
	 */
	public void attempFBtLogin() {
		ArrayList<String> perm1=new ArrayList<String>();
		perm1.add("public_profile");
		//perm1.add("user_friends");
		perm1.add("email");
		
		//Session.openActiveSession(this, true, permissions, callback)
		FacebookProvider.getInstance().registerListener(facebookLoginListner);
		FacebookProvider.getInstance().requestFacebookLogin(this);
		//Session.StatusCallback callback =  new LoginStatsCallback() ;
		//Session.openActiveSession(LoginActivity.this, true, perm1, callback ) ;
		showProgress(true);
	}
	
	private DataRequestCallback registerCallback = new DataRequestCallback() {
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			showProgress(false);
			if (success) {
				switch (data.getFlag()) {
				case ServerAccess.ERROR_CODE_done:
					Intent i = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(i);
					break;
				case ServerAccess.ERROR_CODE_user_exists_before:
					showToast(getString(R.string.login_error_user_exists_before));
					break;
				}
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		try {
			int viewId = v.getId();
			switch (viewId) {
			case R.id.btnLogin:
				login();
				break;
			case R.id.btnDoesntHaveAccount:
				Intent registerActivityIntent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(registerActivityIntent);
				break;
			case R.id.btnLoginFB:
				attempFBtLogin();
				break;
			default:
				break;
			}
		} catch (Exception ex) {

		}
	}
	
	@Override
	public void showProgress(boolean show) {
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
		Toast.makeText(AswaqApp.getAppContext(), msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void setTitle(String title) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadFragment(FragmentType fragmentType,HashMap<String, Object> params) {
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

}
