/**
 * 
 */
package com.adp.addvehicle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * @author miriyals
 *
 */
public class ImageButtonAdapter extends BaseAdapter {

	private Context mContext;

    public ImageButtonAdapter(Context c) {
        mContext = c;
    }

    
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return buttons.length;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try{
		ImageButton imageButton;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	imageButton = new ImageButton(mContext);
        	imageButton.setLayoutParams(new GridView.LayoutParams(85, 85));
        	imageButton.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        	imageButton.setPadding(8, 8, 8, 8);
        	if(0 == position)
	        	imageButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent1 = new Intent(mContext, UploadVehicle.class);
	                    mContext.startActivity(intent1);
						CharSequence text = "Hello toast!";
				    	int duration = Toast.LENGTH_LONG;
	
				    	Toast toast = Toast.makeText(mContext, text, duration);
				    	toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
				    	toast.show();
					}
				});
        	else if(1 == position)
	        	imageButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext, ListViewActivity.class);
	                    mContext.startActivity(intent);
						CharSequence text = "Hello toast1!";
				    	int duration = Toast.LENGTH_LONG;
	
				    	Toast toast = Toast.makeText(mContext, text, duration);
				    	toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
				    	toast.show();
					}
				});
        } else {
        	imageButton = (ImageButton) convertView;
        }

        imageButton.setImageResource(buttons[position]);
        return imageButton;
		}
		catch(Exception e)
		{
			Log.e(getClass().getSimpleName(), "Error in ImageButtonAdapter.getView", e);
		}
		return null;
	}
	
	// references to our images
    private Integer[] buttons = {
            R.drawable.ic_add_car, R.drawable.ic_launcher
    };
    
    public void onFirstButtonClick(View view)
    {
    	CharSequence text = "Hello toast!";
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(mContext, text, duration);
    	toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
    	toast.show();
    }

}
