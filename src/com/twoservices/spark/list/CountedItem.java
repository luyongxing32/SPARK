package com.twoservices.spark.list;

import android.text.SpannableString;

public class CountedItem extends SectionedItem {

	public int section_count;
	
	public CountedItem(String id, String section_title, int section_count, boolean checked, 
			String title, SpannableString extra1, String extra2) {
		
		super(id, section_title, checked, false, title, extra1, extra2);
		this.section_count = section_count;
	}
}
