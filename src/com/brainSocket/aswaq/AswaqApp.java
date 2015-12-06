package com.brainSocket.aswaq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.enums.ImageType;

public class AswaqApp extends Application{
	
	private static Context AppContext ;
	private static Activity currentAcivity ;
	public static final String VERSIOIN_ID = "0.1";
	public static final int SLIDER_TRANSITION_INTERVAL=2000;
	public static final String DEFAULT_USER_IMAGE="x1.png";
	
	
	@Override
	public void onCreate() {
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
		//AppContext = appContext;
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
	
	public static String getImagePath(ImageType imageType,String photo_path)
	{
		String url=null;
		switch(imageType)
		{
		case Category:
			url=ServerAccess.IMAGE_SERVICE_URL+"categories/";
			break;
		case Slide:
			url=ServerAccess.IMAGE_SERVICE_URL+"slides/";
			break;
		case User:
			if(photo_path.length()<=0)
				photo_path="x1.png";
			url=ServerAccess.IMAGE_SERVICE_URL+"users/";
			break;
		}
		return url+photo_path;
	}
	
	public static void resizeImage(Bitmap originalBitmap,String path)
	{
		try
		{
			File imageFile = new File(path);
			Bitmap bmScreenshot = Bitmap.createScaledBitmap(originalBitmap, 120, 120, false);
			OutputStream fOut = new FileOutputStream(imageFile);
			bmScreenshot.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
			fOut.flush();
			fOut.close();           
			bmScreenshot.recycle();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

    public static long getTimestampNow(){
    	long res = 0;
    	try {
    		res = Calendar.getInstance().getTimeInMillis();
    	}
    	catch (Exception e) {}
    	return res;
    }
}
