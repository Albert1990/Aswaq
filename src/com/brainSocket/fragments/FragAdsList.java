package com.brainSocket.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

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

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.enums.ImageType;
import com.brainSocket.models.AdvertiseModel;
import com.brainSocket.views.TextViewCustomFont;

public class FragAdsList extends Fragment{

    LayoutInflater inflater;
    ArrayList<AdvertiseModel> arrayCollections;
    ListView listView;
    View vNoDataPlaceHolder;

    CollectionsAdapter adapter;

    public static FragAdsList newInstance(ArrayList<AdvertiseModel> apps){
        FragAdsList frag = new FragAdsList();
        Bundle extras = new Bundle();
        JSONArray jsonCollections = new JSONArray();
        for (int i = 0; i < apps.size(); i++) {
            jsonCollections.put(apps.get(i).getJsonObject());
        }
        extras.putString("apps",jsonCollections.toString());
        frag.setArguments(extras);
        return frag;
    };

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
            ArrayList<AdvertiseModel> arrayApps = new ArrayList<AdvertiseModel>();
            for (int i = 0; i < jsnArrayApps.length(); i++) {
                 arrayApps.add(new AdvertiseModel(jsnArrayApps.getJSONObject(i)));
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
    }
    
    public void updateData(ArrayList<AdvertiseModel> ads){
        onDataReceived(ads);
    }
    
    private void onDataReceived(ArrayList<AdvertiseModel> collections){
        try {
            this.arrayCollections = collections;
            adapter.updateAdapter();
            if(arrayCollections != null && !arrayCollections.isEmpty()) {
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
            if(arrayCollections == null)
                return;

            for (int i = 0; i < arrayCollections.size(); i++) {
                AdvertiseModel prods = arrayCollections.get(i);
                mItems.add( new LineItem(prods));
            }
            notifyDataSetChanged();
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//            AdvertiseModel collection = mItems.get(arg2).product;
//            if(arrayCollections != null ){
//                Intent i = new Intent(getActivity(), CollectionDetailsActiviy.class);
//                i.putExtra("collection", collection.getJsonObject().toString());
//                startActivity(i);
//            }
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
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ad_full, parent, false);
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
        public AdvertiseModel product;
        public String title; // use only in case the line item is header

        public LineItem(AdvertiseModel prod) {
            this.product = prod;
        }
    }

    class ProductViewHolder {

    	ImageView ivProduct;
		TextViewCustomFont tvUserName;
		TextViewCustomFont tvAdvertiseDescription;
		TextViewCustomFont tvPrice;
		
        ProductViewHolder(View view) {            
            ivProduct=(ImageView) view.findViewById(R.id.ivProd);
    		tvUserName=(TextViewCustomFont) view.findViewById(R.id.tvUserName);
    		tvAdvertiseDescription=(TextViewCustomFont) view.findViewById(R.id.tvAdvertiseDescription);
    		tvPrice=(TextViewCustomFont) view.findViewById(R.id.tvPrice);
        }

        public void bindItem(LineItem mItems) {
            try {
                AdvertiseModel ad = mItems.product;
                ivProduct.setImageResource(R.drawable.ic_launcher);
        		tvUserName.setText(ad.getUser().getName());
        		tvAdvertiseDescription.setText(ad.getDescription());
        		String price=Integer.toString(ad.getPrice())+getString(R.string.lbl_price_unit);
        		tvPrice.setText(price);
        		//String photo_path=AswaqApp.getImagePath(ImageType.Ad, ad.getImages().get(0));
            } catch (Exception ignored) {}
        }
    }


}