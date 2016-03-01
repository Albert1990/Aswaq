package com.brainSocket.aswaq.dialogs;

import com.brainSocket.aswaq.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class DiagUpdateAppVersion extends DialogFragment implements OnClickListener{
	private View btnOk;
	private View btnCancel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_update_app_version, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		btnOk=getView().findViewById(R.id.btnOk);
		btnOk.setOnClickListener(this);
		btnCancel=getView().findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.btnOk:
			try
			{
				Uri uri = Uri.parse("http://aswaqsyria.com.com"); // missing 'http://' will
				Intent i = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(i);
			}catch(Exception ex){}
			break;
		case R.id.btnCancel:
			dismiss();
			break;
		}
	}

}
