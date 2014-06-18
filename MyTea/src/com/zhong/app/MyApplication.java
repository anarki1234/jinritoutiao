package com.zhong.app;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application {
	/** 缓存下来各种图片<网址，bitmap> */
	private Map<String, Bitmap> cacheImageMap = new HashMap<String, Bitmap>();

	@Override
	public void onCreate() {
		super.onCreate();
		cacheImageMap = new HashMap<String, Bitmap>();
	}

	public Map<String, Bitmap> getCacheImageMap() {
		return cacheImageMap;
	}

}
