package com.brainSocket.aswaq.dialogs;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.views.EditTextCustomFont;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;

public class DiagChangePassword extends DialogFragment implements OnClickListener{
	private View btnSendChangePasswordRequest;
	private EditTextCustomFont txtEmail;
	private HomeCallbacks homeCallbacks;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.diag_change_password,container,false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		homeCallbacks=(HomeCallbacks)getActivity();
		txtEmail=(EditTextCustomFont)getView().findViewById(R.id.txtEmail);
		btnSendChangePasswordRequest=getView().findViewById(R.id.btnSendChangePasswordRequest);
		btnSendChangePasswordRequest.setOnClickListener(this);
	}
	
	private DataRequestCallback sendChangePasswordRequestCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			homeCallbacks.showProgress(false);
			if(success)
			{
				homeCallbacks.showToast(getActivity().getString(R.string.diag_change_password_sending_success));
				dismiss();
			}
			else {
				homeCallbacks.showToast(getActivity().getString(R.string.error_connection_error));
			}
		}
	};
	
	private void sendChangePasswordRequest()
	{
		boolean cancel=false;
		String email=txtEmail.getText().toString();
		if (AswaqApp.isEmptyOrNull(email)) {
			txtEmail.setError(getString(R.string.login_error_email_empty));
			cancel = true;
		} else {
			if (!AswaqApp.isEmailValid(email)) {
				txtEmail.setError(getString(R.string.login_error_email_invalid));
				cancel = true;
			}
		}
		if(cancel)
		{
			txtEmail.requestFocus();
		}
		else
		{
			homeCallbacks.showProgress(true);
			DataStore.getInstance().attemptSendChangePasswordRequest(email, sendChangePasswordRequestCallback);
		}
	}

	@Override
	public void onClick(View v) {
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.btnSendChangePasswordRequest:
			sendChangePasswordRequest();
			break;
		}
	}

}
