package com.example.ckms.util;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.example.ckms.model.K;
import com.example.ckms.model.Knowledge;

public class Utility {

	/**
	 * 解析和处理服务器返回的知识列表
	 */
	public synchronized static boolean handleKnowledgeListResponse(K k,
			String jsonData) {
		if (TextUtils.isEmpty(jsonData)) {
			return false;
		}
		try {
			// Log.d("my", "jsonData:" + jsonData);
			JSONArray jsonArray = new JSONArray(jsonData);
			k.clear();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String title = jsonObject.getString("title");
				Knowledge knowledge = new Knowledge();
				knowledge.setId(id);
				knowledge.setTitle(title);
				k.addKnowledge(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 解析和处理服务器返回的知识内容
	 */
	public synchronized static boolean handleKnowledgeContentResponse(K k,
			String response, String value) {
		if (TextUtils.isEmpty(response)) {
			return false;
		}
		k.getList().get(1).setContent(response);
		return true;
	}
}
