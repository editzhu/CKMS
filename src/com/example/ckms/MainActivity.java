package com.example.ckms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ckms.BottomLayout.OnLeftAndRightClickListener;
import com.example.ckms.model.GlobalValue;
import com.example.ckms.model.K;
import com.example.ckms.util.HttpCallbackListener;
import com.example.ckms.util.HttpUtil;
import com.example.ckms.util.KAdapter;
import com.example.ckms.util.Utility;

public class MainActivity extends Activity {
	private ListView listView;
	private ProgressDialog progressDialog;
	private KAdapter adapter;
	private Button button;
	private EditText editText;

	K k = new K();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new KAdapter(this, R.layout.k_listitem, k.getList());
		listView.setAdapter(adapter);

		// ����bottom�ĵ������,Ӧ�ÿ��Բ���д��δ���ķ���
		BottomLayout bottomLayout = (BottomLayout) findViewById(R.id.bottom_layout);
		bottomLayout
				.setOnLeftAndRightClickListener(new OnLeftAndRightClickListener() {

					@Override
					public void onLeftButtonClick() {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MainActivity.this,
								Login.class);
						startActivity(intent);
					}
				});

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index,
					long arg3) {
				String id = k.getList().get(index).getId();
				String title = k.getList().get(index).getTitle();
				Intent intent = new Intent(MainActivity.this, Kshow.class);
				intent.putExtra("title", title);
				intent.putExtra("id", id);
				startActivity(intent);
				finish();
			}
		});

		button = (Button) findViewById(R.id.button1);
		editText = (EditText) findViewById(R.id.editText1);
		// onClickListener ����ΪMainActivity���Ա����
		button.setOnClickListener(onClickListener);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO ������"�س�"��Ӧ����
				onClickListener.onClick(null);
				return true;
			}
		});

		// ����ҳ�������������ťһ��
		// onClickListener.onClick(null);

	}

	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			queryFromServer("1", editText.getText().toString());
			// ���button��,���������
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	};

	// ����solr�ͻ����Դ�httpclient�ڰ�׿�ϵĳ�ͻ,��ʱ�޷����
	// private void queryFromSolrServer(String key, K k) {
	// if ("".equals(key))
	// key = "*";
	// String url = GlobalValue.solrServerIp;
	// SolrServer server = new HttpSolrServer(url);
	// SolrQuery query = new SolrQuery();
	// query.setRows(6);
	// query.setQuery("title:" + key);
	// QueryResponse response = null;
	// try {
	// response = server.query(query);
	// } catch (SolrServerException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// SolrDocumentList docs = response.getResults();
	// Log.d("solr", "�ĵ�������" + docs.getNumFound());
	// Log.d("solr", "��ѯʱ�䣺" + response.getQTime());
	// for (SolrDocument doc : docs) {
	// Knowledge knowledge = new Knowledge();
	// knowledge.setId(doc.getFieldValue("id").toString());
	// knowledge.setTitle(doc.getFieldValue("title").toString());
	// k.addKnowledge(knowledge);
	// }
	// }

	private void queryFromServer(final String type, final String value) {
		String address;
		address = GlobalValue.serverIp + "/liuyanban/Knowledge?type=" + type
				+ "&value=" + value;
		Log.d("my", address);
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("1".equals(type)) {
					Log.d("my", "exec handleProvincesResponse");
					// Log.d("my", "response:" + response);
					result = Utility.handleKnowledgeListResponse(k, response);
				} else if ("2".equals(type)) {
					result = Utility.handleKnowledgeContentResponse(k,
							response, value);
				}
				if (result) {
					// ͨ��runOnUiThread()�����ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							adapter.notifyDataSetChanged();
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// ͨ��runOnUiThread()�����ص����̴߳����߼�
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(MainActivity.this, "����ʧ��,�����������",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
