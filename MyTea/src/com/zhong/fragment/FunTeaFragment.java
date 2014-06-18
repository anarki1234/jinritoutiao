package com.zhong.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhong.activity.R;

/**
 * 功能搜索主页，就是标题为茶百科那页
 * 
 * @author 仲丛旭
 * 
 */
@SuppressLint("ValidFragment")
public class FunTeaFragment extends Fragment {
	private final String TAG = "FunTeaFragment";
	/** 搜索文本框 */
	private EditText editText_funtea_searchEdit;
	/** 搜索按钮 */
	private ImageView imageView_funtea_searchBtn;
	/** 热门搜索：茶 */
	private TextView textView_funtea_serachtea;
	/** 我的收藏 */
	private TextView textView_funtea_mycollect;
	/** 查看访问记录 */
	private TextView textView_funtea_selectlog;
	/** 版权信息 */
	private TextView textView_funtea_copyright;
	/** 意见反馈 */
	private TextView textView_funtea_feedback;
	/** 自定义这个类的单击监听器 */
	private FunTeaOnClickListener funTeaOnClickListener;
	/** 接口回调类型，与Activity交互用 */
	private OnMyButtonClickListener mListener;
	/** 编辑框动画对象 */
	Animation shake;

	/** 防止出错 */
	public FunTeaFragment() {

	}

	/**
	 * 传过来Activity的上下文，用来与fragment交互
	 * 
	 * @param context
	 */
	public FunTeaFragment(Context context) {
		mListener = ((OnMyButtonClickListener) context);
		shake = AnimationUtils.loadAnimation(context, R.anim.shake);// 加载动画资源文件
	}

	/**
	 * 用在与activity交互时，被回周的接口
	 * 
	 * @author 仲丛旭
	 * 
	 */
	public interface OnMyButtonClickListener {
		/**
		 * 查搜索首页面单击监听
		 * 
		 * @param titleTag
		 *            title标记
		 * @param text
		 *            如果是搜索页面，那么把传值带过去，否则写null就行
		 */
		public void onMyButtonClick(int titleTag, String text);// 接口中定义一个方法
	}

	/**
	 * 主要用于数据的初始化
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate()==");
		funTeaOnClickListener = new FunTeaOnClickListener();
	}

	/**
	 * 主要用于布局的初始化
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		Log.i(TAG, "onCreateView()==");
		View view = inflater.inflate(R.layout.fragment_fun_tea, null);
		editText_funtea_searchEdit = (EditText) view
				.findViewById(R.id.editText_funtea_searchEdit);
		imageView_funtea_searchBtn = (ImageView) view
				.findViewById(R.id.imageView_funtea_searchBtn);
		textView_funtea_serachtea = (TextView) view
				.findViewById(R.id.textView_funtea_serachtea);
		textView_funtea_mycollect = (TextView) view
				.findViewById(R.id.textView_funtea_mycollect);
		textView_funtea_selectlog = (TextView) view
				.findViewById(R.id.textView_funtea_selectlog);
		textView_funtea_copyright = (TextView) view
				.findViewById(R.id.textView_funtea_copyright);
		textView_funtea_feedback = (TextView) view
				.findViewById(R.id.textView_funtea_feedback);
		imageView_funtea_searchBtn.setOnClickListener(funTeaOnClickListener);
		textView_funtea_serachtea.setOnClickListener(funTeaOnClickListener);
		textView_funtea_mycollect.setOnClickListener(funTeaOnClickListener);
		textView_funtea_selectlog.setOnClickListener(funTeaOnClickListener);
		textView_funtea_copyright.setOnClickListener(funTeaOnClickListener);
		textView_funtea_feedback.setOnClickListener(funTeaOnClickListener);
		return view;
	}

	class FunTeaOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int vId = v.getId();
			Log.i(TAG, "vId==" + vId);
			if (vId == imageView_funtea_searchBtn.getId()) {
				String searchStr = editText_funtea_searchEdit.getText()
						.toString();
				if (!"".equals(searchStr)) {
					mListener.onMyButtonClick(5, searchStr);
				} else {
					editText_funtea_searchEdit.startAnimation(shake); // 给组件播放动画效果
				}
			} else if (vId == textView_funtea_serachtea.getId()) {
				mListener.onMyButtonClick(5, "茶");
			} else if (vId == textView_funtea_mycollect.getId()) {
				mListener.onMyButtonClick(1, null);
			} else if (vId == textView_funtea_selectlog.getId()) {
				mListener.onMyButtonClick(2, null);
			} else if (vId == textView_funtea_copyright.getId()) {
				mListener.onMyButtonClick(3, null);
			} else if (vId == textView_funtea_feedback.getId()) {
				mListener.onMyButtonClick(4, null);
			}
		}

	}
}
