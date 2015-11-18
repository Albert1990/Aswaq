package com.brainSocket.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.models.AppUser;

import android.provider.Settings.Secure;


public class ServerAccess {
	public static final int MAIN_CATEGORY_ID=1;
	public static final int ERROR_CODE_done= 0;
	public static final int ERROR_CODE_user_not_authenticated= -3;
	public static final int ERROR_CODE_user_authenticated_before= -14;
	public static final int ERROR_CODE_user_exists_before=-16;
	public static final int ERROR_CODE_cant_follow_your_self=-17;
	public static final int ERROR_CODE_no_categories=-18;
	public static final int ERROR_CODE_no_slides=-19;
	public static final int ERROR_CODE_no_ads=-20;

	public static final int ERROR_CODE_user_not_exists = -101;
	public static final int ERROR_CODE_token_not_exists = -103;
	public static final int ERROR_CODE_access_token_expired = -104;
	public static final int ERROR_CODE_invalid_access_token = -106;
	public static final int ERROR_CODE_verification_message_not_exists = -109;
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
	public static final int RESPONCE_FORMAT_ERROR_CODE=-1001;
	
	

	// api
	static final int MAIN_PORT_NUM = 80 ;
	static final String BASE_SERVICE_URL = "http://192.168.1.112:"+MAIN_PORT_NUM+"/aswaq/index.php/";

	// api keys
		private static final String FLAG = "flag";

	
	private static ServerAccess serverAccess = null;
	// Request executers //
		private static HttpClient client  ;
		private static ServerAccess instance;
		
		public static ServerAccess getInstance()
		{
			if(instance==null)
				instance=new ServerAccess();
			return instance;
		}
		
	
	public ServerResult login(String email,String password) {
		ServerResult result = new ServerResult();
		AppUser me  = null ;
		try {
			// parameters
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>() ;
			jsonPairs.add(new BasicNameValuePair("email", email));
			jsonPairs.add(new BasicNameValuePair("password", password));
			
			try{
				String deviceId = Secure.getString(AswaqApp.getAppContext().getContentResolver(),Secure.ANDROID_ID);
				jsonPairs.add(new BasicNameValuePair("imei", deviceId));
			}catch(Exception e){
				e.printStackTrace();
			}
			
			// url
			String url = BASE_SERVICE_URL + "users_api/login";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if(jsonResponse.has("object")){
					if(!jsonResponse.isNull("object"))
					{
						
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
	 * @param name
	 * @param phoneNum
	 * @return
	 */
	public ServerResult registerUser(final String email,
			final String userName, 
			final String mobileNumber,
			final String password,
			final String address,
			final String description) {
		ServerResult result = new ServerResult();
		AppUser me  = null ;
		try {
			// parameters
			List<NameValuePair> jsonPairs = new ArrayList<NameValuePair>();
			jsonPairs.add(new BasicNameValuePair("email", email));
			jsonPairs.add(new BasicNameValuePair("user_name", userName));
			jsonPairs.add(new BasicNameValuePair("mobile_number", mobileNumber));
			jsonPairs.add(new BasicNameValuePair("password", password));
			jsonPairs.add(new BasicNameValuePair("address", address));
			jsonPairs.add(new BasicNameValuePair("description", description));
			jsonPairs.add(new BasicNameValuePair("version", "1.0"));
			jsonPairs.add(new BasicNameValuePair("facebook_id", "-1"));
			jsonPairs.add(new BasicNameValuePair("gender", "1"));
			
			try{
				String deviceId = Secure.getString(AswaqApp.getAppContext().getContentResolver(),Secure.ANDROID_ID);
				jsonPairs.add(new BasicNameValuePair("imei", deviceId));
			}catch(Exception e){
				e.printStackTrace();
			}
			
			// url
			String url = BASE_SERVICE_URL + "users_api/register";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if(jsonResponse.has("object")){
					if(!jsonResponse.isNull("object"))
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
	
	public ServerResult getCategories(int categoryId) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>() ;
			jsonPairs.add(new BasicNameValuePair("category_id", Integer.toString(categoryId)));
			jsonPairs.add(new BasicNameValuePair("access_token", DataCacheProvider.getInstance().getAccessToken()));
			
			// url
			String url = BASE_SERVICE_URL + "categories_api/get_page_components";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if(jsonResponse.has("object")){
					if(!jsonResponse.isNull("object"))
					{
						JSONObject ob=jsonResponse.getJSONObject("object");
						result.addPair("jsonCategories", ob.getJSONArray("categories"));
						result.addPair("jsonSlides", ob.getJSONArray("slides"));
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
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>() ;
			jsonPairs.add(new BasicNameValuePair("verification_code", verificationCode));
			jsonPairs.add(new BasicNameValuePair("access_token", DataCacheProvider.getInstance().getAccessToken()));
			
			// url
			String url = BASE_SERVICE_URL + "verification_messages_api/accept_verification_code";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response is empty
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
	
	public ServerResult searchFor(String keyword)
	{
		ServerResult result=new ServerResult();
		try
		{
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>();

			jsonPairs.add(new BasicNameValuePair("keyword", keyword));
			jsonPairs.add(new BasicNameValuePair("access_token", DataCacheProvider.getInstance().getAccessToken()));
			// url
						String url = BASE_SERVICE_URL + "users_api/search_for";
						// send request
						String response = sendPostRequest(url, jsonPairs);
						// parse response
						if (response != null && !response.equals("")) { // check if response is empty
							JSONObject jsonResponse = new JSONObject(response);
							result.setFlag(jsonResponse.getInt(FLAG));
							if(jsonResponse.has("object")){
								if(!jsonResponse.isNull("object"))
								{
									JSONObject ob=jsonResponse.getJSONObject("object");
									result.addPair("searchResults", ob);
								}
							}
						} else {
							result.setFlag(CONNECTION_ERROR_CODE);
						}
		}
		catch(Exception ex)
		{
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}
	
	public ServerResult addNewAdvertise(String description,
			int categoryId,boolean isUsed,int price,
			JSONArray telephones)
	{
		ServerResult result=new ServerResult();
		try
		{
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>();

			jsonPairs.add(new BasicNameValuePair("description", description));
			jsonPairs.add(new BasicNameValuePair("category_id", Integer.toString(categoryId)));
			jsonPairs.add(new BasicNameValuePair("price", Integer.toString(price)));
			jsonPairs.add(new BasicNameValuePair("is_used", Boolean.toString(isUsed)));
			jsonPairs.add(new BasicNameValuePair("telephones", telephones.toString()));
			jsonPairs.add(new BasicNameValuePair("access_token", DataCacheProvider.getInstance().getAccessToken()));
			jsonPairs.add(new BasicNameValuePair("pay_type","1"));
			// url
						String url = BASE_SERVICE_URL + "users_api/search_for";
						// send request
						String response = sendPostRequest(url, jsonPairs);
						// parse response
						if (response != null && !response.equals("")) { // check if response is empty
							JSONObject jsonResponse = new JSONObject(response);
							result.setFlag(jsonResponse.getInt(FLAG));
							if(jsonResponse.has("object")){
								if(!jsonResponse.isNull("object"))
								{
									JSONObject ob=jsonResponse.getJSONObject("object");
									result.addPair("searchResults", ob);
								}
							}
						} else {
							result.setFlag(CONNECTION_ERROR_CODE);
						}
		}
		catch(Exception ex)
		{
			result.setFlag(RESPONCE_FORMAT_ERROR_CODE);
		}
		return result;
	}
	
	public ServerResult getCategoryAds(int categoryId) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>() ;
			jsonPairs.add(new BasicNameValuePair("category_id", Integer.toString(categoryId)));
			jsonPairs.add(new BasicNameValuePair("access_token", DataCacheProvider.getInstance().getAccessToken()));
			
			// url
			String url = BASE_SERVICE_URL + "ads_api/get_category_ads";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if(jsonResponse.has("object")){
					if(!jsonResponse.isNull("object"))
					{
						JSONObject ob=jsonResponse.getJSONObject("object");
						result.addPair("jsonAds", ob.getJSONArray("ads"));
						result.addPair("jsonSlides", ob.getJSONArray("slides"));
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
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>() ;
			jsonPairs.add(new BasicNameValuePair("ad_id", Integer.toString(adId)));
			jsonPairs.add(new BasicNameValuePair("access_token", DataCacheProvider.getInstance().getAccessToken()));
			
			// url
			String url = BASE_SERVICE_URL + "ads_api/get_details";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if(jsonResponse.has("object")){
					if(!jsonResponse.isNull("object"))
					{
						JSONObject ob=jsonResponse.getJSONObject("object");
						result.addPair("jsonAdDetails", ob);
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
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>() ;
			jsonPairs.add(new BasicNameValuePair("user_id", Integer.toString(userId)));
			jsonPairs.add(new BasicNameValuePair("access_token", DataCacheProvider.getInstance().getAccessToken()));
			
			// url
			String url = BASE_SERVICE_URL + "users_api/get_page";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response is empty
				JSONObject jsonResponse = new JSONObject(response);
				result.setFlag(jsonResponse.getInt(FLAG));
				if(jsonResponse.has("object")){
					if(!jsonResponse.isNull("object"))
					{
						JSONObject ob=jsonResponse.getJSONObject("object");
						result.addPair("jsonUser", ob.getJSONObject("user"));
						result.addPair("jsonUserAds", ob.getJSONArray("userAds"));
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
	
	public ServerResult followUser(int userId) {
		ServerResult result = new ServerResult();
		try {
			// parameters
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>() ;
			jsonPairs.add(new BasicNameValuePair("user_id", Integer.toString(userId)));
			jsonPairs.add(new BasicNameValuePair("access_token", DataCacheProvider.getInstance().getAccessToken()));
			
			// url
			String url = BASE_SERVICE_URL + "followers_api/follow";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response is empty
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
			List<NameValuePair> jsonPairs=new ArrayList<NameValuePair>() ;
			jsonPairs.add(new BasicNameValuePair("access_token", DataCacheProvider.getInstance().getAccessToken()));
			
			// url
			String url = BASE_SERVICE_URL + "followers_api/follow";
			// send request
			String response = sendPostRequest(url, jsonPairs);
			// parse response
			if (response != null && !response.equals("")) { // check if response is empty
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
	
	private String sendPostRequest(String url, List<NameValuePair> jsonPairs) {
        client = new DefaultHttpClient();
        String result = null;
        try {
            HttpPost post = new HttpPost(url);
            //StringEntity entity = new StringEntity(jsonPairs.toString(),HTTP.UTF_8);
            //entity.setContentType("application/json");
            post.setEntity(new UrlEncodedFormEntity(jsonPairs,HTTP.UTF_8));
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
