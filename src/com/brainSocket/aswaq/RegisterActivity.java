package com.brainSocket.aswaq;

import java.util.ArrayList;
import java.util.HashMap;

import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.FacebookProvider;
import com.brainSocket.aswaq.data.FacebookProviderListener;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.enums.FragmentType;
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

public class RegisterActivity extends AppBaseActivity implements
		OnClickListener, HomeCallbacks {
	private EditText txtUserNameRegister;
	private TextViewCustomFont btnRegister;
	private TextViewCustomFont btnLoginFB;
	private Dialog dialogLoading;
	private String selectedMobileNumber="";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initComponents();
	}

	private void initComponents() {
		txtUserNameRegister = (EditText) findViewById(R.id.txtUserNameRegister);
		//txtEmail = (EditText) findViewById(R.id.txtEmailRegister);
		//txtPassword = (EditText) findViewById(R.id.txtPasswordRegister);
		//txtRepeatPassword = (EditText) findViewById(R.id.txtRepeatPasswordRegister);
		btnRegister = (TextViewCustomFont) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
		
		btnLoginFB=(TextViewCustomFont)findViewById(R.id.btnLoginFB);
		btnLoginFB.setOnClickListener(this);
		
		selectedMobileNumber=getIntent().getExtras().getString("mobileNumber", "");
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.btnRegister:
			register();
			break;
			case R.id.btnLoginFB:
			attempFBtLogin();
			break;
		default:

			break;
		}
	}

	private void register() {
		// we have to check inputs first
		boolean cancel = false;
		View focusView = null;

		String userName = txtUserNameRegister.getText().toString();

		if (AswaqApp.isEmptyOrNull(userName)) {
			txtUserNameRegister
					.setError(getString(R.string.login_error_user_name_empty));
			focusView = txtUserNameRegister;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			String facebookAccessToken = "";
			String facebookId = "";
			showProgress(true);
			DataStore.getInstance().attemptSignUp(selectedMobileNumber, userName,
					facebookId, facebookAccessToken, registerCallback);
		}

	}

	private DataRequestCallback registerCallback = new DataRequestCallback() {
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			showProgress(false);
			if (success) {
				switch (data.getFlag()) {
				case ServerAccess.ERROR_CODE_done:
					Intent i = new Intent(RegisterActivity.this,
							VerificationActivity.class);
					startActivity(i);
					finish();
					break;
				case ServerAccess.ERROR_CODE_user_exists_before:
						//txtEmail.setError(getString(R.string.login_error_user_exists_before));
					showToast(getString(R.string.login_error_user_exists_before));
					break;
					case R.id.btnLoginFB:
					attempFBtLogin();
					break;

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
	
	/**
	 * try login first using facebook if success then singning up to the API
	 * Server using the facebook Id and phone number entered in the previous
	 * stage
	 */
	public void attempFBtLogin() {
		ArrayList<String> perm1 = new ArrayList<String>();
		perm1.add("public_profile");
		// perm1.add("user_friends");
		perm1.add("email");

		// Session.openActiveSession(this, true, permissions, callback)
		FacebookProvider.getInstance().registerListener(facebookLoginListner);
		FacebookProvider.getInstance().requestFacebookLogin(this);

		// Session.StatusCallback callback = new LoginStatsCallback() ;
		// Session.openActiveSession(LoginActivity.this, true, perm1, callback )
		// ;
		showProgress(true);
	}
	
	FacebookProviderListener facebookLoginListner = new FacebookProviderListener() {

		@Override
		public void onFacebookSessionOpened(String accessToken, String userId,
				HashMap<String, Object> map) {
			showProgress(false);
			//String email = (String) map.get("email");
			String name = (String) map.get("name");
			//String password = "5982";
			DataStore.getInstance().attemptSignUp(selectedMobileNumber,name,
					userId, accessToken, registerCallback);

			// linkWithFB = true ;
			FacebookProvider.getInstance().unregisterListener();
		}

		@Override
		public void onFacebookSessionClosed() {
			showProgress(false);
			showToast(getString(R.string.error_facebook_permissions_rejected));
		}

		@Override
		public void onFacebookException(Exception exception) {
			showProgress(false);
			showToast(getString(R.string.error_facebook_exception));
			try
			{
			if (exception instanceof FacebookAuthorizationException) {
	            if (AccessToken.getCurrentAccessToken() != null) {
	                LoginManager.getInstance().logOut();
	            }
			}
			}
			catch(Exception ex){}
		}
	};

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
