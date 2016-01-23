package com.brainSocket.aswaq;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.brainSocket.aswaq.adapters.DrawerAdapter;
import com.brainSocket.aswaq.data.DataCacheProvider;
import com.brainSocket.aswaq.data.DataRequestCallback;
import com.brainSocket.aswaq.data.DataStore;
import com.brainSocket.aswaq.data.PhotoProvider;
import com.brainSocket.aswaq.data.ServerAccess;
import com.brainSocket.aswaq.data.ServerResult;
import com.brainSocket.aswaq.dialogs.DiagPickPhoto;
import com.brainSocket.aswaq.dialogs.DiagPickPhoto.PickDiagActions;
import com.brainSocket.aswaq.dialogs.DiagPickPhoto.PickDiagCallBack;
import com.brainSocket.aswaq.enums.FragmentType;
import com.brainSocket.aswaq.enums.ImageType;
import com.brainSocket.aswaq.enums.PhoneNumberCheckResult;
import com.brainSocket.aswaq.models.AppUser;
import com.brainSocket.aswaq.views.EditTextCustomFont;
import com.brainSocket.aswaq.views.TextViewCustomFont;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class UserProfileActivity extends AppBaseActivity implements
		OnClickListener, HomeCallbacks {
	// slide drawer
	private ListView lvDrawer;
	private DrawerAdapter adapter;
	private DrawerLayout dlDrawer;
	//private View llLogout;

	// actionbar
	private TextViewCustomFont tvFragTitle;
	private ImageView ivBackHome;
	private ImageView ivMenu;

	// view members
	private EditTextCustomFont txtUserNameRegister;
	private EditTextCustomFont txtAddressRegister;
	private EditTextCustomFont txtDescriptionRegister;
	private TextViewCustomFont btnEdit;
	private EditTextCustomFont txtFacebookPage;
	private Dialog dialogLoading;
	private ImageView ivUserPhoto;
	private String selectedUserProfilePictureUri="";
	
	//temp
	private Uri outputFileUri; //holder for the image picked from the camera

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		init();
		initCustomActionBar();
		// initSlideDrawer();
	}

	private void init() {
		try {
			AppUser me = DataStore.getInstance().getMe();
			if (me != null) {
				txtUserNameRegister = (EditTextCustomFont) findViewById(R.id.txtUserNameRegister);
				txtAddressRegister = (EditTextCustomFont) findViewById(R.id.txtAddressRegister);
				txtDescriptionRegister = (EditTextCustomFont) findViewById(R.id.txtDescriptionRegister);
				txtFacebookPage=(EditTextCustomFont)findViewById(R.id.txtFacebookPage);
				btnEdit = (TextViewCustomFont) findViewById(R.id.btnEditUserProfile);
				btnEdit.setOnClickListener(this);
				ivUserPhoto=(ImageView)findViewById(R.id.ivUserPhoto);
				ivUserPhoto.setOnClickListener(this);
				txtFacebookPage.setText(me.getFacebookPage().toString());
				bindUiData(me);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void bindUiData(AppUser me) {
		txtUserNameRegister.setText(me.getName());
		txtAddressRegister.setText(me.getAddress());
		txtDescriptionRegister.setText(me.getDescription());
		
		String photo_path=AswaqApp.getImagePath(ImageType.User, me.getPicture());
		PhotoProvider.getInstance().displayPhotoNormal(photo_path, ivUserPhoto);
	}

	private void initSlideDrawer() {
		lvDrawer = (ListView) findViewById(R.id.lvDrawer);
		adapter = new DrawerAdapter(this, lvDrawer);
		lvDrawer.setAdapter(adapter);
		dlDrawer = (DrawerLayout) findViewById(R.id.dlDrawer);
//		llLogout = findViewById(R.id.llLogout);
//		llLogout.setOnClickListener(this);
	}

	private void initCustomActionBar() {
		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setDisplayUseLogoEnabled(false);
		mActionBar.setDisplayHomeAsUpEnabled(false);
		mActionBar.setHomeAsUpIndicator(null);
		mActionBar.setCustomView(R.layout.custom_actionbar);
		setActionBarColor(Color.argb(30, 0, 0, 0));
		mActionBar.setDisplayShowCustomEnabled(true);
		View mCustomView = mActionBar.getCustomView();
		mCustomView.invalidate();

		tvFragTitle = (TextViewCustomFont) mCustomView
				.findViewById(R.id.tvFragTitle);
		tvFragTitle.setText(getString(R.string.actionbar_edit_user_profile));
		ivBackHome = (ImageView) mCustomView.findViewById(R.id.ivBack);
		ivBackHome.setOnClickListener(this);
		ivMenu = (ImageView) mCustomView.findViewById(R.id.ivMenu);

		ivMenu.setVisibility(View.GONE);
	}

	public void setActionBarColor(int color) {
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
	}

	/**
	 * update content of the actionBar accourding to the current fragment
	 * 
	 * @param section
	 */
	private void updateActionbar(FragmentType section) {
		switch (section) {
		case Main:
			setActionBarColor(Color.TRANSPARENT);
			break;
		default:
			setActionBarColor(getResources().getColor(R.color.app_theme));
			break;
		}

	}

	private void toggleDrawer() {
		try {
			if (dlDrawer.isDrawerOpen(Gravity.RIGHT)) {
				dlDrawer.closeDrawer(Gravity.RIGHT);
			} else {
				dlDrawer.openDrawer(Gravity.RIGHT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closeDrawer() {
		try {
			if (dlDrawer.isDrawerOpen(Gravity.RIGHT)) {
				dlDrawer.closeDrawer(Gravity.RIGHT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateUserProfile() {

		boolean cancel = false;
		View focusView = null;

		String userName = txtUserNameRegister.getText().toString();
		String address = txtAddressRegister.getText().toString();
		String description = txtDescriptionRegister.getText().toString();
		String facebookPage=txtFacebookPage.getText().toString();

		if (AswaqApp.isEmptyOrNull(userName)) {
			txtUserNameRegister
					.setError(getString(R.string.login_error_user_name_empty));
			focusView = txtUserNameRegister;
			cancel = true;
		}

		if (cancel)
			focusView.requestFocus();
		else {
			showProgress(true);
			DataStore.getInstance().attemptUpdateUserProfile(userName,
					 address, 
					description,selectedUserProfilePictureUri,facebookPage,
					updateUserProfileCallback);
		}
	}

	private DataRequestCallback updateUserProfileCallback = new DataRequestCallback() {

		@Override
		public void onDataReady(ServerResult data, boolean success) {
			showProgress(false);
			if (success) {
				if (data.getFlag() == ServerAccess.ERROR_CODE_done) {
					AppUser recievedMe = (AppUser) data.getValue("me");
					if (recievedMe != null) {
						AppUser originalMe = DataStore.getInstance().getMe();
						originalMe.setName(recievedMe.getName());
						originalMe.setPhoneNum(recievedMe.getPhoneNum());
						originalMe.setAddress(recievedMe.getAddress());
						originalMe.setDescription(recievedMe.getDescription());
						originalMe.setPicture(recievedMe.getPicture());
						originalMe.setFacebookPage(recievedMe.getFacebookPage());
						DataCacheProvider.getInstance().storeMe(originalMe);
					}
					finish();
				} else {
					showToast(getString(R.string.error_server_error));
				}
			} else {
				showToast(getString(R.string.error_connection_error));
			}

		}
	};
	
	private static File getNewTempImgFile(boolean isForUpload){
		final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "aswaq_temp_imgs" + File.separator);
		root.mkdirs();
		final String fname = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ((isForUpload)?"_resized":"")+ ".jpg";
		File sdImageMainDirectory = new File(root, fname);
		return sdImageMainDirectory;
	}
	
	private void browseImage(final View v){

		final DiagPickPhoto diag;
        diag = new DiagPickPhoto(this, new PickDiagCallBack() {
            @Override
            public void onActionChoose(PickDiagActions action) {
                switch (action){
                    case CAMERA:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        outputFileUri = Uri.fromFile(getNewTempImgFile(false));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(intent,AswaqApp.REQUEST_PICK_IMG_FROM_CAMERA);
                        break;
                    case GALLERY:
                        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intentGallery.setType("image/*");
                        startActivityForResult(Intent.createChooser(intentGallery, "Select File"), AswaqApp.REQUEST_PICK_IMG_FROM_GALLERY);
                        break;
                    case CANCEL:
                        //uriImgInvoice = null;
                        break;
                }
            }
        });
        diag.show();
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
		                	Cursor cursor = getContentResolver().query(uri, filePathField, null, null, null);
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
			            AswaqApp.resizeImage(yourSelectedImage, getNewTempImgFile(true).getAbsolutePath(),300);
			            selectedUserProfilePictureUri = getNewTempImgFile(true).getAbsolutePath();
			            ivUserPhoto.setImageBitmap(yourSelectedImage);
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

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		switch (viewId) {
		case R.id.btnEditUserProfile:
			updateUserProfile();
			break;
		case R.id.ivBack:
			finish();
			break;
		case R.id.ivUserPhoto:
			browseImage(v);
			break;
		}
	}

	@Override
	public void showProgress(boolean show) {
		if (dialogLoading == null) {
			dialogLoading = new Dialog(this);
			dialogLoading.setCancelable(false);
			dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialogLoading.setContentView(R.layout.dialog_custom_loading);
			dialogLoading.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
		}
		if (show)
			dialogLoading.show();
		else
			dialogLoading.dismiss();
	}

	@Override
	public void showToast(String msg) {
		Toast.makeText(AswaqApp.getAppContext(), msg, Toast.LENGTH_SHORT)
		.show();
	}

	@Override
	public void setTitle(String title) {

	}

	@Override
	public void loadFragment(FragmentType fragmentType,
			HashMap<String, Object> params) {

	}

	@Override
	public void openSlideDrawer() {

	}

	@Override
	public void closeSlideDrawer() {

	}

	@Override
	public void backToHome() {

	}

}
