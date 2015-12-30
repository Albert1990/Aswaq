package com.brainSocket.aswaq.adapters;

import java.util.List;

import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.models.CategoryModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SubCategoriesListAdapter extends BaseAdapter {
	private Context context;
	private List<CategoryModel> subCategories;
	private LayoutInflater inflater;

	public SubCategoriesListAdapter(Context context,
			List<CategoryModel> subCategories) {
		this.context = context;
		this.subCategories = subCategories;
		inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return subCategories.size();
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
		ViewHolderItem viewHolder = null;
		inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_list_sub_category,
					parent, false);
			viewHolder = new ViewHolderItem();
			viewHolder.lblSubCategoryName = (TextView) convertView
					.findViewById(R.id.lblSubCategoryName);
			;
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderItem) convertView.getTag();
		}
		viewHolder.lblSubCategoryName.setText(subCategories.get(position)
				.getName());
		return convertView;
	}

	public static class ViewHolderItem {
		public TextView lblSubCategoryName;
	}

}
