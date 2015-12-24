package com.brainSocket.aswaq.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.models.CategoryModel;
import com.brainSocket.aswaq.models.PageModel;

import android.content.Context;
import android.content.SharedPreferences;

public class DataCacheProvider {
	///KEYs
		public static final String PREF_DATA = "AswaqData";
		public static final String PREF_FIRST_TIME = "isFirstTime" ; 
		public static final String PREF_APP_USER  = "userMe" ;
		//public static final String PREF_API_ACCESS_TOKEN = "accessToken" ;
		public static final String PREF_PHOTO_CACHE_CLEARED = "image_cache_clear" ;
		public static final String PREF_APP_ACCESS_MODE = "access_mode";
		public static final String PREF_APP_VERSION_STATUS = "version_status";
		public static final String PREF_APP_PAGES="pages";
		public static final String PREF_APP_CATEGORIES_PAIRS="categories_pairs";
		
		
		private static DataCacheProvider cacheProvider = null;
		// shared preferences
		SharedPreferences prefData;
		SharedPreferences.Editor prefDataEditor;
	
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
	 * Stores the timestamp of the last photo cache clear
	 */
	public void storePhotoClearedCacheTimestamp(long timestamp)
	{
		try {
			prefDataEditor.putLong(PREF_PHOTO_CACHE_CLEARED, timestamp);
			prefDataEditor.commit();
		}
		catch (Exception e) {}
	}
	
	/**
	 * Returns the stored timestamp of the last photo cache clear
	 */
	public long getStoredPhotoClearedCacheTimestamp()
	{
		long timestamp = 0;
		try {
			timestamp = prefData.getLong(PREF_PHOTO_CACHE_CLEARED, 0);
		}
		catch (Exception e) {}
		return timestamp;
	}
	
	/**
	 * Stores the accessToken received on login from the API 
	 * in shared preferences
	 * @param accessToken
	 */
//	public void storeAccessToken(String accessToken)
//	{
//		try {
//			prefDataEditor.putString(PREF_API_ACCESS_TOKEN, accessToken);
//			prefDataEditor.commit();
//		}
//		catch (Exception e) {}
//	}
	
	/**
	 * Returns the cached API accessToken from Shared Preferences,
	 *  null if it wasn't stored before
	 */
//	public String getAccessToken1()
//	{
//		String accessToken=null;
//
//			try {
//				accessToken = prefData.getString(PREF_API_ACCESS_TOKEN, null);
//			}
//			catch(Exception e) {}
//		return accessToken;
//	}
	
	/**
	 * Stores the {@link AppUser} object in Shared Preferences
	 * @param appUser
	 */
	public void storeMe(AppUser me) {
		try {
				String str = me.getJsonString();
				prefDataEditor.putString(PREF_APP_USER, str);
				prefDataEditor.commit();
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
		AppUser me=null;
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
		return me;
	}
	
	public void storePages(HashMap<Integer, PageModel> pages)
	{
		try
		{
			JSONArray jsonPages=new JSONArray();
			
			for (Object ob : pages.values()) {
				PageModel page=(PageModel)ob;
				JSONObject pageJsonObject= page.getJsonObject();
				jsonPages.put(pageJsonObject);
			}

			String str=jsonPages.toString();
			prefDataEditor.putString(PREF_APP_PAGES, str);
			prefDataEditor.commit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public HashMap<Integer, PageModel> getStoredPages()
	{
		HashMap<Integer, PageModel> pages=null;
		try
		{
			String str=prefData.getString(PREF_APP_PAGES, null);
			if(str==null)
				return null;
			pages=new HashMap<Integer, PageModel>();
			JSONArray jsonPages=new JSONArray(str);
			for(int i=0;i<jsonPages.length();i++)
			{
				JSONObject ob=jsonPages.getJSONObject(i);
				PageModel page=new PageModel(ob);
				pages.put(page.getCategoryId(), page);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return pages;
	}
	
	public void removePages()
	{
		try
		{
			prefDataEditor.remove(PREF_APP_PAGES);
			prefDataEditor.commit();
		}
		catch(Exception ex){}
	}
	
	public void storeSubCategoriesPairs(List<CategoryModel> categories)
	{
		try
		{
			JSONArray jsonCategories=new JSONArray();
			for(int i=0;i<categories.size();i++)
				jsonCategories.put(categories.get(i).getJsonObject());
			String str=jsonCategories.toString();
			prefDataEditor.putString(PREF_APP_CATEGORIES_PAIRS, str);
			prefDataEditor.commit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public List<CategoryModel> getStoredCategoriesPairs()
	{
		List<CategoryModel> categories=null;
		try
		{
			String str=prefData.getString(PREF_APP_CATEGORIES_PAIRS, null);
			if(str!=null)
			{
				JSONArray jsonCategories=new JSONArray(str);
				categories=new ArrayList<CategoryModel>();
				for(int i=0;i<jsonCategories.length();i++)
				{
					JSONObject jsOb=jsonCategories.getJSONObject(i);
					categories.add(new CategoryModel(jsOb));
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return categories;
	}
	
	public void removeSubCategoriesPairs()
	{
		try
		{
			prefDataEditor.remove(PREF_APP_CATEGORIES_PAIRS);
			prefDataEditor.commit();
		}
		catch(Exception ex){}
	}
	
	public void removeAllStoredData()
	{
		try
		{
			prefDataEditor.clear();
			prefDataEditor.commit();
		}
		catch(Exception ex){}
	}

}
