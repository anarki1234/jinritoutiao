package com.zhong.fragment;

import com.zhong.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 意见反馈页
 * 
 * @author 仲丛旭
 * 
 */
public class FeedbackFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_feedback, null);
	}
}
