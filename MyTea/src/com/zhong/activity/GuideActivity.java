package com.zhong.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhong.adapter.GuidePagerAdapter;
import com.zhong.config.MyConfig;
import com.zhong.helper.SharedPreferencesHelper;

/**
 * 实现引导页的Activity
 * 
 * @author 仲丛旭
 * 
 */
public class GuideActivity extends Activity {
	/** 日志标记 */
	public final String TAG = "GuideActivity";
	/** SharedPreferences操作,用来设置不再首次运行 */
	private SharedPreferencesHelper sph;
	/** viewPgaer控件，显示各种图片 */
	private ViewPager viewPager_guide;
	/** 引导页小点 */
	private LinearLayout linear_guide_dots;
	/** viewPager适配器 */
	private GuidePagerAdapter guidePagerAdapter;
	/** 适配器数据源 */
	private List<View> views;
	/** 底部图片 */
	private ImageView[] dots;
	/** 已知的引导页个数 */
	private int pageCount;
	/** 保存每次引导上一种状态变量 */
	private int currentIndex;

	/**
	 * 最开始的生命周期，用来初始化
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "==onCreate()");
		setContentView(R.layout.activity_guide);
		initData();
		initLayout();

	}

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		Log.i(TAG, "==initLayout()");
		/* viewPager设置 */
		viewPager_guide = (ViewPager) findViewById(R.id.viewPager_guide);
		LayoutInflater inflater = LayoutInflater.from(this);
		Class<R.drawable> cls = R.drawable.class;// 准备反射R.drawable下资源
		for (int i = 0; i < pageCount; i++) {
			View view = inflater.inflate(R.layout.guide_content, null);
			LinearLayout linear_guide_showImg = (LinearLayout) view
					.findViewById(R.id.linear_guide_showImg);
			int imageId = 0;
			try {
				imageId = cls.getDeclaredField("slide" + (i + 1)).getInt(
						R.drawable.slide1);
			} catch (Exception e) {
				imageId = R.drawable.slide1;
				e.printStackTrace();
			}
			Log.i(TAG, "测试==imageId-->" + imageId + ",views-->" + views);
			linear_guide_showImg.setBackgroundResource(imageId);
			views.add(view);
		}
		guidePagerAdapter = new GuidePagerAdapter(views, this);
		viewPager_guide.setAdapter(guidePagerAdapter);
		viewPager_guide.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				dots[arg0].setEnabled(false);
				dots[currentIndex].setEnabled(true);
				currentIndex = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		/* 引导小点设置 */
		linear_guide_dots = (LinearLayout) findViewById(R.id.linear_guide_dots);
		for (int i = 0; i < pageCount; i++) {// 循环取得小点图片
			dots[i] = (ImageView) linear_guide_dots.getChildAt(i);
			if (i == 0) {
				dots[i].setEnabled(false);// 设置为白色，即选中状态
			} else {
				dots[i].setEnabled(true);// 都设为灰色
			}
		}
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		Log.i(TAG, "==initData()");
		/* 设置标识，表示不再启动这个引导页 */
		sph = new SharedPreferencesHelper(GuideActivity.this);
		sph.putInt(MyConfig.IS_FIRST_RUN, MyConfig.NOT_FIRST);
		/* 小点数据源 */
		pageCount = 3;
		dots = new ImageView[pageCount];
		currentIndex = 0;
		/* 设置viewpager数据源 */
		views = new ArrayList<View>();
	}

}
