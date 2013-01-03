package com.arbis.mobile.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final LinearLayout v = (LinearLayout)getLayoutInflater().inflate(R.layout.main,null);
		setContentView(v);
		
		Button b = (Button) findViewById(R.id.btnFahrziele);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Main.this.startActivity(new Intent("Ziele"));
			}
		});
	}

}
