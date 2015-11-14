package com.brainSocket.aswaq;

import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppBaseActivity implements OnClickListener {
	private EditText txtEmail;
	private EditText txtPassword;
	private Button btnLogin;
	private Button btnDoesntHaveAccount;

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
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnDoesntHaveAccount = (Button) findViewById(R.id.btnDoesntHaveAccount);
		btnLogin.setOnClickListener(this);
		btnDoesntHaveAccount.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
			default:
				break;
			}
		} catch (Exception ex) {

		}
	}

	private void login() {
		boolean cancel = false;
		View focusView = null;

		String email = txtEmail.getText().toString();
		String password = txtPassword.getText().toString();

		// check email if valid
		if (AswaqApp.isEmptyOrNull(email)) {
			txtEmail.setError(getString(R.string.error_email_empty));
			cancel = true;
			focusView = txtEmail;
		} else {
			if (!AswaqApp.isEmailValid(email)) {
				txtEmail.setError(getString(R.string.error_email_invalid));
				cancel = true;
				focusView = txtEmail;
			}
		}
		if (AswaqApp.isEmptyOrNull(password)) {
			txtPassword.setError(getString(R.string.error_password_empty));
			cancel = true;
			focusView = txtPassword;
		} else {
			if (password.length() < 4) {
				txtPassword.setError(getString(R.string.error_password_length));
				cancel = true;
				focusView = txtPassword;
			}
		}
		if (cancel) {
			focusView.requestFocus();
		} else {
			// every thing is good
			DataStore.getInstance()
					.attemptLogin(email, password, loginCallback);
		}
	}

	private DataRequestCallback loginCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					Intent i = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(i);
				} else if (data.getFlag() == ServerAccess.ERROR_CODE_user_not_exists) {
					txtEmail.setError(getString(R.string.error_email_or_password_wrong));
					txtEmail.requestFocus();
				}
			} else {
				showToast(getString(R.string.error_connection_failed));
			}
		}
	};

}
