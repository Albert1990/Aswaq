package com.brainSocket.data;

import org.json.JSONObject;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.models.AppUser;

import android.content.Context;
import android.content.SharedPreferences;

public class DataCacheProvider {
	///KEYs
		public static final String PREF_DATA = "AswaqData";
		public static final String PREF_FIRST_TIME = "isFirstTime" ; 
		public static final String PREF_APP_USER  = "userMe" ;
		public static final String PREF_ENROLLED_FRIENDS = "enroledFriends" ;
		public static final String PREF_SETTINGS_NOTIFICATIONS = "settings_notifications" ;
		public static final String PREF_API_ACCESS_TOKEN = "accessToken" ;
		public static final String PREF_PHOTO_CACHE_CLEARED = "image_cache_clear" ;
		public static final String PREF_USER_STATS = "userStats";
		public static final String PREF_USER_SCORE = "score";
		public static final String PREF_APP_ACCESS_MODE = "access_mode";
		public static final String PREF_APP_VERSION_STATUS = "version_status";
		
		// tutorial flags
		public static final String PREF_TUT_PLANT = "tut_plant";
		public static final String PREF_TUT_SELF = "tut_self";
		public static final String PREF_TUT_SEND = "tut_send";
		public static final String PREF_TUT_OPEN_TASK = "tut_open_task";
		
		// dates
		private static final String PREF_LAST_SESSIONS_UPDATE_SERVER_TIME = "lastSessionsUpdateServer" ; 
		private static final String PREF_LAST_SESSIONS_UPDATE_LOCAL_TIME = "lastSessionsUpdateLocal";
		private static final String PREF_LAST_LONG_TERM_DATA_UPDATE_TIME = "lastLongTermUpdateLocal";
		
		private static DataCacheProvider cacheProvider = null;
		// shared preferences
		SharedPreferences prefData;
		SharedPreferences.Editor prefDataEditor;
		
		//members
		private String accessToken;
		private AppUser me;
	
	private static DataCacheProvider instance;

	private DataCacheProvider() {
		try {
			// initialize
			prefData = AswaqApp.getAppContext().getSharedPreferences(PREF_DATA, Context.MODE_PRIVATE);
			prefDataEditor = prefData.edit();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DataCacheProvider getInstance()
	{
		if(instance==null)
			instance=new DataCacheProvider();
		return instance;
	}
	
	/**
	 * Stores the accessToken received on login from the API 
	 * in shared preferences
	 * @param accessToken
	 */
	public void storeAccessToken(String accessToken)
	{
		try {
			//if(accessToken != null) {
				prefDataEditor.putString(PREF_API_ACCESS_TOKEN, accessToken);
				prefDataEditor.commit();
			//}
			this.accessToken=accessToken;
		}
		catch (Exception e) {}
	}
	
	/**
	 * Returns the cached API accessToken from Shared Preferences,
	 *  null if it wasn't stored before
	 */
	public String getAccessToken()
	{
		if(accessToken==null)
		{
			try {
				accessToken = prefData.getString(PREF_API_ACCESS_TOKEN, null);
			}
			catch(Exception e) {}
		}
		return accessToken;
	}
	
	/**
	 * Stores the {@link AppUser} object in Shared Preferences
	 * @param appUser
	 */
	public void storeMe(AppUser me) {
		try {
			//if(me == null) {
				String str = me.getJsonString();
				prefDataEditor.putString(PREF_APP_USER, str);
				prefDataEditor.commit();
//			}else
//				removeStoredMe();
			this.me=me;
		}
		catch (Exception e) {}
	}
	
	/**
	 * Removes the stored {@link AppUser} from Shared Preferences
	 */
	public void removeStoredMe()
	{
		try {
			prefDataEditor.remove(PREF_APP_USER);
			prefDataEditor.commit();
		}
		catch (Exception e) {}
	}
	
	/**
	 * Returns the {@link AppUser} object stored in Shared Preferences,
	 * null otherwise
	 */
	public AppUser getMe() {
		if(me==null)
		{
			try {
				String str = prefData.getString(PREF_APP_USER, null);
				if(str != null) {
					JSONObject json = new JSONObject(str);
					me = new AppUser(json);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return me;
	}

}
