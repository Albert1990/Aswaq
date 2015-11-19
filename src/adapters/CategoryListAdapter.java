package adapters;

import java.util.List;

import com.brainSocket.aswaq.R;
import com.brainSocket.models.CategoryModel;
import com.brainSocket.views.TextViewCustomFont;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class CategoryListAdapter extends BaseAdapter{
	private Context context;
	private List<CategoryModel> categories;
	
	public CategoryListAdapter(Context context,List<CategoryModel> categories) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.categories=categories;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null)
			convertView=inflater.inflate(R.layout.item_main_grid_category, parent, false);
		TextViewCustomFont lblCategoryName=(TextViewCustomFont) convertView.findViewById(R.id.lblCategoryName);
		lblCategoryName.setText(categories.get(position).getName());
		return convertView;
	}

}
