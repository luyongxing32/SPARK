package com.twoservices.spark.map;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.Activity2;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.link.MapResult;

public class MapActivity extends Activity2 {
	
	private static final String TAG = MapActivity.class.getSimpleName();
	private static final boolean DEBUG = true;
	
	// TextView that will show the name of map
	private TextView mTextMap;
	
	// WebView that will show the map image 
	private WebView mWebMap;
	
	// Result that will hold the response of service API
	private MapResult mMapResult;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		super.setupTitleAndButtons();
		super.hideThreeButtons();
		
		setupSponsorView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loadMapImage();
		//loadSponsors(Action.MAP);
	}

	/**
	 * Load the map information using service API
	 */
	private void loadMapImage() {
		// Run service API using the url with project id and contact id
		new LoadMapTask().execute(Utils.getActionAPIString(Action.MAP, null, null));
	}
	
	/**
	 *  Get sponsor list view
	 */
	private void setupSponsorView() {
		mTextSponsored = (TextView) findViewById(R.id.text_sponsered_by);
		mSponsorView = (LinearLayout) findViewById(R.id.sponsor_view);
	}
	
	/**
	 * AsyncTask to load map image
	 *
	 */
	private class LoadMapTask extends LoadFeedData {

		@Override
		protected void onPostExecute(String response) {
			
			if (Feedback.isSuccess()) {
				mMapResult = Feedback.parseData(response, MapResult.class);
				
				if (DEBUG) Log.i(TAG, "Map URL = " + mMapResult.map);
				
				mTextMap = (TextView) findViewById(R.id.text_map);
				mTextMap.setText(mMapResult.map_text);
				
				mWebMap = (WebView) findViewById(R.id.web_map);
				mWebMap.setWebViewClient(new WebViewClient() {
					@Override
					public void onPageFinished(WebView view, String url) {
						super.onPageFinished(view, url);

						// Stop progress
						Feedback.dismissProgress();
					}
				});
				
				mWebMap.getSettings().setBuiltInZoomControls(true);
				mWebMap.getSettings().setUseWideViewPort(true);
				mWebMap.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
				
				mWebMap.loadUrl(mMapResult.map);
			} else {
				Utils.informMessage(MapActivity.this, "Could not get results from server.");
				Utils.logout(MapActivity.this);
			}
		}
		
	}
	
}
