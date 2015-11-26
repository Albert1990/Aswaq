package com.brainSocket.fragments;

import java.util.HashMap;
import java.util.List;

import com.brainSocket.adapters.SubCategoriesListAdapter;
import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.models.CategoryModel;

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
	
	private FragSubCategories()
	{
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		return inflater.inflate(R.layout.frag_sub_categories, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		try
		{
			homeCallbacks=(HomeCallbacks)getActivity();
			lstSubCategories=(ListView)getActivity().findViewById(R.id.lstSubCategories);
			lstSubCategories.setOnItemClickListener(this);
			homeCallbacks.showProgress(true);
			DataStore.getInstance().attemptGetPageComponents(getArguments().getInt("selectedCategoryId"), getPageComponentsCallback);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
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
				homeCallbacks.showProgress(false);
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int subCategoryId=subCategories.get(position).getId();
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("selectedSubCategoryId", subCategoryId);
		homeCallbacks.loadFragment(FragmentType.ShowAds,params);
	}
	
	public static FragSubCategories newInstance(HashMap<String, Object> params)
	{
		FragSubCategories fragSubCategories=new FragSubCategories();
		try
		{
			Bundle extras=new Bundle();
			if(params.containsKey("selectedCategoryId"))
				extras.putInt("selectedCategoryId", (Integer)params.get("selectedCategoryId"));
			fragSubCategories.setArguments(extras);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return fragSubCategories;
	}

}
