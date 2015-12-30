package com.brainSocket.aswaq.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.SliderPreviewerActivity;
import com.brainSocket.aswaq.data.PhotoProvider;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.enums.SliderType;
import com.brainSocket.aswaq.models.SlideModel;

public class SliderAdapter extends PagerAdapter implements OnClickListener {
	private List<SlideModel> slides;
	private Context context;
	private LayoutInflater inflater;
	private SliderType sliderType;

	public SliderAdapter(Context context, List<SlideModel> slides,
			SliderType sliderType) {
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.slides = slides;
		this.sliderType = sliderType;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View v = null;

		try {
			v = inflater.inflate(R.layout.item_slider, container, false);
			ImageView imgSlider = (ImageView) v.findViewById(R.id.imgSlide);

			ImageType imgType = ImageType.Slide;
			if (sliderType == SliderType.Advertise)
				imgType = ImageType.Ad;
			String photo_path = AswaqApp.getImagePath(imgType,
					slides.get(position).getPhoto_path());
			imgSlider.setTag(photo_path);
			imgSlider.setOnClickListener(this);
			PhotoProvider.getInstance().displayPhotoNormal(photo_path,
					imgSlider);
			container.addView(v);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return v;
	}

	@Override
	public int getCount() {
		return slides.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object view) {
		container.removeView((View) view);
	}

	private void previewSlideImage(String photoPath) {
		try {
			Intent i = new Intent(context, SliderPreviewerActivity.class);
			i.putExtra("photoPath", photoPath);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.imgSlide:
			String photoPath = v.getTag().toString();
			previewSlideImage(photoPath);
			break;
		}
	}

}
