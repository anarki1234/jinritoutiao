package com.zhong.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

/**
 * 主界面viewpager适配器
 * 
 * @author 仲丛旭
 * 
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
	private final String TAG = "MainFragmentPagerAdapter";
	private List<Fragment> list = null;

	public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
		super(fm);
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public Object instantiateItem(View container, int position) {
		Log.i(TAG, "==instantiateItem()");
		return super.instantiateItem(container, position);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
