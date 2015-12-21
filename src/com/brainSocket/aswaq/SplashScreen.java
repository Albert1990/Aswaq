package com.brainSocket.aswaq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;

import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;

public class SplashScreen extends AppBaseActivity {

	private final static int MAIN_ACTIVITY_REQUIEST_CODE = 136;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_splash);
	    animateLogo();
	    //DataStore.getInstance().removeAllStoredData();
		new Handler().postDelayed(startApp, 2000);
	}
	
	private Runnable startApp=new Runnable() {
		
		@Override
		public void run() {
			Intent i=new Intent(SplashScreen.this,MainActivity.class);
			startActivityForResult(i, MAIN_ACTIVITY_REQUIEST_CODE);
		}
	};
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0 == MAIN_ACTIVITY_REQUIEST_CODE)
			finish();
		
	}
	private void animateLogo(){
		View vCircle1 = findViewById(R.id.vLogoCircle1);
		View vCircle2 = findViewById(R.id.vLogoCircle2);
		View vCircle3 = findViewById(R.id.vLogoCircle3);
		View vCircle4 = findViewById(R.id.vLogoCircle4);
		View vCircle5 = findViewById(R.id.vLogoCircle5);
		
		vCircle1.setVisibility(View.INVISIBLE);
		vCircle2.setVisibility(View.INVISIBLE);
		vCircle3.setVisibility(View.INVISIBLE);
		vCircle4.setVisibility(View.INVISIBLE);
		vCircle5.setVisibility(View.INVISIBLE);
		
		animateCircle(vCircle3, 0, 8f);
		animateCircle(vCircle2, 300, 5f);
		animateCircle(vCircle4, 300, 5f);
		animateCircle(vCircle1, 600, 5f);
		animateCircle(vCircle5, 600, 5f);
	}
	
	private void animateCircle(View circle, int delay, float overShoot){
		Animation anim = AnimationUtils.loadAnimation(AswaqApp.getAppContext(), R.anim.logo_anim);
		OvershootInterpolator inter = new OvershootInterpolator(overShoot);
		anim.setInterpolator(inter);
		anim.setStartOffset(delay);
		circle.startAnimation(anim);
	}
	
}
