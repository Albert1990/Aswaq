package com.brainSocket.adapters;

import java.util.List;
import java.util.zip.Inflater;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.enums.ImageType;
import com.brainSocket.models.AppUser;
import com.brainSocket.views.TextViewCustomFont;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

public class ClientsListAdapter extends BaseAdapter implements OnClickListener{
	private Context context;
	private List<AppUser> clients;
	private LayoutInflater inflater;
	
	public ClientsListAdapter(Context context,List<AppUser> clients)
	{
		this.context=context;
		this.clients=clients;
		this.inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return clients.size();
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
		ViewHolderItem viewHolderItem=new ViewHolderItem();
		if(convertView==null)
		{
			convertView=inflater.inflate(R.layout.row_user, parent, false);
			viewHolderItem.ivUser=(ImageView)convertView.findViewById(R.id.ivUser);
			viewHolderItem.tvName=(TextViewCustomFont)convertView.findViewById(R.id.tvName);
			viewHolderItem.rbUserRate=(RatingBar)convertView.findViewById(R.id.rbUserRate);
			viewHolderItem.btnFollow=(TextViewCustomFont)convertView.findViewById(R.id.btnFollow);
			
			convertView.setTag(viewHolderItem);
		}
		else
		{
			viewHolderItem=(ViewHolderItem)convertView.getTag();
		}
		
		String photo_path=AswaqApp.getImagePath(ImageType.User, clients.get(position).getPicture());
		Picasso.with(context).load(photo_path).into(viewHolderItem.ivUser);
		viewHolderItem.tvName.setText(clients.get(position).getName());
		viewHolderItem.rbUserRate.setRating(clients.get(position).getRate());
		viewHolderItem.btnFollow.setOnClickListener(this);
		
		return convertView;
	}
	
	private static class ViewHolderItem
	{
		ImageView ivUser;
		TextViewCustomFont tvName;
		RatingBar rbUserRate;
		TextViewCustomFont btnFollow;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
