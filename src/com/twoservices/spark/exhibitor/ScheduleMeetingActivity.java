/**
 * @author Ry
 * @Date 2013.5.8
 * @FileName ScheduleMeetingActivity.java
 *
 */

package com.twoservices.spark.exhibitor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.twoservices.spark.AccountInfo;
import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.InstanceMessageActivity;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.link.MessageInfo;
import com.twoservices.spark.link.MessageResult;

/**
 * Activity to send message to Network of <B>"Engage US"</B>
 *
 */
public class ScheduleMeetingActivity extends InstanceMessageActivity {
	
	private static final String TAG = ScheduleMeetingActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	
	private static final String SEND_MESSAGE = "Exhibitor_Send_Message";
	private static final String GET_MESSAGE = "Exhibitor_Get_Message"; 
	private static final String ID_TYPE = "uu_exhibitor_id";
	
	// id of Network that will display
	private String mExhibitorId = "";
	
	// company name
	private String mCompanyName = "";
	
	
	/** Called when the activity is starting. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get Network id from intent
		Intent intent = getIntent();
		mExhibitorId = intent.getStringExtra(Action.SELECTED_ID);
		mCompanyName = intent.getStringExtra(Action.EXTRA_DATA);
	}

	/**
	 * Load the messages were sent previously
	 */
	protected void loadOldMessage() {
		new RunOperateMessageTask("").execute(Utils.getActionAPIString(GET_MESSAGE, ID_TYPE, mExhibitorId));
	}
	
	/**
	 * Send the message was inputed by user
	 */
	protected void sendMessage() {
		String newMessage = mEditMessage.getText().toString();
		String apiString = Utils.getActionAPIString(SEND_MESSAGE, ID_TYPE, mExhibitorId);
		apiString += "&message_body=" + Uri.encode(newMessage).toString() 
				+ "&contact_name=" + Uri.encode(AccountInfo.sUserName);
		
		new RunOperateMessageTask(newMessage).execute(apiString);
	}
	
	/**
	 * AsyncTask to run operation
	 * 
	 */
	private class RunOperateMessageTask extends LoadFeedData {
		
		private String mSentMessage;
		
		public RunOperateMessageTask(String sendMsg) {
			mSentMessage = sendMsg;
		}
		
		@Override
		protected void onPreExecute() {
			Feedback.initProgressDialog(ScheduleMeetingActivity.this);
			Feedback.showProgress();
		}

		@Override
		protected void onPostExecute(String response) {
			
			if (Feedback.isSuccess()) {
				if (Feedback.sServiceResponse.action.equals(GET_MESSAGE)) {
					
					MessageResult result = Feedback.parseData(response, MessageResult.class);

					if (DEBUG) Log.i(TAG, "Old message count = " + Feedback.sServiceResponse.total_results);
					
					mTextTitle.setText(Utils.getNormalBoldString(getString(R.string.message_for), mCompanyName));
					
					if (result.messages != null && result.messages.size() != 0) {
						StringBuilder sbOldMessage = new StringBuilder();
						for (MessageInfo info : result.messages) {
							sbOldMessage.append("\u2022 ")
								.append(info.sent_from).append(": ")
								.append(info.message_body).append("\r\n");
						}
						
						mTextSentMessage.setText(sbOldMessage.toString());
					} else {
						mEditMessage.setText(mCompanyName + getString(R.string.setup_meeting));
						sendMessage();
					}
				} else if (Feedback.sServiceResponse.action.equals(SEND_MESSAGE)) {
					
					mEditMessage.setText("");
					
					StringBuilder sbMessage = new StringBuilder(mTextSentMessage.getText());
					sbMessage.append("\u2022 ")
						.append(AccountInfo.sUserName).append(": ")
						.append(mSentMessage).append("\r\n");
					mTextSentMessage.setText(sbMessage.toString());
				}
			} else {
				Utils.informMessage(ScheduleMeetingActivity.this, "Could not get results from server.");
				Utils.logout(ScheduleMeetingActivity.this);
			}
			
			// stop progress
			Feedback.dismissProgress();
		}
		
	}
	
}
