/**
 * @author Ry
 * @Date 2012.11.24
 * @FileName ExhibitorsActivity.java
 *
 */

package com.twoservices.spark.exhibitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.twoservices.spark.Action;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.FilteredListActivity;
import com.twoservices.spark.link.ExhibitorResult;
import com.twoservices.spark.list.SectionedItem;
import com.twoservices.spark.list.SectionedItemView;

/**
 * Exhibitors activity of <B>"Engage US"</B>
 * 
 */
public class ExhibitorsActivity extends FilteredListActivity implements View.OnClickListener {

	private static final String TAG = ExhibitorsActivity.class.getSimpleName();
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
		
		loadList(ExhibitorResult[].class);
		showList("");
		loadSponsors(Action.EXHIBITOR);
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id) {
		case R.id.btn_mysubject:
			String action = mIsMySubject ? Action.EXHIBITOR : Action.MY_EXHIBITOR;
				
			Intent intent = new Intent();
			intent.setClass(ExhibitorsActivity.this, ExhibitorsActivity.class);
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
			super.setTextOnMySubjectButton(getString(R.string.exhibitors));
			super.changeBackgroundOnMySubjectButton(R.drawable.subject_back);
		} else {
			super.setTextOnMySubjectButton(getString(R.string.myexhibitors_tag));
			super.changeBackgroundOnMySubjectButton(R.drawable.my_subject_back);
		}
	}

	/**
	 * Show Exhibitor list
	 * 
	 * @param filter
	 *            string to filter "company" field
	 */
	@Override
	protected void showList(String filter) {
		// Initiate the content of ListView
		mListView.removeAllViews();

		// Filter results with filter string
		if (mAPIResult != null) {
			String strTitle;
			String strSearch = filter.toLowerCase();

			for (ExhibitorResult result : (ExhibitorResult[]) mAPIResult) {
				strTitle = result.company.toLowerCase();

				if (strTitle.indexOf(strSearch) != -1) {
					mListView.addView(new SectionedItemView(
							ExhibitorsActivity.this, 
							new SectionedItem(
									result.exhibitor_id,
									null, 
									result.checked.equals("Y"), 
									result.preferred.equals("1"),
									result.company,
									Utils.getBoothString(result.booth),
									null), 
							"gotoDetailView"));
				}
			}
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
		
		Intent intent = new Intent(ExhibitorsActivity.this, ExhibitorsDetailActivity.class);
		intent.putExtra(Action.SELECTED_ID, item.id);
		
		startActivity(intent);
	}
	
}
