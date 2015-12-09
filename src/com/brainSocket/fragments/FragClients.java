package com.brainSocket.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.brainSocket.adapters.ClientsListAdapter;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.models.AppUser;

public class FragClients extends Fragment{
	private HomeCallbacks homeCallbacks;
	private ListView lvClients;
	
	private FragClients()
	{
		
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
		lvClients=(ListView)getActivity().findViewById(R.id.list);
		homeCallbacks.closeSlideDrawer();
		homeCallbacks.showProgress(true);
		DataStore.getInstance().attemptGetClients(getClientsCallback);
	}
	
	private DataRequestCallback getClientsCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success){
				List<AppUser> clients=(List<AppUser>)data.getValue("clients");
				ClientsListAdapter clientsListAdapter=new ClientsListAdapter(getActivity(), clients);
				lvClients.setAdapter(clientsListAdapter);
				homeCallbacks.showProgress(false);
			}
		}
	};
	
	public static FragClients newInstance()
	{
		return new FragClients();
	}

}
