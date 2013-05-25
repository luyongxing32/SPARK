/**
 * @author Ry
 * @Date 2012.11.09
 * @FileName LoginActivity.java
 *
 */

package com.twoservices.spark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.link.LoginResult;

/**
 * Activity to login to <B>"Engage US"</B>
 * 
 */
public class LoginActivity extends Activity implements View.OnClickListener {

	private static final String TAG = LoginActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	
	private static final String PASSWORD_RESET_ADDR = "http://spark.vulcano-events.com/password/";

	// define the UI state
	private static final int LOGIN_INPUT_WAIT = 0;
	private static final int LOGIN_INPUT_FAILURE = 1;
	private static final int LOGIN_REQUEST_OTHER_ACTION = 2;

	// define member variables
	// sub-screens of login
	private LinearLayout mLayoutLoginInput;
	private LinearLayout mLayoutLoginFailure;
	private LinearLayout mLayoutLoginRequest;

	// login input waiting screen
	private EditText mEditEmail;
	private EditText mEditPassword;
	private TextView mTextPasswdReset;
	private Button mBtnLogin;

	// login input failure screen
	private Button mBtnOK;

	// login other request screen
	private Button mBtnTryAgain;
	private Button mBtnRegister;
	
	private static LoginActivity sMe = null;

	/**
	 * mUIHandler handle the user interface according to login state.
	 */
	private static Handler sUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (sMe != null) {
				switch (msg.what) {
				case LOGIN_INPUT_WAIT:
					sMe.mLayoutLoginInput.setVisibility(View.VISIBLE);
					sMe.mLayoutLoginFailure.setVisibility(View.GONE);
					sMe.mLayoutLoginRequest.setVisibility(View.GONE);
					break;

				case LOGIN_INPUT_FAILURE:
					sMe.mLayoutLoginInput.setVisibility(View.GONE);
					sMe.mLayoutLoginFailure.setVisibility(View.VISIBLE);
					sMe.mLayoutLoginRequest.setVisibility(View.GONE);
					break;

				case LOGIN_REQUEST_OTHER_ACTION:
					sMe.mLayoutLoginInput.setVisibility(View.GONE);
					sMe.mLayoutLoginFailure.setVisibility(View.GONE);
					sMe.mLayoutLoginRequest.setVisibility(View.VISIBLE);
					break;
				}
			}

			super.handleMessage(msg);
		}
	};

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initInputWaitingScreen();
		initInputFailureScreen();
		initOtherRequestScreen();

		initInstance();
	}

	/** Called when a view has been clicked. */
	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.btn_login:
			final String email = mEditEmail.getText().toString();
			final String password = mEditPassword.getText().toString();

			// if both of the fields are filled
			if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
				doLogin(LoginActivity.this, email, password);
			} else {
				// display login input failure screen
				sUIHandler.sendEmptyMessage(LOGIN_INPUT_FAILURE);
			}

			// hide soft ime keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mLayoutLoginInput.getWindowToken(), 0);
			break;

		case R.id.btn_ok:
		case R.id.btn_tryagain:
			// return login input waiting screen
			sUIHandler.sendEmptyMessage(LOGIN_INPUT_WAIT);
			break;

		case R.id.btn_register:
			// goto register activity
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		}
	}
	
	/**
	 * Initiate input waiting screen to login
	 */
	private void initInputWaitingScreen() {
		mLayoutLoginInput = (LinearLayout) findViewById(R.id.login_input_wait);
		
		mEditEmail = (EditText) mLayoutLoginInput.findViewById(R.id.edit_login_email);
		mEditPassword = (EditText) mLayoutLoginInput.findViewById(R.id.edit_login_passwd);
		
		if (DEBUG) {
			mEditEmail.setText("erick@higherstandard.com");
			mEditPassword.setText("test");
		}
		
		mTextPasswdReset = (TextView) mLayoutLoginInput.findViewById(R.id.text_passwd_reset);
		mTextPasswdReset.setText(Utils.getLinkedText(PASSWORD_RESET_ADDR, 
				getString(R.string.login_password_reset), getString(R.string.login_here), "."));
		mTextPasswdReset.setMovementMethod(LinkMovementMethod.getInstance());
		
		mBtnLogin = (Button) mLayoutLoginInput.findViewById(R.id.btn_login);
		mBtnLogin.setOnClickListener(this);
	}
	
	/**
	 * Initiate input failure screen
	 */
	private void initInputFailureScreen() {
		mLayoutLoginFailure = (LinearLayout) findViewById(R.id.login_input_failure);
		
		mBtnOK = (Button) mLayoutLoginFailure.findViewById(R.id.btn_ok);
		mBtnOK.setOnClickListener(this);
	}
	
	/**
	 * Initiate other request screen
	 */
	private void initOtherRequestScreen() {
		mLayoutLoginRequest = (LinearLayout) findViewById(R.id.login_request);
		
		mBtnTryAgain = (Button) mLayoutLoginRequest.findViewById(R.id.btn_tryagain);
		mBtnTryAgain.setOnClickListener(this);
		
		mBtnRegister = (Button) mLayoutLoginRequest.findViewById(R.id.btn_register);
		mBtnRegister.setOnClickListener(this);
	}
	
	/**
	 * Initiate itself state
	 */
	private void initInstance() {
		// Save the activity
		sMe = this;

		// Display the login input waiting screen first.
		sUIHandler.sendEmptyMessage(LOGIN_INPUT_WAIT);
	}

	/**
	 * Run login process
	 * 
	 * @param email
	 *            user email string
	 * @param password
	 *            user password string
	 * @return result of login
	 */
	public static void doLogin(Context context, String email, String password) {
		// store my email string
		AccountInfo.sMyEmail = email;
		
		// run service API using the url with email and password
		new LoginTask(context).execute(Feedback.HTTP_URL
				+ "?action=Login&email=" + email + "&password=" + password);
	}
	
	/**
	 * AsyncTask to login
	 *
	 */
	private static class LoginTask extends LoadFeedData {
		private Context mContext;
		
		public LoginTask(Context context) {
			mContext = context;
		}
		
		@Override
		protected void onPreExecute() {
			Feedback.initProgressDialog(mContext);
			Feedback.showProgress();
		}

		@Override
		protected void onPostExecute(String response) {
			if (DEBUG) Log.i(TAG, "result = " + response);
			
			// send my email and password to engage service, then check the login result.
			if ((response != null) && Feedback.isSuccess()) {
				if (Feedback.sServiceResponse.total_results > 1) {
					// goto project activity
					Intent intent = new Intent(mContext, ProjectsActivity.class);
					intent.putExtra(ProjectsActivity.RESPONSE_INTENT_EXTRA, response);
					mContext.startActivity(intent);
				} else {
					// set account information that will use forward
					AccountInfo.init(Feedback.parseData(response, LoginResult.class));
					
					// goto main activity directly
					mContext.startActivity(new Intent(mContext, MainActivity.class));
				}
			} else {
				// if user account was incorrect or not exist, request "Try Again" or "Register"
				sUIHandler.sendEmptyMessage(LOGIN_REQUEST_OTHER_ACTION);
			}
			
			// stop progress
			Feedback.dismissProgress();
		}
	}
	
}
