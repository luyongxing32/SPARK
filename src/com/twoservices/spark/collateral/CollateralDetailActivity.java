/**
 * @author Ry
 * @Date 2012.11.27
 * @FileName CollateralDetailActivity.java
 *
 */

package com.twoservices.spark.collateral;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.twoservices.spark.AccountInfo;
import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.ItemDetailActivity;
import com.twoservices.spark.link.CollateralDetailResult;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.IDetailResult;

/**
 * Activity to edit own Exhibitor of <B>"Engage US"</B>
 *
 */
public class CollateralDetailActivity extends ItemDetailActivity implements View.OnClickListener {

	private static final String LOAD_ACTION = "Collateral_Detail";
	private static final String ADD_ACTION = "Add_MyCollateral";
	private static final String REMOVE_ACTION = "Remove_MyCollateral";
	
	private static final String ID_TYPE = "uu_collateral_id";
	
	// Flag that will decide whether Collateral or myCollateral mode
	private boolean mIsMyCollateral = false;
	
	// id of Collateral that will display
	private String mCollateralId = "";
	
	
	/** Called when the activity is starting. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setTextOnMySubjectButton(getString(R.string.mycollateral_tag));
		
		// Get Collateral id from intent
		Intent intent = getIntent();
		mCollateralId = intent.getStringExtra(Action.SELECTED_ID);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (TextUtils.isEmpty(mCollateralId)) {
			showEmailMeResult();
			
			// Stop progress
			Feedback.dismissProgress();
		} else {
			loadDetail(Utils.getActionAPIString(LOAD_ACTION, ID_TYPE, mCollateralId), CollateralDetailResult.class);
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_mysubject:
			// Change view to MainActivity
			Intent intent = new Intent();
			intent.setClass(CollateralDetailActivity.this, CollateralActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.MY_Collateral);
			
			startActivity(intent);
			break;
		
		case R.id.btn_red:
			// Remove the current item from myCollateral
			removeFromMyCollateral();
			break;
			
		case R.id.btn_green:
			// Add the current item to myCollateral
			addToMyCollateral();
			break;
		}
	}
	
	/**
	 * Set item data with detail information
	 */
	@Override
	protected void showDetail(IDetailResult result) {
		
		if (result != null) {
			CollateralDetailResult detailResult = (CollateralDetailResult) result;

			mIsMyCollateral = detailResult.is_mycollateral.equals("Y");
			
			mTextItemTitle.setText(detailResult.file_title);
			mTextItemDate.setText(detailResult.file_name);
			
			mTextItemTime.setText(Utils.getLinkedFileText(
					getString(R.string.view_document), detailResult.file_name, detailResult.file_size));
			mTextItemTime.setMovementMethod(LinkMovementMethod.getInstance());
			
			if (!TextUtils.isEmpty(detailResult.file_descrition)) {
				mTextItemContent.setVisibility(View.VISIBLE);
				mTextItemContent.setText(detailResult.file_descrition);
			} else {
				mTextItemContent.setVisibility(View.GONE);
			}
			
			mTextItemAuthor.setVisibility(View.GONE);
		}
		
		setTextOnActionButtons();
	}
	
	/**
	 *  Set text of "add", "remove" buttons
	 */
	private void setTextOnActionButtons() {
		
		if (mIsMyCollateral) {
			mBtnRed.setVisibility(View.VISIBLE);
			mBtnRed.setText(R.string.remove_from_mycollateral);
			
			mBtnGreen.setVisibility(View.GONE);
		} else {
			mBtnRed.setVisibility(View.GONE);
			
			mBtnGreen.setVisibility(View.VISIBLE);
			mBtnGreen.setText(R.string.add_to_mycollateral);
		}
		
		mBtnBlack.setVisibility(View.GONE);
		mBtnGreen2.setVisibility(View.GONE);
	}
	
	/**
	 * Add a Collateral to MyCollateral list
	 */
	private void addToMyCollateral() {
		new RunOperationTask(this).execute(Utils.getActionAPIString(ADD_ACTION, ID_TYPE, mCollateralId));
	}
	
	/**
	 * Remove a Collateral from MyCollateral list
	 */
	private void removeFromMyCollateral() {
		new RunOperationTask(this).execute(Utils.getActionAPIString(REMOVE_ACTION, ID_TYPE, mCollateralId));
	}
	
	/**
	 * Show "Email me" function result
	 */
	private void showEmailMeResult() {
		
		mTextItemTitle.setVisibility(View.GONE);
		mTextItemDate.setVisibility(View.GONE);
		mTextItemTime.setVisibility(View.GONE);
		mTextItemAuthor.setVisibility(View.GONE);
		
		mBtnRed.setVisibility(View.GONE);
		mBtnBlack.setVisibility(View.GONE);
		mBtnGreen.setVisibility(View.GONE);
		mBtnGreen2.setVisibility(View.GONE);
		
		mTextItemContent.setText(getString(R.string.email_sent_success) + AccountInfo.sMyEmail);
	}

	@Override
	protected void processOperationResult() {
		onResume();
	}
	
}
