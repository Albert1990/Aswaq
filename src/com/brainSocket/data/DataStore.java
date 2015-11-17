package com.brainSocket.data;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.brainSocket.models.AppUser;
import com.brainSocket.models.CategoryModel;
import com.brainSocket.models.SlideModel;

import android.os.Handler;

public class DataStore {
	private ServerAccess serverHandler = null;
	private static Handler handler = null;
	private static DataStore instance;
	private DataCacheProvider cacheProvider=null;

	public static DataStore getInstance() {
		if (instance == null)
			instance = new DataStore();
		return instance;
	}

	private DataStore() {
		try {
			serverHandler = ServerAccess.getInstance();
			cacheProvider = DataCacheProvider.getInstance();
			handler = new Handler();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param phoneNumfinal
	 * @param username
	 * @param gender
	 * @param FbID
	 *            : pass null if signing-up without facebook
	 * @param FbAccessToken
	 *            : pass null if signing-up without facebook
	 * @param callback
	 */
	public void attemptSignUp(final String email, final String userName,
			final String mobileNumber, final String password,
			final String address, final String description,
			final DataRequestCallback callback) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = true;
				ServerResult result = serverHandler.registerUser(email,
						userName, mobileNumber, password, address, description);
				if (result.connectionFailed()) {
					success = false;
				} else {
					try {
						AppUser me = (AppUser) result.getPairs().get("appUser");
						// apiAccessToken = me.getAccessToken();
						// setApiAccessToken(apiAccessToken);
					} catch (Exception e) {
						success = false;
					}

				}
				invokeCallback(callback, success, result); // invoking the
															// callback
			}
		}).start();
	}

	/**
	 * attempting login using phone number
	 * 
	 * @param phoneNumfinal
	 * @param callback
	 */
	public void attemptLogin(final String email, final String password,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = true;
				ServerResult result = serverHandler.login(email, password);
				if (result.connectionFailed()) {
					success = false;
				} else {
					if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
						AppUser me = (AppUser) result.getPairs().get("appUser");
						cacheProvider.storeAccessToken(me.getAccessToken());
						cacheProvider.storeMe(me);
					}
				}
				invokeCallback(callback, success, result); // invoking the
															// callback
			}
		}).start();
	}

	public void attemptGetPageComponents(final int categoryId,final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result=serverHandler.getCategories(categoryId);
				if(result.connectionFailed())
					success=false;
				else
				{
					// parsing categories
					if(result.getFlag()==ServerAccess.ERROR_CODE_done)
					{
						try
						{
							JSONArray jsonCategories=(JSONArray)result.getValue("jsonCategories");
							List<CategoryModel> categories=new ArrayList<CategoryModel>();
							for(int i=0;i<jsonCategories.length();i++)
							{
								categories.add(new CategoryModel((JSONObject)jsonCategories.get(i)));
							}
							result.addPair("categories", categories);
							
							JSONArray jsonSlides=(JSONArray)result.getValue("jsonSlides");
							List<SlideModel> slides=new ArrayList<SlideModel>();
							for(int i=0;i<jsonSlides.length();i++)
							{
								slides.add(new SlideModel((JSONObject)jsonSlides.get(i)));
							}
							result.addPair("slides", slides);
						}
						catch(Exception ex){}
					}
				}
				if(callback!=null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}
	
	public void attempVerifyUser(final String verificationCode,final DataRequestCallback callback)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success=true;
				ServerResult result=serverHandler.verifyUser(verificationCode);
				if(result.connectionFailed())
					success=false;
				if(callback!=null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}
	
	public void attemptSearchFor(final String keyword,final DataRequestCallback callback)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success=true;
				ServerResult result=serverHandler.searchFor(keyword);
				if(result.connectionFailed())
					success=false;
				if(callback!=null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}
	
	public void attemptAddNewAdvertise(final String description,
			final int categoryId,final boolean isUsed,final int price,
			final JSONArray telephones,final DataRequestCallback callback)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result=serverHandler.addNewAdvertise(description,
						categoryId,isUsed,price,telephones);
				if(result.connectionFailed())
					success=false;
				else
				{
					if(result.getFlag()==ServerAccess.ERROR_CODE_done)
					{
						try
						{
							
						}
						catch(Exception ex){}
					}
				}
				if(callback!=null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	/**
	 * user to invoke the DataRequestCallback on the main thread
	 */
	private void invokeCallback(final DataRequestCallback callback,
			final boolean success, final ServerResult data) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (callback == null)
					return;
				callback.onDataReady(data, success);
			}
		});
	}
}
