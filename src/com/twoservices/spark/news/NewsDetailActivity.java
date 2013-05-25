/**
 * @author Ry
 * @Date 2012.11.27
 * @FileName NewsDetailActivity.java
 *
 */

package com.twoservices.spark.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.twoservices.spark.Action;
import com.twoservices.spark.MainActivity;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.ItemDetailActivity;
import com.twoservices.spark.link.IDetailResult;
import com.twoservices.spark.link.NewsDetailResult;

/**
 * Activity to edit own News of <B>"Engage US"</B>
 *
 */
public class NewsDetailActivity extends ItemDetailActivity implements View.OnClickListener {

	private static final String LOAD_ACTION = "News_Detail";
	
	private static final String ID_TYPE = "uu_news_id";
	
	// id of News that will display
	private String mNewsId = "";
	
	
	/** Called when the activity is starting. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setTextOnMySubjectButton(getString(R.string.home));
		
		// Get news id from intent
		Intent intent = getIntent();
		mNewsId = intent.getStringExtra(Action.SELECTED_ID);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		loadDetail(Utils.getActionAPIString(LOAD_ACTION, ID_TYPE, mNewsId), NewsDetailResult.class);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_mysubject:
			// Change view to MainActivity
			startActivity(new Intent(NewsDetailActivity.this, MainActivity.class));
			break;
		}
	}
	
	/**
	 * Set item data with detail information
	 */
	@Override
	protected void showDetail(IDetailResult result) {
		
		if (result != null) {
			NewsDetailResult detailResult = (NewsDetailResult) result;
			
			mTextItemTitle.setText(detailResult.title);
			mTextItemDate.setText(/*Utils.toMyDateFormat(*/detailResult.dateposted/*)*/);
			mTextItemTime.setText(detailResult.timeposted);
			mTextItemContent.setText(detailResult.body);
			mTextItemAuthor.setText(getString(R.string.author) + ": " + detailResult.author);
		}
		
		setTextOnActionButtons();
	}
	
	/**
	 *  Set text of "remove", "add", "check", "finish" buttons
	 */
	private void setTextOnActionButtons() {
		mBtnRed.setVisibility(View.GONE);
		mBtnBlack.setVisibility(View.GONE);
		mBtnGreen.setVisibility(View.GONE);
		mBtnGreen2.setVisibility(View.GONE);
	}

	@Override
	protected void processOperationResult() {
	}
	
}
