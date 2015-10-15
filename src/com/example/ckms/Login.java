package com.example.ckms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import com.example.ckms.model.GlobalValue;

public class Login extends Activity {
	private TextView tv_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		tv_result = (TextView) findViewById(R.id.sound_help);// 服务器端返回数据显示的控件

		// 网上找来的防止execute失败的代码
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

		// 采用post方式
		Log.e("login", "begin post");
		// 1.创建 HttpClient 的实例
		DefaultHttpClient client = new DefaultHttpClient();
		HttpContext context = new BasicHttpContext();
		CookieStore cookieStore = new BasicCookieStore();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		// 重新方法,保证post提交后有跳转页面的先返回302
		client.setRedirectHandler(new RedirectHandler() {
			@Override
			public boolean isRedirectRequested(HttpResponse response,
					HttpContext context) {
				Log.d("isRedirectRequested.context:", context.toString());
				return false;
			}

			@Override
			public URI getLocationURI(HttpResponse response, HttpContext context)
					throws ProtocolException {
				return null;
			}
		});
		// 2. 创建某种连接方法的实例，在这里是HttpPost。在 HttpPost 的构造函数中传入待连接的地址
		HttpPost httpPost = new HttpPost(
				"http://10.0.2.16:8081/utry_ckms/login_login.do");
		// 封装传递参数的集合
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("login_id", "admin2"));
		parameters.add(new BasicNameValuePair("password", "123123"));
		// 创建传递参数封装 实体对象
		try {
			// 把实体对象存入到httpPost对象中
			httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
			// 3. 调用第一步中创建好的实例的 execute 方法来执行第二步中创建好的 method 实例

			HttpResponse response = client.execute(httpPost, context); // 执行
			Log.e("login", response.getStatusLine().getStatusCode() + "");
			if (response.getStatusLine().getStatusCode() == 200) {// 判断状态码
				InputStream is = response.getEntity().getContent();// 获取内容
				final String result = streamToStr(is); // 通过工具类转换文本
				Login.this.runOnUiThread(new Runnable() { // 通过runOnUiThread方法处理
							@Override
							public void run() {
								// 设置控件的内容(此内容是从服务器端获取的)
								tv_result.setText(result);
							}
						});
			}
			Header locationHeader = httpPost.getFirstHeader("location");
			String location = null;
			if (locationHeader != null) {
				location = locationHeader.getValue();
				System.out.println("The page was redirected to:" + location);
			} else {
				System.err.println("Location field value is null.");
			}

			// 处理cookie
			List<Cookie> cookies = cookieStore.getCookies();
			if (!cookies.isEmpty()) {
				for (int i = cookies.size(); i > 0; i--) {
					Cookie cookie = (Cookie) cookies.get(i - 1);
					if (cookie.getName().equalsIgnoreCase("jsessionid")) {
						GlobalValue.appCookie = cookie;
						Log.e("cookie", cookie.toString());
					}
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	public static String streamToStr(InputStream is) {
		try {
			// 定义字节数组输出流对象
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			// 定义读取的长度
			int len = 0;
			// 定义读取的缓冲区
			byte buffer[] = new byte[1024];
			// 按照定义的缓冲区进行循环读取，直到读取完毕为止
			while ((len = is.read(buffer)) != -1) {
				// 根据读取的长度写入到字节数组输出流对象中
				os.write(buffer, 0, len);
			}
			// 关闭流
			is.close();
			os.close();
			// 把读取的字节数组输出流对象转换成字节数组
			byte data[] = os.toByteArray();
			// 按照指定的编码进行转换成字符串(此编码要与服务端的编码一致就不会出现乱码问题了，android默认的编码为UTF-8)
			return new String(data, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
