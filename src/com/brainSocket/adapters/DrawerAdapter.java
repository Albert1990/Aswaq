package com.brainSocket.adapters;

import java.util.List;

import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.LoginActivity;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.UserPageActivity;
import com.brainSocket.data.DataCacheProvider;
import com.brainSocket.enums.DrawerItemType;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.models.AppUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter implements OnItemClickListener{
	private DrawerElement [] elements  = {
			  new DrawerElement(R.string.drawer_main, R.drawable.ic_home, R.drawable.ic_home,DrawerItemType.MAIN),
			  new DrawerElement(R.string.drawer_profile, R.drawable.ic_profile, R.drawable.ic_profile, DrawerItemType.PROFILE) ,
			  new DrawerElement(R.string.drawer_agents, R.drawable.ic_customers, R.drawable.ic_customers, DrawerItemType.AGENTS) ,
			  new DrawerElement(R.string.drawer_favourites, R.drawable.ic_fav, R.drawable.ic_fav, DrawerItemType.FAVOURITES) ,
			  new DrawerElement(R.string.drawer_callus, R.drawable.ic_about, R.drawable.ic_about, DrawerItemType.CALLUS) ,
			  };
		
		  protected final Context context;
		  protected Boolean selectable ;
		  protected List<Integer> selected ; 
		  protected ListView list ;
		  int selectedItemIndex = 0 ;
		  private HomeCallbacks homeCallbacks;
		  
		  public void setSelectedItemIndex(int selectedItemIndex) {
			this.selectedItemIndex = selectedItemIndex;
			notifyDataSetChanged() ;
		}
		  
		  public DrawerAdapter(Activity activity, ListView view) {
		    super();
		    this.context = activity;
		    
		    list = view ;
		    list.setOnItemClickListener(this);
		    homeCallbacks=(HomeCallbacks)activity;
		  }

		  public void onFragmentChange(FragmentType fragType){
//			  switch (fragType) {
//			case MAIN:
//				selectedItemIndex = 0 ;
//				break;
//			case TODO:
//				selectedItemIndex = 1 ;
//				break;
//			case STATS:
//				selectedItemIndex = 2 ;
//				break;
//			case ABOUT:
//				selectedItemIndex = 3 ;
//				break;
//			default:
//				selectedItemIndex = -1 ;
//				break;
//			}
//			  notifyDataSetChanged();
		  }
		 
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  
			ViewHolderItem viewHolder=null;
			if(convertView == null){  
			  LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			  convertView = inflater.inflate(R.layout.row_drawer, parent, false);
			  viewHolder=new ViewHolderItem();
			  viewHolder.txt=(TextView) convertView.findViewById(R.id.title);;
			  viewHolder.icon=(ImageView) convertView.findViewById(R.id.drawable_icon);
			  convertView.setTag(viewHolder);
			}else{
			  viewHolder=(ViewHolderItem)convertView.getTag();
			}
			String title = context.getString(elements[position].stringId);
			int imRes  = elements[position].iconId;
			int color = Color.WHITE;
			
			if(selectedItemIndex == position){
				imRes  = elements[position].activeIconId;
				//color = getResources().getColor(R.color.app_theme_orange);
			}
		    
		    viewHolder.txt.setText(title);
		    //txt.setTextColor(color);
		    viewHolder.icon.setImageResource(imRes);
		    
		    return convertView;
		  }
		  
		@Override
		public int getCount() {
			return elements.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			//setSelectedItemIndex(arg2);
			DrawerElement elem = elements[arg2];
			AppUser me=DataCacheProvider.getInstance().getMe();
			if (elem.itemType==DrawerItemType.MAIN) {
				homeCallbacks.backToHome();
			}
			else if(elem.itemType==DrawerItemType.PROFILE)
			{
				homeCallbacks.closeSlideDrawer();
				if(me!=null)
					{
					Intent i=new Intent(context, UserPageActivity.class);
					i.putExtra("userId", me.getId());
					context.startActivity(i);
					}
				else
				{
					//homeCallbacks.loadActivity(LoginActivity.class,null);
					Intent i=new Intent(context, LoginActivity.class);
					context.startActivity(i);
				}
			}
			else if(elem.itemType==DrawerItemType.AGENTS)
			{
				if(me!=null)
					homeCallbacks.loadFragment(FragmentType.MyClients,null);
				else
				{
					//homeCallbacks.loadActivity(LoginActivity.class,null);
					Intent i=new Intent(context, LoginActivity.class);
					context.startActivity(i);
				}
			}
			else if(elem.itemType==DrawerItemType.FAVOURITES)
			{
				homeCallbacks.loadFragment(FragmentType.Favourites, null);
			}
			else if(elem.itemType==DrawerItemType.CALLUS)
			{
			}
			else
			{
			}
		
		}	
		
		private static class ViewHolderItem
		{
			TextView txt;
			ImageView icon;
		}
}
