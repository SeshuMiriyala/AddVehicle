/**
 * 
 */
package com.adp.addvehicle;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

/**
 * @author miriyals
 *
 */
public class HttpUploadHelper  extends AsyncTask<String, Integer, String>{
	private HttpClient client = new DefaultHttpClient();
	
	@Override
	protected String doInBackground(String... params) {
		HttpPost httppost = new HttpPost("http://10.0.2.2/VehicleInfoApi/api/vehicleinfo/SaveVehicleInfo"); 
		try {
			MultipartEntity  multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			multipartEntity.addPart("Vin", new StringBody(params[0]));
			multipartEntity.addPart("Year", new StringBody(params[1]));
			multipartEntity.addPart("Make", new StringBody(params[2]));
			multipartEntity.addPart("Model", new StringBody(params[3]));
			multipartEntity.addPart("Trim", new StringBody(params[4]));
			
			if(params.length == 6 && null != params[5]){
				File image1 = new File(params[5]);
				multipartEntity.addPart("Image", new FileBody(image1));
			}

			httppost.setEntity(multipartEntity);
	        // Execute HTTP Post Request
	         HttpResponse response = client.execute(httppost);
	         return EntityUtils.toString(response.getEntity());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
