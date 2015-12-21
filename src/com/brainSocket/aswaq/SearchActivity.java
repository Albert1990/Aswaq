package com.brainSocket.aswaq;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerResult;
import com.brainSocket.fragments.FragAdsList;
import com.brainSocket.fragments.FragUsersList;
import com.brainSocket.fragments.FragUsersList.FRAG_USERS_TYPE;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.models.AppUser;
import com.brainSocket.views.SlidingTabLayout;



public class SearchActivity extends AppBaseActivity implements OnClickListener{

    LinearLayout llLoading;
    TabsPagerAdapter tabsAdapter ;
    ViewPager tabView;
    SlidingTabLayout tabs;
    EditText etSearch;
    View btnBack;
    View vNoDataPlaceHolder;
    TextView tvPlaceHolderMsg;

    String keyWord;

    DataRequestCallback callbackSearch = new DataRequestCallback() {
        @Override
        public void onDataReady(ServerResult data, boolean success) {
            showProgress(false);
            if(success){
                ArrayList<AdvertiseModel> ads = (ArrayList) data.getValue("ads");
                ArrayList<AppUser> users= (ArrayList) data.getValue("users");
                tabsAdapter.updateAdapter(ads,users);
                tabs.setViewPager(tabView);
                //tabView.setCurrentItem(tabsAdapter.getCount()-1); // select the last item cuz its an RTL app
                vNoDataPlaceHolder.setVisibility(View.GONE);
            }else{
                //clear lists
                //tabsAdapter.updateAdapter(null,null);
                vNoDataPlaceHolder.setVisibility(View.VISIBLE);
                tvPlaceHolderMsg.setText(R.string.error_connection_error);
            }
        }
    };

    OnEditorActionListener callbackSearchQueryChange =new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event)  {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_GO)) {
                searachForKeyword(v.getText().toString());
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        
        try {
        	keyWord = getIntent().getExtras().getString("keyword");
		} catch (Exception e) {}
        
        init();
        initCustomActionBar();
        
        if(keyWord != null && !keyWord.isEmpty())
        	searachForKeyword(keyWord);
    }

    private void initCustomActionBar() {

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.layout_custom_actionbar_search, null);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(mCustomView);

        btnBack = mCustomView.findViewById(R.id.btnBack);
        etSearch = (EditText) mCustomView.findViewById(R.id.etSearch);

        etSearch.setOnEditorActionListener(callbackSearchQueryChange);
        btnBack.setOnClickListener(this);
        
        if(keyWord != null)
        	etSearch.setText(keyWord);
    }

    private void init() {
        llLoading = (LinearLayout) findViewById(R.id.llLoading);
        tabView = (ViewPager) findViewById(R.id.viewPager);
        tabs = (SlidingTabLayout) findViewById(R.id.slidingTabLayout);
        vNoDataPlaceHolder = findViewById(R.id.vNoDataPlaceHolder);
        tvPlaceHolderMsg = (TextView) findViewById(R.id.tvPlaceHolderMsg);

        tabs.setDistributeEvenly(true);
        int[] colors = {getResources().getColor(R.color.app_theme)};
        tabs.setSelectedIndicatorColors(colors);
        //tabs.setCustomTabView(R.layout.row_tab_view_title_small_gray, R.id.tvTitle);

        tabsAdapter = new TabsPagerAdapter (getSupportFragmentManager());
        tabView.setAdapter(tabsAdapter);

        // initial state
        tabs.setVisibility(View.INVISIBLE);
        vNoDataPlaceHolder.setVisibility(View.VISIBLE); /// used as a placeholder for now
        showProgress(false);
    }


    public void showProgress(boolean show) {

        if (show) {
            llLoading.setVisibility(View.VISIBLE);
        } else {
            llLoading.setVisibility(View.GONE);
        }
    }

    private void searachForKeyword(String keyword){
        DataStore.getInstance().attemptSearchFor(keyword,callbackSearch);
        showProgress(true);
        vNoDataPlaceHolder.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnBack:
                finish();
            break;
        }
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        FragAdsList fragAds;
        FragUsersList fragPeople;

        public TabsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        
        public void updateAdapter( ArrayList<AdvertiseModel> apps, ArrayList<AppUser> users){
            if(fragAds == null)
                fragAds = FragAdsList.newInstance(apps);
            else
                fragAds.updateData(apps);

            if(fragPeople == null)
                fragPeople = FragUsersList.newInstance(users, FRAG_USERS_TYPE.SEARCH_RESULTS);
            else
                fragPeople.updateData(users);

            tabs.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int index) {
            switch (index) {
                case 0:
                    return fragAds;
                case 1:
                	return fragPeople;
                    
            }
            return null;
        }

        @Override
        public int getCount() {
            if(fragAds !=null)
                return 2;
            else
               return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            try {
                switch (position) {
                    case 0:
                        title = getString(R.string.search_tab_title_ads);
                        break;
                    case 1:
                        title = getString(R.string.search_tab_title_users);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return title;
        }
    }
}
