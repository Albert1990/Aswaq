package adapters;

import java.util.List;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.enums.ImageType;
import com.brainSocket.models.SlideModel;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SliderAdapter extends PagerAdapter{
	private List<SlideModel> slides;
	private Context context;
	private LayoutInflater inflater;
	
	public SliderAdapter(Context context,List<SlideModel> slides)
	{
		this.context=context;
		this.inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.slides=slides;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.item_slider, container, false);
		ImageView imgSlider=(ImageView)v.findViewById(R.id.imgSlide);
		String photo_path=AswaqApp.getImagePath(ImageType.Slide, slides.get(position).getPhoto_path());
		Picasso.with(context).load(photo_path).into(imgSlider);
		container.addView(v);
		return v;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return slides.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object view) {
		// TODO Auto-generated method stub
		//super.destroyItem(container, position, object);
		container.removeView((View)view);
	}

}
