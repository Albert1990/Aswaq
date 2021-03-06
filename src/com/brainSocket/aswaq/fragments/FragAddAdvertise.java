package com.brainSocket.aswaq.fragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brainSocket.aswaq.AswaqApp;
import com.brainSocket.aswaq.HomeCallbacks;
import com.brainSocket.aswaq.MainActivity;
import com.brainSocket.aswaq.R;
import com.brainSocket.aswaq.data.DataCacheProvider;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.PageTransitionCallback;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.dialogs.DiagCategories;
import com.brainSocket.aswaq.dialogs.DiagFacebookPage;
import com.brainSocket.aswaq.dialogs.DiagPickPhoto;
import com.brainSocket.aswaq.dialogs.DiagPickPhoto.PickDiagActions;
import com.brainSocket.aswaq.dialogs.DiagPickPhoto.PickDiagCallBack;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.models.CategoryModel;
import com.brainSocket.aswaq.views.EditTextCustomFont;
import com.brainSocket.aswaq.views.TextViewCustomFont;

public class FragAddAdvertise extends Fragment implements OnClickListener{
	private HomeCallbacks homeCallback;
	private EditTextCustomFont txtProductDescription;
	private EditTextCustomFont tvPrice;
	private TextView tvRadioButtonUsed, tvRadioButtonNew; 
	private TextViewCustomFont btnSubmit;
	private ImageView btnImg1;
	private ImageView btnImg2;
	private ImageView btnImg3;
	private ImageView btnImg4;
	private ImageView selectedImageView;
	private TextViewCustomFont tvCategory;
	private EditTextCustomFont txtAddress;
	private TextViewCustomFont btnAddPhone;
	private LinearLayout vPhoneNumbersContainer;
	private TextViewCustomFont btnAddYourPage;
	private LayoutInflater inflater;
	int selectedCategoryId = -1;
	String[] imagesURI={null,null,null,null};
	private String facebookPageLink="";
	private String facebooKPrefixLink="https://www.facebook.com/";
	
	// data
	boolean isNew = false;
	
	///temp 
	Uri outputFileUri; //holder for the image picked from the camera
	
	
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
		homeCallback.setTitle("");
		inflater=(LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		AppUser me=DataStore.getInstance().getMe();
		
		txtProductDescription=(EditTextCustomFont)getView().findViewById(R.id.txtProductDescription);
		tvPrice=(EditTextCustomFont)getView().findViewById(R.id.tvPrice);
		
		txtAddress=(EditTextCustomFont)getView().findViewById(R.id.txtAddress);
		txtAddress.setText(me.getAddress());
		
		tvRadioButtonUsed = (TextView) getView().findViewById(R.id.tvRadioButtonUsed);
		tvRadioButtonNew = (TextView) getView().findViewById(R.id.tvRadioButtonNew);
		tvRadioButtonNew.setOnClickListener(this);
		tvRadioButtonUsed.setOnClickListener(this);
		btnSubmit=(TextViewCustomFont)getView().findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		
		btnImg1=(ImageButton)getView().findViewById(R.id.btnImg1);
		btnImg1.setTag(0);
		btnImg1.setOnClickListener(this);
		
		btnImg2=(ImageButton)getView().findViewById(R.id.btnImg2);
		btnImg2.setTag(1);
		btnImg2.setOnClickListener(this);
		
		btnImg3=(ImageButton)getView().findViewById(R.id.btnImg3);
		btnImg3.setTag(2);
		btnImg3.setOnClickListener(this);
		
		btnImg4=(ImageButton)getView().findViewById(R.id.btnImg4);
		btnImg4.setTag(3);
		btnImg4.setOnClickListener(this);
		
		tvCategory=(TextViewCustomFont)getView().findViewById(R.id.tvCategory);
		tvCategory.setOnClickListener(this);
		
		vPhoneNumbersContainer=(LinearLayout)getView().findViewById(R.id.vPhoneNumbersContainer);
		
		btnAddYourPage=(TextViewCustomFont)getView().findViewById(R.id.btnAddYourPage);
		btnAddYourPage.setOnClickListener(this);
		
		btnAddPhone=(TextViewCustomFont)getView().findViewById(R.id.btnAddPhone);
		btnAddPhone.setOnClickListener(this);
		
		if(me.getPhoneNum() != null && !me.getPhoneNum().isEmpty())
		{
			String localizeMobileNumber=AswaqApp.localizeMobileNumber(me.getPhoneNum());
			addPhoneNumberView(localizeMobileNumber);
		}
		if(me.getFacebookPage() != null && !me.getFacebookPage().isEmpty())
		{
			facebookPageLink=me.getFacebookPage();
			btnAddYourPage.setText(facebooKPrefixLink+ me.getFacebookPage());
		}
		// initial State
		setProductNew(false);
		txtProductDescription.requestFocus();
	}
	
	private void addNewAdvertise()
	{
		try
		{
		boolean cancel=false;
		View focusView=null;
		//check description textbox
		String description=txtProductDescription.getText().toString();
		String address=txtAddress.getText().toString();
		int isUsed = 1;
		if(isNew)
			isUsed=0;
		int price=0;
		
		JSONArray telephones=new JSONArray();
		
		int phoneNumberCount=vPhoneNumbersContainer.getChildCount();
		if(phoneNumberCount <= 1)
		{
			homeCallback.showToast(getString(R.string.error_phone_required));
			cancel=true;
		}
		else
		{
			//hello
			EditTextCustomFont firstPhoneNumber=(EditTextCustomFont)vPhoneNumbersContainer.getChildAt(1).findViewById(R.id.tvPhone);
			String firstPhone=firstPhoneNumber.getText().toString();
			if(AswaqApp.isEmptyOrNull(firstPhone))
			{
				firstPhoneNumber.setError(getString(R.string.error_phone_required));
				focusView=firstPhoneNumber;
				cancel=true;
			}
		}
		
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
		
		if(selectedCategoryId == -1)
		{
			tvCategory.setError(getString(R.string.error_chose_suitable_category));
			focusView=tvCategory;
			cancel=true;
		}
		
		
		if(cancel){
			if(focusView!=null)
				focusView.requestFocus();
		}else{
			boolean hasOnePhotoAtLeast=true;//false;
//			for(int i=0;i<imagesURI.length;i++)
//			{
//				if(imagesURI[i]!=null)
//					hasOnePhotoAtLeast=true;
//			}
			if(hasOnePhotoAtLeast)
			{
				// we have to collect all inserted phone numbers
				for(int i=1;i<phoneNumberCount;i++)
				{
					String phone=((EditTextCustomFont)vPhoneNumbersContainer.getChildAt(i).findViewById(R.id.tvPhone)).getText().toString();
					telephones.put(phone);
				}
			homeCallback.showProgress(true);
			DataStore.getInstance().attemptAddNewAdvertise(description,address,
					selectedCategoryId,
					isUsed,price,telephones,facebookPageLink,imagesURI,addNewAdvertiseCallback);
			}
			else
				homeCallback.showToast(getString(R.string.error_photo_required));
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
			homeCallback.showProgress(false);
			if(success){
				if(data.getFlag()==ServerAccess.ERROR_CODE_done){
					MainActivity mainActivity=(MainActivity)getActivity();
					if(!mainActivity.isStopedOrPaused)
					{
						homeCallback.loadFragment(FragmentType.Main,null);
						homeCallback.showToast(getString(R.string.toast_ad_has_been_added));
					}
					else
					{
						mainActivity.AdvertiseUploadedSuccessfully=true;
					}
				}
				else if(data.getFlag()==ServerAccess.ERROR_CODE_photos_upload_error)
				{
					homeCallback.showToast(getString(R.string.frag_add_ad_error_upload_photos));
				}
				else
				{
					homeCallback.showToast(getString(R.string.error_connection_error));
				}
			}
			else
			{
				homeCallback.showProgress(false);
				homeCallback.showToast(getString(R.string.error_connection_error));
			}
			
		}
	};
	
//	private DataRequestCallback adPhotosUploadCallback=new DataRequestCallback() {
//		
//		@Override
//		public void onDataReady(ServerResult data, boolean success) {
//			homeCallback.showProgress(false);
//			if(success)
//			{
//				if(data.getFlag()==ServerAccess.ERROR_CODE_done)
//				{
//					MainActivity mainActivity=(MainActivity)getActivity();
//					if(!mainActivity.isStopedOrPaused)
//					{
//						homeCallback.loadFragment(FragmentType.Main,null);
//						homeCallback.showToast(getString(R.string.toast_ad_has_been_added));
//					}
//					else
//					{
//						mainActivity.AdvertiseUploadedSuccessfully=true;
//					}
//				}
//				else if(data.getFlag()==ServerAccess.ERROR_CODE_photos_upload_error)
//				{
//					homeCallback.showToast(getString(R.string.frag_add_ad_error_upload_photos));
//				}
//				else
//				{
//					homeCallback.showToast(getString(R.string.error_connection_error));
//				}
//			}
//			else
//			{
//				homeCallback.showToast(getString(R.string.error_connection_error));
//			}
//			
//		}
//	};
	
//	private void browseImage(View v){
//		selectedImageView=(ImageView)v;
//		Intent intent = new Intent();
//		intent.setType("image/*");
//		intent.setAction(Intent.ACTION_GET_CONTENT);
//		startActivityForResult(Intent.createChooser(intent, "Select Picture"),200);
//	}
	
	private void setProductNew(boolean isNew){
		if(isNew){
			tvRadioButtonNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_active, 0);
			tvRadioButtonUsed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio, 0);
		}else{
			tvRadioButtonNew.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio, 0);
			tvRadioButtonUsed.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_radio_active, 0);
		}
		this.isNew = isNew;
	}
	
	private void browseImage(final View v){

		final DiagPickPhoto diag;
        diag = new DiagPickPhoto(getActivity(), new PickDiagCallBack() {
            @Override
            public void onActionChoose(PickDiagActions action) {
                switch (action){
                    case CAMERA:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        outputFileUri = Uri.fromFile(getNewTempImgFile(false));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(intent,AswaqApp.REQUEST_PICK_IMG_FROM_CAMERA);
                		selectedImageView = (ImageView) v;
                        break;
                    case GALLERY:
                        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intentGallery.setType("image/*");
                        startActivityForResult(Intent.createChooser(intentGallery, "Select File"),AswaqApp.REQUEST_PICK_IMG_FROM_GALLERY);
                		selectedImageView = (ImageView) v;
                        break;
                    case CANCEL:
                        //uriImgInvoice = null;
                        break;
                }
            }
        });
        diag.show();

		// Determine Uri of camera image to save.

//		    // Camera.
//		    final List<Intent> cameraIntents = new ArrayList<Intent>();
//		    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//		    final PackageManager packageManager = getActivity().getPackageManager();
//		    final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//		    for(ResolveInfo res : listCam) {
//		        final String packageName = res.activityInfo.packageName;
//		        final Intent intent = new Intent(captureIntent);
//		        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//		        intent.setPackage(packageName);
//		    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//		        cameraIntents.add(intent);
//		    }
//
//		    // Filesystem.
//		    final Intent galleryIntent = new Intent();
//		    galleryIntent.setType("image/*");
//		    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//		    // Chooser of filesystem options.
//		    final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");
//
//		    // Add the camera options.
//		    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
//
//		    startActivityForResult(chooserIntent, PICK_IMG_REQUEST_CODE);
		}
	
	private static File getNewTempImgFile(boolean isForUpload){
		final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "aswaq_temp_imgs" + File.separator);
		root.mkdirs();
		final String fname = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ((isForUpload)?"_resized":"")+ ".jpg";
		File sdImageMainDirectory = new File(root, fname);
		return sdImageMainDirectory;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try{
			
			if(resultCode == Activity.RESULT_OK){
				String filePath = null;
	            if(requestCode ==AswaqApp.REQUEST_PICK_IMG_FROM_CAMERA || requestCode == AswaqApp.REQUEST_PICK_IMG_FROM_GALLERY){
		            if(requestCode ==AswaqApp.REQUEST_PICK_IMG_FROM_CAMERA){
		            	filePath = outputFileUri.getPath();
		            }else if(requestCode == AswaqApp.REQUEST_PICK_IMG_FROM_GALLERY){
		                Uri uri = data.getData();
		                if (uri.getScheme() != null && uri.getScheme().equals("file")) {
		                	filePath = uri.getPath();
		                }else{
		                	String[] filePathField = {MediaStore.Images.Media.DATA};
		                	Cursor cursor = getActivity().getContentResolver().query(uri, filePathField, null, null, null);
		                    if (cursor == null) {
		                        throw new IllegalArgumentException("got null cursor when attempting to find path for external storage uri");
		                    }

		                    cursor.moveToFirst();
		                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		                    filePath = cursor.getString(column_index);
		                    
//		                    Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
//		                    cursor.moveToFirst();
//		                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//		                    String path = cursor.getString(idx);
//		                    uri = Uri.parse(path);
		                }
		            }
		            
		            if(filePath != null && !filePath.isEmpty()){
		            	filePath = new File(filePath).getAbsolutePath(); // make sure we have a valid absolute path
			            Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
			            int imageIndex = (Integer) selectedImageView.getTag();
			            String resizedImgPath = getNewTempImgFile(true).getAbsolutePath();
			            AswaqApp.resizeImage(yourSelectedImage, resizedImgPath,1400);
			            imagesURI[imageIndex] = resizedImgPath;
			            selectedImageView.setImageBitmap(yourSelectedImage);
		            }
	            }
	        }else if(requestCode == AswaqApp.REQUEST_PICK_IMG_FROM_CAMERA ){
	            // if picking a picture from camera failed or canceled then reset the URi
                outputFileUri = null;
            }     
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public String getRealPathFromURI(Uri uri) {
		Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null); 
		cursor.moveToFirst(); 
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
		return cursor.getString(idx); 
	}

//	 public String getPath(Uri uri, Activity activity) {
//	        String[] projection = {MediaStore.MediaColumns.DATA};
//	        Cursor cursor = activity
//	                .managedQuery(uri, projection, null, null, null);
//	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//	        cursor.moveToFirst();
//	        return cursor.getString(column_index);
//	    }
	
	private void addPhoneNumberView(String phoneNumber)
	{
		try
		{
			View phoneRowView=inflater.inflate(R.layout.row_phone_number, vPhoneNumbersContainer, false);
			vPhoneNumbersContainer.addView(phoneRowView);
			EditTextCustomFont txtMob=(EditTextCustomFont)phoneRowView.findViewById(R.id.tvPhone);
			if(phoneNumber!=null)
			{
				txtMob.setText(phoneNumber);
			}
			else
				txtMob.requestFocus();
			View btnDeletePhoneNumber=phoneRowView.findViewById(R.id.btnDel);
			btnDeletePhoneNumber.setOnClickListener(this);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private PageTransitionCallback facebookPageCallback=new PageTransitionCallback() {
		
		@Override
		public void onDataReady(HashMap<String, Object> params) {
			try
			{
				if(params.containsKey("facebookPageLink"))
				{
					facebookPageLink=facebooKPrefixLink+(String)params.get("facebookPageLink");
					btnAddYourPage.setText(facebookPageLink);
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	};
	
	private void removePhoneNumberView(View v)
	{
		try
		{
			vPhoneNumbersContainer.removeView((View)v.getParent());
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
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
		case R.id.tvRadioButtonNew:
			setProductNew(true);
			break;
		case R.id.tvRadioButtonUsed:
			setProductNew(false);
			break;
		case R.id.btnAddPhone:
			addPhoneNumberView(null);
			break;
		case R.id.btnDel:
			removePhoneNumberView(v);
			break;
		case R.id.btnAddYourPage:
			DiagFacebookPage diagFacebookPage=new DiagFacebookPage(facebookPageCallback);
			diagFacebookPage.show(getFragmentManager(), "");
			break;
		}
	}
	
	private DataRequestCallback onCategorySelectedCallback = new DataRequestCallback() {
		
		@Override
		public void onDataReady(ServerResult data, boolean success) {
			try
			{
				CategoryModel selectedCategory = (CategoryModel)data.getValue("selectedCategory");
				selectedCategoryId = selectedCategory.getId();
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
