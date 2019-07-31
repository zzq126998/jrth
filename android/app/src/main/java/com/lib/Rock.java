/**
 * 说明：Android基础使用
 * 创建：雨中磐石  from www.rili123.cn
 * 时间：2014-11-28
 * 邮箱：qqqq2900@126.com
 * QQ：290802026/1073744729
 * */

package com.lib;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.rock.xinhuapk.R;

import java.util.List;


public final class Rock{

	public static boolean isEmpt(String str)
	{
		boolean bo = false;
		if(str == null)bo=true;
		if(!bo)if(str.length()==0 || str == "" || str.equals("") || str.equals("null"))bo = true;
		return bo;
	}

	public static boolean equals(String str, String str1)
	{
		if(str==null)str = "";
		if(str1==null)str1 = "";
		return str.equals(str1);
	}
	
	public static boolean contain(String str, String str1)
	{
		boolean  bo = false;
		if(!isEmpt(str) && !isEmpt(str1)){
			if(str.indexOf(str1)>-1){
				bo = true;
			}
		}
		return bo;
	}
	
	public static void Toast(Context context, String s)
	{
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}

	public static void alert(Context context, String s)
	{
		new AlertDialog.Builder(context).setTitle("提示")
				.setMessage(s)
				.setPositiveButton("确定", null)
				.show();
	}

	public static String printBundle(Bundle bundle)
	{
		String str 		 = "";
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			sb.append(",'" + key +"':'" + bundle.getString(key)+"'");
		}
		str 	= sb.toString();
		if(!isEmpt(str))str = str.substring(1);
		return str;
	}

	/**
	 * 判断应用是否在后台运行
	 * */
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 创建视图view
	 * */
	public static View getView(Context context, int viewid)
	{
		LayoutInflater factory = LayoutInflater.from(context);
		View view = factory.inflate(viewid, null);
		return view;
	}

	/**
	 * 复制
	 * */
	public static void copy(Context context, String txt)
	{
		ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		cm.setText(txt);
	}

	/**
	 * 获取剪切板内容
	 * */
	public static String getcopy(Context context)
	{
		ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		return cm.getText().toString();
	}

	/**
	 * 获取预定字符串
	 * */
	public static String getstring(Context context, String str)
	{
		Resources res 	= context.getResources();
		int id			= res.getIdentifier(str, "string", "com.rock.xinhuapk");
		return res.getString(id);
	}
	public static String getstring(Context context, int id)
	{
		Resources res 	= context.getResources();
		return res.getString(id);
	}

	/**
	 * 获取品牌和手机型号
	 * */
	public static String getbrandmodel(Context context)
	{
		String str = android.os.Build.BRAND+"_";
		str+= android.os.Build.MODEL+"_";
		str+= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		str= str.replaceAll(" ", "");
		str= str.toLowerCase();
		return str;
	}

	/**
	 * 获取品牌
	 * */
	public static String getbrand()
	{
		String str = android.os.Build.BRAND;
		str= str.toLowerCase();
		return str;
	}

	/**
	 * 判断是不是小米手机
	 * */
	public static Boolean isxiaomi()
	{
		String brand = getbrand();
		return equals(brand, "xiaomi");
	}

	/**
	 * 判断是不是华为手机
	 * */
	public static Boolean ishuawei()
	{
		String brand = getbrand();
		return equals(brand, "huawei");
	}

	/**
	 * 获取手机品牌类型
	 * */
	public static String getbrandtype()
	{
		String str = "xinhuoa";
		String brand = getbrand();
		if(equals(brand, "xiaomi"))str="xiaomi";
		if(equals(brand, "huawei"))str="huawei";
		if(equals(brand, "vivo"))str="vivo";
		if(equals(brand, "oppo"))str="oppo";
		if(equals(brand, "meizu"))str="meizu";
		return str;
	}

	/**
	 * 设置沉淀
	 * */
	public static void  setStatusBar(Activity obj, int gid) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			View view = (View) obj.findViewById(gid);
			Window window = obj.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			int statusBarHeight = getStatusBarHeight(obj.getBaseContext());
			ViewGroup.LayoutParams lp = view.getLayoutParams();
			lp.height = statusBarHeight-5;
			view.setLayoutParams(lp);
		}
	}

	/**
	 * 获取状态栏高度
	 * */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}