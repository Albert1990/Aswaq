package com.brainSocket.aswaq.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class ContactModel {
	private List<PhoneNumberModel> phones;
	private String facebookPage;
	private String website;
	private String email;
	
	public ContactModel(JSONObject ob)
	{
		if(ob==null)
			return;
		try
		{
			phones=new ArrayList<PhoneNumberModel>();
			JSONArray jsonPhones=ob.getJSONArray("phones");
			if(jsonPhones!=null)
			{
				for(int i=0;i<jsonPhones.length();i++)
				{
					PhoneNumberModel phoneNumber=new PhoneNumberModel(jsonPhones.getJSONObject(i));
					phones.add(phoneNumber);
				}
			}
		}
		catch(Exception ex){ex.printStackTrace();}
		
		try
		{
			this.facebookPage=ob.getString("facebook_page");
		}
		catch(Exception ex){ex.printStackTrace();}
		
		try
		{
			this.website=ob.getString("website");
		}catch(Exception ex){ex.printStackTrace();}
		
		try
		{
			this.email=ob.getString("email");
		}
		catch(Exception ex){ex.printStackTrace();}
	}
	
	public List<PhoneNumberModel> getPhones() {
		return phones;
	}
	public void setPhones(List<PhoneNumberModel> phones) {
		this.phones = phones;
	}
	public String getFacebookPage() {
		return facebookPage;
	}
	public void setFacebookPage(String facebookPage) {
		this.facebookPage = facebookPage;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public JSONObject getJsonObject()
	{
		JSONObject ob=new JSONObject();
		try
		{
			JSONArray jsonPhones=new JSONArray();
			for(int i=0;i<phones.size();i++)
			{
				jsonPhones.put(phones.get(i).getJsonObject());
			}
			ob.put("phones", jsonPhones);
		}catch(Exception ex){ex.printStackTrace();}
		
		try
		{
			ob.put("facebook_page", facebookPage);
		}catch(Exception ex){ex.printStackTrace();}
		
		try
		{
			ob.put("website", website);
		}catch(Exception ex){ex.printStackTrace();}
		
		try
		{
			ob.put("email", email);
		}catch(Exception ex){ex.printStackTrace();}
		
		return ob;
	}
}
