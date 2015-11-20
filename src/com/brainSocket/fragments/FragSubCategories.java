package com.brainSocket.fragments;

import java.util.List;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.models.CategoryModel;

import enums.FragmentType;
import adapters.SubCategoriesListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragSubCategories extends Fragment implements OnItemClickListener {
	private ListView lstSubCategories;
	private HomeCallbacks homeCallbacks;
	private List<CategoryModel> subCategories=null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.frag_sub_categories, container, false);
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
		lstSubCategories=(ListView)getActivity().findViewById(R.id.lstSubCategories);
		lstSubCategories.setOnItemClickListener(this);
		int selectedCategoryId=2;
		DataStore.getInstance().attemptGetPageComponents(selectedCategoryId, getPageComponentsCallback);
	}
	
	private DataRequestCallback getPageComponentsCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				subCategories=(List<CategoryModel>)data.getValue("categories");
				//display categories in the list view
				SubCategoriesListAdapter subCategoriesListAdapter=new SubCategoriesListAdapter(getActivity(), subCategories);
				lstSubCategories.setAdapter(subCategoriesListAdapter);
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int subCategoryId=subCategories.get(position).getId();
		homeCallbacks.loadFragment(FragmentType.ShowAds);
	}

}
