package com.brainSocket.fragments;

import java.util.HashMap;
import java.util.List;

import com.brainSocket.adapters.MainCategoriesListAdapter;
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

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
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
	private int currentSlide=0;
	private Dialog dialogLoading;
	private boolean stopSliderTransition=false;
	
	private FragMain()
	{
		
	}

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
		homeCallbacks.showProgress(true);
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
					MainCategoriesListAdapter categoryListAdapter=new MainCategoriesListAdapter(getActivity(), categories);
					gridViewCategories.setAdapter(categoryListAdapter);
					
					SliderAdapter sliderAdapter=new SliderAdapter(getActivity(), slides);
					vpSlider.setAdapter(sliderAdapter);
					if(slides.size() > 0)
					{
						stopSliderTransition=false;
						new Handler().postDelayed(SliderTransition,AswaqApp.SLIDER_TRANSITION_INTERVAL);
					}
					homeCallbacks.showProgress(false);
				} else {
					homeCallbacks.showToast("error in getting categories");
				}
			}
		}
	};
	
	private Runnable SliderTransition=new Runnable() {
		
		@Override
		public void run() {
			try
			{
				if(currentSlide >= slides.size())
					currentSlide=0;
				vpSlider.setCurrentItem(currentSlide, true);
				currentSlide++;
				if(!stopSliderTransition)
					new Handler().postDelayed(SliderTransition,AswaqApp.SLIDER_TRANSITION_INTERVAL);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId = v.getId();
		switch (viewId) {
		case R.id.btnAddAdvertise:
			stopSliderTransition=true;
			homeCallbacks.loadFragment(FragmentType.AddAdvertise,null);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int categoryId=categories.get(position).getId();
		HashMap<String, Object> params=new HashMap<String, Object>();
		params.put("selectedCategoryId", categoryId);
		stopSliderTransition=true;
		homeCallbacks.loadFragment(FragmentType.SubCategories,params);
	}
	
	public static FragMain newInstance()
	{
		FragMain fragMain=new FragMain();
		return fragMain;
	}

}
