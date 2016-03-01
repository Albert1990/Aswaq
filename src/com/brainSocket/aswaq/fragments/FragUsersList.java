package com.brainSocket.aswaq.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.UserPageActivity;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.PhotoProvider;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.models.AppUser;

public class FragUsersList extends Fragment{
	
    public enum FRAG_USERS_TYPE {FOLLOWERS, FOLLOWING, SEARCH_RESULTS }

    LayoutInflater inflater;
    ArrayList<AppUser> arrayUsers;
    ListView listView;
    View vNoDataPlaceHolder;

    CollectionsAdapter adapter;
    
    public FragUsersList()
    {
    	super();
    }

    public static FragUsersList newInstance(ArrayList<AppUser> collections, FRAG_USERS_TYPE type){
        FragUsersList frag = new FragUsersList();
        Bundle extras = new Bundle();
        if(collections != null) {
            JSONArray jsonCollections = new JSONArray();
            for (int i = 0; i < collections.size(); i++){
                jsonCollections.put(collections.get(i).getJsonObject());
            }
            extras.putString("apps",jsonCollections.toString());
            extras.putSerializable("type",type);
        }
        frag.setArguments(extras);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_list_view, null, false);
        this.inflater = inflater;
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();

        // read passed data
        try {
            String strApps = getArguments().getString("apps");
            JSONArray jsnArrayApps = new JSONArray(strApps);
            ArrayList<AppUser> arrayApps = new ArrayList<AppUser>();
            for (int i = 0; i < jsnArrayApps.length(); i++) {
                 arrayApps.add(new AppUser(jsnArrayApps.getJSONObject(i)));
            }
            onDataReceived(arrayApps);
        }catch (Exception e){
            onDataReceived(null);
        }
    }

    private void init() {
        listView = (ListView) getView().findViewById(R.id.list);
        vNoDataPlaceHolder = getView().findViewById(R.id.vNoDataPlaceHolder);

        // Sticky List init
        listView.setFastScrollEnabled(false);
        listView.setFastScrollAlwaysVisible(false);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        adapter = new CollectionsAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
        
        //DataStore.getInstance().attemptGetClients(getClientsCallback);
    }

    public void updateData(ArrayList<AppUser> apps){
        onDataReceived(apps);
    }

    private void onDataReceived(ArrayList<AppUser> collections){
        try {
            this.arrayUsers = collections;
            adapter.updateAdapter();
            if(arrayUsers != null && !arrayUsers.isEmpty()) {
                vNoDataPlaceHolder.setVisibility(View.GONE);
            }else{
                vNoDataPlaceHolder.setVisibility(View.VISIBLE);
            }
        }catch (Exception ignored){}
    }

    public class CollectionsAdapter extends BaseAdapter implements OnItemClickListener {
        private ArrayList<LineItem> mItems;

        public CollectionsAdapter() {
        }

        public void updateAdapter() {

            mItems = new ArrayList<LineItem>();
            if(arrayUsers == null)
                return;

            for (int i = 0; i < arrayUsers.size(); i++) {
                AppUser prods = arrayUsers.get(i);
                mItems.add( new LineItem(prods));
            }
            notifyDataSetChanged();
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            AppUser selectedUser = mItems.get(arg2).product;
            if(arrayUsers != null ){
                Intent i = new Intent(getActivity(), UserPageActivity.class);
                i.putExtra("userId", selectedUser.getId());
                startActivity(i);
            }
        }

        @Override
        public int getCount() {
            if (mItems != null)
                return mItems.size();
            return 0;
        }

        @Override
        public Object getItem(int arg0) {
            return mItems.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup parent) {
            ProductViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user, parent, false);
                holder = new ProductViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ProductViewHolder) convertView.getTag();
            }
            try {
                LineItem item = mItems.get(arg0);
                holder.bindItem(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    private static class LineItem {
        public AppUser product;

        public LineItem(AppUser prod) {
            this.product = prod;
        }
    }

    class ProductViewHolder {

        TextView tvName1, btnFollow;
        ImageView ivPic;
        RatingBar rbUserRate;

        ProductViewHolder(View view) {
            tvName1 = (TextView) view.findViewById(R.id.tvName);
            ivPic = (ImageView) view.findViewById(R.id.ivUser);
            btnFollow = (TextView) view.findViewById(R.id.btnFollow);
            rbUserRate = (RatingBar) view.findViewById(R.id.rbUserRate);
        }

        public void bindItem(LineItem mItems) {
            try {
                AppUser user = mItems.product;
                tvName1.setText(user.getName());
                rbUserRate.setRating(user.getRate());
                String photo_path=AswaqApp.getImagePath(ImageType.User, user.getPicture());
        		PhotoProvider.getInstance().displayPhotoNormal(photo_path, ivPic);
                
            } catch (Exception ignored) {}
        }
    }

}
