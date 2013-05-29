/**
 * @author Ry
 * @Date 2012.11.24
 * @FileName CollateralActivity.java
 *
 */

package com.twoservices.spark.collateral;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.FilteredListActivity;
import com.twoservices.spark.link.CollateralResult;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.list.CountedItem;
import com.twoservices.spark.list.CountedItemView;

import java.util.ArrayList;

/**
 * Collateral activity of <B>"Engage US"</B>
 * 
 */
public class CollateralActivity extends FilteredListActivity implements View.OnClickListener {

	private static final String TAG = CollateralActivity.class.getSimpleName();
	private static final boolean DEBUG = true;
	
	// define member variables
	// Button that will send me a email
	private Button mBtnEmailMe;
	
	private ArrayList<CountedItem> mItems = new ArrayList<CountedItem>();
	

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collateral);
		
		initEmailMeButton();
		super.getAction();
		
		setupTitleAndButtons();
		super.setupSearchEditText();
		super.setupListAndSponsorView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		loadList(CollateralResult[].class);
		showList("");
		loadSponsors(Action.Collateral);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.btn_email_me:
			sendEmail();
			return;
			
		case R.id.btn_mysubject:
			String action = mIsMySubject ? Action.Collateral : Action.MY_Collateral;
				
			Intent intent = new Intent();
			intent.setClass(CollateralActivity.this, CollateralActivity.class);
			intent.putExtra(Action.EXTRA_DATA, action);
			
			startActivity(intent);
			return;
		}
		
		super.onClick(v);
	}
	
	/**
	 * Setup title bar and bottom three buttons
	 */
	@Override
	protected void setupTitleAndButtons() {
		super.setupTitleAndButtons();
		
		if (mIsMySubject) {
			super.setTextOnMySubjectButton(getString(R.string.collateral));
			super.changeBackgroundOnMySubjectButton(R.drawable.subject_back);
			mBtnEmailMe.setVisibility(View.VISIBLE);
		} else {
			super.setTextOnMySubjectButton(getString(R.string.mycollateral_tag));
			super.changeBackgroundOnMySubjectButton(R.drawable.my_subject_back);
			mBtnEmailMe.setVisibility(View.GONE);
		}
	}

	/**
	 * Get button and add listener
	 */
	private void initEmailMeButton() {
		mBtnEmailMe = (Button) findViewById(R.id.btn_email_me);
		mBtnEmailMe.setOnClickListener(this);
	}
	
	/**
	 * Show Collateral list
	 * 
	 * @param filter
	 *            string to filter "file_title" field
	 */
	@Override
	protected void showList(String filter) {
		// Initiate the content of ListView
		mListView.removeAllViews();

		if (mAPIResult != null) {
			ArrayList<String> sectionList = new ArrayList<String>();
			String strSection;
			String strTitle;
			String strSearch = filter.toLowerCase();
			int sectionItemPos = 0;

			for (CollateralResult result : (CollateralResult[]) mAPIResult) {
				strTitle = result.file_title.toLowerCase();

				if (strTitle.indexOf(strSearch) != -1) {
					strSection = result.company;

					if (!sectionList.contains(strSection)) {
						sectionItemPos = mItems.size();
						sectionList.add(strSection);
					} else {
						mItems.get(sectionItemPos).section_count++;
						strSection = null;
					}
					
					mItems.add(new CountedItem(
							result.collateral_id,
							strSection,
							1,
							result.checked.equals("Y"), 
							result.file_title,
							Utils.getBoldNormalString(result.file_type, result.file_size),
							null));
				}
			}

			for (CountedItem item : mItems)
				mListView.addView(new CountedItemView(this, item, "gotoDetailView"));
			
			sectionList.clear();
			mItems.clear();
		}
	}
	
	/**
	 * Goto DetailActivity when item was clicked
	 * 
	 * @param view
	 *            selected view
	 */
	public void gotoDetailView(View view) {
		CountedItem item = (CountedItem) view.getTag();
		
		if (DEBUG) Log.i(TAG, "Selected item = " + item.title);
		
		Intent intent = new Intent(CollateralActivity.this, CollateralDetailActivity.class);
		intent.putExtra(Action.SELECTED_ID, item.id);
		
		startActivity(intent);
	}
	
	/**
	 * Send me a email
	 */
	private void sendEmail() {
		new SendEmailTask().execute(Utils.getActionAPIString(Action.EMAIL_ME, null, null));
	}

	/**
	 * Send email that MyCollateral items were attached to me 
	 *
	 */
	private class SendEmailTask extends LoadFeedData {

		@Override
		protected void onPreExecute() {
			Feedback.initProgressDialog(CollateralActivity.this);
			Feedback.showProgress();
		}

		@Override
		protected void onPostExecute(String response) {
			if (Feedback.isSuccess()) {
				startActivity(new Intent(CollateralActivity.this, CollateralDetailActivity.class));
			} else {
				Utils.informMessage(CollateralActivity.this, "Could not get results from server.");
			}

			Feedback.dismissProgress();
		}
		
	}
}
