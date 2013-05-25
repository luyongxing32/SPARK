/**
 * @author Ry
 * @Date 2012.12.10
 * @FileName MyProfileActivity.java
 *
 */

package com.twoservices.spark.myprofile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.twoservices.spark.MainActivity;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.Activity2;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.link.ProfileResult;

/**
 * MyProfile Activity of <B>"Engage US"</B>
 * 
 */
public class MyProfileActivity extends Activity2 {
	
	private static final String TAG = MyProfileActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	
	private static final String ACTION = "MyProfile";
	
	// define member variables
	// TextView that will display user name
	private TextView mTextName;
		
	// TextView that will display user company
	private TextView mTextCompany;
	
	// TextView that will display user email
	private TextView mTextEmail;
	
	// Switch that will decide whether user show email or not.
	private ToggleButton mSwitchShowEmail;
	
	// Switch that will decide whether network allows email or not.
	private ToggleButton mSwitchAllowEmail;
	
	// Switch that will decide whether network allows sms or not.
	private ToggleButton mSwitchAllowSMS;
	
	// Switch that will decide whether network allows sms reminders or not.
	private ToggleButton mSwitchAllowReminders;

	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_myprofile);
		super.setupTitleAndButtons();
		super.setTextOnMySubjectButton(getString(R.string.home));
		super.changeBackgroundOnMySubjectButton(R.drawable.home_button_back);
		
		initTextViewAndSwitches();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadProfile();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_mysubject:
			startActivity(new Intent(MyProfileActivity.this, MainActivity.class));
			break;
		}
	}

	/**
	 * Initialize TextViews and Switches
	 */
	private void initTextViewAndSwitches() {
		LinearLayout profileLayout = (LinearLayout) findViewById(R.id.myprofile_content);
		
		mTextName = (TextView) profileLayout.findViewById(R.id.text_name);
		mTextCompany = (TextView) profileLayout.findViewById(R.id.text_company);
		mTextEmail = (TextView) profileLayout.findViewById(R.id.text_email);
		
		mSwitchShowEmail = (ToggleButton) profileLayout.findViewById(R.id.show_email);
		mSwitchAllowEmail = (ToggleButton) profileLayout.findViewById(R.id.allow_email);
		mSwitchAllowSMS = (ToggleButton) profileLayout.findViewById(R.id.allow_sms);
		mSwitchAllowReminders = (ToggleButton) profileLayout.findViewById(R.id.allow_sms_remainder);
	}
	
	/**
	 * Load user profile using service API.
	 */
	private void loadProfile() {
		// Run service API using the url with project id and contact id
		new LoadProfileTask().execute(Utils.getActionAPIString(ACTION, null, null));
	}

	/**
	 * Show user profile with profile result.
	 */
	private void showProfile(ProfileResult result) {
		mTextName.setText(getString(R.string.name) + " " + result.name);
		mTextCompany.setText(getString(R.string.company) + " " + result.company);
		mTextEmail.setText(getString(R.string.email) + " " + result.email);
		
		mSwitchShowEmail.setChecked("1".equals(result.show_email));
		mSwitchAllowEmail.setChecked("1".equals(result.allow_email));
		mSwitchAllowSMS.setChecked("1".equals(result.allow_sms));
		mSwitchAllowReminders.setChecked("1".equals(result.allow_reminders));
	}
	
	/**
	 * AsyncTask to load my profile
	 * 
	 */
	private class LoadProfileTask extends LoadFeedData {

		@Override
		protected void onPreExecute() {
			hideViewsOnScreen();
			
			Feedback.initProgressDialog(MyProfileActivity.this);
			Feedback.showProgress();
		}

		@Override
		protected void onPostExecute(String response) {
			if (DEBUG) Log.i(TAG, "API response = " + response);
			
			if (Feedback.isSuccess()) {
				ProfileResult result = Feedback.parseData(response, ProfileResult.class);
				showProfile(result);
				
				// Show all view that include new state
				showViewsOnScreen();
			} else {
				Utils.informMessage(MyProfileActivity.this, "Could not get results from server.");
				Utils.logout(MyProfileActivity.this);
			}

			Feedback.dismissProgress();
		}
		
	}
	
}
