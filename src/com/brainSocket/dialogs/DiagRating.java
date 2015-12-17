package com.brainSocket.dialogs;

import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.ServerResult;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class DiagRating extends DialogFragment{
	private DataRequestCallback onRatingCallback;
	private RatingBar rbUserRate;
	private float oldUserRate;
	
	public DiagRating(float oldUserRate,DataRequestCallback onRatingCallback)
	{
		this.onRatingCallback=onRatingCallback;
		this.oldUserRate=oldUserRate;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dialog_rating, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		rbUserRate=(RatingBar)getActivity().findViewById(R.id.rbUserRate);
		rbUserRate.setRating(oldUserRate);
		rbUserRate.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				ServerResult result=new ServerResult();
				result.addPair("rating", Float.toString(rating));
				dismiss();
				onRatingCallback.onDataReady(result, true);
			}
		});
		
		getDialog().setTitle(getString(R.string.lbl_rate_user));
	}
	
}
