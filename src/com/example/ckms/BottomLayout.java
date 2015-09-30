package com.example.ckms;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BottomLayout extends LinearLayout {
	private OnLeftAndRightClickListener listener;// 监听点击事件

	// 设置监听器
	public void setOnLeftAndRightClickListener(
			OnLeftAndRightClickListener listener) {
		this.listener = listener;
	}

	public interface OnLeftAndRightClickListener {
		public void onLeftButtonClick();
	}

	public BottomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.bottom, this);

		Button bottom_button1 = (Button) findViewById(R.id.bottom_button1);
		Button bottom_button2 = (Button) findViewById(R.id.bottom_button2);
		Button bottom_button3 = (Button) findViewById(R.id.bottom_button3);
		bottom_button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), "You clicked Edit button",
						Toast.LENGTH_SHORT).show();
				Log.d("bottomlayout", "Click1");
				if (listener != null)
					listener.onLeftButtonClick();// 点击回调

			}
		});
	}
}
