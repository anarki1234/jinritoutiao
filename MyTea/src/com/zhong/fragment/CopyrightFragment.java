package com.zhong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhong.activity.R;

/**
 * 显示版权信息
 * 
 * @author 仲丛旭
 * 
 */
public class CopyrightFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_copyright, null);
		return view;
	}
}
