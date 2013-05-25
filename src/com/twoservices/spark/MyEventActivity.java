/**
 * @author Ry
 * @Date 2012.11.15
 * @FileName MyEventActivity.java
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
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.link.MyEventResult;
import com.twoservices.spark.list.EventItem;
import com.twoservices.spark.list.EventItemView;
import com.twoservices.spark.myprofile.MyProfileActivity;
import com.twoservices.spark.network.NetworkActivity;

/**
 * MyEvent Activity of <B>"Engage US"</B>
 * 
 */
public class MyEventActivity extends Activity2 {
	
	private static final String TAG = MyEventActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	
	// define member variables
	// TextView that will display user name
	private TextView mTextUsername;
	
	// TextView that will display welcome logo
	private TextView mTextWelcome;
	
	// TextView that will display the header of my event list
	private TextView mTextListHeader;

	// ListView that will hold our event items information
	private LinearLayout mListEvents;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		super.setupTitleAndButtons();
		super.hideThreeButtons();

		initTextViewOnScreen();
		setupListAndSponsorView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadAndShowEventInfo();
		//loadSponsors(Action.HOME);
	}

	/**
	 *  Get TextView
	 */
	private void initTextViewOnScreen() {
		// Get and hide TextView
		mTextUsername = (TextView) findViewById(R.id.text_username);
		mTextUsername.setVisibility(View.GONE);
		
		// Get TextView, and display the welcome text
		mTextWelcome = (TextView) findViewById(R.id.text_welcome);

		// Get TextView, and display the list header
		mTextListHeader = (TextView) findViewById(R.id.text_list_header);
		mTextListHeader.setText(Utils.getMyString(getString(R.string.myevent_tag)));
	}

	/**
	 *  Get event list view and sponsor list view
	 */
	private void setupListAndSponsorView() {
		mListEvents = (LinearLayout) findViewById(R.id.list_view);
		//mTextSponsored = (TextView) findViewById(R.id.text_sponsered_by);
		//mSponsorView = (LinearLayout) findViewById(R.id.sponsor_view);
	}

	/**
	 * Load and show user's event information from web service
	 */
	private void loadAndShowEventInfo() {
		new LoadMyEventTask().execute(Utils.getActionAPIString(Action.MY_EVENT, null, null));
	}

	/**
	 * Goto each my subject view
	 * 
	 * @param view
	 *            selected view
	 */
	public void gotoEventView(View view) {
		Intent intent = new Intent();
		EventItem item = (EventItem) view.getTag();

		if (DEBUG) Log.i(TAG, "Selected item = " + item.name);

		switch (item.icon_res_id) {
		case R.drawable.home:
			startActivity(new Intent(MyEventActivity.this, MainActivity.class));
			break;

		case R.drawable.agenda:
			intent.setClass(MyEventActivity.this, AgendaActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.MY_AGENDA);
			
			startActivity(intent);
			break;

		case R.drawable.exhibitors:
			intent.setClass(MyEventActivity.this, ExhibitorsActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.MY_EXHIBITOR);
			
			startActivity(intent);
			break;

		case R.drawable.collateral:
			intent.setClass(MyEventActivity.this, CollateralActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.MY_Collateral);
			
			startActivity(intent);
			break;

		case R.drawable.network:
			intent.setClass(MyEventActivity.this, NetworkActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.MY_NETWORK);
			
			startActivity(intent);
			break;

		case R.drawable.myprofile:
			startActivity(new Intent(MyEventActivity.this, MyProfileActivity.class));
			break;
		}
	}
	
	/**
	 * AsyncTask to load the my events on MyEvent
	 *
	 */
	private class LoadMyEventTask extends LoadFeedData {
		
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
				
				// Now that we have that list lets add it to the ArrayList which will hold our items.
				MyEventResult result = Feedback.parseData(response, MyEventResult.class);
				
				// Now that we have that list lets add it to the ArrayList which will hold our items.
				// Add EventHome item
				mListEvents.addView(new EventItemView(MyEventActivity.this,
						new EventItem(getString(R.string.eventhome_tag), null, R.drawable.home),
						"gotoEventView"));
				
				// Add mySessions item
				if (result.session.show == 1) {
					mListEvents.addView(new EventItemView(MyEventActivity.this,
							new EventItem(getString(R.string.mysessions_tag), "" + result.session.totals, R.drawable.agenda),
							"gotoEventView"));
				}
				
				// Add myExhibitors item
				if (result.exhibitor.show == 1) {
					mListEvents.addView(new EventItemView(MyEventActivity.this,
							new EventItem(getString(R.string.myexhibitors_tag), "" + result.exhibitor.totals, R.drawable.exhibitors),
							"gotoEventView"));
				}
				
				// Add myCollateral item
				if (result.collateral.show == 1) {
					mListEvents.addView(new EventItemView(MyEventActivity.this,
							new EventItem(getString(R.string.mycollateral_tag), "" + result.collateral.totals, R.drawable.collateral),
							"gotoEventView"));
				}
				
				// Add myNetwork item
				if (result.network.show == 1) {
					mListEvents.addView(new EventItemView(MyEventActivity.this,
							new EventItem(getString(R.string.mynetwork_tag), "" + result.network.totals, R.drawable.network),
							"gotoEventView"));
				}
				
				// Add myProfile item
				mListEvents.addView(new EventItemView(MyEventActivity.this,
						new EventItem(getString(R.string.myprofile_tag), "", R.drawable.myprofile),
						"gotoEventView"));
				
				// Display the welcome text
				mTextWelcome.setText(Utils.getFormattedWelcomeString(result.top_text));
				
				// Show all view that include new state
				showViewsOnScreen();
			} else {
				Utils.informMessage(MyEventActivity.this, "Could not get results from server.");
				Utils.logout(MyEventActivity.this);
			}

			// Stop progress
			Feedback.dismissProgress();
		}
		
	}

}
