package com.zhong.config;

public class MyConfig {
	/** 保存是否是第一次运行程序的标记 */
	public final static String IS_FIRST_RUN = "isFirstRun";
	/** 不是第一次运行标识 */
	public final static int NOT_FIRST = 1;
	/** 是第一次运行标识 */
	public final static int IS_FIRST = -1;
	/** 解析广告的网络请求的url */
	public final static String JSON_URL = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getSlideshow";
	/** listview数据解析url */
	public final static String JSON_LIST_DATA_0 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getHeadlines";
	/** listview数据解析url */
	public final static String JSON_LIST_DATA_1 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=16";
	/** listview数据解析url */
	public final static String JSON_LIST_DATA_2 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=52";
	/** listview数据解析url */
	public final static String JSON_LIST_DATA_3 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=53";
	/** listview数据解析url */
	public final static String JSON_LIST_DATA_4 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=54";
	/** 搜索网络地址 */
	public static final String SEARCH = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.searcListByTitle";
	/** 内容新页的网络地址 后面加&id= */
	public static final String CONTENT = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getNewsContent";

}
