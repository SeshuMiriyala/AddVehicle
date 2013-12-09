/**
 * 
 */
package com.adp.addvehicle;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.provider.MediaStore.Images.Media;
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

	private Uri myPicture;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_vehicle_screen);
	}
	
	/** Called when the user clicks the Send button */
	@SuppressLint({ "SdCardPath", "DefaultLocale" })
	public void openCamera(View view) {
		if(!isIntentAvailable(MediaStore.ACTION_IMAGE_CAPTURE))
		{
			msbox("Error!", "Error opening camera!");
		}
		else
		{
			try
			{
				ContentValues values = new ContentValues();
	            values.put(Media.TITLE, "My demo image");
	            values.put(Media.DESCRIPTION, "Image Captured by Camera via an Intent");
	            myPicture = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);

	         Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, myPicture);
	                startActivityForResult(cameraIntent, 0);
			}
			catch(NullPointerException ex)
			{
				msbox("Error!", "Counld not get image from camera!");
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
	
	public void resetUpload(View view) {
		EditText etVin = (EditText) findViewById(R.id.et_vin);
		etVin.setText(null);
		
		EditText etYear = (EditText) findViewById(R.id.et_year);
		etYear.setText(null);
		
		EditText etMake = (EditText) findViewById(R.id.et_make);
		etMake.setText(null);
		
		EditText etModel = (EditText) findViewById(R.id.et_model);
		etModel.setText(null);
		
		EditText etTrim = (EditText) findViewById(R.id.et_trim);
		etTrim.setText(null);
		
		ImageView ivImage = (ImageView) findViewById(R.id.iv_image);
		ivImage.setImageURI(null);
	}
	
	public void uploadVehicle(View view) {
		HttpUploadHelper httpHelper = new HttpUploadHelper();
		try {
			String vin = ((EditText)findViewById(R.id.et_vin)).getText().toString();
			String make = ((EditText)findViewById(R.id.et_make)).getText().toString();
			String model = ((EditText)findViewById(R.id.et_model)).getText().toString();
			String trim = ((EditText)findViewById(R.id.et_trim)).getText().toString();
			String year = ((EditText)findViewById(R.id.et_year)).getText().toString();
			String image = null;
			if(null != ((ImageView)findViewById(R.id.iv_image)).getTag())
			{
				ImageView imv = (ImageView)findViewById(R.id.iv_image);
				image = imv.getTag().toString();
			}
			String result = httpHelper.execute(vin, year, make, model, trim, image).get();
			msbox("Result", result);
		} catch (InterruptedException e) {
			msbox("InterruptedException", e.getMessage());
			e.printStackTrace();
		} catch (ExecutionException e) {
			msbox("ExecutionException", e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void ScanTheBarcode(View view)
    {
           Intent intent = new Intent("com.google.zxing.client.android.SCAN");
           //Intent intent = new Intent("com.addvehicle.client.android.SCAN");
           intent.putExtra("SCAN_MODE", "1D_CODE_MODE");
           startActivityForResult(intent, 2);
    }

	
	public void msbox(String title,String message)
	{
		CharSequence text = message;
    	int duration = Toast.LENGTH_LONG;

    	Toast toast = Toast.makeText(this, text, duration);
    	toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
    	toast.show();
	    //AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
	    //dlgAlert.setTitle(title); 
	    //dlgAlert.setMessage(message); 
	    //dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
	        //public void onClick(DialogInterface dialog, int whichButton) {
	             //finish(); 
	       // }
	   //});
	    //dlgAlert.setCancelable(true);
	    //dlgAlert.create().show();
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
			else if (resultCode != RESULT_CANCELED)
			{
				msbox("Error!", "Error!");
			}
			break;
		case 1:
			if(resultCode == RESULT_OK)
	        {  
	            Uri selectedImageUri = intent.getData();
	            String imagePath = getPath(selectedImageUri);
	            InputStream imageStream = null;
				try {
					imageStream = getContentResolver().openInputStream(intent.getData());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
	            Bitmap selectedPicture = BitmapFactory.decodeStream(imageStream);
	            UpdateImagePath(imagePath, selectedPicture);
	        }
			else if (resultCode != RESULT_CANCELED)
			{
				msbox("ExecutionException", "Error!");
			}
			break;
		case 2:
			if (resultCode == RESULT_OK) {
                decodeVin(intent.getStringExtra("SCAN_RESULT"));
             } else if (resultCode == RESULT_CANCELED) {
            	 msbox("Error!", "Operation cancelled");
             }else
             {
            	 msbox("Error!","Error!");
             }
			break;
		}
		
	}
	
	private void GetCameraPick(Intent intent)
	{
		try
		{
			ImageView imageView = (ImageView) findViewById(R.id.iv_image);
            imageView.setImageURI(myPicture);
            imageView.setTag(getPath(myPicture));
		}
		catch(NullPointerException ex)
		{
			msbox("Error!", "Counld not get image from camera!");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void UpdateImagePath(String path, Bitmap bm)
	{
		ImageView imageView = (ImageView)findViewById(R.id.iv_image);
		imageView.setImageBitmap(bm);
		imageView.setTag(path);
	}
	
	private boolean isIntentAvailable(String action) {
	    final PackageManager packageManager = this.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}

	private void decodeVin(String vin)
	{
		HttpVinDecodeHelper httpHelper = new HttpVinDecodeHelper();
		try{
			VehicleInfo result = httpHelper.execute(vin).get();
			((EditText)findViewById(R.id.et_vin)).setText(vin);
			((EditText)findViewById(R.id.et_make)).setText(result.Make);
			((EditText)findViewById(R.id.et_model)).setText(result.Model);
			((EditText)findViewById(R.id.et_trim)).setText(result.Trim);
			((EditText)findViewById(R.id.et_year)).setText(String.valueOf(result.Year));
		}
		catch(Exception e){
			msbox("Error!", "Could not decode Vin.");
		}
	}
	
	private String getPath(Uri uri) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    @SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	    cursor.moveToFirst();
	    return cursor.getString(column_index);
	}
}
