package com.brainSocket.aswaq.models;

import org.json.JSONObject;

public class CategoryModel {
	private int id;
	private String name;
	private String photoPath;
	private int parentId;
	
	
	public CategoryModel(JSONObject ob)
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
		this.photoPath=ob.getString("photo_path");
		}
		catch(Exception ex){}
		
		try
		{
		this.parentId=ob.getInt("parent_id");
		}
		catch(Exception ex){}
	}
	public JSONObject getJsonObject()
	{
		JSONObject ob=new JSONObject();
		try{ob.put("id", id);}catch(Exception ex){}
		try{ob.put("name", name);}catch(Exception ex){}
		try{ob.put("photo_path", photoPath);}catch(Exception ex){}
		try{ob.put("parent_id", parentId);}catch(Exception ex){}
		return ob;
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
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	
}
