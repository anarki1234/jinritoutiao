package com.zhong.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhong.activity.R;
import com.zhong.config.MyConfig;
import com.zhong.fragment.base.BaseListFragment;
import com.zhong.helper.JsonHelper;
import com.zhong.helper.MyHttpClientHelper;
import com.zhong.widget.MyViewPager;

/**
 * 主页面的fragment内容展示
 * 
 * @author 仲丛旭
 * 
 */
@SuppressLint("ValidFragment")
public class ContentFragment extends BaseListFragment implements
		View.OnClickListener {
	private final String TAG = "ContentFragment";
	private List<Map<String, Object>> list;
	/** listView适配器 */
	protected ContentBaseadapter mAdapter;
	private String urlStr;
	/** 全局变量之图片缓存map */
	protected Map<String, Bitmap> cacheImageMap;
	/** 更新页数 */
	private int page = 1;
	/** fragment标识 */
	private int fragmentIndex;
	// ======================以下是只有第一页才会出现的广告页
	/** 单选按钮组，那些小点 */
	private RadioGroup radioGroup_fragment;
	/** 第一页需要展示的广告 */
	private MyViewPager viewPager_fragment;
	/** 存放viewPager的list */
	private List<ImageView> viewList;
	/** viewpager的适配器 */
	private MyAdvertisementAdapter myAdvertisementAdapter;
	/** 广告上的父控件，为了确定大小而获取 */
	private RelativeLayout relative_fragment_content;
	/** 接收listview解析返回的数据 */
	private List<Map<String, Object>> jsonList;
	/** 轮播图片字符串title数组 */
	private String[] titleArr = new String[3];
	/** 轮播广告上的文字 */
	private TextView textView_fragment_content_titleName;

	public ContentFragment() {
		fragmentIndex = 1;
	}

	/**
	 * 在Activity中创建fragment的构造方法
	 * 
	 * @param urlStr
	 *            第一次请求网址
	 * @param list
	 *            集合
	 * @param cacheImageMap
	 *            缓存的图片
	 */
	public ContentFragment(String urlStr, List<Map<String, Object>> list,
			Map<String, Bitmap> cacheImageMap) {
		this.list = list;
		this.urlStr = urlStr;
		this.cacheImageMap = cacheImageMap;
		if (urlStr.equals(MyConfig.JSON_LIST_DATA_0)) {
			fragmentIndex = 1;
		} else {
			fragmentIndex = 0;
		}
	}

	/**
	 * 于用获取数据
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		}
		mAdapter = new ContentBaseadapter(getActivity(), list);
	}

	/**
	 * 用于创建布局
	 * 
	 * 初始化布局：
	 * 
	 * 得到listView控件
	 * 
	 * 设置listView属性
	 * 
	 * 设置listView监听
	 * 
	 * 创建listview中Adapter
	 * 
	 * 为Adapter追加数据源
	 * 
	 * 把adapter与listview控件绑定
	 * 
	 * 设置listView监听
	 * 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (urlStr.equals("")) {
			/* 如果是第六页，那么不显示listView加载 */
			listview.setPullLoadEnable(false);
		}
		/* 父控件 */
		relative_fragment_content = (RelativeLayout) view
				.findViewById(R.id.relative_fragment_content);
		textView_fragment_content_titleName = (TextView) view
				.findViewById(R.id.textView_fragment_content_titleName);
		if (fragmentIndex == 1) {// 1：是第一页，0：表示其它页
			/* 开始加载轮播广告数据 */
			new MyTask(0).execute(MyConfig.JSON_URL);
			relative_fragment_content
					.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, getButtonViewPager()));
			/* 得到viewPager对象，并设置其子页面 */
			viewPager_fragment = (MyViewPager) view
					.findViewById(R.id.viewPager_fragment);
			/* 重置高度 */
			// viewPager_fragment.setLayoutParams(new LinearLayout.LayoutParams(
			// LayoutParams.MATCH_PARENT, getButtonViewPager()));

			viewList = new ArrayList<ImageView>();
			for (int i = 0; i < 3; i++) {
				ImageView imageView = new ImageView(getActivity());
				imageView.setImageResource(R.drawable.ic_launcher);
				imageView.setOnClickListener(this);
				viewList.add(imageView);
			}
			myAdvertisementAdapter = new MyAdvertisementAdapter(viewList);
			viewPager_fragment.setAdapter(new MyAdvertisementAdapter(viewList));
			/* 页面滑动时的监听 */
			viewPager_fragment
					.setOnPageChangeListener(new OnPageChangeListener() {

						@Override
						public void onPageSelected(int arg0) {
							radioGroup_fragment.getChildAt(arg0).performClick();
						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {

						}

						@Override
						public void onPageScrollStateChanged(int arg0) {

						}
					});
			/* 创建若干单选按钮，被把按钮放到组里 */
			radioGroup_fragment = (RadioGroup) view
					.findViewById(R.id.radioGroup_fragment);
			for (int i = 0; i < radioGroup_fragment.getChildCount(); i++) {
				// RadioButton rButton =new RadioButton(getActivity());// 创建单选按钮
				RadioButton rButton = (RadioButton) radioGroup_fragment
						.getChildAt(i);
				rButton.setTag(i);// 设置tag的下标与滑动界面的下标保持一至
				// rButton.setLayoutParams(new LinearLayout.LayoutParams(15,
				// 15));// 设置点的宽高
				rButton.setBackgroundResource(R.drawable.slide_image_dot_c2);//
				// 背景状态设置
				rButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));// 去掉单选按钮前面的点颜色为空
				// radioGroup_fragment.addView(rButton);
			}
			/* 单选按钮组被选中的监听 */
			radioGroup_fragment
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							// Log.i(TAG, "==得到按钮，并改变当前按钮显示的颜色");
							RadioButton rButton = (RadioButton) group
									.findViewById(checkedId);

							// Log.i(TAG, "==得到相应的tag值(tag值就是下标)");
							int index = (Integer) (rButton.getTag());

							// Log.i(TAG, "拿出按钮中的tag值，就是下标 index==" + index);
							viewPager_fragment.setCurrentItem(index);

							// 改变当前显示的文字
							textView_fragment_content_titleName
									.setText(titleArr[index]);
						}
					});
			radioGroup_fragment.getChildAt(0).performClick();// 默认设置第一个页面是单击状态
		} else {
			relative_fragment_content.setVisibility(View.GONE);
		}
		Log.i(TAG, "==onCreateView(),listview-->" + listview);
		listview.setXListViewListener(this);// 设置监听由这个类完成
		listview.setAdapter(mAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(TAG, "==onItemClick(),position=" + position + ",id=" + id);
				Log.i(TAG, "==自定义adapter中的每一项，把这数据弄下来");
				String idStr = list.get((int) id).get("id").toString();
				goContentActivity(idStr);
			}
		});

		return view;
	}

	/**
	 * 实现左右滑动广告Adapter
	 * 
	 * @author 仲丛旭
	 * 
	 */
	class MyAdvertisementAdapter extends PagerAdapter {
		private List<ImageView> list = null;

		public MyAdvertisementAdapter(List<ImageView> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			if (list != null) {
				return list.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));// 每一个item实例化对象
			return list.get(position);
		}
	}

	/**
	 * 根据手机尺寸获得ViewPager高度
	 * 
	 * @param i
	 */
	private int getButtonViewPager() {
		Display display = getActivity().getWindowManager().getDefaultDisplay(); // Activity#getWindowManager()
		int lenButton = 0;
		lenButton = display.getWidth() * 1 / 2;
		return lenButton;
	}

	/**
	 * fragment中listview适配器
	 * 
	 * @author 仲丛旭
	 * 
	 */
	class ContentBaseadapter extends BaseAdapter {
		private Context context;
		private List<Map<String, Object>> list;

		public ContentBaseadapter(Context context,
				List<Map<String, Object>> list) {
			this.context = context;
			this.list = list;
		}

		public void addList(List<Map<String, Object>> list) {
			this.list.addAll(list);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mhHolder;
			if (convertView == null) {
				mhHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_listview, null);
				mhHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				mhHolder.source = (TextView) convertView
						.findViewById(R.id.source);
				mhHolder.nickname = (TextView) convertView
						.findViewById(R.id.nickname);
				mhHolder.create_time = (TextView) convertView
						.findViewById(R.id.create_time);
				mhHolder.wap_thumb = (ImageView) convertView
						.findViewById(R.id.wap_thumb);
				convertView.setTag(mhHolder);
			} else {
				mhHolder = (ViewHolder) convertView.getTag();
			}

			String title = (String) list.get(position).get("title");
			String source = (String) list.get(position).get("source");
			String create_time = (String) list.get(position).get("create_time");
			String nickname = (String) list.get(position).get("nickname");
			String wap_thumb = list.get(position).get("wap_thumb").toString();

			mhHolder.title.setText(title);
			mhHolder.source.setText(source);
			mhHolder.create_time.setText(create_time);
			mhHolder.nickname.setText(nickname);
			// mhHolder.wap_thumb.setImageResource(R.drawable.ic_launcher);
			/* 判断要不要给图片留位置 */
			if (wap_thumb == null || wap_thumb.equals("")) {
				mhHolder.wap_thumb.setVisibility(View.GONE);
			} else {
				mhHolder.wap_thumb.setVisibility(View.INVISIBLE);
			}
			if (cacheImageMap.get(wap_thumb) == null) {
				/* 不存在开始请求网络，并设置图片 */
				new MyTask(mhHolder.wap_thumb, wap_thumb, 1).execute(wap_thumb);
			} else {
				mhHolder.wap_thumb.setImageBitmap(cacheImageMap.get(wap_thumb));
				mhHolder.wap_thumb.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		class ViewHolder {
			private TextView title;
			private TextView source;
			private TextView nickname;
			private TextView create_time;
			private ImageView wap_thumb;
		}
	}

	/** 异步任务，访问网络获取图片，并设置 */
	class MyTask extends AsyncTask<String, Integer, Object> {
		/** 作为缓存下来的图片网址字符串 */
		private String urlStr;
		/** 显示图片的控件 */
		private ImageView image;
		/** 操作标记：0加载listview小图片，1：加载页数据 */
		private int flag;

		public MyTask(ImageView image, String urlStr, int flag) {
			this.urlStr = urlStr;
			this.image = image;
			this.flag = flag;
		}

		public MyTask(int flag) {
			this.flag = flag;
		}

		@Override
		protected Object doInBackground(String... params) {
			Object obj = null;
			byte[] bitmapByte = MyHttpClientHelper.loadByteFromURL(params[0]);
			switch (flag) {
			case 0:
				Log.i(TAG, "==轮播图片josn中各种地址解析");
				jsonTolist(bitmapByte,
						new String[] { "image_s", "id", "title" }, "data", flag);
				break;
			case 1:
				obj = bitmapByte;
				break;
			case 2:
				String jsonString = MyHttpClientHelper.loadTextFromURL(
						params[0], "UTF-8");// 网络访问得到数据
				List<Map<String, Object>> list = JsonHelper.jsonStringToList(
						jsonString, new String[] { "title", "source",
								"nickname", "create_time", "wap_thumb", "id" },
						"data");
				obj = list;

				break;
			default:
				obj = bitmapByte;
				break;
			}
			return obj;
		}

		@Override
		protected void onPostExecute(Object obj) {
			super.onPostExecute(obj);
			if (obj == null) {
				return;
			}
			switch (flag) {
			case 1:
				byte[] result = (byte[]) obj;
				Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
						result.length);
				image.setImageBitmap(bitmap);
				image.setVisibility(View.VISIBLE);
				cacheImageMap.put(urlStr, bitmap);
				break;
			case 2:
				List<Map<String, Object>> list = (List<Map<String, Object>>) obj;
				mAdapter.addList(list);
				mAdapter.notifyDataSetChanged();
				if (list.size() == 0) {
					/* 确定本次请求没有数据了，那么listView不可以上拉加载了 */
					listview.setPullLoadEnable(false);
				}
				break;
			case 3:
				Log.i(TAG, "==每个输播图片的数据加载");
				getBitmap(urlStr, (byte[]) obj, 0);
				break;
			case 4:
				Log.i(TAG, "==每个输播图片的数据加载");
				getBitmap(urlStr, (byte[]) obj, 1);
				break;
			case 5:
				Log.i(TAG, "==每个输播图片的数据加载");
				getBitmap(urlStr, (byte[]) obj, 2);
				break;
			default:
				break;
			}

		}
	}

	/**
	 * 获取字节数组，转成bitmap图片显示
	 * 
	 * @param urlStr
	 * @param result
	 * @param i
	 * @return
	 */
	public Bitmap getBitmap(String urlStr, byte[] result, int i) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
		ImageView imageView = (ImageView) viewList.get(i);
		imageView.setImageBitmap(bitmap);
		cacheImageMap.put(urlStr, bitmap);
		myAdvertisementAdapter.notifyDataSetChanged();
		radioGroup_fragment.getChildAt(0).performClick();
		// 改变当前显示的文字也为第一页
		textView_fragment_content_titleName.setText(titleArr[0]);
		return bitmap;
	}

	/**
	 * JSON解析数据方法
	 * 
	 * @param result
	 * @param jsonStrings
	 * @param string
	 * @param flag
	 * @return
	 */
	public void jsonTolist(byte[] result, String[] jsonStrings, String string,
			int flag) {
		String str = "";
		String urlStr = null;
		try {
			str = new String(result, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		jsonList = JsonHelper.jsonStringToList(str, jsonStrings, string);
		Log.i(TAG, "jsonList==" + jsonList);
		for (int i = 0; i < jsonList.size(); i++) {
			urlStr = jsonList.get(i).get("image_s").toString();
			String id = jsonList.get(i).get("id").toString();
			String title = jsonList.get(i).get("title").toString();
			titleArr[i] = title;
			Log.i(TAG, "urlStr==" + urlStr);
			ImageView imageView = (ImageView) viewList.get(i);
			imageView.setTag(id);
			if (cacheImageMap.get(urlStr) == null) {
				new MyTask(3 + i).execute(urlStr);// 加载每一张图片
			} else {
				imageView.setImageBitmap(cacheImageMap.get(urlStr));
				myAdvertisementAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * ListView刷新
	 */
	@Override
	public void onRefresh() {
	}

	/**
	 * ListView加载
	 */
	@Override
	public void onLoadMore() {
		new MyTask(2).execute(urlStr + "&page=" + ++page);
	}

	/**
	 * 广告三张图片的单击监听
	 */
	@Override
	public void onClick(View v) {
		Log.i(TAG, "v.getId()==" + v.getId());
		goContentActivity(v.getTag().toString());
	}

}
