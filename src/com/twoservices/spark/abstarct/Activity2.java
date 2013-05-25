/**
 * @author Ry
 * @Date 2012.11.18
 * @FileName Activity2.java
 *
 */

package com.twoservices.spark.abstarct;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twoservices.spark.AccountInfo;
import com.twoservices.spark.Action;
import com.twoservices.spark.LandingActivity;
import com.twoservices.spark.MainActivity;
import com.twoservices.spark.MyEventActivity;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.link.SponsorResult;

/**
 * Skeleton activity of <B>"Engage US"</B> with sponsor function
 * 
 */
public abstract class Activity2 extends Activity implements View.OnClickListener {
	
	private static final String TAG = Activity2.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;

	// ImageView correspond to Back button
	private ImageView mImageBack;

	// ImageView correspond to Home button
	private ImageView mImageHome;

	// TextView that will display the Title
	private TextView mTextTitle;
	
	// Layout that includes three buttons
	private LinearLayout mLayoutButtonGroup;
	
	// Button that will goto "myEvent"
	private Button mBtnMyEvent;

	// Button that will goto "MyAgenda"
	private Button mBtnMySubject;

	// Button that will log out on "Engage US"
	private Button mBtnLogout;
	
	// ScrollView that will include all other view
	private View mVScroll;
		
	// TextView that will display "Sponsored by"
	protected TextView mTextSponsored;
	
	// LinearLayout that will hold sponsor images
	protected LinearLayout mSponsorView;
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Feedback.initProgressDialog(this);
		Feedback.showProgress();
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.btn_myevent:
			startActivity(new Intent(this, MyEventActivity.class));
			break;
			
		case R.id.btn_logout:
			startActivity(new Intent(this, LandingActivity.class));
			finish();
			break;
			
		case R.id.img_title_back:
			onBackPressed();
			break;
			
		case R.id.img_title_home:
			startActivity(new Intent(this, MainActivity.class));
			break;
		}
	}
	
	/**
	 *  Initialize TextView and buttons and add listeners
	 */
	protected void setupTitleAndButtons() {
		initTitleBar();
		addListenerOnThreeButton();
		
		// Get vertical scroll view that include all other views
		mVScroll = findViewById(R.id.vscroll_view);
	}
	
	/**
	 * Change back button to logout button, hide home button
	 */
	protected void setupTitleBarOnHomeScreen() {
		mImageBack.setBackgroundResource(R.drawable.title_logout_back);
		mImageHome.setVisibility(View.INVISIBLE);
	}
	
	/**
	 *  Hide button bar on bottom of screen
	 */
	protected void hideThreeButtons() {
		mLayoutButtonGroup.setVisibility(View.GONE);
	}
	
	/**
	 * Set text on MySubjectButton
	 * 
	 * @param text
	 *            string that will display on MySubjectButton
	 */
	protected void setTextOnMySubjectButton(String text) {
		mBtnMySubject.setText(Utils.getMyString(text));
	}
	
	/**
	 * Set background on MySubjectButton
	 * 
	 * @param res_id
	 *            resource id of background image that will display on MySubjectButton
	 */
	protected void changeBackgroundOnMySubjectButton(int res_id) {
		mBtnMySubject.setBackgroundResource(res_id);
	}

	/**
	 *  Get and initiate title text, back and home button 
	 */
	private void initTitleBar() {
		// Get ImageView for back button, then set visible state and onClickListener
		mImageBack = (ImageView) findViewById(R.id.img_title_back);
		mImageBack.setVisibility(View.VISIBLE);
		mImageBack.setOnClickListener(this);

		// Get ImageView for home button, then set visible state and onClickListener
		mImageHome = (ImageView) findViewById(R.id.img_title_home);
		mImageHome.setVisibility(View.VISIBLE);
		mImageHome.setOnClickListener(this);

		// Get TextView for title, the set text 
		mTextTitle = (TextView) findViewById(R.id.text_title);
		mTextTitle.setText(AccountInfo.sProjectName);
	}

	/**
	 * Hide all views while loading event data
	 */
	protected void hideViewsOnScreen() {
		mVScroll.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * After loading event data, show all views 
	 */
	protected void showViewsOnScreen() {
		mVScroll.setVisibility(View.VISIBLE);
	}
	
	/**
	 *  Add listener to each button
	 */
	private void addListenerOnThreeButton() {
		mLayoutButtonGroup = (LinearLayout) findViewById(R.id.three_button_bars);
		
		mBtnMyEvent = (Button) findViewById(R.id.btn_myevent);
		mBtnMyEvent.setText(Utils.getMyString(getString(R.string.myevent_tag)));
		mBtnMyEvent.setOnClickListener(this);

		mBtnMySubject = (Button) findViewById(R.id.btn_mysubject);
		mBtnMySubject.setOnClickListener(this);

		mBtnLogout = (Button) findViewById(R.id.btn_logout);
		mBtnLogout.setText(R.string.logout);
		mBtnLogout.setOnClickListener(this);
	}
	
	/**
	 * Load sponsor images
	 */
	protected void loadSponsors(String pageName) {
		String action = Action.GET_SPONSOR + "&page_name=" + pageName;
		
		// Run service API using the url with project id and contact id
		new LoadSponsorTask().execute(Utils.getActionAPIString(action, null, null));
	}
	
	/**
	 * Show sponsor images
	 */
	protected void showSponsors(SponsorResult[] results) {
		for (SponsorResult result : results) {
			ImageView image = new ImageView(this);
			image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			image.setPadding(10, 10, 10, 10);
			image.setScaleType(ScaleType.FIT_XY);
			new GetImageTask(image).execute(result.sponsor_url);
			
			mSponsorView.addView(image);
		}
	}
	
	//private final int gap = scale(8);
	//private final int scale(int value) { return (int) (value * getResources().getDisplayMetrics().density); }
	
	private class LoadSponsorTask extends LoadFeedData {

		@Override
		protected void onPostExecute(String response) {
			if (DEBUG) Log.i(TAG, "API response = " + response);

			if (Feedback.isSuccess() && Feedback.sServiceResponse.total_results > 0) {
				mSponsorView.removeAllViews();
				mTextSponsored.setVisibility(View.VISIBLE);
				
				showSponsors(Feedback.parseDataArray(response, SponsorResult[].class));
			} else {
				mTextSponsored.setVisibility(View.GONE);
			}
			
			// Stop progress
			Feedback.dismissProgress();
		}
		
	}
	
	/**
	 * Get/Set image using remote URL string
	 *
	 */
	protected class GetImageTask extends AsyncTask<String, Void, Bitmap> {
		private ImageView mImageView;
		
		public GetImageTask(ImageView view) {
			mImageView = view;
		}
		
		@Override
		protected Bitmap doInBackground(String... urls) {
			Bitmap map = null;
			for (String url : urls) {
				map = downloadImage(url);
			}
			return map;
		}

		// Sets the Bitmap returned by doInBackground
		@Override
		protected void onPostExecute(Bitmap result) {
			mImageView.setImageBitmap(result);
		}

		// Creates Bitmap from InputStream and returns it
		private Bitmap downloadImage(String url) {
			Bitmap bitmap = null;
			InputStream stream = null;
			BitmapFactory.Options bmOptions = new BitmapFactory.Options();
			bmOptions.inSampleSize = 1;

			try {
				stream = getHttpConnection(url);
				bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
				if (stream != null)	stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return bitmap;
		}

		// Makes HttpURLConnection and returns InputStream
		private InputStream getHttpConnection(String urlString)
				throws IOException {
			InputStream stream = null;
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			try {
				HttpURLConnection httpConnection = (HttpURLConnection) connection;
				httpConnection.setRequestMethod("GET");
				httpConnection.connect();

				if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					stream = httpConnection.getInputStream();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return stream;
		}
	}
	
}
