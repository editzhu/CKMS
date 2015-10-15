package com.example.ckms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom);
		BottomLayout bottomLayout = (BottomLayout) findViewById(R.id.bottom_layout);

		if (null == bottomLayout)
			Log.d("BaseActivity", "error");
	}

	// public void setContentLayout(int resId) {
	//
	// LayoutInflater inflater = (LayoutInflater)
	// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View v = inflater.inflate(resId, null);
	// // bottomLayout.addView(v);
	// }
}
