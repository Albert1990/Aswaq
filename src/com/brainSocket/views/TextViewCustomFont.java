package com.brainSocket.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.brainSocket.aswaq.R;

public class TextViewCustomFont extends TextView{
	
	// fonts
		private static Typeface tfDinarOneLight = null;
		private static Typeface tfSCDubai = null;
	
	public TextViewCustomFont(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	public TextViewCustomFont(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	
	private void init(AttributeSet attrs) 
	{ 
		try {
			if(!isInEditMode()) {
				// get the typed array for the custom attrs
			    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
			    // get fontId set in the XML
			    int fontId = a.getInteger(R.styleable.CustomFontTextView_fontId, 0);
			    // check fontId if equal to any or the predefined ids for the custom fonts
			    switch (fontId) {
				case 1:
					this.setTypeface(getTFRegular(getContext()));
					break;	
				case 2:
					this.setTypeface(getTFBold(getContext()));
					break;	
				default:	
					break;
				}

			    //Don't forget this
			    a.recycle();
			}
		}
		catch (Exception e) {}
	}
	
	public static Typeface getTFRegular(Context context)
	{
		try {
			if(tfDinarOneLight == null) {
				tfDinarOneLight = Typeface.createFromAsset(context.getAssets(), "fonts/GE_Dinar_One_Light.otf");
			}
		} 
		catch (Exception e) {
			tfDinarOneLight = Typeface.DEFAULT;
		}
		return tfDinarOneLight;
	}

	
	/**
	 * Returns SC Duabi typeface used in the app
	 * @return
	 */
	public static Typeface getTFBold(Context context)
	{
		try {
			if(tfSCDubai == null) {
				tfSCDubai = Typeface.createFromAsset(context.getAssets(), "fonts/SC_DUBAI.ttf");
			}
		} 
		catch (Exception e) {
			tfSCDubai = Typeface.DEFAULT;
		}
		return tfSCDubai;
	}
}
