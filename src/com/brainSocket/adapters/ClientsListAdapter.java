package com.brainSocket.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.PhotoProvider;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.enums.ImageType;
import com.brainSocket.models.AppUser;
import com.brainSocket.views.TextViewCustomFont;

import android.app.Activity;
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
	private HomeCallbacks homeCallbacks;
	private View unfollowClickedButton=null;
	
	public ClientsListAdapter(Activity context,List<AppUser> clients)
	{
		this.context=context;
		this.clients=clients;
		this.homeCallbacks=(HomeCallbacks)context;
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
		//Picasso.with(context).load(photo_path).into(viewHolderItem.ivUser);
		PhotoProvider.getInstance().displayPhotoNormal(photo_path, viewHolderItem.ivUser);
		viewHolderItem.ivUser.setOnClickListener(this);
		viewHolderItem.ivUser.setTag(clients.get(position).getId());
		viewHolderItem.tvName.setText(clients.get(position).getName());
		viewHolderItem.tvName.setOnClickListener(this);
		viewHolderItem.tvName.setTag(clients.get(position).getId());
		viewHolderItem.rbUserRate.setRating(clients.get(position).getRate());
		viewHolderItem.btnFollow.setOnClickListener(this);
		viewHolderItem.btnFollow.setTag(clients.get(position).getId());
		
		return convertView;
	}
	
	private static class ViewHolderItem
	{
		ImageView ivUser;
		TextViewCustomFont tvName;
		RatingBar rbUserRate;
		TextViewCustomFont btnFollow;
	}
	
	private void unfollow(View v)
	{
		try
		{
			unfollowClickedButton=v;
			int clientId=(Integer)v.getTag();

			homeCallbacks.showProgress(true);
			DataStore.getInstance().attemptFollowUser(clientId, false, unfollowCallback);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private DataRequestCallback unfollowCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					int clientId= (Integer)unfollowClickedButton.getTag();
					removeClient(clientId);
					notifyDataSetChanged();
					homeCallbacks.showProgress(false);
				}
			}
		}
	};
	
	private void removeClient(int clientId)
	{
		for(int i=0;i<clients.size();i++)
		{
			if(clients.get(i).getId()==clientId)
			{
				clients.remove(i);
			}
		}
	}
	
	private void openUserProfile(View v)
	{
		int clientId=(Integer)v.getTag();
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("userId", clientId);
		homeCallbacks.loadFragment(FragmentType.UserPage, params);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.btnFollow:
			unfollow(v);
			break;
		case R.id.tvName:
			openUserProfile(v);
			break;
		case R.id.ivUser:
			openUserProfile(v);
			break;
		}
	}

}
