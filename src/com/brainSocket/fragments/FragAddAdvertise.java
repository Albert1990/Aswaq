package com.brainSocket.fragments;

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONArray;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.R;
import com.brainSocket.data.DataRequestCallback;
import com.brainSocket.data.DataStore;
import com.brainSocket.data.ServerAccess;
import com.brainSocket.data.ServerResult;
import com.brainSocket.dialogs.DiagCategories;
import com.brainSocket.enums.FragmentType;
import com.brainSocket.models.CategoryModel;
import com.brainSocket.views.EditTextCustomFont;
import com.brainSocket.views.TextViewCustomFont;

public class FragAddAdvertise extends Fragment implements OnClickListener{
	private HomeCallbacks homeCallback;
	private EditTextCustomFont txtProductDescription;
	private EditTextCustomFont tvPrice;
	private EditTextCustomFont tvPhone;
	private Switch swhNew;
	private TextViewCustomFont btnSubmit;
	private ImageView btnImg1;
	private ImageView btnImg2;
	private ImageView btnImg3;
	private ImageView btnImg4;
	private ImageView selectedImageView;
	private TextViewCustomFont tvCategory;
	int selectedCategoryId=-1;
	String[] imagesURI={null,null,null,null};
	
	private FragAddAdvertise()
	{
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_add_advertise, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		init();
	}
	
	private void init()
	{
		homeCallback=(HomeCallbacks)getActivity();
		txtProductDescription=(EditTextCustomFont)getActivity().findViewById(R.id.txtProductDescription);
		tvPrice=(EditTextCustomFont)getActivity().findViewById(R.id.tvPrice);
		tvPhone=(EditTextCustomFont)getActivity().findViewById(R.id.tvPhone);
		swhNew=(Switch)getActivity().findViewById(R.id.swhNew);
		btnSubmit=(TextViewCustomFont)getActivity().findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		
		btnImg1=(ImageButton)getActivity().findViewById(R.id.btnImg1);
		btnImg1.setTag(0);
		btnImg1.setOnClickListener(this);
		
		btnImg2=(ImageButton)getActivity().findViewById(R.id.btnImg2);
		btnImg2.setTag(1);
		btnImg2.setOnClickListener(this);
		
		btnImg3=(ImageButton)getActivity().findViewById(R.id.btnImg3);
		btnImg3.setTag(2);
		btnImg3.setOnClickListener(this);
		
		btnImg4=(ImageButton)getActivity().findViewById(R.id.btnImg4);
		btnImg4.setTag(3);
		btnImg4.setOnClickListener(this);
		
		tvCategory=(TextViewCustomFont)getActivity().findViewById(R.id.tvCategory);
		tvCategory.setOnClickListener(this);
		
	}
	
	private void addNewAdvertise()
	{
		try
		{
		boolean cancel=false;
		View focusView=null;
		//check description textbox
		String description=txtProductDescription.getText().toString();
		boolean isUsed=swhNew.isActivated();
		int price=0;
		String phone=tvPhone.getText().toString();
		JSONArray telephones=new JSONArray();
		
		if(AswaqApp.isEmptyOrNull(description))
		{
			txtProductDescription.setError(getString(R.string.error_description_required));
			focusView=txtProductDescription;
			cancel=true;
		}
		
		if(AswaqApp.isEmptyOrNull(tvPrice.getText().toString()))
		{
			tvPrice.setError(getString(R.string.error_price_required));
			focusView=tvPrice;
			cancel=true;
		}
		else
		{
			price=Integer.parseInt(tvPrice.getText().toString());
		}
		
		if(AswaqApp.isEmptyOrNull(phone))
		{
			tvPhone.setError(getString(R.string.error_phone_required));
			focusView=tvPhone;
			cancel=true;
		}
		
		if(selectedCategoryId==-1)
		{
			tvCategory.setError(getString(R.string.error_chose_suitable_category));
			focusView=tvCategory;
			cancel=true;
		}
		
		
		if(cancel){
			if(focusView!=null)
				focusView.requestFocus();
		}else{
			telephones.put(phone);
			homeCallback.showProgress(true);
			DataStore.getInstance().attemptAddNewAdvertise(description,
					selectedCategoryId,
					isUsed,price,telephones,addNewAdvertiseCallback);
		}
	}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private DataRequestCallback addNewAdvertiseCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			if(success){
				if(data.getFlag()==ServerAccess.ERROR_CODE_done){
					int adId=(Integer)data.getValue("adId");
					DataStore.getInstance().attemptUploadAdPhotos(adId,imagesURI,adPhotosUploadCallback);
					
				}
			}
		}
	};
	
	private DataRequestCallback adPhotosUploadCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			if(data.getFlag()==ServerAccess.ERROR_CODE_done)
			{
				homeCallback.showProgress(false);
				homeCallback.loadFragment(FragmentType.Main,null);
				homeCallback.showToast(getString(R.string.toast_ad_has_been_added));
			}
		}
	};
	
	private void browseImage(View v)
	{
		selectedImageView=(ImageView)v;
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),200);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try
		{
			Uri selectedImage = data.getData();
			
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor =AswaqApp.getAppContext().getContentResolver().query(
                    selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
            int imageIndex=(Integer)selectedImageView.getTag();
			imagesURI[imageIndex]=filePath;
            AswaqApp.resizeImage(yourSelectedImage, selectedImage.toString());
            selectedImageView.setImageBitmap(yourSelectedImage);
            //yourSelectedImage.get
		}
		catch(Exception ex){ex.printStackTrace();}
	}

	@Override
	public void onClick(View v) {
		int viewId=v.getId();
		switch(viewId){
		case R.id.btnSubmit:
			addNewAdvertise();
			break;
		case R.id.btnImg1:
			browseImage(v);
			break;
		case R.id.btnImg2:
			browseImage(v);
			break;
		case R.id.btnImg3:
			browseImage(v);
			break;
		case R.id.btnImg4:
			browseImage(v);
			break;
		case R.id.tvCategory:
			DiagCategories categoriesDialog= new DiagCategories(onCategorySelectedCallback);
			categoriesDialog.show(getFragmentManager(), "Select Category");
			
			break;
		}
	}
	
	private DataRequestCallback onCategorySelectedCallback=new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			// TODO Auto-generated method stub
			try
			{
				CategoryModel selectedCategory=(CategoryModel)data.getValue("selectedCategory");
				selectedCategoryId=selectedCategory.getId();
				tvCategory.setText(selectedCategory.getName());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	};
	
	public static FragAddAdvertise newInstance(HashMap<String, Object> params)
	{
		FragAddAdvertise fragAddAdvertise=new FragAddAdvertise();
		return fragAddAdvertise;
	}

}
