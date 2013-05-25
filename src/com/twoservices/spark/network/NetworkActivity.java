/**
 * @author Ry
 * @Date 2012.11.25
 * @FileName NetworkActivity.java
 *
 */

package com.twoservices.spark.network;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.FilteredListActivity;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.link.NetworkResult;
import com.twoservices.spark.list.TwoRowItem;
import com.twoservices.spark.list.TwoRowItemView;

/**
 * News activity of <B>"Engage US"</B>
 * 
 */
public class NetworkActivity extends FilteredListActivity implements View.OnClickListener {

	private static final String TAG = NetworkActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	
	// define member variables
	// LinearLayout that will include views of "Pending Request" part.
	private LinearLayout mRequestPart;
	
	// LinearLayout that will include views of "Network" part.
	private LinearLayout mNetworkPart;
	
	// TextView that will indicate "Network" part
	@SuppressWarnings("unused")
	private TextView mTextNetwork;
	
	// EditText that the string will fill to search the matched items
	private EditText mEditNetworkSearch;
	private EditText mEditRequestSearch;
	
	// Image correspond to close search
	private ImageView mImgCloseNetworkSearch;
	private ImageView mImgCloseRequestSearch;

	// ListView that will hold own pending request items information
	private LinearLayout mRequestListView;

	private NetworkResult[] mRequestResult = null;
	

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_list);
		
		super.getAction();

		initViewsOnScreen();
		setupTitleAndButtons();
		setupSearchEditText();
		super.setupListAndSponsorView();
		
		/*if (mIsMySubject) {
			mRequestPart.setVisibility(View.VISIBLE);
			mTextNetwork.setVisibility(View.VISIBLE);
		}*/
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loadList(NetworkResult[].class);
		loadSponsors(Action.NETWORK);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.img_search_close:
			String tag = (String) v.getTag();
			if (getString(R.string.pending_requests).equals(tag))
				mEditRequestSearch.setText("");
			else if (getString(R.string.network).equals(tag))
				mEditNetworkSearch.setText("");
			return;
			
		case R.id.btn_mysubject:
			String action = mIsMySubject ? Action.NETWORK : Action.MY_NETWORK;
				
			Intent intent = new Intent();
			intent.setClass(NetworkActivity.this, NetworkActivity.class);
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
			super.setTextOnMySubjectButton(getString(R.string.network));
			super.changeBackgroundOnMySubjectButton(R.drawable.subject_back);
		} else {
			super.setTextOnMySubjectButton(getString(R.string.mynetwork_tag));
			super.changeBackgroundOnMySubjectButton(R.drawable.my_subject_back);
		}
	}
	
	/**
	 * Initiate views on this activity 
	 */
	private void initViewsOnScreen() {
		// Request part
		mRequestPart = (LinearLayout) findViewById(R.id.pending_request);
		mEditRequestSearch = (EditText) mRequestPart.findViewById(R.id.edit_search);
		mImgCloseRequestSearch = (ImageView) mRequestPart.findViewById(R.id.img_search_close);
		mRequestListView = (LinearLayout) mRequestPart.findViewById(R.id.request_list_view);
		
		// Network part
		mNetworkPart = (LinearLayout) findViewById(R.id.network_part);
		mTextNetwork = (TextView) mNetworkPart.findViewById(R.id.text_network);
		mEditNetworkSearch = (EditText) mNetworkPart.findViewById(R.id.edit_search);
		mImgCloseNetworkSearch = (ImageView) mNetworkPart.findViewById(R.id.img_search_close);
	}
	
	/**
	 *  Get EditText and add listener
	 */
	@Override
	protected void setupSearchEditText() {
		// Request part
		mEditRequestSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0)
					mImgCloseRequestSearch.setVisibility(View.VISIBLE);
				else
					mImgCloseRequestSearch.setVisibility(View.GONE);

				showRequestList(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mImgCloseRequestSearch.setOnClickListener(this);
		mImgCloseRequestSearch.setTag(getString(R.string.pending_requests));
		
		// Network part
		mEditNetworkSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() > 0)
					mImgCloseNetworkSearch.setVisibility(View.VISIBLE);
				else
					mImgCloseNetworkSearch.setVisibility(View.GONE);
				
				showNetworkList(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		mImgCloseNetworkSearch.setOnClickListener(this);
		mImgCloseNetworkSearch.setTag(getString(R.string.network));
	}

	/**
	 * Load the Network list form web server
	 */
	@Override
	protected void loadList(Class<?> clsType) {
		setEditSearch(mEditNetworkSearch);
		super.loadList(clsType);
		
		// Request part
		/*if (mIsMySubject) {
			// Run service API using the url with project id and contact id
			new LoadRequestListTask().execute(Utils.getActionAPIString(mAction, null, null));
		}*/
	}
	
	/**
	 * Show Request and Network list
	 * 
	 * @param filter
	 *            string to filter "name" and "title" field
	 */
	@Override
	protected void showList(String filter) {
		// Network part
		showNetworkList(filter);
	}
	
	/**
	 * Show Request list
	 * 
	 * @param filter
	 *            string to filter "name" and "title" field
	 */
	private void showRequestList(String filter) {
		// Initiate the content of ListView
		mRequestListView.removeAllViews();

		if (mRequestResult != null) {
			ArrayList<String> sectionList = new ArrayList<String>();
			String strName;
			String strTitle;
			String strSearch = filter.toLowerCase();

			for (NetworkResult result : mRequestResult) {
				strName = result.name.toLowerCase();
				strTitle = result.title.toLowerCase();

				if (strTitle.indexOf(strSearch) != -1
						|| strName.indexOf(strSearch) != -1) {
					mRequestListView.addView(new TwoRowItemView(
							NetworkActivity.this,
							new TwoRowItem(
									result.network_contact_id,
									"Y".equals(result.invited) || "Invited".equals(result.relationship), 
									result.name,
									result.title),
									"gotoDetailView"));
				}
			}

			sectionList.clear();
		}
	}
	
	/**
	 * Show Network list
	 * 
	 * @param filter
	 *            string to filter "name" and "title" field
	 */
	private void showNetworkList(String filter) {
		// Initiate the content of ListView
		mListView.removeAllViews();

		if (mAPIResult != null) {
			ArrayList<String> sectionList = new ArrayList<String>();
			String strName;
			String strTitle;
			String strSearch = filter.toLowerCase();

			for (NetworkResult result : (NetworkResult[]) mAPIResult) {
				strName = result.name.toLowerCase();
				strTitle = result.title.toLowerCase();

				if (strTitle.indexOf(strSearch) != -1
						|| strName.indexOf(strSearch) != -1) {
					mListView.addView(new TwoRowItemView(
							NetworkActivity.this,
							new TwoRowItem(
									result.network_contact_id,
									"Y".equals(result.invited) || "Invited".equals(result.relationship), 
									result.name,
									result.title),
									"gotoDetailView"));
				}
			}

			sectionList.clear();
		}
	}
	
	/**
	 * Goto DetailActivity when item was clicked
	 * 
	 * @param view
	 *            selected view
	 */
	public void gotoDetailView(View view) {
		TwoRowItem item = (TwoRowItem) view.getTag();
		
		if (DEBUG) Log.i(TAG, "Selected item = " + item.title);
		
		Intent intent = new Intent(NetworkActivity.this, NetworkDetailActivity.class);
		intent.putExtra(Action.SELECTED_ID, item.id);
		
		startActivity(intent);
	}
	
	/**
	 * AsyncTask to load the list of each event
	 *
	 */
	@SuppressWarnings("unused")
	private class LoadRequestListTask extends LoadFeedData {

		@Override
		protected void onPostExecute(String response) {
			// if (DEBUG) Log.i(TAG, "API response = " + response);

			if (Feedback.isSuccess()) {
				mRequestResult = Feedback.parseDataArray(response, NetworkResult[].class);
				showRequestList("");
			} else {
				mRequestResult = null;
				Utils.informMessage(NetworkActivity.this, "Could not get results from server.");
				Utils.logout(NetworkActivity.this);
			}
			
		}
		
	}
	
}