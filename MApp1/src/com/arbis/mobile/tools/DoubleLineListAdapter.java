package com.arbis.mobile.tools;

import java.util.List;

import android.content.Context;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.arbis.mobile.test.R;

/**
 * Add 2 line list adapter for Name/Address
 */
public class DoubleLineListAdapter extends ArrayAdapter<DoubleLineItem> {

	private final Context context;
	private final List<? extends DoubleLineItem> values;

	public DoubleLineListAdapter(Context context, List<? extends DoubleLineItem> values) {
		super(context, R.layout.double_line_item_row, (List<DoubleLineItem>)values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.double_line_item_row, parent, false);
		final TextView textView1 = (TextView) rowView.findViewById(R.id.textView1);
		final TextView textView2 = (TextView) rowView.findViewById(R.id.textView2);
		final DoubleLineItem z = values.get(position);
		textView1.setText(z.getLine1());
		textView2.setText(z.getLine2());
		return rowView;
	}
}
