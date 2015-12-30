package com.brainSocket.aswaq.dialogs;

import java.util.HashMap;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.data.PageTransitionCallback;
import com.brainSocket.aswaq.views.EditTextCustomFont;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;

public class DiagFacebookPage extends DialogFragment implements OnClickListener{
	private EditTextCustomFont txtFacebookPageLink;
	private View btnAccept;
	private PageTransitionCallback callback=null;
	
	public DiagFacebookPage(PageTransitionCallback callback)
	{
		this.callback=callback;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.diag_facebook_page, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		txtFacebookPageLink=(EditTextCustomFont)getView().findViewById(R.id.txtFacebookPageLink);
		btnAccept=getView().findViewById(R.id.btnAccept);
		btnAccept.setOnClickListener(this);
	}
	
	private void processFacebookPageLink()
	{
		String facebookPageLink=txtFacebookPageLink.getText().toString();
		HashMap<String, Object> params=new HashMap<String, Object>();
		if(!AswaqApp.isEmptyOrNull(facebookPageLink))
		{
			params.put("facebookPageLink", facebookPageLink);
		}
		callback.onDataReady(params);
		dismiss();
	}

	@Override
	public void onClick(View v) {
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.btnAccept:
			processFacebookPageLink();
			break;
		}
	}

}
