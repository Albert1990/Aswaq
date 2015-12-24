package com.brainSocket.aswaq.dialogs;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.data.FacebookProvider;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.views.TextViewCustomFont;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class DiagConfirm extends DialogFragment implements OnClickListener{
	private TextViewCustomFont tvConfirmTitle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.diag_confirm, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		tvConfirmTitle=(TextViewCustomFont)getView().findViewById(R.id.tvConfirmTitle);
		getView().findViewById(R.id.btnConfirmYes).setOnClickListener(this);
		getView().findViewById(R.id.btnConfirmCancel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.btnConfirmYes:
			//String photoPath=AswaqApp.getImagePath(ImageType.Ad, photo_path)
			//FacebookProvider.getInstance().sharePhotoViaFacebook(this, xphoto, caption);
			break;
		case R.id.btnConfirmCancel:
			Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
