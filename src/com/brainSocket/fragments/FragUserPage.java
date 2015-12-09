package com.brainSocket.fragments;

import java.util.HashMap;
import java.util.List;

import com.brainSocket.adapters.AdvertisesListAdapter;
import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.enums.ImageType;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.models.AppUser;
import com.brainSocket.views.TextViewCustomFont;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;

public class FragUserPage extends Fragment implements OnClickListener{
	private HomeCallbacks homeCallbacks;
	private ImageView ivUser;
	private TextViewCustomFont tvUserName;
	private TextViewCustomFont tvFollowers;
	private ImageView btnFbPage;
	private TextViewCustomFont tvUserRating;
	private RatingBar rbUserRate;
	private TextViewCustomFont tvFollow;
	private TextViewCustomFont tvDesc;
	private ListView lvAds;
	private int isFollowedByMe; 
	private int userId;
	
	
	private FragUserPage()
	{
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_user_page, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		homeCallbacks=(HomeCallbacks)getActivity();
		homeCallbacks.showProgress(true);
		userId=getArguments().getInt("userId");
		if(userId==0)
		{
			AppUser me=DataCacheProvider.getInstance().getMe();
			if(me==null)
				return;
			userId=me.getId();
		}
		ivUser=(ImageView)getActivity().findViewById(R.id.ivUser);
		tvUserName=(TextViewCustomFont)getActivity().findViewById(R.id.tvUserName);
		tvFollowers=(TextViewCustomFont)getActivity().findViewById(R.id.tvFollowers);
		btnFbPage=(ImageView)getActivity().findViewById(R.id.btnFbPage);
		//btnFbPage.setOnClickListener(this);
		tvUserRating=(TextViewCustomFont)getActivity().findViewById(R.id.tvUserRating);
		rbUserRate=(RatingBar)getActivity().findViewById(R.id.rbUserRate);
		tvFollow=(TextViewCustomFont)getActivity().findViewById(R.id.tvFollow);
		tvFollow.setOnClickListener(this);
		tvDesc=(TextViewCustomFont)getActivity().findViewById(R.id.tvDesc);
		lvAds=(ListView)getActivity().findViewById(R.id.lvAds);
		
		DataStore.getInstance().attemptGetUserPage(userId,getUserPageCallback);
	}
	
	private DataRequestCallback getUserPageCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			try
			{
			if(success)
			{
				AppUser user=(AppUser)data.getValue("user");
				List<AdvertiseModel> userAds=(List<AdvertiseModel>)data.getValue("userAds");
				int followersCount=(Integer)data.getValue("followersCount");
				isFollowedByMe=(Integer)data.getValue("isFollowedByMe");
				String photoPath=AswaqApp.getImagePath(ImageType.User, user.getPicture());
				Picasso.with(getActivity()).load(photoPath).into(ivUser);
				tvUserName.setText(user.getName());
				tvFollowers.setText(Integer.toString(followersCount));
				if(user.getFacebookId()==-1)
					btnFbPage.setVisibility(View.INVISIBLE);
				tvUserRating.setText(Float.toString(user.getRate()));
				rbUserRate.setRating(user.getRate());
				tvDesc.setText(user.getDescription());
				//AdvertisesListAdapter advertisesListAdapter=new AdvertisesListAdapter(getActivity(), userAds);
				//lvAds.setAdapter(advertisesListAdapter);
				if(isFollowedByMe==0)
				{
					tvFollow.setText(getString(R.string.user_list_follow));
				}
				else
				{
					tvFollow.setText(getString(R.string.user_list_unfollow));
				}
				homeCallbacks.showProgress(false);
			}
		}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	};
	
	private void follow(int userId)
	{
		
		DataStore.getInstance().attemptFollowUser(userId,true,followUserCallback);
	}
	
	private DataRequestCallback followUserCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					tvFollow.setText(getString(R.string.user_list_unfollow));
					isFollowedByMe=1;
					homeCallbacks.showProgress(false);
				}
			}
		}
	};
	
private DataRequestCallback unfollowUserCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					tvFollow.setText(getString(R.string.user_list_follow));
					isFollowedByMe=0;
					homeCallbacks.showProgress(false);
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.tvFollow:
			AppUser me=DataCacheProvider.getInstance().getMe();
			if(me!=null)
			{
				homeCallbacks.showProgress(true);
				if(isFollowedByMe==0)
					DataStore.getInstance().attemptFollowUser(userId, true, followUserCallback);
				else
					DataStore.getInstance().attemptFollowUser(userId, false, unfollowUserCallback);
			}
			else
			{
				homeCallbacks.loadActivity();
			}
			break;
		}
	}
	
	public static FragUserPage newInstance(HashMap<String, Object> params)
	{
		FragUserPage fragUserPage=new FragUserPage();
		try
		{
			int userId=0;
			if(params.containsKey("userId"))
			{
				userId=(Integer)params.get("userId");
			}
			Bundle extras=new Bundle();
			extras.putInt("userId", userId);
			fragUserPage.setArguments(extras);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return fragUserPage; 
	}

}
