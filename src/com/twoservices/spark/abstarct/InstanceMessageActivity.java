package com.twoservices.spark.abstarct;
/**
 * @author Ry
 * @Date 2013.5.8
 * @FileName InstanceMessageActivity.java
 *
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.twoservices.spark.MainActivity;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;

/**
 * Activity to send instance message
 *
 */
public abstract class InstanceMessageActivity extends Activity2 {
	
	static final String TAG = InstanceMessageActivity.class.getSimpleName();
	static final boolean DEBUG = Utils.DEBUG;
	
	// TextView that will display title
	protected TextView mTextTitle;
	
	// EditText that will display the new message was inputed.
	protected EditText mEditMessage;
	
	// TextView that will display the message was sent.
	protected TextView mTextSentMessage;
	
	// Button that will send message
	private Button mBtnSend;

	
	/** Called when the activity is starting. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_sendmessage);
		super.setupTitleAndButtons();
		super.setTextOnMySubjectButton(getString(R.string.home));
		super.changeBackgroundOnMySubjectButton(R.drawable.home_button_back);
		
		initWidgetsOnScreen();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		loadOldMessage();
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		case R.id.btn_mysubject:
			startActivity(new Intent(InstanceMessageActivity.this, MainActivity.class));
			break;
			
		case R.id.btn_send:
			if (mEditMessage.length() > 0)
				sendMessage();
			break;
		}
	}

	/**
	 * Initialize the widgets
	 */
	private void initWidgetsOnScreen() {
		mTextTitle = (TextView) findViewById(R.id.text_message_title);
		mEditMessage = (EditText) findViewById(R.id.edit_message);
		mTextSentMessage = (TextView) findViewById(R.id.text_sent_message);

		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
	}
	
	/**
	 * Load the messages were sent previously
	 */
	protected abstract void loadOldMessage();
	
	/**
	 * Send the message was inputed by user
	 */
	protected abstract void sendMessage();
	
}
