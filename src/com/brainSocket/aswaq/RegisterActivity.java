package com.brainSocket.aswaq;

import java.util.HashMap;

import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppBaseActivity implements
		OnClickListener,HomeCallbacks {
	private EditText txtEmail, txtPassword,txtRepeatPassword;
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
		txtPassword = (EditText) findViewById(R.id.txtPasswordRegister);
		txtRepeatPassword=(EditText)findViewById(R.id.txtRepeatPasswordRegister);
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
		String password = txtPassword.getText().toString();
		String repeatedPassword=txtRepeatPassword.getText().toString();

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
			txtPassword.setError(getString(R.string.login_error_password_empty));
			focusView = txtPassword;
			cancel = true;
		} 
		else {
//			if (password.length() < 4) {
//				txtPassword.setError(getString(R.string.login_error_password_length));
//				focusView = txtPassword;
//				cancel = true;
//			}
			if (AswaqApp.isEmptyOrNull(repeatedPassword)) {
				txtRepeatPassword.setError(getString(R.string.login_error_password_empty));
				focusView = txtRepeatPassword;
				cancel = true;
			}
			else
			{
				if(!password.equals(repeatedPassword))
				{
					txtRepeatPassword.setError(getString(R.string.error_password_not_same));
					focusView = txtRepeatPassword;
					cancel = true;
				}
			}
		}
		
		if (cancel) {
			focusView.requestFocus();
		} else {
			String facebookAccessToken="";
			String facebookId="";
			DataStore.getInstance().attemptSignUp(email, "",
					"", password, "", "",
					facebookId,facebookAccessToken,
					registerCallback);
		}

	}

	private DataRequestCallback registerCallback = new DataRequestCallback() {
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			if (success) {
				switch (data.getFlag()) {
				case ServerAccess.ERROR_CODE_done:
					Intent i = new Intent(RegisterActivity.this,
							MainActivity.class);
					startActivity(i);
					break;
				case ServerAccess.ERROR_CODE_user_exists_before:
					txtEmail.setError(getString(R.string.login_error_user_exists_before));
					break;
				}
			}
		}
	};
	
	@Override
	public void showProgress(boolean show, int msg) {
		// TODO Auto-generated method stub
		
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
	
}
