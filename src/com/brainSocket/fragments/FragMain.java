package com.brainSocket.fragments;

import java.util.List;

import com.brainSocket.adapters.CategoryListAdapter;
import com.brainSocket.adapters.SliderAdapter;
import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.models.CategoryModel;
import com.brainSocket.models.SlideModel;
import com.github.clans.fab.FloatingActionButton;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class FragMain extends Fragment implements OnClickListener,OnItemClickListener {
	private HomeCallbacks homeCallbacks;
	private FloatingActionButton btnAddAdvertise;
	private GridView gridViewCategories;
	private List<CategoryModel> categories =null;
	private List<SlideModel> slides=null;
	private ViewPager vpSlider;

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
		gridViewCategories.setOnItemClickListener(this);
		DataStore.getInstance().attemptGetPageComponents(
				ServerAccess.MAIN_CATEGORY_ID, getPageComponentsCallback);
		btnAddAdvertise = (FloatingActionButton) getActivity().findViewById(
				R.id.btnAddAdvertise);
		btnAddAdvertise.setOnClickListener(this);
		vpSlider=(ViewPager)getActivity().findViewById(R.id.vpSliderMain);
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
					categories = (List<CategoryModel>) data
							.getValue("categories");
					slides = (List<SlideModel>) data
							.getValue("slides");
					CategoryListAdapter categoryListAdapter=new CategoryListAdapter(getActivity(), categories);
					gridViewCategories.setAdapter(categoryListAdapter);
					
					SliderAdapter sliderAdapter=new SliderAdapter(getActivity(), slides);
					vpSlider.setAdapter(sliderAdapter);
					
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int categoryId=categories.get(position).getId();
		AswaqApp.addPair("selectedCategoryId", categoryId);
		homeCallbacks.loadFragment(FragmentType.SubCategories);
	}

}
