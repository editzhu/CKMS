package com.example.ckms.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class K {
	/**
	 * 从索引读取符合条件的知识数据。
	 */
	private Knowledge knowledge = null;
	private static List<Knowledge> list = new ArrayList<Knowledge>();

	public void addKnowledge(Knowledge knowledge) {
		list.add(knowledge);
	}

	public void clear() {
		list.clear();
	}

	public List<Knowledge> getList() {
		return list;
	}

	public List<Knowledge> loadKnowledge2() {

		sendRequestWithHttpClient();
		Log.d("my", list.size() + "");
		return list;
	}

	private void sendRequestWithHttpClient() {

		try {
			HttpClient httpClient = new DefaultHttpClient();
			// 指定访问的服务器地址是电脑本机
			HttpGet httpGet = new HttpGet(GlobalValue.serverIp
					+ "/liuyanban/Knowledge?type=1&value=java");
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 请求和响应都成功了
				HttpEntity entity = httpResponse.getEntity();
				String response = EntityUtils.toString(entity, "utf-8");
				parseJSONWithJSONObject(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getContent(final String id) {
		String response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			// 指定访问的服务器地址是电脑本机
			HttpGet httpGet = new HttpGet(GlobalValue.serverIp
					+ "/liuyanban/Knowledge?type=2&id=" + id);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 请求和响应都成功了
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity, "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private void parseJSONWithJSONObject(String jsonData) {
		try {
			JSONArray jsonArray = new JSONArray(jsonData);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String title = jsonObject.getString("title");
				knowledge = new Knowledge();
				knowledge.setId(id);
				knowledge.setTitle(title);
				list.add(knowledge);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
