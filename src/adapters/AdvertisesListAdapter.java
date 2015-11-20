package adapters;

import java.util.List;

import com.brainSocket.aswaq.R;
import com.brainSocket.models.AdvertiseModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdvertisesListAdapter extends BaseAdapter{
	private Context context;
	private List<AdvertiseModel> ads;
	
	public AdvertisesListAdapter(Context context,List<AdvertiseModel> ads)
	{
		this.context=context;
		this.ads=ads;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ads.size();
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
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		if(convertView==null)
			convertView=inflater.inflate(R.layout.item_list_ad, parent, false);
		TextView lblAdvertiseUserName=(TextView)convertView.findViewById(R.id.lblAdvertiseUserName);
		TextView lblAdvertiseDescription=(TextView)convertView.findViewById(R.id.lblAdvertiseDescription);
		
		lblAdvertiseUserName.setText("soso");
		lblAdvertiseDescription.setText(ads.get(position).getDescription());
		
		return convertView;
	}
	

}
