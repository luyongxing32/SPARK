/**
 * @author Ry
 * @Date 2012.12.12
 * @FileName CountedEntryItemView.java
 *
 */

package com.twoservices.spark.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twoservices.spark.R;

public class CountedItemView extends LinearLayout {

	public CountedItemView(final Context context, final CountedItem item, final String method) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.sectioned_count_listitem, null);
		layout.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		
		View sectionBar = layout.findViewById(R.id.section_bar);
		TextView txtSectionTitle = (TextView) sectionBar.findViewById(R.id.text_section_title);
		TextView txtSectionCount = (TextView) sectionBar.findViewById(R.id.text_section_count);
		if (item.section_title != null) {
			sectionBar.setVisibility(View.VISIBLE);
			txtSectionTitle.setText(item.section_title);
			txtSectionCount.setText(String.valueOf(item.section_count));
		} else {
			txtSectionTitle.setVisibility(View.GONE);
		}
		
        ImageView imgCheck = (ImageView) layout.findViewById(R.id.img_check);
        if (item.checked)
        	imgCheck.setImageResource(R.drawable.apply);
		else
			imgCheck.setImageResource(R.drawable.no_apply);
        
        TextView txtName = (TextView) layout.findViewById(R.id.text_name);
        txtName.setText(item.title);
        
        TextView txtExtra = (TextView) layout.findViewById(R.id.text_extra);
        txtExtra.setText(item.extra1);
		
        View clickItem = layout.findViewById(R.id.listitem_view);
        clickItem.setTag(item);
        clickItem.setFocusable(true);
        clickItem.setOnClickListener(new View.OnClickListener() {
        	@Override
        	public void onClick(View view) { runMethod(context, method, view); }
        });

		addView(layout);
	}

	private void runMethod(Context context, String method, View view) {
		try {
			context.getClass().getMethod(method, View.class).invoke(context, view);
		} catch (Throwable exc) { exc.printStackTrace(); }
	}
	
}
