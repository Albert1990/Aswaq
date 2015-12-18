package com.brainSocket.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.ServerResult;
import com.brainSocket.views.TextViewCustomFont;

public class DiagRating extends DialogFragment implements OnClickListener{
	private DataRequestCallback onRatingCallback;
	private RatingBar rbUserRate;
	private float oldUserRate;
	
	public DiagRating(float oldUserRate,DataRequestCallback onRatingCallback)
	{
		this.onRatingCallback=onRatingCallback;
		this.oldUserRate=oldUserRate;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_rating, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init(){
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		rbUserRate = (RatingBar) getView().findViewById(R.id.rbUserRate);
		View btnOk = getView().findViewById(R.id.btnOk);
		
		btnOk.setOnClickListener(this);
		rbUserRate.setRating(oldUserRate);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOk:
			ServerResult result = new ServerResult();
			result.addPair("rating", Float.toString(rbUserRate.getRating()));
			dismiss();
			onRatingCallback.onDataReady(result, true);
			break;

		default:
			break;
		}
		
	}
}
