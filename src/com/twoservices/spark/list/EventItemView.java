/**
 * @author Ry
 * @Date 2012.12.12
 * @FileName EventItemView.java
 *
 */

package com.twoservices.spark.list;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.twoservices.spark.R;
import com.twoservices.spark.Utils;

public class EventItemView extends RelativeLayout {
	
	public EventItemView(final Context context, final EventItem item, final String method) {
		super(context);

		// 2. Get inflater and inflate item view
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.listitem, null);
		layout.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		
		// 3. Get the image view and text views from the listitem.xml file
		ImageView imgType = (ImageView) layout.findViewById(R.id.img_icon);
		TextView txtName = (TextView) layout.findViewById(R.id.text_name);
		TextView txtExtra = (TextView) layout.findViewById(R.id.text_extra);

		// 4. Assign the icon from our EventInfo object above
		imgType.setBackgroundResource(item.icon_res_id);
		imgType.setContentDescription(item.name);

		// 5. Assign the event name from EventInfo object above
		txtName.setText(Utils.getMyString(item.name));

		// 6. Assign the event extra information from EventInfo object above
		txtExtra.setText(item.extra_info);
		if (!TextUtils.isEmpty(item.extra_info))
			txtExtra.setVisibility(View.VISIBLE);
		else
			txtExtra.setVisibility(View.GONE);
		
		// 7. Assign the click event to item view
		layout.setTag(item);
		layout.setFocusable(true);
		layout.setOnClickListener(new View.OnClickListener() {
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
