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
		tv_result = (TextView) findViewById(R.id.sound_help);// �������˷���������ʾ�Ŀؼ�

		// ���������ķ�ֹexecuteʧ�ܵĴ���
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

		// ����post��ʽ
		Log.e("login", "begin post");
		// 1.���� HttpClient ��ʵ��
		DefaultHttpClient client = new DefaultHttpClient();
		HttpContext context = new BasicHttpContext();
		CookieStore cookieStore = new BasicCookieStore();
		context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		// ���·���,��֤post�ύ������תҳ����ȷ���302
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
		// 2. ����ĳ�����ӷ�����ʵ������������HttpPost���� HttpPost �Ĺ��캯���д�������ӵĵ�ַ
		HttpPost httpPost = new HttpPost(
				"http://10.0.2.16:8081/utry_ckms/login_login.do");
		// ��װ���ݲ����ļ���
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("login_id", "admin2"));
		parameters.add(new BasicNameValuePair("password", "123123"));
		// �������ݲ�����װ ʵ�����
		try {
			// ��ʵ�������뵽httpPost������
			httpPost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
			// 3. ���õ�һ���д����õ�ʵ���� execute ������ִ�еڶ����д����õ� method ʵ��

			HttpResponse response = client.execute(httpPost, context); // ִ��
			Log.e("login", response.getStatusLine().getStatusCode() + "");
			if (response.getStatusLine().getStatusCode() == 200) {// �ж�״̬��
				InputStream is = response.getEntity().getContent();// ��ȡ����
				final String result = streamToStr(is); // ͨ��������ת���ı�
				Login.this.runOnUiThread(new Runnable() { // ͨ��runOnUiThread��������
							@Override
							public void run() {
								// ���ÿؼ�������(�������Ǵӷ������˻�ȡ��)
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

			// ����cookie
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
			// �����ֽ��������������
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			// �����ȡ�ĳ���
			int len = 0;
			// �����ȡ�Ļ�����
			byte buffer[] = new byte[1024];
			// ���ն���Ļ���������ѭ����ȡ��ֱ����ȡ���Ϊֹ
			while ((len = is.read(buffer)) != -1) {
				// ���ݶ�ȡ�ĳ���д�뵽�ֽ����������������
				os.write(buffer, 0, len);
			}
			// �ر���
			is.close();
			os.close();
			// �Ѷ�ȡ���ֽ��������������ת�����ֽ�����
			byte data[] = os.toByteArray();
			// ����ָ���ı������ת�����ַ���(�˱���Ҫ�����˵ı���һ�¾Ͳ���������������ˣ�androidĬ�ϵı���ΪUTF-8)
			return new String(data, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
