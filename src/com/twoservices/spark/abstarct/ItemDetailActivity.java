package com.twoservices.spark.abstarct;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.IDetailResult;
import com.twoservices.spark.link.LoadFeedData;

public abstract class ItemDetailActivity extends Activity2 implements View.OnClickListener {

	// red, black, green buttons 
	protected Button mBtnRed;
	protected Button mBtnBlack;
	protected Button mBtnGreen;
	protected Button mBtnGreen2;
	
	// TextView that will display the title of selected item
	protected TextView mTextItemTitle;
	
	// ImageView that will display the logo of selected item
	protected ImageView mImageItemLogo;
	
	// TextView that will display the website and email of selected item
	protected TextView mTextItemWebsite;
	protected TextView mTextItemEmail;
	
	// TextView that will display the time of selected item
	protected TextView mTextItemDate;
	protected TextView mTextItemTime;
	
	// TextView that will display the track and company etc of selected item
	protected TextView mTextItemMisc;
	
	// TextView that will display the content of selected item
	protected TextView mTextItemContent;
	
	// TextView that will display the author of selected item
	protected TextView mTextItemAuthor;
	
	// Class type to cast
	private Class<?> mClsType;
	
	
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_details);
		
		super.setupTitleAndButtons();
		
		addListenerToButtons();
		initTextView();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	
	/**
	 * Add listener to buttons on screen
	 */
	private void addListenerToButtons() {
		mBtnRed = (Button) findViewById(R.id.btn_red);
		mBtnRed.setOnClickListener(this);
		
		mBtnBlack = (Button) findViewById(R.id.btn_black);
		mBtnBlack.setOnClickListener(this);
		
		mBtnGreen = (Button) findViewById(R.id.btn_green);
		mBtnGreen.setOnClickListener(this);
		
		mBtnGreen2 = (Button) findViewById(R.id.btn_green2);
		mBtnGreen2.setOnClickListener(this);
	}

	/**
	 * Initiate the TextViews on screen
	 */
	private void initTextView() {
		mTextItemTitle = (TextView) findViewById(R.id.text_item_title);
		mImageItemLogo = (ImageView) findViewById(R.id.image_item_logo);
		mTextItemWebsite = (TextView) findViewById(R.id.text_item_website);
		mTextItemEmail = (TextView) findViewById(R.id.text_item_email);
		mTextItemDate = (TextView) findViewById(R.id.text_item_date);
		mTextItemTime = (TextView) findViewById(R.id.text_item_time);
		mTextItemContent = (TextView) findViewById(R.id.text_item_content);
		mTextItemAuthor = (TextView) findViewById(R.id.text_item_author);
		mTextItemMisc = (TextView) findViewById(R.id.text_item_misc);

		mImageItemLogo.setVisibility(View.GONE);
		mTextItemWebsite.setVisibility(View.GONE);
		mTextItemEmail.setVisibility(View.GONE);
		mTextItemMisc.setVisibility(View.GONE);
	}
	
	/**
	 * Load event detail result from service API
	 * 
	 * @return IDetialResult object
	 */
	protected void loadDetail(String apiStr, Class<?> clsType) {
		mClsType = clsType;
		new LoadDetailTask().execute(apiStr);
	}

	/**
	 * Show item data with detail information
	 */
	protected abstract void showDetail(IDetailResult result);
	
	/**
	 * Process operation result
	 */
	protected abstract void processOperationResult();
	
	
	/**
	 * AsyncTask to load the detail information of event
	 *
	 */
	private class LoadDetailTask extends LoadFeedData {
		
		@Override
		protected void onPreExecute() {
			// Hide all views
			hideViewsOnScreen();
		}

		@Override
		protected void onPostExecute(String response) {
			// if (DEBUG) Log.i(TAG, "API response = " + response);

			if (Feedback.isSuccess()) {
				showDetail((IDetailResult) Feedback.parseData(response, mClsType));
				
				// Show all view that include new state
				showViewsOnScreen();
			} else {
				showDetail(null);
				Utils.informMessage(ItemDetailActivity.this, "Could not get results from server.");
				Utils.logout(ItemDetailActivity.this);
			}
			
			// stop progress
			Feedback.dismissProgress();
		}
		
	}
	
	/**
	 * AsyncTask to run operation
	 * 
	 */
	protected class RunOperationTask extends LoadFeedData {

		private Context mContext;
		
		public RunOperationTask(Context context) {
			mContext = context;
		}
		
		@Override
		protected void onPreExecute() {
			Feedback.initProgressDialog(mContext);
			Feedback.showProgress();
		}

		@Override
		protected void onPostExecute(String response) {
			if (Feedback.isSuccess()) 
				processOperationResult();
			else
				Utils.informMessage(ItemDetailActivity.this, "Could not get results from server.");
		}
		
	}
	
}
