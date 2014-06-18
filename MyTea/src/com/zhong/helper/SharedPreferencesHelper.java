package com.zhong.helper;

import android.content.Context;
import android.content.SharedPreferences;

//1、SharedPreferences（共享选择）
//（一）特点：
//（1）以键值对的形式保存到目录为/data/data/包名/shared_prefs目录的*.xml文件中。
//（2）目前支持的数据类型有string、int、long、float、boolean。
//（3）不支持自定义的Object。
//（4）通常用来存储app上的配置信息，如是否振动、是否打开音效、小游戏积分。
//（5）SharedPreferences是一个接口，无法直接创建实例，可通过Context的getSharedPreferences(String name, int mode)方式来获取实例。
//（6）mode参数有三种值
//Context.MODE_PRIVATE：指定该SharedPreferences的数据只能被本应用程序读、写。
//Context.MODE_WORLD_READABLE：指定SharedPreferences数据能被其它应用程序读，但是不支持写；
//Context.MODE_WORLD_WRITEABLE：指定该ShaedPreferences数据能被其他应用程序读、写。
//（二）操作：(可创建工具类，无权限)
//（1）创建
//通过Context下的getSharedPreferences()方法获取实例。
//prefs = Context.getSharedPreferences(“标记字符串”,int mode);//获取SharedPreferences实例
//mode参数有三种值
//Context.MODE_PRIVATE：指定该SharedPreferences的数据只能被本应用程序读、写。
//Context.MODE_WORLD_READABLE：指定SharedPreferences数据能被其它应用程序读，但是不支持写；
//Context.MODE_WORLD_WRITEABLE：指定该ShaedPreferences数据能被其他应用程序读、写。
//调用edit()获取SharedPreferences.Editor。得到Editor对象
//SharedPreferences.Editor editor = prefs.edit();//获取Editor实例
//（2）增：
//通过SharedPreferences.Editor接口提供的put()方法对SharedPreferences进行更新；
//editor.putInt(“age”,38);//数据更新
//editor:putString();
//editor.commit();
//（3）删：手动找到文件删了，或者重新安装软件
//（4）改：通过键名相同来重复put值就可以。
//（5）查：prefs.getString(“age”);//如果遍历？值得思考

/**
 * 
 * @author 仲丛旭
 * 
 */
public class SharedPreferencesHelper {
	private final static String TAG = "SharedPreferencesHelper";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;

	public SharedPreferencesHelper(Context context) {
		prefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		editor = prefs.edit();
	}

	/**
	 * 根据键字符串，存储一个字符串值
	 * 
	 * @param key
	 * @param value
	 * @return 返回提交是否成功
	 */
	public boolean putString(String key, String value) {
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * 根据key值得到存储结果，如果没有找到value就返回null
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return prefs.getString(key, null);
	}

	/**
	 * 根据键字符串，存储一个字符串值
	 * 
	 * @param key
	 * @param value
	 * @return 返回提交是否成功
	 */
	public boolean putInt(String key, int value) {
		editor.putInt(key, value);
		return editor.commit();
	}

	/**
	 * 根据key值得到存储结果，如果没有找到value就返回-1
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return prefs.getInt(key, -1);
	}

	/**
	 * 清空数据
	 * 
	 * @return
	 */
	public boolean clear() {
		editor.clear();
		return editor.commit();
	}

	/**
	 * 关闭当前对象
	 * 
	 * @return
	 */
	public void close() {
		prefs = null;
	}
}