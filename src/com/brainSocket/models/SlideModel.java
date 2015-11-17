package com.brainSocket.models;

import org.json.JSONObject;

public class SlideModel {
	private int id;
	private String name;
	private int categoryId;
	private String photo_path;
	private String alt;
	private String url;
	
	public SlideModel(JSONObject ob)
	{
		if(ob==null)
			return ;
		try
		{
		this.id=ob.getInt("id");
		}
		catch(Exception ex){}
		
		try
		{
			this.name=ob.getString("name");
		}
		catch(Exception ex){}
		
		try
		{
			this.categoryId=ob.getInt("category_id");
		}
		catch(Exception ex){}
		
		try
		{
			this.photo_path=ob.getString("photo_path");
		}
		catch(Exception ex){}
		
		try
		{
			this.alt=ob.getString("alt");
		}
		catch(Exception ex){}
		
		try
		{
			this.url=ob.getString("url");
		}
		catch(Exception ex){}
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
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getPhoto_path() {
		return photo_path;
	}
	public void setPhoto_path(String photo_path) {
		this.photo_path = photo_path;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public JSONObject getJsonObject()
	{
		JSONObject ob=new JSONObject();
		try{ob.put("id", id);}catch(Exception ex){}
		try{ob.put("name", name);}catch(Exception ex){}
		try{ob.put("category_id", categoryId);}catch(Exception ex){}
		try{ob.put("photo_path", photo_path);}catch(Exception ex){}
		try{ob.put("url", url);}catch(Exception ex){}
		return ob;
	}
}
