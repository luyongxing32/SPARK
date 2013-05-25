/**
 * @author Ry
 * @Date 2012.11.10
 * @FileName RegisterActivity.java
 *
 */

package com.twoservices.spark;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;

/**
 * Activity to register the account to <B>"Engage US"</B>
 *
 */
public class RegisterActivity extends Activity implements View.OnClickListener {
	
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	
	// define the UI state
	private static final int REGISTER_INPUT_WAIT = 0;
	private static final int REGISTER_INPUT_FAILURE = 1;
	private static final int REGISTER_NETWORK_FAILURE = 2;
	
	// define member variables
	// sub-screens of login
	private LinearLayout mLayoutRegisterInputWaiting;
	private LinearLayout mLayoutRegisterInputFailure;
	
	// register input waiting screen
	private EditText mEditFirstname;
	private EditText mEditLastname;
	private EditText mEditMobile;
	private EditText mEditEmail;
	private EditText mEditPassword;
	private Button mBtnRegister;
	
	// register input screen failure
	private Button mBtnOK;
	
	private static RegisterActivity sMe = null;
	
	/**
	 * mUIHandler handle the user interface according to login state.
	 */
	private static Handler sUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (sMe != null) {
				switch (msg.what) {
				case REGISTER_INPUT_WAIT:
					sMe.mLayoutRegisterInputWaiting.setVisibility(View.VISIBLE);
					sMe.mLayoutRegisterInputFailure.setVisibility(View.GONE);
					break;
					
				case REGISTER_INPUT_FAILURE:
					sMe.mLayoutRegisterInputWaiting.setVisibility(View.GONE);
					sMe.mLayoutRegisterInputFailure.setVisibility(View.VISIBLE);
					break;
					
				case REGISTER_NETWORK_FAILURE:
					break;
				}
			}
			
			super.handleMessage(msg);
		};
	};

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		initInputWaitingScreen();
		initInputFailureScreen();
		
		initInstance();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.btn_register:
			String firstname = mEditFirstname.getText().toString();
			String lastname = mEditLastname.getText().toString();
			String mobile = mEditMobile.getText().toString();
			String email = mEditEmail.getText().toString();
			String password = mEditPassword.getText().toString();
			
			// if all of the fields are filled
			if (!TextUtils.isEmpty(firstname) && !TextUtils.isEmpty(lastname) && !TextUtils.isEmpty(mobile)
					&& !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
				doRegister(firstname, lastname, mobile, email, password);
			} else {
				// display register input failure screen
				sUIHandler.sendEmptyMessage(REGISTER_INPUT_FAILURE);
			}
				
			// hide soft ime keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mLayoutRegisterInputWaiting.getWindowToken(), 0);
			break;
			
		case R.id.btn_ok:
			sUIHandler.sendEmptyMessage(REGISTER_INPUT_WAIT);
			break;
		}
	}
	
	/**
	 * Initiate input waiting screen to register
	 */
	private void initInputWaitingScreen() {
		// Get LinearLayouts according to sub-screen
		mLayoutRegisterInputWaiting = (LinearLayout) findViewById(R.id.register_input_wait);

		// Get views of register input waiting
		mEditFirstname = (EditText) mLayoutRegisterInputWaiting.findViewById(R.id.edit_register_firstname);
		mEditLastname = (EditText) mLayoutRegisterInputWaiting.findViewById(R.id.edit_register_lastname);
		mEditMobile = (EditText) mLayoutRegisterInputWaiting.findViewById(R.id.edit_register_email);
		mEditPassword = (EditText) mLayoutRegisterInputWaiting.findViewById(R.id.edit_register_mobile);
		mEditEmail = (EditText) mLayoutRegisterInputWaiting.findViewById(R.id.edit_register_passwd);

		mBtnRegister = (Button) mLayoutRegisterInputWaiting.findViewById(R.id.btn_register);
		mBtnRegister.setOnClickListener(this);
	}
	
	/**
	 * Initiate input failure screen
	 */
	private void initInputFailureScreen() {
		mLayoutRegisterInputFailure = (LinearLayout) findViewById(R.id.register_input_failure);
		
		// Get view of register input failure
		mBtnOK = (Button) mLayoutRegisterInputFailure.findViewById(R.id.btn_ok);
		mBtnOK.setOnClickListener(this);
	}
	
	/**
	 * Initiate itself state
	 */
	private void initInstance() {
		// Save the activity
		sMe = this;

		// Display the login input waiting screen first.
		sUIHandler.sendEmptyMessage(REGISTER_INPUT_WAIT);
	}
	
	/**
	 * Register new account
	 * 
	 * @param firstname
	 *            user first name string
	 * @param lastname
	 *            user last name string
	 * @param mobile
	 *            user mobile number string
	 * @param email
	 *            user email string
	 * @param password
	 *            user password string
	 * @return register result
	 */
	private void doRegister(String firstname, String lastname,
			String mobile, String email, String password) {
		
		if (DEBUG) Log.i(TAG, "doRegister(): firstname=" + firstname + ", lastname=" + lastname
				+ ", mobile=" + mobile + ", email=" + email + ", password=" + password);
		
		StringBuilder sbAPI = new StringBuilder();
		sbAPI.append(Feedback.HTTP_URL).append("?action=Register");
		sbAPI.append("&first_name=").append(firstname);
		sbAPI.append("&last_name=").append(lastname);
		sbAPI.append("&mobile=").append(mobile);
		sbAPI.append("&email=").append(email);
		sbAPI.append("&password=").append(password);
		
		// run service API using the url with firstname, lastname, mobile, email and password
		new RegisterTask().execute(sbAPI.toString());
	}
	
	/**
	 * AsyncTask to register with account information
	 * 
	 */
	private class RegisterTask extends LoadFeedData {

		@Override
		protected void onPreExecute() {
			Feedback.initProgressDialog(RegisterActivity.this);
			Feedback.showProgress();
		}

		@Override
		protected void onPostExecute(String response) {
			
			if (DEBUG) Log.i(TAG, "result = " + response);
			
			if (response != null && Feedback.isSuccess()) {
				Toast.makeText(RegisterActivity.this, getString(R.string.register_success), Toast.LENGTH_LONG).show();
				LoginActivity.doLogin(RegisterActivity.this, 
						mEditEmail.getText().toString(), 
						mEditPassword.getText().toString());
			} else {
				Feedback.dismissProgress();
				Utils.informMessage(RegisterActivity.this, "Could not get results from server.");
			}
		}
		
	}
	
}
