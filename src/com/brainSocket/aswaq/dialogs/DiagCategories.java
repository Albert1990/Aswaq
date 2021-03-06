package com.brainSocket.aswaq.dialogs;

import java.util.List;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.adapters.MainCategoriesListAdapter;
import com.brainSocket.aswaq.adapters.SliderAdapter;
import com.brainSocket.aswaq.adapters.SubCategoriesListAdapter;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.models.CategoryModel;
import com.brainSocket.aswaq.models.SlideModel;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DiagCategories extends DialogFragment implements OnItemClickListener{
	private ListView lvDiagCategories;
	private HomeCallbacks homeCallbacks;
	private List<CategoryModel> categories=null;
	private DataRequestCallback onCategorySelectedcallback;
	
	public DiagCategories(DataRequestCallback onCategorySelectedcallback)
	{
		this.onCategorySelectedcallback=onCategorySelectedcallback;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateDialog(savedInstanceState);
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		init();
	}
	
	private void init()
	{
		homeCallbacks=(HomeCallbacks)getActivity();
		homeCallbacks.showProgress(true);
		DataStore.getInstance().attemptGetSubCategoriesAsPairs(getSubCategoriesCallback);
	}
	
	private DataRequestCallback getSubCategoriesCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(success)
			{
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					categories = (List<CategoryModel>) data
							.getValue("categories");
					SubCategoriesListAdapter subCategoriesListAdapter=new SubCategoriesListAdapter(getActivity(), categories);
					lvDiagCategories.setAdapter(subCategoriesListAdapter);
					homeCallbacks.showProgress(false);
				} else {
					homeCallbacks.showToast("error in getting categories");
				}
			}
			else
			{
				homeCallbacks.showToast(getString(R.string.error_connection_error));
			}
		}
	};
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().setTitle(getString(R.string.lbl_suitable_category));
		View v=inflater.inflate(R.layout.dialog_categories, container, false);
		lvDiagCategories=(ListView)v.findViewById(R.id.lvDiagCategories);
		lvDiagCategories.setOnItemClickListener(this);
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(categories!=null)
		{
			ServerResult data=new ServerResult();
			data.addPair("selectedCategory", categories.get(position));
			onCategorySelectedcallback.onDataReady(data, true);
			dismiss();
		}
	}

}
