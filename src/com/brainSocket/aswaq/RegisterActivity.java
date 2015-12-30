package com.brainSocket.aswaq;

import java.util.HashMap;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.views.TextViewCustomFont;
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
	private EditText txtUserNameRegister, txtEmail, txtPassword,
			txtRepeatPassword;
	private TextViewCustomFont btnRegister;
	private Dialog dialogLoading;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		initComponents();
	}

	private void initComponents() {
		txtUserNameRegister = (EditText) findViewById(R.id.txtUserNameRegister);
		txtEmail = (EditText) findViewById(R.id.txtEmailRegister);
		txtPassword = (EditText) findViewById(R.id.txtPasswordRegister);
		txtRepeatPassword = (EditText) findViewById(R.id.txtRepeatPasswordRegister);
		btnRegister = (TextViewCustomFont) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.btnRegister:
			register();
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
		String email = txtEmail.getText().toString();
		String password = txtPassword.getText().toString();
		String repeatedPassword = txtRepeatPassword.getText().toString();

		if (AswaqApp.isEmptyOrNull(userName)) {
			txtUserNameRegister
					.setError(getString(R.string.login_error_user_name_empty));
			focusView = txtUserNameRegister;
			cancel = true;
		}
		if (AswaqApp.isEmptyOrNull(email)) {
			txtEmail.setError(getString(R.string.login_error_email_empty));
			focusView = txtEmail;
			cancel = true;
		} else {
			if (!AswaqApp.isEmailValid(email)) {
				txtEmail.setError(getString(R.string.login_error_email_invalid));
				focusView = txtEmail;
				cancel = true;
			}
		}
		if (AswaqApp.isEmptyOrNull(password)) {
			txtPassword
					.setError(getString(R.string.login_error_password_empty));
			focusView = txtPassword;
			cancel = true;
		} else {
			// if (password.length() < 4) {
			// txtPassword.setError(getString(R.string.login_error_password_length));
			// focusView = txtPassword;
			// cancel = true;
			// }
			if (AswaqApp.isEmptyOrNull(repeatedPassword)) {
				txtRepeatPassword
						.setError(getString(R.string.login_error_password_empty));
				focusView = txtRepeatPassword;
				cancel = true;
			} else {
				if (!password.equals(repeatedPassword)) {
					txtRepeatPassword
							.setError(getString(R.string.error_password_not_same));
					focusView = txtRepeatPassword;
					cancel = true;
				}
			}
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			String facebookAccessToken = "";
			String facebookId = "";
			showProgress(true);
			DataStore.getInstance().attemptSignUp(email, userName, password,
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
					break;
				case ServerAccess.ERROR_CODE_user_exists_before:
					txtEmail.setError(getString(R.string.login_error_user_exists_before));
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
