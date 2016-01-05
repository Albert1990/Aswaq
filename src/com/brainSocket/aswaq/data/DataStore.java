package com.brainSocket.aswaq.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.models.AdvertiseModel;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.models.CategoryModel;
import com.brainSocket.aswaq.models.PageModel;
import com.brainSocket.aswaq.models.SlideModel;

import android.graphics.Bitmap;
import android.os.Handler;

public class DataStore {
	private ServerAccess serverHandler = null;
	private static Handler handler = null;
	private static DataStore instance;
	private DataCacheProvider cacheProvider = null;
	private AppUser me = null;
	private List<CategoryModel> subCategoriesPairs=null;
	private HashMap<Integer, PageModel> pages=null;
	private final int TIMER_TICK=15*60*1000; //15 mins

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
			loadLocalData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadLocalData()
	{
		me=cacheProvider.getMe();
		subCategoriesPairs=DataCacheProvider.getInstance().getStoredCategoriesPairs();
		pages=DataCacheProvider.getInstance().getStoredPages();
	}
	
	public void startScheduledUpdates() {
		try {
			handler.postDelayed(runnableUpdate, TIMER_TICK);
		}catch (Exception e) {}
	}
	
	public void stopScheduledUpdates() {
		try {
			handler.removeCallbacks(runnableUpdate);
		}catch (Exception e) {}
	}
	
	private Runnable runnableUpdate=new Runnable() {
		
		@Override
		public void run() {
			attemptLoadAllPages();
			handler.postDelayed(runnableUpdate, TIMER_TICK);
		}
	};
	
	private void attemptLoadAllPages()
	{
		new Thread(new Runnable() {
		
		@Override
		public void run() {
			boolean success=true;
			ServerResult result=serverHandler.getAllPages();
			if(result.connectionFailed())
				success=false;
			else
			{
				try
				{
					if(result.getFlag()==ServerAccess.ERROR_CODE_done)
					{
						pages=(HashMap<Integer, PageModel>)result.getValue("pages");
						DataCacheProvider.getInstance().storePages(pages);
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
			//invokeCallback(callback, success, result);
		}
	}).start();
	}
	
	private void loadAllPages()
	{
		boolean success=true;
		ServerResult result=serverHandler.getAllPages();
		if(result.connectionFailed())
			success=false;
		else
		{
			try
			{
				if(result.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					pages=(HashMap<Integer, PageModel>)result.getValue("pages");
					DataCacheProvider.getInstance().storePages(pages);
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public AppUser getMe()
	{
		return me;
	}
	
	public String getAccessToken()
	{
		if(me!=null)
			return me.getAccessToken();
		return null;
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
			final String password, final String facebookId,
			final String facebookAccessToken, final DataRequestCallback callback) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = true;
				ServerResult result = serverHandler.registerUser(email,
						userName, password, facebookId, facebookAccessToken);
				if (result.connectionFailed()) {
					success = false;
				} else {
					try {
						if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
							me = (AppUser) result.getPairs().get("appUser");
							//cacheProvider.storeAccessToken(me.getAccessToken());
							cacheProvider.storeMe(me);
						}
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
						me = (AppUser) result.getPairs().get("appUser");
						//cacheProvider.storeAccessToken(me.getAccessToken());
						cacheProvider.storeMe(me);
					}
				}
				invokeCallback(callback, success, result); // invoking the
															// callback
			}
		}).start();
	}

	public void attemptGetPageComponents(final int categoryId,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// cacheProvider.removePages();
				boolean success = true;
				PageModel page = null;
				ServerResult result = null;
				
				if(pages==null)
					loadAllPages();
				
				page = pages.get(categoryId);
				if(page!=null)
				{
					result = new ServerResult();
					result.setFlag(ServerAccess.ERROR_CODE_done);
					result.addPair("page", page);
				}
				
				
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptGetSubCategoriesAsPairs(
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = new ServerResult();

				try {
					if (subCategoriesPairs == null) {
						result = serverHandler.getSubCategoriesAsPairs();
						if (result.connectionFailed())
							success = false;
						else {
							// parsing categories
							if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
								try {
									subCategoriesPairs = (List<CategoryModel>) result
											.getValue("categories");
									cacheProvider
											.storeSubCategoriesPairs(subCategoriesPairs);
									result.addPair("categories", subCategoriesPairs);
								} catch (Exception ex) {
								}
							}
						}
					} else {
						result.setFlag(ServerAccess.ERROR_CODE_done);
						result.addPair("categories", subCategoriesPairs);
					}
				} catch (Exception ex) {
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attempVerifyUser(final String verificationCode,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler
						.verifyUser(verificationCode);
				if (result.connectionFailed())
					success = false;
				if(result.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					me.setVerified(1);
					cacheProvider.removeStoredMe();
					cacheProvider.storeMe(me);
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptSearchFor(final String keyword,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler.searchFor(keyword);
				if (result.connectionFailed())
					success = false;
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptAddNewAdvertise(final String description,
			final String address, final int categoryId, final int isUsed,
			final int price, final JSONArray telephones,
			final String facebookPageLink,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean success = true;
				ServerResult result = serverHandler.addNewAdvertise(
						description, address, categoryId, isUsed, price,
						telephones,facebookPageLink);
				if (result.connectionFailed())
					success = false;
				else {
					if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
						try {

						} catch (Exception ex) {
						}
					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptGetCategoryAds(final int categoryId,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler.getCategoryAds(categoryId);
				if (result.connectionFailed())
					success = false;
				else {
					if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
						try {

						} catch (Exception ex) {
							success = false;
						}
					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptGetAdvertiseDetails(final int adId,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler.getAdvertiseDetails(adId);
				if (result.connectionFailed())
					success = false;
				else {
					if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
						try {
						} catch (Exception ex) {
							success = false;
						}
					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptGetUserPage(final int userId,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler.getUserPage(userId);
				if (result.connectionFailed())
					success = false;
				else {
					if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
						try {
						} catch (Exception ex) {
							success = false;
						}
					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptFollowUser(final int userId, final boolean follow,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean success = true;
				ServerResult result = serverHandler.followUser(userId, follow);
				if (result.connectionFailed())
					success = false;
				else {
					if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptAddAdvertiseToFavourite(final int ad_id,
			final boolean add, final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler.addAdvertiseToFavourite(
						ad_id, add);
				if (result.connectionFailed())
					success = false;
				else {
					if (result.getFlag() == ServerAccess.ERROR_CODE_done) {

					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptGetClients(final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler.getClients();
				if (result.connectionFailed())
					success = false;
				else {
					try {
						if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
						}
					} catch (Exception ex) {
						success = false;
					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptSendVerificationCode(final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler.sendVerificationCode();
				if (result.connectionFailed())
					success = false;
				else {
					try {

					} catch (Exception ex) {
					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptUpdateUserProfile(final String userName,
			final String mobileNumber, final String address,
			final String description, final String userProfilePicturePath,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean success = true;
				ServerResult result = serverHandler.updateUserProfile(userName,
						mobileNumber, address, description,userProfilePicturePath);
				if (result.connectionFailed())
					success = false;
				else {

				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptGetMyFavourites(final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler.getMyFavourites();
				if (result.connectionFailed())
					success = false;
				else {
					if (result.getFlag() == ServerAccess.ERROR_CODE_done) {
						try {

						} catch (Exception ex) {
							success = false;
						}
					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptRateUser(final int userId, final String rate,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				ServerResult result = serverHandler.rateUser(userId, rate);
				if (result.connectionFailed())
					success = false;
				else {
					if (result.getFlag() == ServerAccess.ERROR_CODE_done) {

					}
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}

	public void attemptUploadAdPhotos(final int adId, final String[] photos,
			final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean success = true;
				ServerResult result = serverHandler.uploadAdvertisePhotos(adId,
						photos);
				if (result.connectionFailed())
					success = false;
				else {
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}
	
	public void attemptDownloadPhoto(final String photoPath, final DataRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				boolean success = true;
				Bitmap photo=PhotoProvider.getInstance().downloadImage(photoPath);
				ServerResult result=new ServerResult();
				
				if (result.connectionFailed())
					success = false;
				else {
					result.addPair("photo", photo);
				}
				if (callback != null)
					invokeCallback(callback, success, result);
			}
		}).start();
	}
	public void removeAllStoredData()
	{
		DataCacheProvider.getInstance().removeAllStoredData();
		me=null;
		subCategoriesPairs=null;
		pages=null;
		stopScheduledUpdates();
	}
	
	public void removePages()
	{
		DataCacheProvider.getInstance().removePages();
	}
	
	public void removeSubCategoriesPairs()
	{
		DataCacheProvider.getInstance().removeSubCategoriesPairs();
	}

}
