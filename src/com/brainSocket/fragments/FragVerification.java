package com.brainSocket.fragments;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;

import enums.FragmentType;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FragVerification extends Fragment implements OnClickListener{
	private Button btnVerifyCode;
	private EditText txtVerificationCode;
	private HomeCallbacks homeCallback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.frag_verification, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		btnVerifyCode=(Button)getActivity().findViewById(R.id.btnVerifyCode);
		btnVerifyCode.setOnClickListener(this);
		txtVerificationCode=(EditText)getActivity().findViewById(R.id.txtVerificationCode);
		homeCallback=(HomeCallbacks)getActivity();
	}
	
	private void resendVerificationCode()
	{
		DataStore.getInstance().attemptSendVerificationCode(resendVerificationCodeCallback);
	}
	
	private DataRequestCallback resendVerificationCodeCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private DataRequestCallback verifyUserCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(data.getFlag()==ServerAccess.ERROR_CODE_done)
			{
				homeCallback.loadFragment(FragmentType.Main);
			}
			else if(data.getFlag()==ServerAccess.ERROR_CODE_verification_message_not_exists)
			{
				txtVerificationCode.setError(getString(R.string.verif_error_verification_message_wrong));
				txtVerificationCode.requestFocus();
			}
			else
			{
				
			}
		}
	};
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.btnVerifyCode:
			//check input
			String verificationCode=txtVerificationCode.getText().toString();
			DataStore.getInstance().attempVerifyUser(verificationCode,verifyUserCallback);
			break;
		}
	}

}
