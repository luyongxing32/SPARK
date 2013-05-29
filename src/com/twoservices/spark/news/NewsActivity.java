/**
 * @author Ry
 * @Date 2012.11.24
 * @FileName NewsActivity.java
 *
 */

package com.twoservices.spark.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.twoservices.spark.Action;
import com.twoservices.spark.MainActivity;
import com.twoservices.spark.R;
import com.twoservices.spark.Utils;
import com.twoservices.spark.abstarct.FilteredListActivity;
import com.twoservices.spark.link.NewsResult;
import com.twoservices.spark.list.SectionedItem;
import com.twoservices.spark.list.SectionedItemView;

import java.util.ArrayList;

/**
 * News activity of <B>"Engage US"</B>
 * 
 */
public class NewsActivity extends FilteredListActivity implements View.OnClickListener {

	private static final String TAG = NewsActivity.class.getSimpleName();
	private static final boolean DEBUG = Utils.DEBUG;
	

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_sponser);
		
		setupTitleAndButtons();
		super.setTextOnMySubjectButton(getString(R.string.home));
		super.changeBackgroundOnMySubjectButton(R.drawable.home_button_back);

		super.setupSearchEditText();
		super.setupListAndSponsorView();
		setAction(Action.NEWS);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loadList(NewsResult[].class);
		showList("");
		loadSponsors(Action.NEWS);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_mysubject:
			startActivity(new Intent(NewsActivity.this, MainActivity.class));
			return;
		}
		
		super.onClick(v);
	}

	/**
	 * Show News list
	 * 
	 * @param filter
	 *            string to filter "title" field
	 */
	@Override
	protected void showList(String filter) {
		// Initiate the content of ListView
		mListView.removeAllViews();

		if (mAPIResult != null) {
			ArrayList<String> sectionList = new ArrayList<String>();
			String strSection;
			String strTitle;
			String strSearch = filter.toLowerCase();

			for (NewsResult result : (NewsResult[]) mAPIResult) {
				strTitle = result.title.toLowerCase();

				if (strTitle.indexOf(strSearch) != -1) {
					strSection = result.dateposted;//Utils.toMyDateFormat(result.dateposted);

					if (!sectionList.contains(strSection)) {
						sectionList.add(strSection);
					} else {
						strSection = null;
					}

					mListView.addView(new SectionedItemView(
							NewsActivity.this,
							new SectionedItem(
									result.news_id,
									strSection, 
									result.checked.equals("Y"),
									false,
									result.title,
									Utils.getPostTimeString(result.timeposted),
									result.checked.equals("Y") ? null : getString(R.string.unread)),
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
		
		Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
		intent.putExtra(Action.SELECTED_ID, item.id);
		
		startActivity(intent);
	}
	
}
