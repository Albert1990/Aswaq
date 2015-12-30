package com.brainSocket.aswaq.adapters;

import java.util.List;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.data.PhotoProvider;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.models.CategoryModel;
import com.brainSocket.aswaq.views.TextViewCustomFont;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

public class MainCategoriesListAdapter extends BaseAdapter {
	private Context context;
	private List<CategoryModel> categories;
	private LayoutInflater inflater;

	public MainCategoriesListAdapter(Context context,
			List<CategoryModel> categories) {
		this.context = context;
		this.categories = categories;
		inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return categories.size();
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
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_main_grid_category,
					parent, false);
			viewHolder = new ViewHolderItem();
			viewHolder.lblCategoryName = (TextViewCustomFont) convertView
					.findViewById(R.id.lblCategoryName);
			viewHolder.ivIcon = (ImageView) convertView
					.findViewById(R.id.ivIcon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderItem) convertView.getTag();
		}
		String imgPath = AswaqApp.getImagePath(ImageType.Category, categories
				.get(position).getPhotoPath());// ServerAccess.IMAGE_SERVICE_URL+"categories/"+(categories.get(position).getPhotoPath());
		PhotoProvider.getInstance().displayPhotoNormal(imgPath,
				viewHolder.ivIcon);
		viewHolder.lblCategoryName.setText(categories.get(position).getName());
		return convertView;
	}

	public static class ViewHolderItem {
		public TextViewCustomFont lblCategoryName;
		public ImageView ivIcon;
	}

}
