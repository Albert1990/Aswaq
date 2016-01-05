package com.brainSocket.aswaq.adapters;

import java.util.List;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.data.PhotoProvider;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.models.AdvertiseModel;
import com.brainSocket.aswaq.views.TextViewCustomFont;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdvertisesListAdapter extends BaseAdapter {
	private Context context;
	private List<AdvertiseModel> ads;
	private LayoutInflater inflater;

	public AdvertisesListAdapter(Context context, List<AdvertiseModel> ads) {
		this.context = context;
		this.ads = ads;
		inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return ads.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			ViewHolderItem viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_list_ad_full,
						parent, false);
				viewHolder = new ViewHolderItem();
				viewHolder.bindViews(convertView);
				convertView.setTag(viewHolder);
			} else
				viewHolder = (ViewHolderItem) convertView.getTag();

			String imagePath = null;
			if (ads.get(position).getImages().size() > 0)
				imagePath = ads.get(position).getImages().get(0)
						.getPhoto_path();
			imagePath = AswaqApp.getImagePath(ImageType.AdThumb, imagePath);
			PhotoProvider.getInstance().displayPhotoNormal(imagePath,
					viewHolder.ivProduct);
			viewHolder.rbUserRate.setRating(ads.get(position).getUser()
					.getRate());
			viewHolder.tvUserName
					.setText(ads.get(position).getUser().getName());
			viewHolder.tvAdvertiseDescription.setText(ads.get(position)
					.getDescription());
			viewHolder.tvPrice.setText(ads.get(position).getPriceWithUnit());
			if (ads.get(position).IsPinned() == 0)
				viewHolder.tvPaid.setVisibility(View.INVISIBLE);
			else
				viewHolder.tvPaid.setVisibility(View.VISIBLE);
			if(ads.get(position).isUsed()==1)
			{
				viewHolder.tvIsUsed.setVisibility(View.INVISIBLE);//.setText(context.getString(R.string.advertise_details_lbl_used));
			}
			else
			{
				viewHolder.tvIsUsed.setVisibility(View.VISIBLE);
				viewHolder.tvIsUsed.setText(context.getString(R.string.advertise_details_lbl_new));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return convertView;
	}

	private static class ViewHolderItem {
		ImageView ivProduct;
		RatingBar rbUserRate;
		TextViewCustomFont tvUserName;
		TextViewCustomFont tvAdvertiseDescription;
		TextViewCustomFont tvPrice;
		TextViewCustomFont tvPaid;
		TextViewCustomFont tvIsUsed;

		public void bindViews(View convertView) {
			ivProduct = (ImageView) convertView.findViewById(R.id.ivProd);
			rbUserRate = (RatingBar) convertView.findViewById(R.id.rbUserRate);
			tvUserName = (TextViewCustomFont) convertView
					.findViewById(R.id.tvUserName);
			tvAdvertiseDescription = (TextViewCustomFont) convertView
					.findViewById(R.id.tvAdvertiseDescription);
			tvPaid = (TextViewCustomFont) convertView
					.findViewById(R.id.tvPaid1);
			tvPrice = (TextViewCustomFont) convertView
					.findViewById(R.id.tvPrice);
			tvIsUsed=(TextViewCustomFont)convertView.findViewById(R.id.tvIsUsed);
		}

	}

}
