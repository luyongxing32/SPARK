/**
 * @author Ry
 * @Date 2012.11.27
 * @FileName NetworkDetailActivity.java
 *
 */

package com.twoservices.spark.network;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.ItemDetailActivity;
import com.twoservices.spark.link.IDetailResult;
import com.twoservices.spark.link.NetworkDetailResult;

/**
 * Activity to edit own Network of <B>"Engage US"</B>
 *
 */
public class NetworkDetailActivity extends ItemDetailActivity implements View.OnClickListener {

	private static final String LOAD_ACTION = "Network_Contact_Detail";
	private static final String ADD_ACTION = "Add_MyNetwork";
	private static final String REMOVE_ACTION = "Remove_MyNetwork";
	
	private static final String ID_TYPE = "uu_network_contact_id";
	
	// Flag that will decide whether Network or myNetwork mode
	private boolean mIsMyNetwork = false;
	
	// id of Network that will display
	private String mNetworkId = "";
	
	
	/** Called when the activity is starting. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setTextOnMySubjectButton(getString(R.string.mynetwork_tag));
		
		// Get Network id from intent
		Intent intent = getIntent();
		mNetworkId = intent.getStringExtra(Action.SELECTED_ID);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		loadDetail(Utils.getActionAPIString(LOAD_ACTION, ID_TYPE, mNetworkId), NetworkDetailResult.class);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_mysubject:
			// Change view to NetworkActivity
			Intent intent = new Intent();
			intent.setClass(NetworkDetailActivity.this, NetworkActivity.class);
			intent.putExtra(Action.EXTRA_DATA, Action.MY_NETWORK);
			
			startActivity(intent);
			break;
		
		case R.id.btn_red:
			// Remove the current item from myNetwork
			removeFromMyNetwork();
			break;
			
		case R.id.btn_green:
			// Add the current item to myNetwork
			addToMyNetwork();
			break;
			
		case R.id.btn_green2:
			// Send message with myNetwork list
			sendMessage();
			break;
		}
	}
	
	/**
	 * Set item data with detail information
	 */
	@Override
	protected void showDetail(IDetailResult result) {
		
		if (result != null) {
			NetworkDetailResult detailResult = (NetworkDetailResult) result;
			
			mIsMyNetwork = detailResult.is_mynetwork.equals("Y");
			
			mTextItemTitle.setText(detailResult.name);
			mTextItemDate.setVisibility(View.GONE);
			mTextItemTime.setVisibility(View.GONE);
			mTextItemContent.setText(Utils.getBoldNormalString(detailResult.company, detailResult.title));
			mTextItemAuthor.setVisibility(View.GONE);
		}
		
		setTextOnActionButtons();
	}
	
	/**
	 *  Set text of "remove", "add", "send message" buttons
	 */
	private void setTextOnActionButtons() {
		
		if (mIsMyNetwork) {
			mBtnRed.setVisibility(View.VISIBLE);
			mBtnRed.setText(R.string.remove_from_mynetwork);
			
			mBtnGreen.setVisibility(View.GONE);
		} else {
			mBtnRed.setVisibility(View.GONE);
			
			mBtnGreen.setVisibility(View.VISIBLE);
			mBtnGreen.setText(R.string.add_to_mynetwork);
		}
		
		mBtnBlack.setVisibility(View.GONE);
		mBtnGreen2.setText(R.string.send_message);
	}
	
	/**
	 * Add a Network to MyNetwork list
	 */
	private void addToMyNetwork() {
		new RunOperationTask(this).execute(Utils.getActionAPIString(ADD_ACTION, ID_TYPE, mNetworkId));
	}
	
	/**
	 * Remove a Network from MyNetwork list
	 */
	private void removeFromMyNetwork() {
		new RunOperationTask(this).execute(Utils.getActionAPIString(REMOVE_ACTION, ID_TYPE, mNetworkId));
	}
	
	/**
	 * Send message with myNetwork list
	 */
	private void sendMessage() {
		Intent intent = new Intent(NetworkDetailActivity.this, SendMessageActivity.class);
		intent.putExtra(Action.SELECTED_ID, mNetworkId);
		startActivity(intent);
	}

	@Override
	protected void processOperationResult() {
		onResume();
	}
	
}
