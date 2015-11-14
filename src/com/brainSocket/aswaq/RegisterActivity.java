package com.brainSocket.aswaq;

import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppBaseActivity implements
		OnClickListener {
	private EditText txtEmail, txtUserName, txtMobileNumber, txtPassword,
			txtAddress, txtDescription;
	private Button btnRegister;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		// TODO Auto-generated method stub
		initComponents();
	}

	private void initComponents() {
		txtEmail = (EditText) findViewById(R.id.txtEmailRegister);
		txtUserName = (EditText) findViewById(R.id.txtUserNameRegister);
		txtMobileNumber = (EditText) findViewById(R.id.txtMobileNumberRegister);
		txtPassword = (EditText) findViewById(R.id.txtPasswordRegister);
		txtAddress = (EditText) findViewById(R.id.txtAddressRegister);
		txtDescription = (EditText) findViewById(R.id.txtDescriptionRegister);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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

		String email = txtEmail.getText().toString();
		String userName = txtUserName.getText().toString();
		String mobileNumber = txtMobileNumber.getText().toString();
		String password = txtPassword.getText().toString();
		String address = txtAddress.getText().toString();
		String description = txtDescription.getText().toString();

		if (AswaqApp.isEmptyOrNull(email)) {
			txtEmail.setError(getString(R.string.error_email_empty));
			focusView = txtEmail;
			cancel = true;
		} else {
			if (!AswaqApp.isEmailValid(email)) {
				txtEmail.setError(getString(R.string.error_email_invalid));
				focusView = txtEmail;
				cancel = true;
			}
		}
		if (AswaqApp.isEmptyOrNull(userName)) {
			txtUserName.setError(getString(R.string.error_user_name_empty));
			focusView = txtUserName;
			cancel = true;
		}
		if (AswaqApp.isEmptyOrNull(mobileNumber)) {
			txtMobileNumber
					.setError(getString(R.string.error_mobile_number_empty));
			focusView = txtMobileNumber;
			cancel = true;
		} else {
			// check if mobile number has been written in the right format

		}
		if (AswaqApp.isEmptyOrNull(password)) {
			txtPassword.setError(getString(R.string.error_password_empty));
			focusView = txtPassword;
			cancel = true;
		} else {
			if (password.length() < 4) {
				txtPassword.setError(getString(R.string.error_password_length));
				focusView = txtPassword;
				cancel = true;
			}
		}
		if (cancel) {
			focusView.requestFocus();
		} else {
			DataStore.getInstance().attemptSignUp(email, userName,
					mobileNumber, password, address, description,
					registerCallback);
		}

	}

	private DataRequestCallback registerCallback = new DataRequestCallback() {
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if (success) {
				switch (data.getFlag()) {
				case ServerAccess.ERROR_CODE_done:
					Intent i = new Intent(RegisterActivity.this,
							MainActivity.class);
					startActivity(i);
					break;
				case ServerAccess.ERROR_CODE_user_exists_before:
					showToast(getString(R.string.error_user_exists_before));
					break;
				}
			}
		}
	};

}
