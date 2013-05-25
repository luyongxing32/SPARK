/**
 * @author Ry
 * @Date 2012.11.11
 * @FileName MainActivity.java
 *
 */

package com.twoservices.spark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twoservices.spark.abstarct.Activity2;
import com.twoservices.spark.agenda.AgendaActivity;
import com.twoservices.spark.collateral.CollateralActivity;
import com.twoservices.spark.exhibitor.ExhibitorsActivity;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.HomeResult;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.list.EventItem;
import com.twoservices.spark.list.EventItemView;
import com.twoservices.spark.map.MapActivity;
import com.twoservices.spark.network.NetworkActivity;
import com.twoservices.spark.news.NewsActivity;

/**
 * Main Activity of <B>"Engage US"</B>
 * 
 */
public class MainActivity extends Activity2 {

	private static final String TAG = MainActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;

	// define member variables
	// TextView that will display user name
	private TextView mTextUsername;

	// TextView that will display welcome logo
	private TextView mTextWelcome;

	// ListView that will hold our event items information
	private LinearLayout mListEvents;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		super.setupTitleAndButtons();
		super.hideThreeButtons();
		super.setupTitleBarOnHomeScreen();

		initTextViewsOnScreen();
		setupListAndSponsorView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadAndShowEventInfo();
		loadSponsors(Action.HOME);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_title_back:
			startActivity(new Intent(MainActivity.this, LandingActivity.class));
			return;
		}

		super.onClick(v);
	}

	/**
	 * Initialize TextViews on screen
	 */
	private void initTextViewsOnScreen() {
		// Get view that will display information
		mTextUsername = (TextView) findViewById(R.id.text_username);
		mTextWelcome = (TextView) findViewById(R.id.text_welcome);
	}

	/**
	 * Get event list view and sponsor list view
	 */
	private void setupListAndSponsorView() {
		mListEvents = (LinearLayout) findViewById(R.id.list_view);
		mTextSponsored = (TextView) findViewById(R.id.text_sponsered_by);
		mSponsorView = (LinearLayout) findViewById(R.id.sponsor_view);
	}

	/**
	 * Load and show user's event information from web service
	 */
	private void loadAndShowEventInfo() {
		new LoadEventTask().execute(Utils.getActionAPIString(Action.HOME, null, null));
	}

	/**
	 * Goto each event view
	 * 
	 * @param view
	 *            selected view
	 */
	public void gotoEventView(View view) {
		Intent intent = new Intent();
		EventItem item = (EventItem) view.getTag();

		if (DEBUG)
			Log.i(TAG, "Selected item = " + item.name);

		switch (item.icon_res_id) {
		case R.drawable.myevent:
			startActivity(new Intent(MainActivity.this, MyEventActivity.class));
			break;

		case R.drawable.agenda:
			intent.setClass(MainActivity.this, AgendaActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.AGENDA);

			startActivity(intent);
			break;

		case R.drawable.exhibitors:
			intent.setClass(MainActivity.this, ExhibitorsActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.EXHIBITOR);

			startActivity(intent);
			break;

		case R.drawable.map:
			startActivity(new Intent(MainActivity.this, MapActivity.class));
			break;

		case R.drawable.collateral:
			intent.setClass(MainActivity.this, CollateralActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.Collateral);

			startActivity(intent);
			break;

		case R.drawable.news:
			startActivity(new Intent(MainActivity.this, NewsActivity.class));
			break;

		case R.drawable.network:
			intent.setClass(MainActivity.this, NetworkActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.NETWORK);
			startActivity(intent);
			break;
		}
	}

	/**
	 * AsyncTask to load the events on Home
	 * 
	 */
	private class LoadEventTask extends LoadFeedData {

		@Override
		protected void onPreExecute() {
			// Hide all views
			hideViewsOnScreen();
		}

		@Override
		protected void onPostExecute(String response) {
			if (DEBUG) Log.i(TAG, "API response = " + response);

			if (Feedback.isSuccess()) {
				// Initialize ListView
				mListEvents.removeAllViews();

				HomeResult result = Feedback.parseData(response, HomeResult.class);

				// Now that we have that list lets add it to the ArrayList which
				// will hold our items.
				// Add myEvent item
				mListEvents.addView(new EventItemView(MainActivity.this,
						new EventItem(getString(R.string.myevent_tag), null,
								R.drawable.myevent), "gotoEventView"));

				// Add Agenda item
				if (result.agenda.show == 1) {
					mListEvents.addView(new EventItemView(MainActivity.this,
							new EventItem(getString(R.string.agenda),
									getString(R.string.sessions) + " "
											+ result.agenda.totals,
									R.drawable.agenda), "gotoEventView"));
				}

				// Add Exhibitors item
				if (result.exhibitor.show == 1) {
					mListEvents.addView(new EventItemView(MainActivity.this,
							new EventItem(getString(R.string.exhibitors), ""
									+ result.exhibitor.totals,
									R.drawable.exhibitors), "gotoEventView"));
				}

				// Add Map item
				if (result.map.show == 1) {
					mListEvents.addView(new EventItemView(MainActivity.this,
							new EventItem(getString(R.string.map), null,
									R.drawable.map), "gotoEventView"));
				}

				// Add Collateral item
				if (result.collateral.show == 1) {
					mListEvents.addView(new EventItemView(MainActivity.this,
							new EventItem(getString(R.string.collateral), ""
									+ result.collateral.totals,
									R.drawable.collateral), "gotoEventView"));
				}

				// Add News item
				if (result.news.show == 1) {
					mListEvents.addView(new EventItemView(MainActivity.this,
							new EventItem(getString(R.string.news), ""
									+ result.news.totals, R.drawable.news),
							"gotoEventView"));
				}

				// Add Network item
				if (result.network.show == 1) {
					mListEvents.addView(new EventItemView(MainActivity.this,
									new EventItem(getString(R.string.network),
											"" + result.network.totals,
											R.drawable.network),
									"gotoEventView"));
				}

				// Set icon resource ids
				/*
				 * for (EventResult info : mEventResults) { info.icon_res_id =
				 * getResources().getIdentifier( info.name.toLowerCase(),
				 * "drawable", getPackageName()); }
				 */

				// Since we've got the user name, display it again
				mTextUsername.setText(result.username);
				AccountInfo.sUserName = result.username;

				// Display the welcome text
				mTextWelcome.setText(Utils.getFormattedWelcomeString(result.top_text));
				
				// Show all view that include new state
				showViewsOnScreen();
			} else {
				Utils.informMessage(MainActivity.this, "Could not get results from server.");
				Utils.logout(MainActivity.this);
			}
		}

	}

}
