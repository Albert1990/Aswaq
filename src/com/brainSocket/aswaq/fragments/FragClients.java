package com.brainSocket.aswaq.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.brainSocket.aswaq.AdvertiseDetailsActivity;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.UserPageActivity;
import com.brainSocket.aswaq.adapters.ClientsListAdapter;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.models.AppUser;

public class FragClients extends Fragment implements OnItemClickListener{
	private HomeCallbacks homeCallbacks;
	private ListView lvClients;
	private View vNoDataPlaceHolder;
	private List<AppUser> clients=null;
	
	public FragClients()
	{
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_clients, container, false);
		
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
		lvClients=(ListView)getView().findViewById(R.id.list);
		lvClients.setOnItemClickListener(this);
		homeCallbacks.closeSlideDrawer();
		homeCallbacks.showProgress(true);
		vNoDataPlaceHolder=getView().findViewById(R.id.vNoDataPlaceHolder);
		DataStore.getInstance().attemptGetClients(getClientsCallback);
	}
	
	private DataRequestCallback getClientsCallback=new DataRequestCallback() {
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			if(success){
				clients=(List<AppUser>)data.getValue("clients");
				if(clients.size()>0)
				{
					vNoDataPlaceHolder.setVisibility(View.GONE);
					ClientsListAdapter clientsListAdapter=new ClientsListAdapter(getActivity(), clients);
					lvClients.setAdapter(clientsListAdapter);
				}
			}
			else
			{
				homeCallbacks.showToast(getString(R.string.error_connection_error));
			}
			homeCallbacks.showProgress(false);
		}
	};
	
	public static FragClients newInstance()
	{
		return new FragClients();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if(clients!=null)
		{
			Intent i = new Intent(getActivity(),
					UserPageActivity.class);
			i.putExtra("userId", clients.get(position).getId());
			startActivity(i);
		}
	}

}
