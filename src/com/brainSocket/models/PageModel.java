package com.brainSocket.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PageModel {
	private int categoryId;
	private List<CategoryModel> categories;
	private List<SlideModel> slides;
	
	
	public PageModel(int categoryId,List<CategoryModel> categories,List<SlideModel> slides)
	{
		this.categoryId=categoryId;
		this.categories=categories;
		this.slides=slides;
	}
	public PageModel(JSONObject ob)
	{
		if(ob==null)
			return;
		try
		{
			if(ob.has("categoryId"))
				this.categoryId=ob.getInt("categoryId");
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("categories"))
			{
				this.categories=new ArrayList<CategoryModel>();
				JSONArray cats=ob.getJSONArray("categories");
				for(int i=0;i<cats.length();i++)
				{
					JSONObject categoryJsonObject=(JSONObject)cats.get(i);
					categories.add(new CategoryModel(categoryJsonObject));
				}
			}
		}
		catch(Exception ex){}
		
		try
		{
			if(ob.has("slides"))
			{
				slides=new ArrayList<SlideModel>();
				JSONArray jsonSlides=ob.getJSONArray("slides");
				for(int i=0;i<jsonSlides.length();i++)
				{
					JSONObject slideJsonObject=jsonSlides.getJSONObject(i);
					slides.add(new SlideModel(slideJsonObject));
				}
			}
		}
		catch(Exception ex){}
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public List<CategoryModel> getCategories() {
		return categories;
	}
	public void setCategories(List<CategoryModel> subCategories) {
		this.categories = subCategories;
	}
	public List<SlideModel> getSlides() {
		return slides;
	}
	public void setSlides(List<SlideModel> slides) {
		this.slides = slides;
	}
	
	public JSONObject getJsonObject()
	{
		JSONObject ob=new JSONObject();
		try
		{
			ob.put("categoryId", categoryId);
		}
		catch(Exception ex){}
		
		try
		{
			JSONArray jsonCategories=new JSONArray();
			for(int i=0;i<categories.size();i++)
				jsonCategories.put(categories.get(i).getJsonObject());
			ob.put("categories", jsonCategories);
		}
		catch(Exception ex){}
		
		try
		{
			JSONArray jsonSlides=new JSONArray();
			for(int i=0;i<slides.size();i++)
				jsonSlides.put(slides.get(i).getJsonObject());
			ob.put("slides", jsonSlides);
		}
		catch(Exception ex){}
		
		return ob;
	}
	
}
