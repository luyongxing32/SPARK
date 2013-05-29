/**
 * @author Ry
 * @Date 2012.12.10
 * @FileName SendMessageActivity.java
 *
 */

package com.twoservices.spark.network;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.InstanceMessageActivity;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.link.MessageInfo;
import com.twoservices.spark.link.MessageResult;

import java.util.ArrayList;

/**
 * Activity to send message to Network of <B>"Engage US"</B>
 *
 */
public class SendMessageActivity extends InstanceMessageActivity {
	
	private static final String TAG = SendMessageActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	
	private static final String SEND_MESSAGE = "Send_Message";
	private static final String GET_MESSAGE = "Get_Message"; 
	private static final String ID_TYPE = "uu_network_contact_id";
	
	// id of Network that will display
	private String mNetworkId = "";
	
	
	/** Called when the activity is starting. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get Network id from intent
		Intent intent = getIntent();
		mNetworkId = intent.getStringExtra(Action.SELECTED_ID);
	}

	/**
	 * Sort MessageInfo array object
	 */
	private void sortMessageInfo(ArrayList<MessageInfo> messageInfos) {
		MessageInfo first, second, tmp = new MessageInfo();
		int size = messageInfos.size();

		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				first = messageInfos.get(i);
				second = messageInfos.get(j);

				if (first.message_id > second.message_id) {
					tmp.message_id = first.message_id;
					tmp.message_body = first.message_body;

					first.message_id = second.message_id;
					first.message_body = second.message_body;
					
					second.message_id = tmp.message_id;
					second.message_body = tmp.message_body;
				}
			}
		}
	}
	
	/**
	 * Load the messages were sent previously
	 */
	protected void loadOldMessage() {
		new RunOperateMessageTask("").execute(Utils.getActionAPIString(GET_MESSAGE, ID_TYPE, mNetworkId));
	}
	
	/**
	 * Send the message was inputed by user
	 */
	protected void sendMessage() {
		String newMessage = mEditMessage.getText().toString();
		String apiString = Utils.getActionAPIString(SEND_MESSAGE, ID_TYPE, mNetworkId);
		apiString += "&message_body=" + Uri.encode(newMessage).toString();
		
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
			Feedback.initProgressDialog(SendMessageActivity.this);
			Feedback.showProgress();
		}

		@Override
		protected void onPostExecute(String response) {
			
			if (Feedback.isSuccess()) {
				if (Feedback.sServiceResponse.action.equals(GET_MESSAGE)) {
					
					MessageResult result = Feedback.parseData(response, MessageResult.class);

					if (DEBUG) Log.i(TAG, "Old message count = " + Feedback.sServiceResponse.total_results);
					
					mTextTitle.setText(Utils.getNormalBoldString(getString(R.string.message_for), result.name));
					
					if (result.messages != null) {
						sortMessageInfo(result.messages);
						
						StringBuilder sbOldMessage = new StringBuilder();
						for (MessageInfo info : result.messages) {
							sbOldMessage.append("\u2022 ").append(info.message_body).append("\r\n");
						}
						
						mTextSentMessage.setText(sbOldMessage.toString());
					}
				} else if (Feedback.sServiceResponse.action.equals(SEND_MESSAGE)) {
					
					mEditMessage.setText("");
					
					StringBuilder sbMessage = new StringBuilder(mTextSentMessage.getText());
					sbMessage.append("\u2022 ").append(mSentMessage).append("\r\n");
					mTextSentMessage.setText(sbMessage.toString());
				}
			} else {
				Utils.informMessage(SendMessageActivity.this, "Could not get results from server.");
				Utils.logout(SendMessageActivity.this);
			}
			
			// stop progress
			Feedback.dismissProgress();
		}
		
	}
	
}
