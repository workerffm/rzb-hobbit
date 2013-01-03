package com.arbis.mobile.test;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.arbis.mobile.test.db.ZielDAO;
import com.arbis.mobile.test.domain.Ziel;
import com.arbis.mobile.tools.DoubleLineListAdapter;

public class ZieleActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final List<Ziel> ziele = ZielDAO.populateList(new ArrayList<Ziel>());
		final DoubleLineListAdapter mAdapter = new DoubleLineListAdapter(this, ziele);
		final ListView listView = getListView();
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				showLocation(ziele.get(position));
			}
		});
	}

	protected void showLocation(final Ziel ziel) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + ziel.getAddress()));
		startActivity(intent);
	}

}
