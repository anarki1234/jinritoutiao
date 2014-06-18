/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package com.zhong.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhong.activity.R;

public class XListViewFooter extends LinearLayout {
	private final String TAG = "XListViewFooter";
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	private View mContentView;
	private View mProgressBar;
	private TextView mHintView;

	public XListViewFooter(Context context) {
		super(context);
		Log.i(TAG, "start==XListViewFooter(1)");
		initView(context);
		Log.i(TAG, "end==XListViewFooter(1)");

	}

	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG, "start==XListViewFooter(2)");
		initView(context);
		Log.i(TAG, "end==XListViewFooter(2)");
	}

	public void setState(int state) {
		mProgressBar.setVisibility(View.VISIBLE);
		// mHintView.setVisibility(View.INVISIBLE);
		// mProgressBar.setVisibility(View.INVISIBLE);
		// mHintView.setVisibility(View.INVISIBLE);
		// if (state == STATE_READY) {
		// mHintView.setVisibility(View.VISIBLE);
		// mHintView.setText("松开加载");
		// } else if (state == STATE_LOADING) {
		// mProgressBar.setVisibility(View.VISIBLE);
		// } else {
		// mHintView.setVisibility(View.VISIBLE);
		// mHintView.setText("准备加载");
		// }
	}

	public void setBottomMargin(int height) {
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal() {
		mHintView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
	}

	/**
	 * loading status
	 */
	public void loading() {
		mHintView.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.VISIBLE);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView
				.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.xlistview_footer, null);
		Log.i(TAG, "moreView==start");
		addView(moreView);
		Log.i(TAG, "moreView==end");
		moreView.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		Log.i(TAG, "progressbar==start");
		mProgressBar = moreView.findViewById(R.id.xlistview_footer_progressbar);
		Log.i(TAG, "progressbar==end");
		mHintView = (TextView) moreView
				.findViewById(R.id.xlistview_footer_hint_textview);
	}

}
