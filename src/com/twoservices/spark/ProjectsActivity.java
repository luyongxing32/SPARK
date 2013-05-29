/**
 * @author Ry
 * @Date 2012.11.16
 * @FileName ProjectsActivity.java
 *
 */

package com.twoservices.spark;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoginResult;

import java.util.ArrayList;

/**
 * Project selection Activity of <B>"Engage US"</B>
 * 
 */
public class ProjectsActivity extends Activity {

	private static final String TAG = ProjectsActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;

	public static final String RESPONSE_INTENT_EXTRA = "LOGIN_RESPONSE";

	// define member variables
	// Spinner that will hold the available projects
	private Spinner mSpinProject;

	// Button to login to "Engage US"
	private Button mBtnLogin;

	// List that will host our items
	private ArrayList<String> mProjectList;
	
	// Login result by service API
	private LoginResult[] mLoginResult;
	

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_projects);

		addItemsOnSpinner();
		addListenerOnSpinnerItemSelection();
		addListenerOnButton();
	}

	/**
	 *  Add items into spinner dynamically
	 */
	private void addItemsOnSpinner() {
		mSpinProject = (Spinner) findViewById(R.id.spin_project);

		String response = getIntent().getStringExtra(RESPONSE_INTENT_EXTRA);
		mLoginResult = Feedback.parseDataArray(response, LoginResult[].class);
		
		mProjectList = new ArrayList<String>();
		for (LoginResult result : mLoginResult) {
			mProjectList.add(result.project_name);
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				R.layout.spinner_text_item, mProjectList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinProject.setAdapter(dataAdapter);
	}

	/**
	 *  Add listener to Spinner
	 */
	private void addListenerOnSpinnerItemSelection() {
		mSpinProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				if (DEBUG)
					Log.i(TAG, "onItemSelected(): selected <" + mProjectList.get(pos) + ">");

				// Set account information that will use forward
				AccountInfo.init(mLoginResult[pos]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}

		});
	}

	/**
	 *  Add listener to Button
	 */
	public void addListenerOnButton() {
		mBtnLogin = (Button) findViewById(R.id.btn_login);

		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AccountInfo.sProjectName = mSpinProject.getSelectedItem().toString();
				
				startActivity(new Intent(ProjectsActivity.this, MainActivity.class));
			}
		});
	}

}
