/**
 * 说明：json解析
 * 创建：雨中磐石  from www.rili123.cn
 * 时间：2014-11-28
 * 邮箱：qqqq2900@126.com
 * QQ：290802026/1073744729
 * */

package com.lib;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;






public final class Json{
	
	/**
	 * Json解析，解析字符串：[{"key":"value"},{},{}];
	 * return
	 * */
	public static List<Map<String, String>> getJsonArray(String str, String fields[])
	{
		ArrayList<Map<String, String>> jsonlist = new ArrayList<Map<String, String>>();
		if(Rock.isEmpt(str) || !str.substring(0,1).equals("["))return jsonlist;
		try{
			JSONArray json = new JSONArray(str);
			for (int i = 0; i < json.length(); i++) { 
				JSONObject data		= json.getJSONObject(i);		
				Map<String, String> newScanMap= new HashMap<String, String>();
				for(String fieldss:fields)newScanMap.put(fieldss, "");		
				Iterator<?> it 		= data.keys(); 
				String keys 		= "";
				String valus		= "";
				while(it.hasNext()){
					keys = (String) it.next().toString();
					valus= data.getString(keys);
					if(valus=="null")valus="";
					newScanMap.put(keys, valus);	
	            } 
				jsonlist.add(newScanMap);
			}
		} catch (JSONException e) {

		}
		return jsonlist;
	}
	public static List<Map<String, String>> getJsonArray(String str)
	{
		return getJsonArray(str, new String[]{});
	}


	/**
	 * Json解析，解析字符串：{"key":"value"};
	 * return
	 * */
	public static Map<String, String> getJsonObject(String str)
	{
		Map<String, String> newScanMap= new HashMap<String, String>();
		if(Rock.isEmpt(str) || !str.substring(0,1).equals("{"))return newScanMap;
		try{
			JSONObject json = new JSONObject(str);
			Iterator<?> it 		= json.keys();
			String keys 		= "";
			String valus		= "";
			while(it.hasNext()){
				keys = (String) it.next().toString();
				valus= json.getString(keys);
				if(valus=="null")valus="";
				newScanMap.put(keys, valus);
			}
		} catch (JSONException e) {

		}
		return newScanMap;
	}
}