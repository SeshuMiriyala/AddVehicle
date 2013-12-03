/**
 * 
 */
package com.adp.addvehicle;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author miriyals
 *
 */
public class HttpHelper  extends AsyncTask<String, Integer, ArrayList<VehicleInfo>>{
	private DefaultHttpClient client = new DefaultHttpClient();
	
	private ArrayList<VehicleInfo> ConvertToList(InputStream inputStream){
		ArrayList<VehicleInfo> vehicleList = new ArrayList<VehicleInfo>();
		try {
			String response = inputStream.toString();
			if(!TextUtils.isEmpty(response)){
			JSONArray jsonMainArr = new JSONArray(response);
			for (int i = 0; i < jsonMainArr.length(); i++) {
				VehicleInfo vehicle = new VehicleInfo();
			    JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
			    vehicle.Vin = childJSONObject.getString("Vin");
			    vehicle.Year = childJSONObject.getInt("Year");
			    vehicle.Make = childJSONObject.getString("Make");
			    vehicle.Model = childJSONObject.getString("Model");
			    vehicle.Trim = childJSONObject.getString("Trim");
			    vehicleList.add(vehicle);
			}
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return vehicleList;
	}

	@Override
	protected ArrayList<VehicleInfo> doInBackground(String... params) {
		String url = "http://10.0.2.2/VehicleInfoApi/api/vehicleinfo?query=B";
		HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url);
				return null;
				}
			HttpEntity getResponseEntity = getResponse.getEntity();
			if (getResponseEntity != null) {
				return ConvertToList(getResponseEntity.getContent());
				}
			}
		catch (IOException e) {
			getRequest.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
			}
		return null;
	}
}
