package com.brainSocket.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.models.AppUser;
import com.brainSocket.models.CategoryModel;
import com.brainSocket.models.PageModel;

import android.content.Context;
import android.content.SharedPreferences;

public class DataCacheProvider {
	///KEYs
		public static final String PREF_DATA = "AswaqData";
		public static final String PREF_FIRST_TIME = "isFirstTime" ; 
		public static final String PREF_APP_USER  = "userMe" ;
		public static final String PREF_API_ACCESS_TOKEN = "accessToken" ;
		public static final String PREF_PHOTO_CACHE_CLEARED = "image_cache_clear" ;
		public static final String PREF_APP_ACCESS_MODE = "access_mode";
		public static final String PREF_APP_VERSION_STATUS = "version_status";
		public static final String PREF_APP_MAIN_CATEGORIES="main_categories";
		public static final String PREF_APP_PAGES="pages";
		public static final String PREF_APP_CATEGORIES_PAIRS="categories_pairs";
		
		
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
	
	public void storePage(PageModel page)
	{
		try
		{
			String str=prefData.getString(PREF_APP_PAGES, null);
			JSONArray jsonPages=null;
			if(str!=null)
				jsonPages=new JSONArray(str);
			else
				jsonPages=new JSONArray();
			int objectIndex=-1;
			for(int i=0;i<jsonPages.length();i++)
			{
				JSONObject ob=jsonPages.getJSONObject(i);
				if(ob.getInt("categoryId")==page.getCategoryId())
				{
					objectIndex=i;
					break;
				}
			}
			JSONObject jsonPage=page.getJsonObject();
			if(objectIndex!=-1)
			{
				// we have to replace the old object
				jsonPages.put(objectIndex, jsonPage);
			}
			else
				jsonPages.put(jsonPage);
			str=jsonPages.toString();
			prefDataEditor.putString(PREF_APP_PAGES, str);
			prefDataEditor.commit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public PageModel getStoredPage(int categoryId)
	{
		PageModel page=null;
		try
		{
			String str=prefData.getString(PREF_APP_PAGES, null);
			if(str==null)
				return null;
			JSONArray jsonPages=new JSONArray(str);
			for(int i=0;i<jsonPages.length();i++)
			{
				JSONObject ob=jsonPages.getJSONObject(i);
				if(ob.getInt("categoryId")==categoryId)
				{
					page=new PageModel(ob);
					break;
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return page;
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
			removeStoredMe();
			removePages();
			removeSubCategoriesPairs();
		}
		catch(Exception ex){}
	}

}
