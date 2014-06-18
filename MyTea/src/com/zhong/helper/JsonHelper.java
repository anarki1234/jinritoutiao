package com.zhong.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonHelper {
	// 将json字符串反序列化成字符串数组
	public static String[] jsonStringToArray(String jsonString, String key) {
		JSONArray jsonArray = null;
		String[] arrString = null;
		try {
			if (key == null) {
				jsonArray = new JSONArray(jsonString);
			} else {
				jsonArray = new JSONObject(jsonString).getJSONArray(key);
			}
			arrString = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++) {
				arrString[i] = jsonArray.getString(i);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return arrString;
	}

	// 将json字符串反序列化成Map对象
	public static Map<String, Object> jsonStringToMap(String jsonString,
			String[] keyNames, String key) {
		JSONObject jsonObject = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (key == null) {
				jsonObject = new JSONObject(jsonString);
			} else {
				jsonObject = new JSONObject(jsonString).getJSONObject(key);
			}
			for (int i = 0; i < keyNames.length; i++) {
				map.put(keyNames[i], jsonObject.get(keyNames[i]));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	// json字符串反序列化成List对象
	public static List<Map<String, Object>> jsonStringToList(String jsonString,
			String[] keyNames, String key) {
		JSONArray jsonArray = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (key == null) {
				jsonArray = new JSONArray(jsonString);
			} else {
				jsonArray = new JSONObject(jsonString).getJSONArray(key);
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				for (int j = 0; j < keyNames.length; j++) {
					map.put(keyNames[j], jsonObject.get(keyNames[j]));
				}
				list.add(map);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
}
