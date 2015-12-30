package com.brainSocket.aswaq.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdvertiseModel {
	private int id;
	private int userId;
	private int categoryId;
	private String description;
	private String address;
	private int price;
	private int isUsed;
	private String facebookPage;
	private int isPinned;
	private List<String> telephones=new ArrayList<String>();
	private AppUser user;
	private CategoryModel category;
	private List<SlideModel> images=new ArrayList<SlideModel>();
	private String date;

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
			if(ob.has("address"))
				this.address=ob.getString("address");
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
				this.isUsed=ob.getInt("is_used");
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
			if(ob.has("is_pinned"))
				this.isPinned=ob.getInt("is_pinned");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("telephones"))
			{
				JSONArray tels=ob.getJSONArray("telephones");
				for(int i=0;i<tels.length();i++)
					telephones.add(tels.getString(i));
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		try
		{
			if(ob.has("user"))
			{
				this.user=new AppUser((JSONObject)ob.get("user"));
			}
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("category")){
				this.category=new CategoryModel(ob.getJSONObject("category"));
			}
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("images")){
				JSONArray jsonImages=ob.getJSONArray("images");
				for(int i=0;i<jsonImages.length();i++)
				{
					images.add(new SlideModel(jsonImages.getJSONObject(i)));
				}
			}
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("created_at"))
				this.date=ob.getString("created_at");
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
	public String getAddress()
	{
		return address;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setAddress(String address)
	{
		this.address=address;
	}
	public int getPrice() {
		return price;
	}
	public String getPriceWithUnit()
	{
		return Integer.toString(price)+" ل.س";
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int isUsed() {
		return isUsed;
	}
	public void setUsed(int isUsed) {
		this.isUsed = isUsed;
	}
	public String getFacebookPage() {
		return facebookPage;
	}
	public void setFacebookPage(String facebookPage) {
		this.facebookPage = facebookPage;
	}
	public int IsPinned() {
		return isPinned;
	}
	public void setIsPinned(int isPinned) {
		this.isPinned = isPinned;
	}
	public List<String> getTelephones() {
		return telephones;
	}
	
	
	
	public AppUser getUser() {
		return user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}
	
	

	public CategoryModel getCategory() {
		return category;
	}

	public void setCategory(CategoryModel category) {
		this.category = category;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public List<SlideModel> getImages() {
		return images;
	}

	public void setImages(List<SlideModel> images) {
		this.images = images;
	}
	
	public JSONObject getJsonObject()
	{
		JSONObject ob=new JSONObject();
		try{ob.put("id", id);}catch(Exception ex){}
		try{ob.put("user_id", userId);}catch(Exception ex){}
		try{ob.put("category_id", categoryId);}catch(Exception ex){}
		try{ob.put("description", description);}catch(Exception ex){}
		try{ob.put("address",address);}catch(Exception ex){}
		try{ob.put("price", price);}catch(Exception ex){}
		try{ob.put("is_used", isUsed);}catch(Exception ex){}
		try{ob.put("facebook_page", facebookPage);}catch(Exception ex){}
		try{ob.put("is_pinned", isPinned);}catch(Exception ex){}
		try{
			JSONArray tels=new JSONArray();
			for(int i=0;i<telephones.size();i++)
				tels.put(telephones.get(i));
			ob.put("telephones", tels);
			}catch(Exception ex){}
		try{ob.put("user", user.getJsonObject());}catch(Exception ex){}
		try{ob.put("category", category.getJsonObject());}catch(Exception ex){}
		try
		{
			JSONArray jsonImages=new JSONArray();
			for(int i=0;i<images.size();i++)
			{
				jsonImages.put(images.get(i).getJsonObject());
			}
			ob.put("images", jsonImages);
		}catch(Exception ex){}
		try{ob.put("created_at", date);}catch(Exception ex){}
		
		return ob;
	}

	
	
	
}
