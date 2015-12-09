package com.brainSocket.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class AppUser /*implements Parcelable */{	
	
	private List<AdvertiseModel> ads=new ArrayList<AdvertiseModel>();
	
	public enum GENDER {MALE, FEMALE} ;
	
	public static final String DEFAULT_AVATAR=  "https://s3-us-west-2.amazonaws.com/almuajaha/profile/avatar01.png" ;
	

	String phoneNum = null;
	int id = 0;
	String name = null;
	String countryId ;
	String gcmId ;
	String picture ;
	String accessToken ;
	GENDER gender ;
	Long date ;
	String email;
	int isVerified=0;
	String password;
	String version;
	long facebookId;
	float rate=0;
	String description;
	String address;
	
	 
		

	public AppUser(JSONObject json)
	{
		if(json == null) {
			return;
		}
		
		try {
			if(json.has("id")) 
				id = json.getInt("id");
		} 
		catch (Exception e) {}
		try {
			if(json.has("country_id")) 
				countryId = json.getString("country_id");
		} 
		catch (Exception e) {}
		try {
			if(json.has("user_name")) 
				name = json.getString("user_name");
		} 
		catch (Exception e) {}
		try {
			if(json.has("email")) 
				email = json.getString("email");
		} 
		catch (Exception e) {}
		try {
			if(json.has("mobile_number")) 
				phoneNum = json.getString("mobile_number");
		} 
		catch (Exception e) {}
		try {
			if(json.has("is_verified"))
			{
				isVerified=json.getInt("is_verified");
			}
		} 
		catch (Exception e) {}
		try {
			if(json.has("password")) 
				password = json.getString("password");
		} 
		catch (Exception e) {}
		try {
			if(json.has("version")) 
				version = json.getString("version");
		} 
		catch (Exception e) {}
		try {
			if(json.has("gender")) 
				gender = (json.getInt("isMale") == 1)?GENDER.MALE:GENDER.FEMALE;
		}catch (Exception e) {gender = GENDER.MALE;}
		try {
			if(json.has("photo_path")) 
				picture = json.getString("photo_path");
			if(picture == null || picture.equals("")) {
				//picture = DEFAULT_AVATAR;
			}
		}catch (Exception e) {}
		try {
			if(json.has("facebook_id")) 
				facebookId = json.getLong("facebook_id");
		} 
		catch (Exception e) {}
		try {
			if(json.has("gcm_id"))
				gcmId = json.getString("gcm_id");
		}catch (Exception e) {}
		try {
			if(json.has("access_token"))
				accessToken = json.getString("access_token");
		}catch (Exception e) {}
		try {
			if(json.has("rate"))
				rate = json.getLong("rate");
		}catch (Exception e) {}
		try {
			if(json.has("description"))
				description = json.getString("description");
		}catch (Exception e) {}
		try {
			if(json.has("address"))
				address = json.getString("address");
		}catch (Exception e) {}
		
	}
	
	/**
	 * Returns the {@link JSONObject} containing the user 
	 * details, just like the structure received from the API
	 * @return
	 */
	public JSONObject getJsonObject()
	{
		JSONObject json = new JSONObject();
		try {json.put("id", id);} catch (Exception e) {}
		try {json.put("country_id", countryId);} catch (Exception e) {}
		try {json.put("user_name", name);} catch (Exception e) {}
		try {json.put("email", email);} catch (Exception e) {}
		try {json.put("mobile_number", phoneNum);} catch (Exception e) {}
		try {json.put("is_verified", isVerified);} catch (Exception e) {}
		try {json.put("password", password);} catch (Exception e) {}
		try {json.put("version", version);} catch (Exception e) {}
		try {json.put("gender", (gender==GENDER.MALE)?1:2);} catch (Exception e) {}
		try {json.put("photo_path", picture);} catch (Exception e) {}
		try {json.put("facebook_id", facebookId);} catch (Exception e) {}
		try {json.put("gcm_id", gcmId);} catch (Exception e) {}
		try {json.put("access_token", accessToken);} catch (Exception e) {}
		try {json.put("rate", rate);} catch (Exception e) {}
		try {json.put("description", description);} catch (Exception e) {}
		try {json.put("address", address);} catch (Exception e) {}
		//try {json.put("created_at", date);} catch (Exception e) {}
		return json;
	}
	
	/**
	 * Returns a string formatted {@link JSONObject} of the user object
	 * @return
	 */
	public String getJsonString()
	{
		JSONObject json = getJsonObject();
		return json.toString();
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getGcmId() {
		return gcmId;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public GENDER getGender() {
		return gender;
	}

	public void setGender(GENDER gender) {
		this.gender = gender;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isVerified() {
		if(isVerified>0)
			return true;
		return false;
	}

	public void setVerified(int isVerified) {
		this.isVerified = isVerified;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public long getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(long facebookId) {
		this.facebookId = facebookId;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	

	
//	public Drawable getPlaceHolderDrawable(){
//		
//		ColorGenerator generator = ColorGenerator.SABEEN; // or use DEFAULT
//		int color2 = generator.getColor(name);
//		TextDrawable drawable = TextDrawable.builder()
//                .beginConfig()
//                    .textColor(Color.WHITE)
//                    .useFont(Typeface.SERIF)
////                    .bold()
////                    .width(150)
////                    .height(150)
////                    .toUpperCase()
//                .endConfig()
//                .buildRound("Me", color2);
//		return drawable ;
//		
//		
////		ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
////		int color2 = generator.getColor(name);	
////		TextDrawable drawable = TextDrawable.builder().buildRect("me", color2);
////		return drawable ;
//	}
	

/*	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) 	{		
		parcel.writeString(id);
		parcel.writeString(name);
		parcel.writeString(picture);
		parcel.writeString(fbId);
		parcel.writeString(phoneNum);
		parcel.writeInt(isAppUser);
	}
	
	public AppUser(Parcel parcel) 	{	// order does matter here
		id = parcel.readString();
		name = parcel.readString();
		picture = parcel.readString();
		fbId = parcel.readString();
		phoneNum = parcel.readString();
		isAppUser= parcel.readInt();

	}*/
	

	/*public static final Parcelable.Creator<AppUser> CREATOR = new Parcelable.Creator<AppUser>()
    {
		public AppUser createFromParcel(Parcel in)
		{
			return new AppUser(in);
		}

		public AppUser[] newArray(int size)
		{
			return new AppUser[size];
		}
    };*/
        
}
