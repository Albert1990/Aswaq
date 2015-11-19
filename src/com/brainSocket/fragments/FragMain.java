package com.brainSocket.fragments;

import java.util.List;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.models.CategoryModel;
import com.brainSocket.models.SlideModel;
import com.github.clans.fab.FloatingActionButton;

import enums.FragmentType;
import adapters.CategoryListAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class FragMain extends Fragment implements OnClickListener {
	private HomeCallbacks homeCallbacks;
	private FloatingActionButton btnAddAdvertise;
	private GridView gridViewCategories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.frag_main, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	private void init() {
		homeCallbacks = (HomeCallbacks) getActivity();
		gridViewCategories=(GridView)getActivity().findViewById(R.id.gridViewCategories);
		DataStore.getInstance().attemptGetPageComponents(
				ServerAccess.MAIN_CATEGORY_ID, getPageComponentsCallback);
		btnAddAdvertise = (FloatingActionButton) getActivity().findViewById(
				R.id.btnAddAdvertise);
		btnAddAdvertise.setOnClickListener(this);
	}

	private void search() {
		String keyword = "computer";
		if (AswaqApp.isEmptyOrNull(keyword)) {
			// set text error on the text box and request for focus
		} else {
			DataStore.getInstance().attemptSearchFor(keyword,
					getSearchResultsCallback);
		}
	}

	private DataRequestCallback getSearchResultsCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub

		}
	};

	private void displaySubCategories(int parentCategoryId) {
		homeCallbacks.loadFragment(FragmentType.SubCategories);
	}

	private DataRequestCallback getPageComponentsCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					List<CategoryModel> categories = (List<CategoryModel>) data
							.getValue("categories");
					homeCallbacks
							.showToast(Integer.toString(categories.size()));

					List<SlideModel> slides = (List<SlideModel>) data
							.getValue("slides");
					CategoryListAdapter categoryListAdapter=new CategoryListAdapter(getActivity(), categories);
					gridViewCategories.setAdapter(categoryListAdapter);
					
				} else {
					homeCallbacks.showToast("error in getting categories");
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		switch (viewId) {
		case R.id.btnAddAdvertise:
			homeCallbacks.loadFragment(FragmentType.AddAdvertise);
			break;
		}
	}

}
