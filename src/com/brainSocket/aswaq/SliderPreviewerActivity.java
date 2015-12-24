package com.brainSocket.aswaq;

import com.brainSocket.aswaq.data.PhotoProvider;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class SliderPreviewerActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slider_previewer);
		init();
	}
	
	private void init()
	{
		String photoPath=getIntent().getStringExtra("photoPath");
		ImageView ivSlidePreview=(ImageView)findViewById(R.id.ivSlidePreview);
		PhotoProvider.getInstance().displayPhotoNormal(photoPath, ivSlidePreview);
	}

}
