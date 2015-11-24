package com.brainSocket.adapters;

import java.util.List;

import com.brainSocket.aswaq.R;
import com.brainSocket.enums.DrawerItemType;
import com.brainSocket.enums.FragmentType;

import android.content.Context;
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
			  new DrawerElement(R.string.drawer_main, R.drawable.ic_home, R.drawable.ic_home_active,DrawerItemType.MAIN),
			  new DrawerElement(R.string.drawer_profile, R.drawable.ic_todo, R.drawable.ic_todo_active, DrawerItemType.PROFILE) ,
			  new DrawerElement(R.string.drawer_agents, R.drawable.ic_chart, R.drawable.ic_chart_active, DrawerItemType.AGENTS) ,
			  new DrawerElement(R.string.drawer_favourites, R.drawable.ic_chart, R.drawable.ic_chart_active, DrawerItemType.FAVOURITES) ,
			  new DrawerElement(R.string.drawer_callus, R.drawable.ic_info, R.drawable.ic_info_active, DrawerItemType.CALLUS) ,
			  };
		
		  protected final Context context;
		  protected Boolean selectable ;
		  protected List<Integer> selected ; 
		  protected ListView list ;
		  int selectedItemIndex = 0 ;
		  
		  public void setSelectedItemIndex(int selectedItemIndex) {
			this.selectedItemIndex = selectedItemIndex;
			notifyDataSetChanged() ;
		}
		  
		  public DrawerAdapter(Context context, ListView view ) {
		    super();
		    this.context = context;
		    
		    list = view ;
		    list.setOnItemClickListener(this);
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
			  
			View rowView ;
			if(convertView == null){  
			  LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
			  rowView = inflater.inflate(R.layout.row_drawer, parent, false);
			}else{
			  rowView = convertView ;
			}
			String title = context.getString(elements[position].stringId);
			int imRes  = elements[position].iconId;
			int color = Color.WHITE;
			
			if(selectedItemIndex == position){
				imRes  = elements[position].activeIconId;
				//color = getResources().getColor(R.color.app_theme_orange);
			}
			
		    TextView txt  = (TextView) rowView.findViewById(R.id.title);
		    ImageView icon = (ImageView) rowView.findViewById(R.id.drawable_icon);
		    
		    txt.setText(title);
		    //txt.setTextColor(color);
		    icon.setImageResource(imRes);
		    
		    return rowView;
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
			switch (elem.itemType) {
			case MAIN:
				//backToHome();
				break;
			case PROFILE:
				//switchSection(FRAG_TYPE.TODO);
				break;
			case AGENTS:
				//switchSection(FRAG_TYPE.STATS);
				break;
			case FAVOURITES:
				//switchSection(FRAG_TYPE.ABOUT);
				break;
			case CALLUS:
				break;
			}
		
		}	
}
