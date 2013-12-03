package com.adp.addvehicle;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class FileSelectActivity extends Activity {

	// The path to the root of this app's internal storage
    private File mPrivateRootDir;
    // The path to the "images" sub directory
    private File mImagesDir;
    // Array of files in the images sub directory
    File[] mImageFiles;
    // Array of filenames corresponding to mImageFiles
    String[] mImageFilenames;
    
    // Initialize the Activity
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_select);
		
		// Set up an Intent to send back to applications that request a file
        Intent mResultIntent =
                new Intent("com.adp.addvehicle.ACTION_RETURN_FILE");
        // Get the files/ sub directory of internal storage
        mPrivateRootDir = getFilesDir();
        // Get the files/images sub directory;
        mImagesDir = new File(mPrivateRootDir, "images");
        // Get the files in the images sub directory
        mImageFiles = mImagesDir.listFiles();
        // Set the Activity's result to null to begin with
        setResult(Activity.RESULT_CANCELED, null);
        //startActivityForResult(mResultIntent, 0);
        /*
         * Display the file names in the ListView mFileListView.
         * Back the ListView with the array mImageFilenames, which
         * you can create by iterating through mImageFiles and
         * calling File.getAbsolutePath() for each File
         */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.file_select, menu);
		return true;
	}

}
