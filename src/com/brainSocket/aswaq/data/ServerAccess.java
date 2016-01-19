package com.brainSocket.aswaq.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings.Secure;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.data.AndroidMultiPartEntity.ProgressListener;
import com.brainSocket.aswaq.models.AdvertiseModel;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.models.CategoryModel;
import com.brainSocket.aswaq.models.ContactModel;
import com.brainSocket.aswaq.models.PageModel;
import com.brainSocket.aswaq.models.SlideModel;

public class ServerAccess {
	public static final int MAIN_CATEGORY_ID = 1;
	public static final int ERROR_CODE_done = 0;
	public static final int ERROR_CODE_user_not_authenticated = -3;
	public static final int ERROR_CODE_user_authenticated_before = -14;
	public static final int ERROR_CODE_user_exists_before = -16;
	public static final int ERROR_CODE_cant_follow_your_self = -17;
	public static final int ERROR_CODE_no_categories = -18;
	public static final int ERROR_CODE_no_slides = -19;
	public static final int ERROR_CODE_no_ads = -20;

	public static final int ERROR_CODE_user_not_exists = -101;
	public static final int ERROR_CODE_token_not_exists = -103;
	public static final int ERROR_CODE_access_token_expired = -104;
	public static final int ERROR_CODE_invalid_access_token = -106;
	public static final int ERROR_CODE_error_in_input_params = -116;
	public static final int ERROR_CODE_user_not_verified = -120;
	public static final int ERROR_CODE_app_version_invalid = -121;
	public static final int ERROR_CODE_update_available = -122;
	public static final int ERROR_CODE_cant_complete_process = -123;
	public static final int ERROR_CODE_version_expired = -125;
	public static final int ERROR_CODE_to_user_not_exists = -126;
	public static final int ERROR_CODE_wrong_verification_code = -131;
	public static final int ERROR_CODE_verification_attempts_exceeded = -132;
	public static final int ERROR_CODE_error_in_mobile_number_format = -133;
	public static final int ERROR_CODE_follow_relation_exists_before = -134;

	public static final int ERROR_CODE_unknown_exception = -100;
	public static final int CONNECTION_ERROR_CODE = -1000;
	public static final int RESPONCE_FORMAT_ERROR_CODE = -1001;

	// api
	public static final int MAIN_PORT_NUM = 80;
	private boolean inProductionMode = true;
	public static String IMAGE_SERVICE_URL = "http://192.168.10.182:"
			+ MAIN_PORT_NUM + "/aswaq/imgs/";
	public static String BASE_SERVICE_URL = "http://192.168.10.182:"
			+ MAIN_PORT_NUM + "/aswaq/index.php/";

	// api keys
	private static final String FLAG = "flag";

	private static ServerAccess serverAccess = null;
	// Request executers //
	private static HttpClient client;
	private static ServerAccess instance;

	private ServerAccess() {
		if (inProductionMode) {
			IMAGE_SERVICE_URL = "http://104.217.253.15:" + MAIN_PORT_NUM
					+ "/aswaq/imgs/";
			BASE_SERVICE_URL = "http://104.217.253.15:" + MAIN_PORT_NUM
					+ "/aswaq/index.php/";
		} else {
			IMAGE_SERVICE_URL = "http://192.168.1.112:" + MAIN_PORT_NUM
					+ "/aswaq/imgs/";
			BASE_SERVICE_URL = "http://192.168.1.112:" + MAIN_PORT_NUM
					+ "/aswaq/index.php/";
		}
	}

	public static ServerAccess getInstance() {
		if (instance == null)
			instance = new ServerAccess();
		return instance;
	}

	public ServerResult login(String mobileNumber) {
		ServerResult result = new ServerResult();
		AppUser me = null;
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("mobile_number", mobileNumber));

			try {
				String deviceId = Secure.getString(AswaqApp.getAppContext()
						.getContentResolver(), Secure.ANDROID_ID);
				jsonPairs.add(new BasicNameValuePair("imei", deviceId));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// url
			String url = BASE_SERVICE_URL + "users_api/login";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {

						me = new AppUser(jsonResponse.getJSONObject("object")
								.getJSONObject("user"));
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		result.addPair("appUser", me);

		return result;
	}

	/**
	 * register a new user with UserName and phoneNumber
	 * 
	 * @param name
	 * @param phoneNum
	 * @return
	 */
	public ServerResult registerUser(final String mobileNumber, final String userName,
			final String facebookId,
			final String facebookAccessToken) {
		ServerResult result = new ServerResult();
		AppUser me = null;
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("mobile_number", mobileNumber));
			jsonPairs.add(new BasicNameValuePair("user_name", userName));
			jsonPairs.add(new BasicNameValuePair("version", "1.0"));
			jsonPairs.add(new BasicNameValuePair("facebook_id", facebookId));
			jsonPairs.add(new BasicNameValuePair("facebook_access_token",
					facebookAccessToken));

			try {
				String deviceId = Secure.getString(AswaqApp.getAppContext()
						.getContentResolver(), Secure.ANDROID_ID);
				jsonPairs.add(new BasicNameValuePair("imei", deviceId));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// url
			String url = BASE_SERVICE_URL + "users_api/register";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object"))
						me = new AppUser(jsonResponse.getJSONObject("object"));
				}

			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		result.addPair("appUser", me);

		return result;
	}

	public ServerResult getPageComponents(int categoryId) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("category_id", Integer
					.toString(categoryId)));
			// jsonPairs.add(new BasicNameValuePair("access_token",
			// DataCacheProvider.getInstance().getAccessToken()));

			// url
			String url = BASE_SERVICE_URL
					+ "categories_api/get_page_components";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						JSONObject ob = jsonResponse.getJSONObject("object");

						JSONArray jsonCategories = ob
								.getJSONArray("categories");
						List<CategoryModel> categories = new ArrayList<CategoryModel>();
						for (int i = 0; i < jsonCategories.length(); i++) {
							categories.add(new CategoryModel(
									(JSONObject) jsonCategories.get(i)));
						}
						// result.addPair("categories", categories);

						JSONArray jsonSlides = ob.getJSONArray("slides");
						List<SlideModel> slides = new ArrayList<SlideModel>();
						for (int i = 0; i < jsonSlides.length(); i++) {
							slides.add(new SlideModel((JSONObject) jsonSlides
									.get(i)));
						}
						// result.addPair("jsonSlides", slides);

						PageModel page = new PageModel(categoryId, categories,
								slides);
						result.addPair("page", page);
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}
	
	public ServerResult getAllPages() {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();

			// url
			String url = BASE_SERVICE_URL
					+ "categories_api/get_all_pages";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						JSONArray jsonPages = jsonResponse.getJSONArray("object");
						HashMap<Integer, PageModel> pages=new HashMap<Integer, PageModel>();
						for(int k=0;k<jsonPages.length();k++)
						{
							JSONObject jsonPage=jsonPages.getJSONObject(k);
							JSONArray jsonSubCategories = jsonPage
									.getJSONArray("sub_categories");
							List<CategoryModel> categories = new ArrayList<CategoryModel>();
							for (int i = 0; i < jsonSubCategories.length(); i++) {
								categories.add(new CategoryModel(
										(JSONObject) jsonSubCategories.get(i)));
							}

							JSONArray jsonSlides = jsonPage.getJSONArray("slides");
							List<SlideModel> slides = new ArrayList<SlideModel>();
							for (int i = 0; i < jsonSlides.length(); i++) {
								slides.add(new SlideModel((JSONObject) jsonSlides
										.get(i)));
							}
							int parentCategoryId=jsonPage.getInt("category_id");
							PageModel page = new PageModel(parentCategoryId, categories,
									slides);
							pages.put(parentCategoryId, page);
						}
						result.addPair("pages", pages);
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult getSubCategoriesAsPairs() {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));

			// url
			String url = BASE_SERVICE_URL
					+ "categories_api/get_sub_categories_as_pairs";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						JSONArray jsonCategories = jsonResponse
								.getJSONArray("object");
						List<CategoryModel> categories = new ArrayList<CategoryModel>();
						for (int i = 0; i < jsonCategories.length(); i++) {
							categories.add(new CategoryModel(
									(JSONObject) jsonCategories.get(i)));
						}

						result.addPair("categories", categories);
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult verifyUser(String verificationCode) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("verification_code",
					verificationCode));
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));

			// url
			String url = BASE_SERVICE_URL
					+ "verification_messages_api/accept_verification_code";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult searchFor(String keyword) {
		ServerResult result = new ServerResult();
		try {
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();

			jsonPairs.add(new BasicNameValuePair("keyword", keyword));
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));
			// url
			String url = BASE_SERVICE_URL + "users_api/search_for";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getJSONObject("attach"));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						JSONObject ob = jsonResponse.getJSONObject("object");

						JSONArray jsonAds = ob.getJSONArray("ads");
						List<AdvertiseModel> ads = new ArrayList<AdvertiseModel>();
						for (int i = 0; i < jsonAds.length(); i++) {
							ads.add(new AdvertiseModel((JSONObject) jsonAds
									.get(i)));
						}

						JSONArray jsonUsers = ob.getJSONArray("users");
						List<AppUser> users = new ArrayList<AppUser>();
						for (int i = 0; i < jsonUsers.length(); i++) {
							users.add(new AppUser((JSONObject) jsonUsers.get(i)));
						}
						result.addPair("ads", ads);
						result.addPair("users", users);
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception ex) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult addNewAdvertise(String description, String address,
			int categoryId, int isUsed, int price, JSONArray telephones,
			String facebookPageLink) {
		ServerResult result = new ServerResult();
		try {
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();

			jsonPairs.add(new BasicNameValuePair("description", description));
			jsonPairs.add(new BasicNameValuePair("address", address));
			jsonPairs.add(new BasicNameValuePair("category_id", Integer
					.toString(categoryId)));
			jsonPairs.add(new BasicNameValuePair("price", Integer
					.toString(price)));
			jsonPairs.add(new BasicNameValuePair("is_used", Integer
					.toString(isUsed)));
			jsonPairs.add(new BasicNameValuePair("telephones", telephones
					.toString()));
			jsonPairs.add(new BasicNameValuePair("facebook_page", facebookPageLink));
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));
			jsonPairs.add(new BasicNameValuePair("pay_type", "1"));
			// url
			String url = BASE_SERVICE_URL + "ads_api/add";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						// TODO read response
						// JSONObject ob=jsonResponse.getJSONObject("object");
						result.addPair("adId", jsonResponse.getInt("object"));
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception ex) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult getCategoryAds(int categoryId) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("category_id", Integer
					.toString(categoryId)));
			// jsonPairs.add(new BasicNameValuePair("access_token",
			// DataCacheProvider.getInstance().getAccessToken()));

			// url
			String url = BASE_SERVICE_URL + "ads_api/get_category_ads";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						JSONObject ob = jsonResponse.getJSONObject("object");

						JSONArray jsonAds = ob.getJSONArray("ads");
						List<AdvertiseModel> ads = new ArrayList<AdvertiseModel>();
						for (int i = 0; i < jsonAds.length(); i++) {
							ads.add(new AdvertiseModel((JSONObject) jsonAds
									.get(i)));
						}
						result.addPair("ads", ads);

						JSONArray jsonSlides = ob.getJSONArray("slides");
						List<SlideModel> slides = new ArrayList<SlideModel>();
						for (int i = 0; i < jsonSlides.length(); i++) {
							slides.add(new SlideModel((JSONObject) jsonSlides
									.get(i)));
						}
						result.addPair("slides", slides);
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult getMyFavourites() {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));

			// url
			String url = BASE_SERVICE_URL + "favourites_api/get_my_favourites";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						JSONArray jsonAds = jsonResponse.getJSONArray("object");
						List<AdvertiseModel> ads = new ArrayList<AdvertiseModel>();
						for (int i = 0; i < jsonAds.length(); i++) {
							ads.add(new AdvertiseModel((JSONObject) jsonAds
									.get(i)));
						}
						result.addPair("ads", ads);
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult getAdvertiseDetails(int adId) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("ad_id", Integer
					.toString(adId)));
			if (DataStore.getInstance().getMe() != null)
				jsonPairs.add(new BasicNameValuePair("access_token",
						DataStore.getInstance().getAccessToken()));

			// url
			String url = BASE_SERVICE_URL + "ads_api/get_ad_details";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						JSONObject ob = jsonResponse.getJSONObject("object");
						if (ob.has("ad")) {
							if (!ob.isNull("ad")) {
								JSONObject jsonAdDetails = ob
										.getJSONObject("ad");
								result.addPair("adDetails", new AdvertiseModel(
										jsonAdDetails));
							}
						}
						if (ob.has("is_favourite")) {
							if (!ob.isNull("is_favourite"))
								result.addPair("isFavourite",
										ob.getBoolean("is_favourite"));
						}
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult getUserPage(int userId) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("user_id", Integer
					.toString(userId)));
			String accessToken = DataStore.getInstance()
					.getAccessToken();
			if (accessToken != null)
				jsonPairs.add(new BasicNameValuePair("access_token",
						accessToken));

			// url
			String url = BASE_SERVICE_URL + "users_api/get_user_page";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						JSONObject ob = jsonResponse.getJSONObject("object");
						JSONObject jsonUser = ob.getJSONObject("user");
						result.addPair("user", new AppUser(jsonUser));

						result.addPair("followersCount",
								ob.getInt("followers_count"));
						List<AdvertiseModel> ads = new ArrayList<AdvertiseModel>();
						JSONArray jsonUserAds = ob.getJSONArray("user_ads");
						for (int i = 0; i < jsonUserAds.length(); i++) {
							ads.add(new AdvertiseModel(jsonUserAds
									.getJSONObject(i)));
						}
						result.addPair("userAds", ads);
						result.addPair("isFollowedByMe",
								ob.getInt("is_followed_by_me"));
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult followUser(int userId, boolean follow) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("user_id", Integer
					.toString(userId)));
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));

			// url

			String url = BASE_SERVICE_URL + "followers_api/follow";
			if (follow == false)
				url = BASE_SERVICE_URL + "followers_api/unfollow";

			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if(jsonResponse.has("object"))
				{
					if(!jsonResponse.isNull("object"))
					{
						result.addPair("newFollowersCount", jsonResponse.getString("object"));
					}
				}

			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult addAdvertiseToFavourite(int adId, boolean add) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("ad_id", Integer
					.toString(adId)));
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));

			// url

			String url = BASE_SERVICE_URL
					+ "favourites_api/add_advertise_to_favourites";
			if (add == false)
				url = BASE_SERVICE_URL
						+ "favourites_api/remove_advertise_from_favourites";

			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));

			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult getClients() {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));

			// url
			String url = BASE_SERVICE_URL + "followers_api/get_clients";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					List<AppUser> clients = new ArrayList<AppUser>();
					JSONArray jsonClients = jsonResponse.getJSONArray("object");
					for (int i = 0; i < jsonClients.length(); i++) {
						clients.add(new AppUser((JSONObject) jsonClients.get(i)));
					}
					result.addPair("clients", clients);
				}

			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception e) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult sendVerificationCode() {
		ServerResult result = new ServerResult();
		try {
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));
			// url
			String url = BASE_SERVICE_URL
					+ "verification_messages_api/send_verification_code";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));

			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception ex) {
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}

	public ServerResult rateUser(int userId, String rate) {
		ServerResult result = new ServerResult();
		try {
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("access_token",
					DataStore.getInstance().getAccessToken()));
			jsonPairs.add(new BasicNameValuePair("user_id", Integer
					.toString(userId)));
			jsonPairs.add(new BasicNameValuePair("rate", rate));
			// url
			String url = BASE_SERVICE_URL + "users_api/rate_user";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						result.addPair("newRate", Double.toString(jsonResponse
								.getDouble("object")));
					}
				}

			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public ServerResult uploadAdvertisePhotos(int ad_id, String[] imgPath) {
		String url = BASE_SERVICE_URL + "ads_api/upload_ad_photos";
		String responseString = null;
		ServerResult res = new ServerResult();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
					new ProgressListener() {

						@Override
						public void transferred(long num) {
							// publishProgress((int) ((num / (float) totalSize)
							// * 100));
						}
					});

			if (imgPath != null) {
				File sourceFile;
				for (int i = 0; i < imgPath.length; i++) {
					if (imgPath[i] != null) {
						sourceFile = new File(imgPath[i]);
						entity.addPart("ad_image" + i, new FileBody(sourceFile));
					}
				}
			}

			// Adding file data to http body
			entity.addPart("ad_id", new StringBody(Integer.toString(ad_id))); // Extra
																				// parameters
																				// if
																				// you
																				// want
																				// to
																				// pass
																				// to
																				// server
			String access_token = DataStore.getInstance()
					.getAccessToken();
			entity.addPart("access_token", new StringBody(access_token));

			// totalSize = entity.getContentLength();
			httppost.setEntity(entity);

			// Making server call
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				try {
					// Server response
					responseString = EntityUtils.toString(r_entity);
					if (responseString != null && !responseString.equals("")) { // check
																				// if
																				// response
																				// is
																				// empty
						JSONObject jsonResponse = new JSONObject(responseString);
						res.setFlag(jsonResponse.getInt(FLAG));

					} else {
						res.setFlag(CONNECTION_ERROR_CODE);
					}
				} catch (Exception ex) {
					res.setFlag(CONNECTION_ERROR_CODE);
					ex.printStackTrace();
				}
			} else {
				responseString = "Error occurred! Http Status Code: "
						+ statusCode;
			}

		} catch (ClientProtocolException e) {
			res.setFlag(ServerAccess.ERROR_CODE_unknown_exception);
		} catch (IOException e) {
			res.setFlag(ServerAccess.ERROR_CODE_unknown_exception);
		}
		return res;
	}
	
	public ServerResult updateUserProfile(String userName,
			String address, String description, String imgPath,String facebookPage) {
		String url = BASE_SERVICE_URL + "users_api/edit_user_profile";
		String responseString = null;
		ServerResult res = new ServerResult();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
					new ProgressListener() {

						@Override
						public void transferred(long num) {
							// publishProgress((int) ((num / (float) totalSize)
							// * 100));
						}
					});

			if (imgPath != "") {
				File sourceFile;
				sourceFile = new File(imgPath);
				entity.addPart("user_photo", new FileBody(sourceFile));
			}

			// Adding file data to http body
			entity.addPart("user_name", new StringBody(userName,Charset.forName(HTTP.UTF_8)));
			entity.addPart("address", new StringBody(address,Charset.forName(HTTP.UTF_8)));
			entity.addPart("description", new StringBody(description,Charset.forName(HTTP.UTF_8)));
			String access_token = DataStore.getInstance()
					.getAccessToken();
			entity.addPart("access_token", new StringBody(access_token));
			entity.addPart("facebook_page", new StringBody(facebookPage,Charset.forName(HTTP.UTF_8)));

			// totalSize = entity.getContentLength();
//			httppost.setHeader(HTTP.CONTENT_TYPE,
//                    "application/x-www-form-urlencoded;charset=UTF-8");
			httppost.setEntity(entity);

			// Making server call
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				try {
					// Server response
					responseString = EntityUtils.toString(r_entity);
					if (responseString != null && !responseString.equals("")) { // check
																				// if
																				// response
																				// is
																				// empty
						JSONObject jsonResponse = new JSONObject(responseString);
						res.setFlag(jsonResponse.getInt(FLAG));
						if(jsonResponse.has("object"))
						{
							if(!jsonResponse.isNull("object"))
							{
								JSONObject meJson = jsonResponse
										.getJSONObject("object");
								AppUser me = new AppUser(meJson);
								res.addPair("me", me);
							}
						}

					} else {
						res.setFlag(CONNECTION_ERROR_CODE);
					}
				} catch (Exception ex) {
					res.setFlag(CONNECTION_ERROR_CODE);
					ex.printStackTrace();
				}
			} else {
				responseString = "Error occurred! Http Status Code: "
						+ statusCode;
			}

		} catch (ClientProtocolException e) {
			res.setFlag(ServerAccess.ERROR_CODE_unknown_exception);
		} catch (IOException e) {
			res.setFlag(ServerAccess.ERROR_CODE_unknown_exception);
		}
		return res;
	}
	
	public ServerResult sendChangePasswordRequest(String email) {
		ServerResult result = new ServerResult();
		try {
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("email", email));
			// url
			String url = BASE_SERVICE_URL + "users_api/send_change_password_request";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
	
	public ServerResult getContactInfo() {
		ServerResult result = new ServerResult();
		try {
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			// url
			String url = BASE_SERVICE_URL + "contact_api/get_contact_info";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response
															// is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if (jsonResponse.has("object")) {
					if (!jsonResponse.isNull("object")) {
						JSONObject jsonContactInfo = jsonResponse
								.getJSONObject("object");
						ContactModel contactModel = new ContactModel(jsonContactInfo);
						result.addPair("contactModel", contactModel);
					}
				}
			} else {
				result.setFlag(CONNECTION_ERROR_CODE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	private String sendPostRequest(String url, List<NameValuePair> jsonPairs) {
		client = new DefaultHttpClient();
		String result = null;
		try {
			HttpPost post = new HttpPost(url);
			// StringEntity entity = new
			// StringEntity(jsonPairs.toString(),HTTP.UTF_8);
			// entity.setContentType("application/json");
			post.setEntity(new UrlEncodedFormEntity(jsonPairs, HTTP.UTF_8));
			HttpParams params=new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(params, 5000);
			post.setParams(params);
			
			HttpResponse response = client.execute(post);

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));
				StringBuilder str = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					str.append(line + "\n");
				}
				reader.close();
				result = str.toString().trim();
			} catch (Exception ex) {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

}
