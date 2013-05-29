/**
 * @author Ry
 * @Date 2012.12.13
 * @FileName FinishSurveyActivity.java
 *
 */

package com.twoservices.spark.agenda;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.twoservices.spark.Action;
import com.twoservices.spark.MainActivity;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.Activity2;
import com.twoservices.spark.link.Feedback;
import com.twoservices.spark.link.LoadFeedData;
import com.twoservices.spark.link.SurveyItem;
import com.twoservices.spark.link.SurveyResult;

/**
 * Finish Survey activity of <B>"Engage US"</B>
 * 
 */
public class FinishSurveyActivity extends Activity2 {
	
	private static final String TAG = FinishSurveyActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	
	private static final String LOAD_ACTION = "Finish_Survey";
	private static final String SUBMIT_ACTION = "Finish_Survey_Submit";
	
	private static final String RADIO_BESTWORST_TYPE = "BestWorst";
    private static final String RADIO_EXCELLENTPOOR_TYPE = "ExcellentPoor";
    private static final String RATING_TYPE = "rating";
	private static final String COMMENT_TYPE = "comment";
	
	private static final String ID_TYPE = "uu_session_id";
	
	// Session id of Agenda that will display
	private String mSessionId = "";
	
	// TextView that will display welcome string
	private TextView mTextWelcome;
	
	// TextView that will display help me string
	private TextView mTextHelpMe;
	
	// LinearLayout that will contain survey items
	private ViewGroup mSurveyList;
	
	// Button that will submit survey result
	private Button mBtnSubmit;
	
	// LayoutInflator to inflate view
	private LayoutInflater mInflater;
	
	// Array object for radio type survey
	private ArrayList<View> mSurveyViewList = new ArrayList<View>();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_survey);
		
		super.setupTitleAndButtons();
		super.setTextOnMySubjectButton(getString(R.string.home));
		super.changeBackgroundOnMySubjectButton(R.drawable.home_button_back);
		
		initViewsOnScreen();

		// Get session id from intent
		Intent intent = getIntent();
		mSessionId = intent.getStringExtra(Action.SELECTED_ID);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		new LoadSurveyTask().execute(Utils.getActionAPIString(LOAD_ACTION, ID_TYPE, mSessionId));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_mysubject:
			startActivity(new Intent(FinishSurveyActivity.this, MainActivity.class));
			return;
			
		case R.id.btn_submit:
			submitSurvey();
			return;
		}
		
		super.onClick(v);
	}
	
	/**
	 * Initialize views on screen
	 */
	private void initViewsOnScreen() {
		mInflater = LayoutInflater.from(this);
		
		mTextWelcome = (TextView) findViewById(R.id.text_welcome);
		mTextHelpMe = (TextView) findViewById(R.id.text_help_me);
		mSurveyList = (ViewGroup) findViewById(R.id.survey_list);
		mBtnSubmit = (Button) findViewById(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(this);
	}
	
	/**
	 * Add and show survey items
	 */
	private void showSurvey(SurveyResult result) {
		mSurveyList.removeAllViews();
		mSurveyViewList.clear();
		
		mTextWelcome.setText("Welcome " + result.name);
		
		mTextHelpMe.setText(Utils.getNormalBoldNormalString(
				getString(R.string.thank_you), 
				result.title, 
				getString(R.string.help_us)));
		
		for (SurveyItem item : result.questions) {
			if (RADIO_BESTWORST_TYPE.equalsIgnoreCase(item.type)
                    || RADIO_EXCELLENTPOOR_TYPE.equalsIgnoreCase(item.type)) {
				addRadioGroup(item);
			} else if (RATING_TYPE.equalsIgnoreCase(item.type)) {
				addRatingBar(item);
			} else if (COMMENT_TYPE.equalsIgnoreCase(item.type)) {
				addCommentEdit(item);
			}
		}
		
		// stop progress
		Feedback.dismissProgress();
	}
	
	/**
	 *
	 * @param survey SurveyItem
	 */
	private void addRadioGroup(SurveyItem survey) {
		TextView textView = new TextView(this);
		textView.setText(survey.title);
		textView.setPadding(0, 10, 0, 5);
		mSurveyList.addView(textView);
		
		// Set icon resource id
		View radioGroupView = mInflater.inflate(R.layout.radio_group_view, null);
		RadioGroup radioGroup = (RadioGroup) radioGroupView.findViewById(R.id.radioGroupQuality);

        // Get title strings
        String[] titles = null;
        if (survey.type.equals(RADIO_BESTWORST_TYPE)) {
            titles = getResources().getStringArray(R.array.quality_titles);
        } else if (survey.type.equals(RADIO_EXCELLENTPOOR_TYPE)) {
            titles = getResources().getStringArray(R.array.feeling_titles);
        }

        // Change title of RadioButton
        if (titles != null) {
            for (int i = 0; i < 5; i++) {
                int buttonResId = getResources().getIdentifier(
                        String.format("radio%d", i + 1), "id", getPackageName());
                RadioButton button = (RadioButton) radioGroup.findViewById(buttonResId);
                button.setText(titles[i]);
            }
        }

        int selectedRadioButtonId = getResources().getIdentifier(
                "radio" + survey.response_int, "id", getPackageName());

        radioGroup.check(selectedRadioButtonId);
		radioGroup.setTag(survey.eval_question_id);
		mSurveyList.addView(radioGroupView);
		
		mSurveyViewList.add(radioGroup);
	}
	
	/**
	 * 
	 * @param survey SurveyItem
	 */
	private void addRatingBar(SurveyItem survey) {
		TextView textView = new TextView(this);
		textView.setText(survey.title);
		textView.setPadding(0, 10, 0, 5);
		mSurveyList.addView(textView);
		
		RatingBar ratingBar = new RatingBar(this);
		ratingBar.setMax(5);
		ratingBar.setStepSize(1.0f);
		ratingBar.setRating(Integer.valueOf(survey.response_int));
		ratingBar.setTag(survey.eval_question_id);
		ratingBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		mSurveyList.addView(ratingBar);
		
		mSurveyViewList.add(ratingBar);
	}
	
	/**
	 * 
	 * @param survey SurveyItem
	 */
	private void addCommentEdit(SurveyItem survey) {
		TextView textView = new TextView(this);
		textView.setText(survey.title);
		textView.setPadding(0, 10, 0, 5);
		mSurveyList.addView(textView);
		
		EditText editView = new EditText(this);
		editView.setBackgroundResource(R.drawable.edittext_back);
		editView.setMinLines(2);
		editView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
		editView.setLineSpacing(1.0f, 1.1f);
		editView.setText(survey.response_text);
		editView.setTag(survey.eval_question_id);
		mSurveyList.addView(editView);
		
		mSurveyViewList.add(editView);
	}
	
	/** 
	 * Submit survey
	 */
	private void submitSurvey() {
		String apiStr = Utils.getActionAPIString(SUBMIT_ACTION, ID_TYPE, mSessionId);
		int index = 0;
		
		for (View v : mSurveyViewList) {
			String eval_id = (String) v.getTag();
			String resp_val = "";
			
			if (!TextUtils.isEmpty(eval_id)) {

				if (v instanceof RadioGroup) {
					switch (((RadioGroup) v).getCheckedRadioButtonId()) {
					case R.id.radio1:
						resp_val = "1";
						break;
					case R.id.radio2:
						resp_val = "2";
						break;
					case R.id.radio3:
						resp_val = "3";
						break;
					case R.id.radio4:
						resp_val = "4";
						break;
					case R.id.radio5:
						resp_val = "5";
						break;
					}
				} else if (v instanceof RatingBar) {
					int ret_val = (int) ((RatingBar) v).getRating();
					if (ret_val == 0) ret_val = -1;
					
					resp_val = String.valueOf(ret_val);
				} else if (v instanceof EditText) {
					resp_val = Uri.encode(((EditText) v).getText().toString());
				}

				index++;
				apiStr += String.format("&eval_question_id%d=%s&response_val%d=%s", index, eval_id, index, resp_val);
			}
		}

		if (DEBUG) Log.i(TAG, "api string ===>" + apiStr);
		
		new SubmitSurveyTask().execute(apiStr);
	}
	
	/**
	 * AsyncTask to load the Survey content
	 *
	 */
	private class LoadSurveyTask extends LoadFeedData {
		
		@Override
		protected void onPreExecute() {
			// Hide all views
			hideViewsOnScreen();
		}

		@Override
		protected void onPostExecute(String response) {
			if (DEBUG) Log.i(TAG, "API response = " + response);

			if (Feedback.isSuccess()) {
				showSurvey(Feedback.parseData(response, SurveyResult.class)); 
				
				// Show all view that include new state
				showViewsOnScreen();
			} else {
				Utils.informMessage(FinishSurveyActivity.this, "Could not get results from server.");
				Utils.logout(FinishSurveyActivity.this);
			}
		}
	}
	
	/**
	 * AsyncTask to load the Survey content
	 *
	 */
	private class SubmitSurveyTask extends LoadFeedData {
		
		@Override
		protected void onPreExecute() {
			Feedback.initProgressDialog(FinishSurveyActivity.this);
			Feedback.showProgress();
		}

		@Override
		protected void onPostExecute(String response) {
			if (Feedback.isSuccess()) {
				if (DEBUG) Log.i(TAG, "submit success!");
			} else {
				Utils.informMessage(FinishSurveyActivity.this, "Could not get results from server.");
			}
			
			Feedback.dismissProgress();
			onBackPressed();
		}
	}
}
