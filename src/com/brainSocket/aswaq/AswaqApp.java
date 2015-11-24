package com.brainSocket.aswaq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;

import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.ImageType;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

public class AswaqApp extends Application{
	
	private static Context AppContext ;
	private static Activity currentAcivity ;
	public static final String VERSIOIN_ID = "0.1";
	private static HashMap<String, Object> crossData;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		AppContext = this ;
		DataStore.getInstance() ; // to make sure DataStore is initalized
		crossData=new HashMap<String, Object>();
	}
	
	public static void addPair(String key,Object ob)
	{
		if(crossData.containsKey(key))
			crossData.remove(key);
		crossData.put(key, ob);
	}
	
	public static Object getPair(String key)
	{
		Object ob=null;
		try
		{
			ob= crossData.get(key);
			crossData.remove(key);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return ob;
	}
	
	public static boolean hasPair(String key)
	{
		return crossData.containsKey(key);
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
	
	public static void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}

}
