package com.brainSocket.aswaq;

import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.models.AppUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppBaseActivity {
	Intent i=null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_splash);
	    // TODO Auto-generated method stub
	    DataStore.getInstance().justWait(1000, afterWaitCallback);
	    init();
	}
	
	private DataRequestCallback afterWaitCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(i!=null)
				startActivity(i);
		}
	};
	
	private void init()
	{
		try
		{
			DataCacheProvider cacheProvider=DataCacheProvider.getInstance();
			cacheProvider.removeAllStoredData();
//			AppUser me= cacheProvider.getMe();
//			if(me==null)
//			{
//				i=new Intent(SplashScreen.this,LoginActivity.class);
//			}
//			else
//			{
				i=new Intent(SplashScreen.this,MainActivity.class);
//			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
