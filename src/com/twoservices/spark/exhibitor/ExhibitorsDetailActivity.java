/**
 * @author Ry
 * @Date 2012.11.26
 * @FileName ExhibitorsDetailActivity.java
 *
 */

package com.twoservices.spark.exhibitor;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Toast;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.ItemDetailActivity;
import com.twoservices.spark.link.ExhibitorDetailResult;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.IDetailResult;

/**
 * Activity to edit own Exhibitor of <B>"Engage US"</B>
 *
 */
public class ExhibitorsDetailActivity extends ItemDetailActivity implements View.OnClickListener {

	private static final String LOAD_ACTION = "Exhibitor_Detail";
	private static final String ADD_ACTION = "Add_MyMap";
	private static final String REMOVE_ACTION = "Remove_MyMap";
	private static final String SCHEDULE_MEETING_ACTION = "Schedule_Meeting";
	
	private static final String ID_TYPE = "uu_exhibitor_id";
	
	// Flag that will decide whether Exhibitors or myExhibitors mode
	private boolean mIsMyExhibitor = false;
	
	// id of Exhibitor that will display
	private String mExhibitorId = "";
	
	
	/** Called when the activity is starting. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setTextOnMySubjectButton(getString(R.string.myexhibitors_tag));
		
		// Get exhibitor id from intent
		Intent intent = getIntent();
		mExhibitorId = intent.getStringExtra(Action.SELECTED_ID);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		loadDetail(Utils.getActionAPIString(LOAD_ACTION, ID_TYPE, mExhibitorId), ExhibitorDetailResult.class);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_mysubject:
			// Change view to ExhibitorsActivity
			Intent intent = new Intent(ExhibitorsDetailActivity.this, ExhibitorsActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.MY_EXHIBITOR);
			startActivity(intent);
			break;
		
		case R.id.btn_red:
			// Remove the current item from myMap
			removeFromMyMap();
			break;
			
		case R.id.btn_green:
			// Add the current item to myMap
			addToMyMap();
			break;
			
		case R.id.btn_green2:
			// Schedule Meeting with current exhibitor
			scheduleMeeting();
			break;
		}
	}
	
	/**
	 * Set item data with detail information
	 */
	@Override
	protected void showDetail(IDetailResult result) {
		
		if (result != null) {
			ExhibitorDetailResult detailResult = (ExhibitorDetailResult) result;
			
			mIsMyExhibitor = detailResult.is_mymap.equals("Y");
			
			mTextItemTitle.setText(detailResult.company);
			
			if (!TextUtils.isEmpty(detailResult.logo_url)) {
				mImageItemLogo.setVisibility(View.VISIBLE);
				new GetImageTask(mImageItemLogo).execute(detailResult.logo_url);
			}
			
			String webAddr = detailResult.website;
			if (!TextUtils.isEmpty(webAddr)) {
				mTextItemWebsite.setVisibility(View.VISIBLE);
				mTextItemWebsite.setText(Utils.getLinkedText("http://" + webAddr, getString(R.string.website), webAddr, ""));
				mTextItemWebsite.setMovementMethod(LinkMovementMethod.getInstance());
			}
			
			String email = detailResult.email;
			if (!TextUtils.isEmpty(email)) {
				mTextItemEmail.setVisibility(View.VISIBLE);
				mTextItemEmail.setText(Utils.getLinkedText("mailto:" + email, getString(R.string.email), email, ""));
				mTextItemEmail.setMovementMethod(LinkMovementMethod.getInstance());
			}
            
            String contact = detailResult.contact;
            if (!TextUtils.isEmpty(contact)) {
                mTextItemContact.setVisibility(View.VISIBLE);
                mTextItemContact.setText(getString(R.string.contact) + contact);
            }

            String phone = detailResult.phone;
            if (!TextUtils.isEmpty(phone)) {
                mTextItemPhone.setVisibility(View.VISIBLE);
                mTextItemPhone.setText(getString(R.string.phone) + phone);
            }

            String address = detailResult.address;
            if (!TextUtils.isEmpty(address)) {
                mTextItemAddress.setVisibility(View.VISIBLE);
                mTextItemAddress.setText(getString(R.string.address) + address);
            }
			
			mTextItemDate.setText(getString(R.string.booth) + " " + detailResult.booth);
			mTextItemTime.setVisibility(View.GONE);
			mTextItemContent.setText(detailResult.description);
			mTextItemAuthor.setVisibility(View.GONE);

            if (TextUtils.isEmpty(detailResult.schedule_meeting))
                mBtnGreen2.setVisibility(View.GONE);
        }

		setTextOnActionButtons();
	}
	
	/**
	 *  Set text of "add", "remove", "schedule meeting" buttons
	 */
	private void setTextOnActionButtons() {
		
		if (mIsMyExhibitor) {
			mBtnRed.setVisibility(View.VISIBLE);
			mBtnRed.setText(R.string.remove_from_mymap);
			
			mBtnGreen.setVisibility(View.GONE);
		} else {
			mBtnRed.setVisibility(View.GONE);
			
			mBtnGreen.setVisibility(View.VISIBLE);
			mBtnGreen.setText(R.string.add_to_mymap);
		}
		
		mBtnBlack.setVisibility(View.GONE);
		mBtnGreen2.setText(R.string.schedule_meeting);
	}
	
	/**
	 * Add a Map to MyMap list
	 */
	private void addToMyMap() {
		new RunOperationTask(this).execute(Utils.getActionAPIString(ADD_ACTION, ID_TYPE, mExhibitorId));
	}
	
	/**
	 * Remove a Map from MyMap list
	 */
	private void removeFromMyMap() {
		new RunOperationTask(this).execute(Utils.getActionAPIString(REMOVE_ACTION, ID_TYPE, mExhibitorId));
	}
	
	/**
	 * Schedule meeting with current exhibitor
	 */
	private void scheduleMeeting() {
		
		Intent intent = new Intent(ExhibitorsDetailActivity.this, ScheduleMeetingActivity.class);
		intent.putExtra(Action.SELECTED_ID, mExhibitorId);
		intent.putExtra(Action.EXTRA_DATA, mTextItemTitle.getText().toString());
		startActivity(intent);
	}

	@Override
	protected void processOperationResult() {

		if (Feedback.sServiceResponse.action.equals(SCHEDULE_MEETING_ACTION)) {
			// stop progress
			Feedback.dismissProgress();

			Toast.makeText(ExhibitorsDetailActivity.this,
					getString(R.string.schedule_success), Toast.LENGTH_LONG).show();
			
		} else {
			onResume();
		}
	}
	
}
