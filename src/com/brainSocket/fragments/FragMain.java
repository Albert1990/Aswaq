package com.brainSocket.fragments;

import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.brainSocket.adapters.MainCategoriesListAdapter;
import com.brainSocket.adapters.SliderAdapter;
import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.LoginActivity;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.SearchActivity;
import com.brainSocket.aswaq.VerificationActivity;
import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.enums.SliderType;
import com.brainSocket.models.AppUser;
import com.brainSocket.models.CategoryModel;
import com.brainSocket.models.PageModel;
import com.brainSocket.models.SlideModel;
import com.github.clans.fab.FloatingActionButton;

public class FragMain extends Fragment implements OnClickListener,OnItemClickListener {
	private HomeCallbacks homeCallbacks;
	private FloatingActionButton btnAddAdvertise;
	private GridView gridViewCategories;
	private EditText etSearch;
	private List<CategoryModel> categories =null;
	private List<SlideModel> slides=null;
	private ViewPager vpSlider;
	private int currentSlide=0;
	private Dialog dialogLoading;
	private boolean stopSliderTransition=false;
	
	private FragMain()
	{
		
	}

	 OnEditorActionListener callbackSearchQueryChange =new OnEditorActionListener() {
	        @Override
	        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)  {
	            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_GO)) {
	                search();
	                return true;
	            }
	            return false;
	        }
	    };
	    
	    OnTouchListener serchDrawableToutchLIstener = new OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            final int DRAWABLE_LEFT = 0;
	            final int DRAWABLE_TOP = 1;
	            final int DRAWABLE_RIGHT = 2;
	            final int DRAWABLE_BOTTOM = 3;

	            if(event.getAction() == MotionEvent.ACTION_UP) {
	                if(event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
	                	search();
	                 return true;
	                }
	            }
	            return false;
	        }
	    };
	
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
		etSearch = (EditText) getActivity().findViewById(R.id.etSearch);
		gridViewCategories.setOnItemClickListener(this);
		DataStore.getInstance().attemptGetPageComponents(
				ServerAccess.MAIN_CATEGORY_ID, getPageComponentsCallback);
		btnAddAdvertise = (FloatingActionButton) getActivity().findViewById(
				R.id.btnAddAdvertise);
		btnAddAdvertise.setOnClickListener(this);
		vpSlider=(ViewPager)getActivity().findViewById(R.id.vpSliderMain);
		etSearch.setOnEditorActionListener(callbackSearchQueryChange);
		etSearch.setOnTouchListener(serchDrawableToutchLIstener);
		
		homeCallbacks.showProgress(true);
		homeCallbacks.closeSlideDrawer();
	}

	private void search() {
		String input = etSearch.getText().toString();
		Intent i = new Intent(getActivity(), SearchActivity.class);
		if(input != null && !input.isEmpty())
			i.putExtra("keyword", input);
		startActivity(i);
	}

	private DataRequestCallback getPageComponentsCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			try
			{
				homeCallbacks.showProgress(false);
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					PageModel page=(PageModel)data.getValue("page");
					categories = page.getCategories();
					slides = page.getSlides();
					MainCategoriesListAdapter categoryListAdapter=new MainCategoriesListAdapter(getActivity(), categories);
					gridViewCategories.setAdapter(categoryListAdapter);
					
					SliderAdapter sliderAdapter=new SliderAdapter(getActivity(), slides,SliderType.Banner);
					vpSlider.setAdapter(sliderAdapter);
					if(slides.size() > 0)
					{
						stopSliderTransition=false;
						new Handler().postDelayed(SliderTransition,AswaqApp.SLIDER_TRANSITION_INTERVAL);
					}
					
				} else {
					homeCallbacks.showToast("error in getting categories");
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
			try
			{
				stopSliderTransition=true;
				AppUser me=DataCacheProvider.getInstance().getMe();
				if(me!=null)
				{
					if(me.isVerified()==true)
					{
						homeCallbacks.loadFragment(FragmentType.AddAdvertise,null);
					}
					else{
						Intent i=new Intent(getActivity(), VerificationActivity.class);
						startActivity(i);
					}
					
				}
				else
				{
					Intent i=new Intent(getActivity(), LoginActivity.class);
					startActivity(i);
				}
			}
			catch(Exception ex){}
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
