/**
 * 
 */
package com.adp.addvehicle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.MediaColumns;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author miriyals
 *
 */
public class UploadVehicle extends Activity {

	private String imageDirPath;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vehicle_screen);
	}
	
	/** Called when the user clicks the Send button */
	@SuppressLint({ "SdCardPath", "DefaultLocale" })
	public void openCamera(View view) {
		if(!isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE))
		{
			CharSequence text = "Error opening camera !";
	    	int duration = Toast.LENGTH_LONG;

	    	Toast toast = Toast.makeText(this, text, duration);
	    	toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
	    	toast.show();
		}
		else
		{
			try
			{
				File imageDirectory = new File("/sdcard/myprog");
				   if (!imageDirectory.isDirectory()) imageDirectory.mkdir();

				  imageDirPath = imageDirectory.toString().toLowerCase();
				  String name = imageDirectory.getName().toLowerCase();

				  ContentValues values = new ContentValues(); 
				  values.put(Media.TITLE, "Image"); 
				  values.put(Images.Media.BUCKET_ID, imageDirPath.hashCode());
				  values.put(Images.Media.BUCKET_DISPLAY_NAME,name);

				  values.put(Images.Media.MIME_TYPE, "image/jpeg");
				  values.put(Media.DESCRIPTION, "Image capture by camera");
				  values.put("_data", "/sdcard/myprog/1111.jpg");
				  Uri uri = getContentResolver().insert( Media.EXTERNAL_CONTENT_URI , values);
				  Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 

				  i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				  startActivityForResult(i, 0);
				//Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    //startActivityForResult(takePictureIntent, 0);
			    //Bundle extras = takePictureIntent.getExtras();
			    //Bitmap mImageBitmap = (Bitmap) extras.get("data");
			    //mImageView.setImageBitmap(mImageBitmap);
			}
			catch(NullPointerException ex)
			{
				CharSequence text = "Counld not get image from camera";
		    	int duration = Toast.LENGTH_LONG;

		    	Toast toast = Toast.makeText(this, text, duration);
		    	toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
		    	toast.show();
			}
		}
	}
	
	/** Called when the user clicks the Send button */
	public void browseFile(View view) {
		//Intent intent = new Intent(this, FileSelectActivity.class);
        //this.startActivity(intent);
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, 1);
		//finish();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent); 
		switch(requestCode)
		{
		case 0:
			if (resultCode == RESULT_OK) {
				GetCameraPick(intent);
			}
			else
			{
				Toast.makeText(
                        getApplicationContext(),
                        "Error!", Toast.LENGTH_LONG)
                        .show();
			}
			break;
		case 1:
			if(resultCode == RESULT_OK)
	        {  
	            Uri selectedImage = intent.getData();
	            InputStream imageStream = null;
				try {
					imageStream = getContentResolver().openInputStream(intent.getData());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            Bitmap selectedContactPicture = BitmapFactory.decodeStream(imageStream);
	            UpdateImagePath(selectedImage, selectedContactPicture);
	            //InputStream imageStream = null;
	            //try 
	            //{
	                //imageStream = getContentResolver().openInputStream(intent.getData());
	            //} 
	            //catch (FileNotFoundException e) 
	            //{
	                //e.printStackTrace();
	            //}
	            //Bitmap selectedContactPicture = BitmapFactory.decodeStream(imageStream);
	            //setContactPicture.setBackgroundDrawable(new BitmapDrawable(selectedContactPicture));
	            //ImageView mImageView = new ImageView(getApplicationContext());
			    //mImageView.setImageBitmap(selectedContactPicture);
			    //setContentView(mImageView);
	        }
			else
			{
				Toast.makeText(
                        getApplicationContext(),
                        "Error!" + resultCode, Toast.LENGTH_LONG)
                        .show();
			}
			break;
		}
		
	}
	
	private void GetCameraPick(Intent intent)
	{
		try
		{
			Bundle extras = intent.getExtras();
			Uri uri = Uri.parse(extras.get(MediaStore.EXTRA_OUTPUT).toString());
			Bitmap mImageBitmap = (Bitmap) extras.get("data");
			UpdateImagePath(uri, mImageBitmap);
		    //Bitmap mImageBitmap = (Bitmap) extras.get("data");
		    //ImageView mImageView = new ImageView(getApplicationContext());
		    //mImageView.setImageBitmap(mImageBitmap);
		    //setContentView(mImageView);
		}
		catch(NullPointerException ex)
		{
			CharSequence text = "Counld not get image from camera";
	    	int duration = Toast.LENGTH_LONG;

	    	Toast toast = Toast.makeText(this, text, duration);
	    	toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
	    	toast.show();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void UpdateImagePath(Uri path, Bitmap bm)
	{
		ImageView editText = (ImageView)findViewById(R.id.iv_image);
		ContentResolver contentResolver = getContentResolver();
		String path1 = getFilePathFromContentUri(path);
		editText.setImageBitmap(bm);
		editText.setTag(path);
	}
	
	private boolean isIntentAvailable(String action) {
	    final PackageManager packageManager = this.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	private String getFilePathFromContentUri(Uri selectedUri) {
	    String filePath = null;
	    String[] filePathColumn = {MediaColumns.DATA};

	    Cursor cursor = getContentResolver().query(selectedUri, new String[]{Media.DATA, Media.DATE_ADDED, MediaStore.Images.ImageColumns.ORIENTATION}, Media.DATE_ADDED, null, "date_added ASC");
		if(cursor != null && cursor.moveToFirst())
		{
		    do {
		        Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(Media.DATA)));
		        filePath = uri.toString();
		    }while(cursor.moveToNext());
		    cursor.close();
		}
		return filePath;
	}

}
