package com.zhong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.zhong.config.MyConfig;
import com.zhong.helper.SharedPreferencesHelper;

/**
 * 实现每次启动软件显示3秒界面，并判断是否是第一次运行，是否要开启引导页。
 * 
 * @author 仲丛旭
 * 
 */
public class StartActivity extends Activity {
	/** 日志标记 */
	private final String TAG = "StartActivity";
	/** SharedPreferences操作,功能判断是否是首次运行 */
	private SharedPreferencesHelper sph;
	/** 主要功能，跳转到不同的界面 */
	private Handler mHandler;

	/**
	 * 生命周期最开始
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		Log.i(TAG, "==onCreate()");
		initData();
	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		Log.i(TAG, "==initData()");
		sph = new SharedPreferencesHelper(StartActivity.this);
		mHandler = new Handler() {
			/**
			 * 0：跳转到用户首页；1：跳转到引导页。
			 */
			public void handleMessage(android.os.Message msg) {
				super.handleMessage(msg);
				Log.i(TAG, "==handleMessage(),msg=" + msg);
				switch (msg.what) {
				case 0:
					goActivity(MainActivity.class);
					break;
				case 1:
					goActivity(GuideActivity.class);
					break;
				}
			};
		};
		int isFirst = sph.getInt(MyConfig.IS_FIRST_RUN);
		Log.i(TAG, "测试引导页标识，isFirst==" + isFirst);
		if (isFirst == MyConfig.NOT_FIRST) {
			mHandler.sendEmptyMessageDelayed(0, 3000);
		} else {
			mHandler.sendEmptyMessageDelayed(1, 3000);
		}
	}

	/**
	 * 路转到某页面
	 */
	protected void goActivity(Class<?> cls) {
		Log.i(TAG, "==goActivity()");
		Intent intent = new Intent(this, cls);
		startActivity(intent);
		this.finish();
	}

}
