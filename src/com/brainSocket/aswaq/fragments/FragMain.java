package com.brainSocket.aswaq.fragments;

import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.LoginActivity;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.SearchActivity;
import com.brainSocket.aswaq.VerificationActivity;
import com.brainSocket.aswaq.adapters.CirclePageIndicator;
import com.brainSocket.aswaq.adapters.MainCategoriesListAdapter;
import com.brainSocket.aswaq.adapters.SliderAdapter;
import com.brainSocket.aswaq.data.DataCacheProvider;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.enums.SliderType;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.models.CategoryModel;
import com.brainSocket.aswaq.models.PageModel;
import com.brainSocket.aswaq.models.SlideModel;
import com.brainSocket.aswaq.views.EditTextCustomFont;
import com.github.clans.fab.FloatingActionButton;

public class FragMain extends Fragment implements OnClickListener,
		OnItemClickListener {
	private HomeCallbacks homeCallbacks;
	private FloatingActionButton btnAddAdvertise;
	private GridView gridViewCategories;
	private EditTextCustomFont etSearch;
	private List<CategoryModel> categories = null;
	private List<SlideModel> slides = null;
	private ViewPager vpSlider;
	private int currentSlide = 0;
	private Dialog dialogLoading;
	private Handler sliderHandler = null;
	private CirclePageIndicator circleIndicator;

	private FragMain() {
		if (sliderHandler == null)
			sliderHandler = new Handler();
	}

	OnEditorActionListener callbackSearchQueryChange = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
					|| (actionId == EditorInfo.IME_ACTION_GO)) {
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

			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (event.getRawX() >= (etSearch.getRight() - etSearch
						.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds()
						.width())) {
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
		return inflater.inflate(R.layout.frag_main, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}

	private void init() {
		homeCallbacks = (HomeCallbacks) getActivity();
		gridViewCategories = (GridView) getView().findViewById(
				R.id.gridViewCategories);
		etSearch = (EditTextCustomFont) getView().findViewById(R.id.etSearch);
		etSearch.setEnabled(false);
		gridViewCategories.setOnItemClickListener(this);

		btnAddAdvertise = (FloatingActionButton) getView().findViewById(
				R.id.btnAddAdvertise);
		btnAddAdvertise.setOnClickListener(this);
		vpSlider = (ViewPager) getView().findViewById(R.id.vpSliderMain);
		vpSlider.setOnClickListener(this);

		etSearch.setOnEditorActionListener(callbackSearchQueryChange);
		etSearch.setOnTouchListener(serchDrawableToutchLIstener);
		circleIndicator = (CirclePageIndicator) getView().findViewById(
				R.id.titles);

		homeCallbacks.showProgress(true);
		homeCallbacks.closeSlideDrawer();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				try {
					if (getActivity() != null) {
						View view = getActivity().getCurrentFocus();
						if (view != null) {
							InputMethodManager imm = (InputMethodManager) getActivity()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(view.getWindowToken(),
									0);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, 2000);
		DataStore.getInstance().attemptGetPageComponents(
				ServerAccess.MAIN_CATEGORY_ID, getPageComponentsCallback);
	}

	private void search() {
		String input = etSearch.getText().toString();
		Intent i = new Intent(getActivity(), SearchActivity.class);
		if (input != null && !input.isEmpty())
			i.putExtra("keyword", input);
		startActivity(i);
	}

	private DataRequestCallback getPageComponentsCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			try {
				homeCallbacks.showProgress(false);

				if (success) {
					if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
						PageModel page = (PageModel) data.getValue("page");
						if (page != null) {
							categories = page.getCategories();
							slides = page.getSlides();
							MainCategoriesListAdapter categoryListAdapter = new MainCategoriesListAdapter(
									getActivity(), categories);
							gridViewCategories.setAdapter(categoryListAdapter);

							SliderAdapter sliderAdapter = new SliderAdapter(
									getActivity(), slides, SliderType.Banner);
							vpSlider.setAdapter(sliderAdapter);
							circleIndicator.setViewPager(vpSlider);
							if (slides.size() > 1) {
								new Handler().postDelayed(SliderTransition,
										AswaqApp.SLIDER_TRANSITION_INTERVAL);
							}
						} else
							homeCallbacks
									.showToast(getString(R.string.error_connection_error));

					} else {
						homeCallbacks
								.showToast(getString(R.string.error_connection_error));
					}

				} else {
					homeCallbacks
							.showToast(getString(R.string.error_connection_error));
				}
				etSearch.setEnabled(true);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	private Runnable SliderTransition = new Runnable() {

		@Override
		public void run() {
			try {
				if (currentSlide >= slides.size())
					currentSlide = 0;
				vpSlider.setCurrentItem(currentSlide, true);
				currentSlide++;
				try {
					sliderHandler.removeCallbacks(SliderTransition);
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				sliderHandler.postDelayed(SliderTransition,
						AswaqApp.SLIDER_TRANSITION_INTERVAL);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};

	private void addAdvertise() {
		try {
			AppUser me = DataStore.getInstance().getMe();
			if (me != null) {
				if (me.isVerified() == true) {
					homeCallbacks.loadFragment(FragmentType.AddAdvertise, null);
				} else {
					Intent i = new Intent(getActivity(),
							VerificationActivity.class);
					startActivity(i);
				}
			} else {
				Intent i = new Intent(getActivity(), LoginActivity.class);
				startActivity(i);
			}
		} catch (Exception ex) {
		}
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.btnAddAdvertise:
			addAdvertise();
			break;
		case R.id.vpSlider:
			try {
				sliderHandler.removeCallbacks(SliderTransition);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MainCategoriesListAdapter.ViewHolderItem viewHolder = (MainCategoriesListAdapter.ViewHolderItem) view
				.getTag();
		String categoryName = viewHolder.lblCategoryName.getText().toString();
		int categoryId = categories.get(position).getId();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("selectedCategoryId", categoryId);
		params.put("selectedCategoryName", categoryName);
		homeCallbacks.loadFragment(FragmentType.SubCategories, params);
	}

	public static FragMain newInstance() {
		FragMain fragMain = new FragMain();
		return fragMain;
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			sliderHandler.removeCallbacks(SliderTransition);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			try {
				sliderHandler.removeCallbacks(SliderTransition);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			sliderHandler.postDelayed(SliderTransition,
					AswaqApp.SLIDER_TRANSITION_INTERVAL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
