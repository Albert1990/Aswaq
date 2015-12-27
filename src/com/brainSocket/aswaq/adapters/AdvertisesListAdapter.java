package com.brainSocket.aswaq.adapters;

import java.util.List;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.data.PhotoProvider;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.models.AdvertiseModel;
import com.brainSocket.aswaq.views.TextViewCustomFont;

import android.content.Context;
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
			inflater = (LayoutInflater) context
					.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			ViewHolderItem viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_list_ad_full,
						parent, false);
				viewHolder = new ViewHolderItem();
				viewHolder.ivProduct = (ImageView) convertView
						.findViewById(R.id.ivProd);
				viewHolder.rbUserRate = (RatingBar) convertView
						.findViewById(R.id.rbUserRate);
				viewHolder.tvUserName = (TextViewCustomFont) convertView
						.findViewById(R.id.tvUserName);
				viewHolder.tvAdvertiseDescription = (TextViewCustomFont) convertView
						.findViewById(R.id.tvAdvertiseDescription);
				viewHolder.tvPrice = (TextViewCustomFont) convertView
						.findViewById(R.id.tvPrice);
				viewHolder.tvPaid = (TextViewCustomFont) convertView
						.findViewById(R.id.tvPaid);
				convertView.setTag(viewHolder);
			} else
				viewHolder = (ViewHolderItem) convertView.getTag();

			// viewHolder.ivProduct.setImageResource(R.drawable.ic_launcher);
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
//			viewHolder.tvPrice.setText(ads.get(position).getPriceWithUnit());
//			if (ads.get(position).IsPinned() == 0)
//				viewHolder.tvPaid.setVisibility(View.INVISIBLE);
//			else
//				viewHolder.tvPaid.setVisibility(View.VISIBLE);
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

	}

}
