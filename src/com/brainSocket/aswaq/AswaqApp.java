package com.brainSocket.aswaq;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.enums.PhoneNumberCheckResult;

public class AswaqApp extends Application {

	private static Context AppContext;
	private static Activity currentAcivity;
	public static final String VERSIOIN_ID = "0.1";
	public static final int SLIDER_TRANSITION_INTERVAL = 3000;
	public static final String DEFAULT_USER_IMAGE = "default_user.png";
	public static final String DEFAULT_Ad_IMAGE = "logo_splash.png";
	public final static int REQUEST_PICK_IMG_FROM_CAMERA = 894;
    public final static int REQUEST_PICK_IMG_FROM_GALLERY = 89;

	@Override
	public void onCreate() {
		super.onCreate();
		AppContext = this; 
		DataStore.getInstance(); // to make sure DataStore is initalized
	}

	public static Context getAppContext() {
		return AppContext;
	}

	/**
	 * used by the GCM receiver to change context on receive if not set
	 * 
	 * @param appContext
	 */
	public static void setAppContext(Context appContext) {
		// AppContext = appContext;
	}

	public static void setCurrentAcivity(Activity currentAcivity) {
		AswaqApp.currentAcivity = currentAcivity;
	}

	public static Activity getCurrentAcivity() {
		return currentAcivity;
	}

	public static boolean isEmailValid(final String email) {
		if (TextUtils.isEmpty(email)) {
		    return false;
		  } else {
		    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
		  }
	}

	public static boolean isEmptyOrNull(final String txt) {
		if (txt == null)
			return true;
		if (txt.equals(""))
			return true;
		if (txt.length() == 0)
			return true;
		return false;
	}

	public static boolean checkMobileNumber(final String mobileNumber) {
		return true;
	}

	public static String getImagePath(ImageType imageType, String photo_path) {
		String url = null;
		switch (imageType) {
		case Category:
			url = ServerAccess.IMAGE_SERVICE_URL + "categories/";
			break;
		case Slide:
			url = ServerAccess.IMAGE_SERVICE_URL + "slides/";
			break;
		case User:
			if (photo_path.length() <= 0)
				photo_path = AswaqApp.DEFAULT_USER_IMAGE;
			url = ServerAccess.IMAGE_SERVICE_URL + "users/";
			break;
		case Ad:
			if (photo_path == null)
				photo_path = AswaqApp.DEFAULT_Ad_IMAGE;
			url = ServerAccess.IMAGE_SERVICE_URL + "ads/";
			break;
		case AdThumb:
			if (photo_path == null)
				photo_path = AswaqApp.DEFAULT_Ad_IMAGE;
			url = ServerAccess.IMAGE_SERVICE_URL + "ads/thumbs/";
			break;
		}
		return url + photo_path;
	}

	public static void resizeImage(Bitmap originalBitmap, String path,int newWidth) {
		try {
			int newHeight = (int) (originalBitmap.getHeight()*(newWidth/(float)originalBitmap.getWidth()));
			File imageFile = new File(path);
			Bitmap bmScreenshot = Bitmap.createScaledBitmap(originalBitmap,
					newWidth,newHeight , false);
			OutputStream fOut = new FileOutputStream(imageFile);
			bmScreenshot.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
			fOut.flush();
			fOut.close();
			bmScreenshot.recycle();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static long getTimestampNow() {
		long res = 0;
		try {
			res = Calendar.getInstance().getTimeInMillis();
		} catch (Exception e) {
		}
		return res;
	}

	public static PhoneNumberCheckResult validatePhoneNum(String num) {
		if (num == null || "".equals(num.trim()))
			return PhoneNumberCheckResult.EMPTY;

		PhoneNumberCheckResult result = PhoneNumberCheckResult.OK;
		if (num.length() <= 8) {
			result = PhoneNumberCheckResult.SHORT;
		}

//		 if(!(num.startsWith("00") || num.startsWith("+") ) )
//		 result = PhoneNumberCheckResult.WRONG;

		return result;
	}
	
	public static String localizeMobileNumber(String mobileNumber)
	{
		return "0"+mobileNumber.substring(4,mobileNumber.length()); // to remove +963 and add 0 to the first of the number
	}
}
