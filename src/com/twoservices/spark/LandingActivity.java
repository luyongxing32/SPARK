/**
 * @author Ry
 * @Date 2012.11.16
 * @FileName LandingActivity.java
 *
 */

package com.twoservices.spark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * First Activity of <B>"Engage US"</B>
 * 
 */
public class LandingActivity extends Activity {
	
	private static final String TAG = LandingActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	
	// define member variables
	// Button to sign in "Engage US"
	Button mBtnSignIn;
	

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_landing);
		
		// Get button and set onClickListener
		mBtnSignIn = (Button) findViewById(R.id.btn_signin);
		mBtnSignIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (DEBUG) Log.i(TAG, "Call onClick(), now begin LoginActivity!");
				
				startActivity(new Intent(LandingActivity.this, LoginActivity.class));
			}
		});
	}
	
}
