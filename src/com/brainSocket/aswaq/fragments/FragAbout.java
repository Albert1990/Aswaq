package com.brainSocket.aswaq.fragments;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.models.ContactModel;
import com.brainSocket.aswaq.models.PhoneNumberModel;
import com.brainSocket.aswaq.views.TextViewCustomFont;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FragAbout extends Fragment implements OnClickListener,OnItemClickListener{
	private HomeCallbacks homeCallbacks;
	private ContactModel contactInfo;
	private TextViewCustomFont tvfbPage;
	private TextViewCustomFont tvEmail;
	private View ivBrainSocketLogo;
	private ListView lstPhones;

	public FragAbout()
	{
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_about, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		homeCallbacks=(HomeCallbacks)getActivity();
		homeCallbacks.closeSlideDrawer();
		homeCallbacks.showProgress(true);
		tvfbPage=(TextViewCustomFont)getView().findViewById(R.id.tvfbPage);
		tvfbPage.setOnClickListener(this);
		tvEmail=(TextViewCustomFont)getView().findViewById(R.id.tvEmail);
		ivBrainSocketLogo=getView().findViewById(R.id.ivBrainSocketLogo);
		ivBrainSocketLogo.setOnClickListener(this);
		lstPhones=(ListView)getView().findViewById(R.id.lstPhones);;
		lstPhones.setOnItemClickListener(this);
		DataStore.getInstance().attemptGetContactInfo(getContactInfoCallback);
	}
	
	private DataRequestCallback getContactInfoCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			homeCallbacks.showProgress(false);
			if(success)
			{
				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
				{
					contactInfo=(ContactModel)data.getValue("contactModel");
					PhoneListAdapter adapter=new PhoneListAdapter();
					lstPhones.setAdapter(adapter);
					tvfbPage.setText(contactInfo.getFacebookPage());
					tvEmail.setText(contactInfo.getEmail());
				}
				else
				{
					homeCallbacks.showToast(getString(R.string.error_connection_error));
				}
			}
			else
			{
				homeCallbacks.showToast(getString(R.string.error_connection_error));
			}
		}
	};
	
	public static FragAbout newInstance()
	{
		return new FragAbout();
	}
	
	private class PhoneListAdapter extends BaseAdapter
	{
		private LayoutInflater inflater;
		
		public PhoneListAdapter() {
			inflater=(LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return contactInfo.getPhones().size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PhoneNumberViewHolder viewHolder=null;
			if(convertView==null)
			{
				convertView=inflater.inflate(R.layout.item_phone_number, parent, false);
				viewHolder=new PhoneNumberViewHolder();
				viewHolder.tvPhoneNumber=(TextViewCustomFont)convertView.findViewById(R.id.tvPhoneNumber);
				viewHolder.ivPhone=(ImageView)convertView.findViewById(R.id.ivPhone);
				viewHolder.ivWhats=(ImageView)convertView.findViewById(R.id.ivWhats);
				viewHolder.ivViber=(ImageView)convertView.findViewById(R.id.ivViber);
				convertView.setTag(viewHolder);
			}
			else
				viewHolder=(PhoneNumberViewHolder)convertView.getTag();
			
			PhoneNumberModel phoneModel=contactInfo.getPhones().get(position);
			viewHolder.tvPhoneNumber.setText(phoneModel.getNumber());
			if(phoneModel.hasWhatsapp())
				viewHolder.ivWhats.setVisibility(View.VISIBLE);
			if(phoneModel.hasViber())
				viewHolder.ivViber.setVisibility(View.VISIBLE);
			if(phoneModel.isPhone())
				viewHolder.ivPhone.setVisibility(View.VISIBLE);
			
			return convertView;
		}
		
		private class PhoneNumberViewHolder
		{
			TextViewCustomFont tvPhoneNumber;
			ImageView ivPhone;
			ImageView ivWhats;
			ImageView ivViber;
		}
	}

	@Override
	public void onClick(View v) {
		int viewId=v.getId();
		Intent i=null;
		switch(viewId)
		{
		case R.id.tvfbPage:
			try
			{
				Uri uri = Uri.parse(contactInfo.getFacebookPage()); // missing 'http://' will
				i = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(i);
			}
			catch(Exception ex){ex.printStackTrace();}
			break;
		case R.id.ivBrainSocketLogo:
			try
			{
			Uri uri = Uri.parse("http://brain-socket.com/"); // missing 'http://' will
			i = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(i);
			}
			catch(Exception ex){}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String selectedPhoneNumber = contactInfo.getPhones().get(position).getNumber();
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ selectedPhoneNumber));
		startActivity(i);
	}

}
