/**
 * @author Ry
 * @Date 2012.11.23
 * @FileName EntryItem.java
 *
 */

package com.twoservices.spark.list;

import android.text.SpannableString;


public class SectionedItem {

	public final String id;
	public final String section_title;
	public final boolean checked;
	public final boolean preferred;
	public final String title;
	public final SpannableString extra1;
	public final String extra2;

	public SectionedItem(String id, String section_title, boolean checked, 
			boolean preferred, String title, SpannableString extra1, String extra2) {
		this.id = id;
		this.section_title = section_title;
		this.checked = checked;
		this.preferred = preferred;
		this.title = title;
		this.extra1 = extra1;
		this.extra2 = extra2;
	}

}
