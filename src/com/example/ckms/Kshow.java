package com.example.ckms;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.ckms.model.GlobalValue;

public class Kshow extends Activity {

	private TextView titleText;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.k_show);

		titleText = (TextView) findViewById(R.id.title);
		webView = (WebView) findViewById(R.id.web_view);
		webView.getSettings().setJavaScriptEnabled(false);
		// webView.getSettings().setUseWideViewPort(true);
		// webView.getSettings().setLoadWithOverviewMode(true);
		// webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// webView.getSettings().setDefaultFontSize(20);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); // 根据传入的参数再去加载新的网页
				return true; // 表示当前WebView可以处理打开新网页的请求，不用借助系统浏览器
			}
		});

		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		String title = intent.getStringExtra("title");
		// String content = intent.getStringExtra("content");
		titleText.setText(title);
		// String url = GlobalValue.serverIp
		// + "/liuyanban/Knowledge?type=2&value=" + id;
		String url = "http://10.0.2.16:8081/utry_ckms/episteme_view.do?kid="
				+ id + "&type=dq&showType=client";
		Log.d("my", url);

		/* 载入cookie 20151015 */
		String url4load = url;
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		Cookie sessionCookie = GlobalValue.appCookie;
		if (sessionCookie != null) {
			String cookieString = sessionCookie.getName() + "="
					+ sessionCookie.getValue() + "; domain="
					+ sessionCookie.getDomain();
			cookieManager.setCookie(url4load, cookieString);
			CookieSyncManager.getInstance().sync();
			webView.loadUrl(url);
		} else {
			Intent intentToLogin = new Intent(Kshow.this, Login.class);
			startActivity(intentToLogin);
			finish();
		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
