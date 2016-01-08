package com.brainSocket.aswaq.models;

import org.json.JSONObject;

public class PhoneNumberModel {
	private String number;
	private boolean whatsapp=false;
	private boolean viber=false;
	private boolean phone=false;

	public PhoneNumberModel(JSONObject ob)
	{
		if(ob==null)
			return;
		if(ob.has("number"))
		{
			try
			{
				number=ob.getString("number");
			}
			catch(Exception ex){ex.printStackTrace();}
		}
		
		if(ob.has("whatsapp"))
		{
			try
			{
				whatsapp=ob.getBoolean("whatsapp");
			}catch(Exception ex){ex.printStackTrace();}
		}
		
		if(ob.has("viber"))
		{
			try
			{
				viber=ob.getBoolean("viber");
			}catch(Exception ex){ex.printStackTrace();}
		}
		
		if(ob.has("phone"))
		{
			try
			{
				phone=ob.getBoolean("phone");
			}catch(Exception ex){ex.printStackTrace();}
		}
	}
	
	public String getNumber()
	{
		return number;
	}
	
	public boolean hasWhatsapp()
	{
		return whatsapp;
	}
	
	public boolean hasViber()
	{
		return viber;
	}
	
	public boolean isPhone()
	{
		return phone;
	}
	
	public JSONObject getJsonObject()
	{
		JSONObject ob=new JSONObject();
		
		try
		{
			ob.put("number", number);
		}catch(Exception ex){ex.printStackTrace();}
		
		try
		{
			ob.put("whatsapp", whatsapp);
		}catch(Exception ex){ex.printStackTrace();}
		
		try
		{
			ob.put("viber", viber);
		}catch(Exception ex){ex.printStackTrace();}
		
		try
		{
			ob.put("phone", phone);
		}catch(Exception ex){ex.printStackTrace();}
		
		return ob;
	}
	
}
