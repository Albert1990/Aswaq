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
import com.brainSocket.models.PageModel;

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
	private View vNoDataPlaceHolder;
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
			vNoDataPlaceHolder=getActivity().findViewById(R.id.vNoDataPlaceHolder);
			homeCallbacks.showProgress(true);
			String selectedCategoryName=getArguments().getString("selectedCategoryName");
			homeCallbacks.setTitle(selectedCategoryName);
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
			try
			{
				homeCallbacks.showProgress(false);
				if(success)
				{
					PageModel page=(PageModel)data.getValue("page");
					subCategories=page.getCategories();
					if(subCategories.size()>0)
					{
						//display categories in the list view
						vNoDataPlaceHolder.setVisibility(View.GONE);
						SubCategoriesListAdapter subCategoriesListAdapter=new SubCategoriesListAdapter(getActivity(), subCategories);
						lstSubCategories.setAdapter(subCategoriesListAdapter);
					}

				}
				else
				{
					homeCallbacks.showToast(getString(R.string.error_connection_error));
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		SubCategoriesListAdapter.ViewHolderItem viewHolder=(SubCategoriesListAdapter.ViewHolderItem)view.getTag();
		String subCategoryName=viewHolder.lblSubCategoryName.getText().toString();
		int subCategoryId=subCategories.get(position).getId();
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("selectedSubCategoryId", subCategoryId);
		params.put("selectedSubCategoryName", subCategoryName);
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
			if(params.containsKey("selectedCategoryName"))
				extras.putString("selectedCategoryName", (String)params.get("selectedCategoryName"));
			fragSubCategories.setArguments(extras);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return fragSubCategories;
	}

}
