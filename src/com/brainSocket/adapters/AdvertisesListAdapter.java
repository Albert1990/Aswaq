package com.brainSocket.adapters;

import java.util.List;

import com.brainSocket.aswaq.R;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.views.TextViewCustomFont;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdvertisesListAdapter extends BaseAdapter{
	private Context context;
	private List<AdvertiseModel> ads;
	private LayoutInflater inflater;
	
	public AdvertisesListAdapter(Context context,List<AdvertiseModel> ads)
	{
		this.context=context;
		this.ads=ads;
		inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ads.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		ViewHolderItem viewHolder;
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.item_list_ad, parent, false);
			viewHolder=new ViewHolderItem();
			viewHolder.ivProduct =(ImageView)convertView.findViewById(R.id.ivProd);
			viewHolder.tvUserName =(TextViewCustomFont)convertView.findViewById(R.id.tvUserName);
			viewHolder.tvAdvertiseDescription =(TextViewCustomFont)convertView.findViewById(R.id.tvAdvertiseDescription);
			viewHolder.tvPrice =(TextViewCustomFont)convertView.findViewById(R.id.tvPrice);
			convertView.setTag(viewHolder);
		}
		else
			viewHolder=(ViewHolderItem)convertView.getTag();
		
		viewHolder.ivProduct.setImageResource(R.drawable.ic_launcher);
		viewHolder.tvUserName.setText(ads.get(position).getUser().getName());
		viewHolder.tvAdvertiseDescription.setText(ads.get(position).getDescription());
		viewHolder.tvPrice.setText(Integer.toString(ads.get(position).getPrice()));
		
		return convertView;
	}
	
	private static class ViewHolderItem
	{
		ImageView ivProduct;
		TextViewCustomFont tvUserName;
		TextViewCustomFont tvAdvertiseDescription;
		TextViewCustomFont tvPrice;
	}

}
