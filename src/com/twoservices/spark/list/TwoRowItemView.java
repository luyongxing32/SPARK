package com.twoservices.spark.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twoservices.spark.R;

public class TwoRowItemView extends LinearLayout {

	public TwoRowItemView(final Context context, final TwoRowItem item, final String method) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.two_row_listitem, null);
		layout.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		
		ImageView imgCheck = (ImageView) layout.findViewById(R.id.img_check);
        if (item.invited)
        	imgCheck.setImageResource(R.drawable.apply);
		else
			imgCheck.setImageResource(R.drawable.no_apply);
		
        TextView txtName = (TextView) layout.findViewById(R.id.text_item_name);
        txtName.setText(item.name);
        
        TextView txtTitle = (TextView) layout.findViewById(R.id.text_item_title);
        txtTitle.setText(item.title);
        
        TextView txtInvited = (TextView) layout.findViewById(R.id.text_item_invited);
        if (item.invited)
        	txtInvited.setVisibility(View.VISIBLE);
		else
			txtInvited.setVisibility(View.GONE);
		
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
