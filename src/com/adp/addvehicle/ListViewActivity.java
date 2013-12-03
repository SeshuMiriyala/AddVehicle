/**
 * 
 */
package com.adp.addvehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author miriyals
 *
 */
public class ListViewActivity extends Activity {
	
	public static ArrayList<VehicleInfo> list;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vehicle_list_view);
		try{
		HttpHelper httpHelper = new HttpHelper();
		final ListView listview = (ListView) findViewById(R.id.listview);
		
		final ArrayList<VehicleInfo> list = httpHelper.execute("").get();
		
		final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		
		listview.setAdapter(adapter);
		}
		catch(Exception e){
			Log.e(getClass().getSimpleName(), "Error in ListViewActivity.OnCreate", e);
		}
	}
	
	private class StableArrayAdapter extends ArrayAdapter<VehicleInfo> {

	    HashMap<VehicleInfo, Integer> mIdMap = new HashMap<VehicleInfo, Integer>();
	    Context mContext;

	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<VehicleInfo> objects) {
	      super(context, textViewResourceId, objects);
	      mContext = context;
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }

	    @Override
	    public long getItemId(int position) {
	      VehicleInfo item = getItem(position);
	      return mIdMap.get(item);
	    }

	    @Override
	    public boolean hasStableIds() {
	      return true;
	    }
	    
	    @Override
		public View getView(int position, View convertView, ViewGroup parent) {
	    	try{
	    	ListView listview = (ListView) findViewById(R.layout.list_item_view);
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	        listview.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext, UploadVehicle.class);
		                    mContext.startActivity(intent);
							CharSequence text = "Hello toast!";
					    	int duration = Toast.LENGTH_LONG;
		
					    	Toast toast = Toast.makeText(mContext, text, duration);
					    	toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
					    	toast.show();
						}
					});
        } else {
	        	listview = (ListView) convertView;
	        }
	        TextView textMake = (TextView) listview.findViewById(R.id.make);
	        VehicleInfo info = getItem(position);
	        textMake.setText(info.Make);
	        textMake.setText(info.Model);
	        //imageButton.setImageResource(buttons[position]);
	        return listview;
	    	}
	    	catch(Exception e){
	    		Log.e(getClass().getSimpleName(), "Error in StableArrayAdapter.getView", e);
	    	}
	    	return null;
	    }

	  }

} 
