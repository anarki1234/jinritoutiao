package com.zhong.activity;

import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhong.config.MyConfig;
import com.zhong.helper.JsonHelper;
import com.zhong.helper.MyHttpClientHelper;
import com.zhong.helper.SQLiteDataBaseHelper;

/**
 * 显示内容页的Activity
 * 
 * @author 仲丛旭
 * 
 */
public class ContentActivity extends Activity {
	/** 日志声明 */
	private final String TAG = "ContentActivity";
	/** 这个网页的标题 */
	private TextView textView_content_title;
	/** 创建时间信息 */
	private TextView textView_content_create_time;
	/** 资源 */
	private TextView textView_content_source;
	/** 显示网页的控件 */
	private WebView webView_content_wap_content;
	/** 当前这个页面请求网络得到的值 */
	private Map<String, Object> mapValue;
	/** 数据库操作 */
	SQLiteDataBaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content);
		initLayout();
		initData();
	}

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		textView_content_title = (TextView) findViewById(R.id.textView_content_title);
		textView_content_create_time = (TextView) findViewById(R.id.textView_content_create_time);
		textView_content_source = (TextView) findViewById(R.id.textView_content_source);
		webView_content_wap_content = (WebView) findViewById(R.id.webView_content_wap_content);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		db = new SQLiteDataBaseHelper(this, "tea");
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		Log.i(TAG, "initData()id==" + id);
		if (id != null) {
			new ContentTask(ContentActivity.this).execute(MyConfig.CONTENT
					+ "&id=" + id);
		} else {

		}
	}

	class ContentTask extends AsyncTask<String, Void, Object> {
		/** 上下文对象 */
		private Context context;
		/** 加载数据的提示对话框 */
		private ProgressDialog pDialog;

		public ContentTask(Context context) {
			this.context = context;
			pDialog = new ProgressDialog(this.context);
			pDialog.setTitle("请稍后");
			pDialog.setMessage("正在加载请稍后...");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.show();
		}

		/**
		 * 访问网络并解析数据
		 */
		@Override
		protected Object doInBackground(String... params) {
			Log.i(TAG, "doInBackground()params[0]==" + params[0]);
			String jsonString = MyHttpClientHelper.loadTextFromURL(params[0],
					"UTF-8");
			// Log.i(TAG, "jsonString==" + jsonString);
			Map<String, Object> map = JsonHelper.jsonStringToMap(jsonString,
					new String[] { "id", "title", "source", "wap_content",
							"create_time" }, "data");
			if (map != null && map.size() != 0) {
				/** 添加到数据库 */
				String id = map.get("id").toString();
				String title = map.get("title").toString();
				String source = map.get("source").toString();
				String create_time = map.get("create_time").toString();

				String sql = "INSERT INTO tb_teacontents(_id,title,source,create_time,type) values (?,?,?,?,?)";
				boolean flag = db.updataData(sql, new String[] { id, title,
						source, create_time, "1" });// 1表示已经查看过，2表示被收藏了。
				Log.i(TAG, "是否已经存在flag==" + flag);
			}
			// Log.i(TAG,"doInBackground()map=="+map);
			return map;
		}

		/**
		 * 网络解析得到数据后的操作
		 */
		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			// Log.i(TAG, "onPostExecute()result==" + result);
			if (result != null) {
				 Log.i(TAG, "map==" + (Map<String, Object>) result);
				mapValue = (Map<String, Object>) result;
				textView_content_title.setText(mapValue.get("title").toString());
				textView_content_source.setText(mapValue.get("source").toString());
				textView_content_create_time.setText(mapValue.get("create_time")
						.toString());
				webView_content_wap_content.loadDataWithBaseURL(null,
						mapValue.get("wap_content").toString(), "text/html",
						"UTF-8", null);

			}
			pDialog.dismiss();

		}
	}

	/**
	 * 自己写个三个按钮的单击监听，监听方式写在了控件属性里
	 */
	public void clickButton(View view) {
		switch (view.getId()) {
		case R.id.imageView_content_back:
			/* 返回 */
			this.finish();
			break;
		case R.id.imageView_content_collect:
			/* 收藏 */
			Log.i(TAG,"==这是收藏");
			String sql = "UPDATE tb_teacontents SET type = ? WHERE _id = ?";
			Log.i(TAG,"mapValue=="+mapValue);
			if(mapValue!=null&&!mapValue.equals("")){
				String type = "2";//把被浏览过改为被收藏了
				String id = mapValue.get("id").toString();
				Log.i(TAG, "type==" + type + ",id==" + id);
				db.updataData(sql, new String[] { type, id });
				Toast.makeText(ContentActivity.this, "收藏成功", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.imageView_content_share:
			/* 分享 */
			Intent intent = new Intent(ContentActivity.this,ShareActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);// 下往上推出效果
			break;

		default:
			break;
		}
	}@Override
	public void onDestroy() {
		super.onDestroy();
		// 关闭数据库
		if(db!=null){
			db.destroy();
		}
	}

}
