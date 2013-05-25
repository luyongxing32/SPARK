/**
 * @author Ry
 * @Date 2012.11.26
 * @FileName AgendaItemDetailsActivity.java
 *
 */

package com.twoservices.spark.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.ItemDetailActivity;
import com.twoservices.spark.link.AgendaDetailResult;
import com.twoservices.spark.link.IDetailResult;

/**
 * Activity to edit own Agenda of <B>"Engage US"</B>
 *
 */
public class AgendaDetailActivity extends ItemDetailActivity implements View.OnClickListener {

	private static final String LOAD_ACTION = "Agenda_Detail";
	private static final String ADD_ACTION = "Add_MyAgenda";
	private static final String REMOVE_ACTION = "Remove_MyAgenda";
	private static final String CHECKED_ACTION = "Check_in";
	
	private static final String ID_TYPE = "uu_session_id";
	
	// Flag that will decide whether Agenda or myAgenda mode
	private boolean mIsMyAgenda = false;
	
	// Flag that will decide whether checked in or not.
	private boolean mIsCheckedIn = false;
	
	// Number of survey question
	private int mQuestionNum = 0;
	
	// Number of survey response
	private int mResponseNum = 0;
	
	// Session id of Agenda that will display
	private String mSessionId = "";
	
	
	/** Called when the activity is starting. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setTextOnMySubjectButton(getString(R.string.myagenda_tag));
		
		// Get session id from intent
		Intent intent = getIntent();
		mSessionId = intent.getStringExtra(Action.SELECTED_ID);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		loadDetail(Utils.getActionAPIString(LOAD_ACTION, ID_TYPE, mSessionId), AgendaDetailResult.class);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_mysubject:
			// Change view to myAgenda
			Intent intent = new Intent();
			intent.setClass(AgendaDetailActivity.this, AgendaActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.MY_AGENDA);
			
			startActivity(intent);
			break;
		
		case R.id.btn_red:
			// Remove the current item from myAgenda
			removeFromMyAgenda();
			break;
			
		case R.id.btn_black:
			// Check the current item
			checkInMyAgenda();
			break;
			
		case R.id.btn_green:
			if (mIsMyAgenda) {
				// Finish the survey of the current item
				finishSurveyMyAgenda();
			} else {
				// Add the current item to myAgenda
				addToMyAgenda();
			}
			break;
		}
	}
	
	
	/**
	 * Set item data with detail information
	 */
	@Override
	protected void showDetail(IDetailResult result) {
		
		if (result != null) {
			AgendaDetailResult detailResult = (AgendaDetailResult) result;
			
			mIsMyAgenda = detailResult.is_myagenda.equals("Y");
			mIsCheckedIn = (detailResult.checked_in == 1);
			mQuestionNum = detailResult.question_num;
			mResponseNum = detailResult.response_num;
			
			mTextItemTitle.setText(detailResult.title);
			mTextItemDate.setText(/*Utils.toMyDateFormat(*/detailResult.start_date/*)*/);
			mTextItemTime.setText(Utils.getTimeDurationString(detailResult.start_time, detailResult.end_time));
			
			String misc = "";
			
			if (!TextUtils.isEmpty(detailResult.track)) {
				misc += (getString(R.string.track) + detailResult.track);
			}
			
			if (!TextUtils.isEmpty(detailResult.company)) {
				if (!TextUtils.isEmpty(misc)) misc += "\n";
				misc += (getString(R.string.company) + detailResult.company);
			}
			
			if (!TextUtils.isEmpty(detailResult.speaker)) {
				if (!TextUtils.isEmpty(misc)) misc += "\n";
				misc += (getString(R.string.speaker) + detailResult.speaker);
			}
			
			if (!TextUtils.isEmpty(misc)) {
				mTextItemMisc.setVisibility(View.VISIBLE);
				mTextItemMisc.setText(misc);
			} else {
				mTextItemMisc.setVisibility(View.GONE);
			}

			mTextItemContent.setText(Html.fromHtml(detailResult.description));
			mTextItemAuthor.setVisibility(View.GONE);
		}
		
		setTextOnActionButtons();
	}
	
	/**
	 *  Set text of "remove", "add", "check", "finish" buttons
	 */
	private void setTextOnActionButtons() {
		
		if (mIsMyAgenda) {
			mBtnRed.setVisibility(View.VISIBLE);
			mBtnRed.setText(R.string.remove_from_myagenda);
			
			mBtnBlack.setVisibility(View.VISIBLE);
			if (mIsCheckedIn) {
				mBtnBlack.setBackgroundResource(R.drawable.black_button_back);
				mBtnBlack.setText(R.string.checked_in);
				mBtnBlack.setEnabled(false);
				mBtnGreen.setVisibility(View.VISIBLE);
			} else {
				mBtnBlack.setBackgroundResource(R.drawable.green_button_back);
				mBtnBlack.setText(R.string.check_in);
				mBtnGreen.setVisibility(View.GONE);
			}
			
			if (mQuestionNum == 0) {
				mBtnGreen.setVisibility(View.GONE);
			} else {
				String state = String.format(" - (%d/%d)", mResponseNum, mQuestionNum);
				
				if (mResponseNum == 0) {
					mBtnGreen.setText(getString(R.string.take_survey) + state);
				} else {
					mBtnGreen.setText(getString(R.string.finish_survey) + state);
				}
			}
		} else {
			mBtnRed.setVisibility(View.GONE);
			mBtnBlack.setVisibility(View.GONE);
			mBtnGreen.setText(R.string.add_to_myagenda);
		}
		
		mBtnGreen2.setVisibility(View.GONE);
	}
	
	/**
	 * Add a Agenda to MyAgenda list
	 */
	private void addToMyAgenda() {
		new RunOperationTask(this).execute(Utils.getActionAPIString(ADD_ACTION, ID_TYPE, mSessionId));
	}
	
	/**
	 * Remove a Agenda from MyAgenda list
	 */
	private void removeFromMyAgenda() {
		new RunOperationTask(this).execute(Utils.getActionAPIString(REMOVE_ACTION, ID_TYPE, mSessionId));
	}
	
	/**
	 * Check in MyAgenda
	 */
	private void checkInMyAgenda() {
		new RunOperationTask(this).execute(Utils.getActionAPIString(CHECKED_ACTION, ID_TYPE, mSessionId));
	}
	
	/**
	 * Finish this MyAgenda
	 */
	private void finishSurveyMyAgenda() {
		Intent intent = new Intent(AgendaDetailActivity.this, FinishSurveyActivity.class);
		intent.putExtra(Action.SELECTED_ID, mSessionId);
		startActivity(intent);
	}

	@Override
	protected void processOperationResult() {
		onResume();
	}
	
}
