/**
 * 
 */
package com.adp.addvehicle;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author miriyals
 *
 */
public class UploadVehicle extends Activity {
	    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vehicle_screen);
	}
	
	/** Called when the user clicks the Send button */
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
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    startActivityForResult(takePictureIntent, 0);
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
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent); 
		switch(requestCode)
		{
		case 0:
			GetCameraPick(intent);
			break;
		case 1:
			if(resultCode == RESULT_OK)
	        {  
	            //Uri selectedImage = intent.getData();
	            InputStream imageStream = null;
	            try 
	            {
	                imageStream = getContentResolver().openInputStream(intent.getData());
	            } 
	            catch (FileNotFoundException e) 
	            {
	                e.printStackTrace();
	            }
	            Bitmap selectedContactPicture = BitmapFactory.decodeStream(imageStream);
	            //setContactPicture.setBackgroundDrawable(new BitmapDrawable(selectedContactPicture));
	            ImageView mImageView = new ImageView(getApplicationContext());
			    mImageView.setImageBitmap(selectedContactPicture);
			    setContentView(mImageView);
	        }
			break;
		}
		
	}
	
	private void GetCameraPick(Intent intent)
	{
		try
		{
			Bundle extras = intent.getExtras();
		    Bitmap mImageBitmap = (Bitmap) extras.get("data");
		    ImageView mImageView = new ImageView(getApplicationContext());
		    mImageView.setImageBitmap(mImageBitmap);
		    setContentView(mImageView);
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
	
	public boolean isIntentAvailable(String action) {
	    final PackageManager packageManager = this.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}

}
