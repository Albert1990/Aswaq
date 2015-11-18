package com.brainSocket.models;

import org.json.JSONObject;

public class AdvertiseModel {
	private int id;
	private int userId;
	private int categoryId;
	private String description;
	private int price;
	private boolean isUsed;
	private String facebookPage;
	private int payType;
	private String telephones;
	
	public AdvertiseModel(JSONObject ob)
	{
		if(ob==null)
			return;
		
		try
		{
			if(ob.has("id"))
				this.id=ob.getInt("id");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("user_id"))
				this.userId=ob.getInt("user_id");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("category_id"))
				this.categoryId=ob.getInt("category_id");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("description"))
				this.description=ob.getString("description");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("price"))
				this.price=ob.getInt("price");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("is_used"))
				this.isUsed=ob.getBoolean("isUsed");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("facebook_page"))
				this.facebookPage=ob.getString("facebook_page");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("pay_type"))
				this.payType=ob.getInt("pay_type");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("telephones"))
				this.telephones=ob.getString("telephones");
		}
		catch(Exception ex){}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	public String getFacebookPage() {
		return facebookPage;
	}
	public void setFacebookPage(String facebookPage) {
		this.facebookPage = facebookPage;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public String getTelephones() {
		return telephones;
	}
	public void setTelephones(String telephones) {
		this.telephones = telephones;
	}
	
	public JSONObject getJsonObject()
	{
		JSONObject ob=new JSONObject();
		try{ob.put("id", id);}catch(Exception ex){}
		try{ob.put("user_id", userId);}catch(Exception ex){}
		try{ob.put("category_id", categoryId);}catch(Exception ex){}
		try{ob.put("description", description);}catch(Exception ex){}
		try{ob.put("price", price);}catch(Exception ex){}
		try{ob.put("is_used", isUsed);}catch(Exception ex){}
		try{ob.put("facebook_page", facebookPage);}catch(Exception ex){}
		try{ob.put("pay_type", payType);}catch(Exception ex){}
		try{ob.put("telephones", telephones);}catch(Exception ex){}
		return ob;
	}
}
