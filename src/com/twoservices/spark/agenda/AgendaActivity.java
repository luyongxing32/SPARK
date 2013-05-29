/**
 * @author Ry
 * @Date 2012.11.18
 * @FileName AgendaActivity.java
 *
 */

package com.twoservices.spark.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.FilteredListActivity;
import com.twoservices.spark.link.AgendaResult;
import com.twoservices.spark.list.SectionedItem;
import com.twoservices.spark.list.SectionedItemView;

import java.util.ArrayList;

/**
 * Agenda activity of <B>"Engage US"</B>
 * 
 */
public class AgendaActivity extends FilteredListActivity {

	private static final String TAG = AgendaActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_sponser);
		
		super.getAction();
		
		setupTitleAndButtons();
		super.setupSearchEditText();
		super.setupListAndSponsorView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loadList(AgendaResult[].class);
		loadSponsors(Action.AGENDA);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.btn_mysubject:
			String action = mIsMySubject ? Action.AGENDA : Action.MY_AGENDA;
				
			Intent intent = new Intent();
			intent.setClass(AgendaActivity.this, AgendaActivity.class);
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
			super.setTextOnMySubjectButton(getString(R.string.agenda));
			super.changeBackgroundOnMySubjectButton(R.drawable.subject_back);
		} else {
			super.setTextOnMySubjectButton(getString(R.string.myagenda_tag));
			super.changeBackgroundOnMySubjectButton(R.drawable.my_subject_back);
		}
	}

	/**
	 * Show Agenda list
	 * 
	 * @param filter
	 *            string to filter "title" field
	 */
	@Override
	protected void showList(String filter) {
		// Initiate the content of ListView
		mListView.removeAllViews();

		// Filter results with filter string
		if (mAPIResult != null) {
			ArrayList<String> sectionList = new ArrayList<String>();
			String strSection;
			String strTitle;
			String strSearch = filter.toLowerCase();

			for (AgendaResult result : (AgendaResult[]) mAPIResult) {
				strTitle = result.title.toLowerCase();

				if (strTitle.indexOf(strSearch) != -1) {
					strSection = result.start_date;//Utils.toMyDateFormat(result.start_date);

					if (!sectionList.contains(strSection)) {
						sectionList.add(strSection);
					} else {
						strSection = null;
					}
					
					mListView.addView(new SectionedItemView(
							AgendaActivity.this, 
							new SectionedItem(
									result.session_id,
									strSection, 
									result.checked.equals("Y"),
									false,
									result.title,
									Utils.getTimeString(result.start_time),
									null),
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
		SectionedItem item = (SectionedItem) view.getTag();
		
		if (DEBUG) Log.i(TAG, "Selected item = " + item.title);
		
		Intent intent = new Intent(AgendaActivity.this, AgendaDetailActivity.class);
		intent.putExtra(Action.SELECTED_ID, item.id);
		
		startActivity(intent);
	}
	
}
