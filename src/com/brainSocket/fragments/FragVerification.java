package com.brainSocket.fragments;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.models.AppUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;

public class FragVerification extends Fragment implements OnClickListener{
	private Button btnVerifyCode;
	private Button btnResendVerificationCode;
	private EditText txtVerificationCode;
	private HomeCallbacks homeCallback;
	
	private FragVerification()
	{
		
	}
	
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
		btnResendVerificationCode=(Button)getActivity().findViewById(R.id.btnResendVerificationCode);
		btnResendVerificationCode.setOnClickListener(this);
		txtVerificationCode=(EditText)getActivity().findViewById(R.id.txtVerificationCode);
		homeCallback=(HomeCallbacks)getActivity();
	}
	
	private void resendVerificationCode()
	{
		DataStore.getInstance().attemptSendVerificationCode(resendVerificationCodeCallback);
		homeCallback.showProgress(true, 0);
	}
	
	private DataRequestCallback resendVerificationCodeCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					homeCallback.showToast(getString(R.string.toast_verification_sent));
				}
			}
		}
	};
	
	private DataRequestCallback verifyUserCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(data.getFlag()==ServerAccess.ERROR_CODE_done)
			{
				DataCacheProvider cacheProvider=DataCacheProvider.getInstance();
				AppUser me=cacheProvider.getMe();
				me.setVerified(1);
				cacheProvider.removeStoredMe();
				cacheProvider.storeMe(me);
				homeCallback.loadFragment(FragmentType.Main,null);
			}
			else if(data.getFlag()==ServerAccess.ERROR_CODE_wrong_verification_code)
			{
				txtVerificationCode.setError(getString(R.string.verif_error_verification_message_wrong));
				txtVerificationCode.requestFocus();
			}
			else
			{
				homeCallback.showToast("unknown error:"+data.getFlag());
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
case R.id.btnResendVerificationCode:
			resendVerificationCode();
			break;
		}
		
	}
	
	public static FragVerification newInstance()
	{
		FragVerification fragVerification=new FragVerification();
		return fragVerification;
	}

}
