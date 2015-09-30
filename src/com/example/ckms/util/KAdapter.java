package com.example.ckms.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ckms.R;
import com.example.ckms.model.Knowledge;

public class KAdapter extends ArrayAdapter<Knowledge> {
	private int resourceId;

	public KAdapter(Context context, int textViewResourceId,
			List<Knowledge> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Knowledge knowledge = getItem(position); // 获取当前项的Knowledge实例
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView textView1 = (TextView) view.findViewById(R.id.textView1);
		TextView textView2 = (TextView) view.findViewById(R.id.textView2);
		textView1.setText(knowledge.getId());
		textView2.setText(knowledge.getTitle());
		return view;
	}
}
