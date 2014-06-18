package com.zhong.fragment;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhong.activity.R;
import com.zhong.fragment.base.BaseListFragment;
import com.zhong.helper.SQLiteDataBaseHelper;

/**
 * 收藏页，主要用来显示“我的收藏”与“查看访问记录"。
 * 
 * @author 仲丛旭
 * 
 */
@SuppressLint("ValidFragment")
public class CollectFragment extends BaseListFragment {
	/** 打印日志 */
	private final String TAG = "CollectFragment";
	/** listView的数据源 */
	private List<Map<String, String>> list = null;
	/** 数据库操作 */
	private SQLiteDataBaseHelper db;
	/** listView的adapter */
	private CollectAdapter collectAdapter;
	/**不要首页广告*/
	private RelativeLayout relative_fragment_content;

	/**
	 * 防止出错
	 */
	public CollectFragment() {

	}

	/**
	 * 利用构造方法确定加载哪里的值
	 * 
	 * @param type
	 *            1表显示浏览（查询的时候忽略该条件），2表示收藏
	 */
	public CollectFragment(String type,Context context) {
		db = new SQLiteDataBaseHelper(context, "tea");
		if ("1".equals(type)) {
			String sql = "SELECT * FROM tb_teacontents";
			list = db.SelectData(sql, null);
		} else if ("2".equals(type)) {
			String sql = "SELECT * FROM tb_teacontents WHERE type = ?";
			list = db.SelectData(sql, new String[]{"2"});
		}
		Log.i(TAG,"CollectFragment()list=="+list);
	}

	/**
	 * 初始布局
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);// 回调父类的构造方法，生成listview，以及view布局对话
		relative_fragment_content = (RelativeLayout) view
				.findViewById(R.id.relative_fragment_content);
		relative_fragment_content.setVisibility(View.GONE);
		listview.setPullLoadEnable(false);//不加载了
		if (list != null&&list.size()!=0) {
			collectAdapter = new CollectAdapter(getActivity(), list);
			listview.setAdapter(collectAdapter);
			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String idStr = list.get((int) arg3).get("_id").toString();
					goContentActivity( idStr);

				}
			});
		} else {

		}
		return view;// 最终返回这个view对象完成本生命周期

	}

	class CollectAdapter extends BaseAdapter {
		private Context context;
		private List<Map<String, String>> list;

		public CollectAdapter(Context context, List<Map<String, String>> list) {
			this.context = context;
			this.list = list;
		}

		// public void addList(List<Map<String, String>> list) {
		// this.list.addAll(list);
		// }

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
				mhHolder.create_time = (TextView) convertView
						.findViewById(R.id.create_time);
				convertView.setTag(mhHolder);
			} else {
				mhHolder = (ViewHolder) convertView.getTag();
			}

			String title = (String) list.get(position).get("title");
			String source = (String) list.get(position).get("source");
			String create_time = (String) list.get(position).get("create_time");

			mhHolder.title.setText(title);
			mhHolder.source.setText(source);
			mhHolder.create_time.setText(create_time);
			return convertView;
		}

		class ViewHolder {
			private TextView title;
			private TextView source;
			private TextView create_time;
		}

	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//关闭数据库
		if(db!=null){
			db.destroy();
		}
	}
}
