package com.brainSocket.adapters;

import java.util.List;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.PhotoProvider;
import com.brainSocket.enums.ImageType;
import com.brainSocket.enums.SliderType;
import com.brainSocket.models.SlideModel;

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
	private SliderType sliderType;
	
	public SliderAdapter(Context context,List<SlideModel> slides,SliderType sliderType)
	{
		this.context=context;
		this.inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.slides=slides;
		this.sliderType=sliderType;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.item_slider, container, false);
		ImageView imgSlider=(ImageView)v.findViewById(R.id.imgSlide);
		ImageType imgType=ImageType.Slide;
		if(sliderType==SliderType.Advertise)
			imgType=ImageType.Ad;
		String photo_path=AswaqApp.getImagePath(imgType, slides.get(position).getPhoto_path());
		//Picasso.with(context).load(photo_path).into(imgSlider);
		PhotoProvider.getInstance().displayPhotoNormal(photo_path, imgSlider);
//		if((position %2) == 0){
//			imgSlider.setImageResource(R.drawable.banner1);
//		}else{
//			imgSlider.setImageResource(R.drawable.banner2);
//		}
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
