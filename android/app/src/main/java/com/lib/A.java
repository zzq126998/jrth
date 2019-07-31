/**
 * 说明：Android基础使用
 * 创建：雨中磐石  from www.rili123.cn/xh829.com
 * 时间：2014-11-28
 * 邮箱：qqqq2900@126.com
 * QQ：290802026
 * */

package com.lib;


public final class A{

	public static final String VERSION 		= "1.2.3"; //系统版本,对应string.xml,和androidmanifest.xml也要改

	public static final int DB_VERSION 		= 1; //数据库版本

	public static final int HTTPB_SUCCESS 	= 200;
	public static final int HTTPB_TIMEOUT 	= 300;
	public static final int HTTPB_ERROR   	= 301;
	public static final int HTTPB_NOTWEN   	= 302;//无法访问网络
	public static final int HTTPB_BACKCODE 	= 303;//网络请求

	public static final int HTTPG_SOTIMEOUT 	= 30*1000; //读取超时时间
	public static final int HTTPG_CONNTIMEOUT 	= 30*1000; //请求超时时间
	public static final int HTTPG_UPLOADTIMEOUT = 2*60*1000; //上传超时时间(2分钟)




	public static final String APPPAGE   	= "rockxinhuapk";
	public static final boolean DEBUG   	= true;

	public static int MAXUPSIZE 			= 2;//最多上传M

}