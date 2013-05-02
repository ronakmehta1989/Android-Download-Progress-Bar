package com.androidbegin.progressbartutorial;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	// Declare variables
	ProgressDialog mProgressDialog;
	Button button;

	// Insert in your direct download link
	String URL = "http://www.androidbegin.com/wp-content/uploads/2013/03/Progress.png";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set Title
		setTitle("ProgressBar Tutorial");
		// Set the layout from main.xml
		setContentView(R.layout.main);

		// Find the Button in your main.xml layout
		button = (Button) findViewById(R.id.MyButton);

		// Wait for the Button click
		button.setOnClickListener(new OnClickListener() {

			// On ButtonClick do the following task
			public void onClick(View arg0) {
				// Execute DownloadFile AsyncTask
				new DownloadFile().execute(URL);
			}
		});

	}

	// DownloadFile AsyncTask
	private class DownloadFile extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create ProgressBar
			mProgressDialog = new ProgressDialog(MainActivity.this);
			// Set your ProgressBar Title
			mProgressDialog.setTitle("Downloads");
			// Set your ProgressBar Message
			mProgressDialog.setMessage("Downloading, Please Wait!");
			mProgressDialog.setIndeterminate(false);
			mProgressDialog.setMax(100);
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// Show ProgressBar
			mProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... sUrl) {
			try {
				URL url = new URL(sUrl[0]);
				URLConnection connection = url.openConnection();
				connection.connect();

				// Detect the file lenght
				int fileLength = connection.getContentLength();

				// Declare the internal storage location
				String filepath = Environment.getExternalStorageDirectory()
						.getPath();

				// Download the file
				InputStream input = new BufferedInputStream(url.openStream());

				// Save the downloaded file
				OutputStream output = new FileOutputStream(filepath + "/"
						+ "test.jpg");

				byte data[] = new byte[1024];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					// Publish the progress
					publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}

				// Close connection
				output.flush();
				output.close();
				input.close();
			} catch (Exception e) {
				// Error Log
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			// Update the ProgressBar
			mProgressDialog.setProgress(progress[0]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
