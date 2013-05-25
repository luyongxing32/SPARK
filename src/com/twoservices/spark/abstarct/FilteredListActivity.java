/**
 * @author Ry
 * @Date 2012.11.18
 * @FileName FilteredListActivity.java
 *
 */

package com.twoservices.spark.abstarct;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.IResult;
import com.twoservices.spark.link.LoadFeedData;

/**
 * abstract ListActivity with search function
 * 
 */
public abstract class FilteredListActivity extends Activity2 {
	
	// define member variables
	// EditText that the string will fill to search the matched items
	private EditText mEditSearch;

	// Image correspond to close search
	private ImageView mImgCloseSearch;

	// LinearLayout that will hold own list items information
	protected LinearLayout mListView;

	// IResult object array has been got using service API
	protected IResult[] mAPIResult;

	// Action string be used in API
	private String mAction;

	// Flag whether current event item is My<Subject> or not 
	protected boolean mIsMySubject = false;
	
	// Class type to cast
	private Class<?> mClsType;
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_search_close:
			mEditSearch.setText("");
			return;
		}
		
		super.onClick(v);
	}
	
	/**
	 * Get action value from intent
	 */
	protected void getAction() {
		Intent intent = getIntent();
		
		mAction = intent.getStringExtra(Action.EXTRA_DATA);
		mIsMySubject = mAction.contains("My");//mAction.equals(Action.MY_AGENDA);
	}
	
	/**
	 * Set action value
	 */
	protected void setAction(String action) {
		mAction = action;
	}
	
	/**
	 * Set mEditSearch variable by edit
	 */
	protected void setEditSearch(EditText edit) {
		mEditSearch = edit;
	}
	
	/**
	 *  Get EditText and add listener
	 */
	protected void setupSearchEditText() {
		mEditSearch = (EditText) findViewById(R.id.edit_search);
		mEditSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0)
					mImgCloseSearch.setVisibility(View.VISIBLE);
				else
					mImgCloseSearch.setVisibility(View.GONE);
				
				showList(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		mImgCloseSearch = (ImageView) findViewById(R.id.img_search_close);
		mImgCloseSearch.setOnClickListener(this);
	}

	/**
	 *  Get event list view and sponsor list view
	 */
	protected void setupListAndSponsorView() {
		mListView = (LinearLayout) findViewById(R.id.list_view);
		mTextSponsored = (TextView) findViewById(R.id.text_sponsered_by);
		mSponsorView = (LinearLayout) findViewById(R.id.sponsor_view);
	}

	/**
	 * Load the event list form web server
	 */
	protected void loadList(Class<?> clsType) {
		mClsType = clsType;
		
		// Run service API using the url with project id and contact id
		new LoadListTask().execute(Utils.getActionAPIString(mAction, null, null));
	}

	/**
	 * Show list of current subject
	 * 
	 * @param filter
	 *            string to filter "file_title" field
	 */
	protected abstract void showList(String filter);

	/**
	 * AsyncTask to load the list of each event
	 *
	 */
	protected class LoadListTask extends LoadFeedData {

		@Override
		protected void onPreExecute() {
			// Hide all views
			hideViewsOnScreen();
		}
		
		@Override
		protected void onPostExecute(String response) {
			// if (DEBUG) Log.i(TAG, "API response = " + response);

			if (Feedback.isSuccess()) {
				mAPIResult = (IResult[]) Feedback.parseDataArray(response, mClsType);
				showList(mEditSearch.getText().toString());

				// Show all view that include new state
				showViewsOnScreen();
			} else {
				mAPIResult = null;
				Utils.informMessage(FilteredListActivity.this, "Could not get results from server.");
				Utils.logout(FilteredListActivity.this);
			}
		}
		
	}
}
