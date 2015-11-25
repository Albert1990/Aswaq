package com.brainSocket.aswaq;

import java.util.List;

import com.brainSocket.adapters.DrawerAdapter;
import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.models.AppUser;
import com.brainSocket.models.CategoryModel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppBaseActivity implements OnBackStackChangedListener,OnClickListener{
	private ListView lvDrawer ;
	private DrawerAdapter adapter ;
	private DrawerLayout dlDrawer ;
	private View llLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();		
	}
	
	private void init()
	{
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		AppUser me=DataCacheProvider.getInstance().getMe();
		if(!me.isVerified())
		{
			loadFragment(FragmentType.Verification);
		}
		else
		{
			lvDrawer = (ListView) findViewById(R.id.lvDrawer);
			adapter = new DrawerAdapter(this, lvDrawer);
			lvDrawer.setAdapter(adapter);
			dlDrawer = (DrawerLayout) findViewById(R.id.dlDrawer);
			llLogout=findViewById(R.id.llLogout);
			llLogout.setOnClickListener(this);
			loadFragment(FragmentType.Main);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackStackChanged() {
		
	}
	
//	@Override
//	public void onBackPressed() {

//		//super.onBackPressed();
//		int  entrys = getSupportFragmentManager().getBackStackEntryCount() ;
//		
//		try {
//			FragmentType backFragmentType;
//			if(entrys>1)
//			{
//				backFragmentType = FragmentType.valueOf(getSupportFragmentManager().getBackStackEntryAt(entrys-2).getName());
//				if(backFragmentType!=FragmentType.Main)
//				{
//					getSupportFragmentManager().popBackStackImmediate();
//					getSupportFragmentManager().popBackStackImmediate();
//					loadFragment(backFragmentType);
//				}
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int viewId=v.getId();
		switch(viewId)
		{
		case R.id.llLogout:
			DataCacheProvider.getInstance().removeStoredMe();
			break;
		}
	}
	
}
