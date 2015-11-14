package com.brainSocket.aswaq;

import com.brainSocket.data.DataStore;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class AswaqApp extends Application{
	
	private static Context AppContext ;
	private static Activity currentAcivity ;
	public static final String VERSIOIN_ID = "0.1";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		AppContext = this ;
		DataStore.getInstance() ; // to make sure DataStore is initalized 
	}
	
	public static Context getAppContext() {
		return AppContext;
	}
	
	/**
	 * used by the GCM receiver to change context on receive if not set
	 * @param appContext
	 */
	public static void setAppContext(Context appContext) {
		AppContext = appContext;
	}
	public static void setCurrentAcivity(Activity currentAcivity) {
		AswaqApp.currentAcivity = currentAcivity;
	}
	public static Activity getCurrentAcivity() {
		return currentAcivity;
	}
	
	public static boolean isEmailValid(final String email)
	{
		return true;
	}
	
	public static boolean isEmptyOrNull(final String txt)
	{
		if(txt==null)
			return true;
		if(txt.equals(""))
			return true;
		if(txt.length()==0)
			return true;
		return false;
	}
	
	public static boolean checkMobileNumber(final String mobileNumber)
	{
		return true;
	}

}
