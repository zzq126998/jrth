package com.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

/**
 * 文件读取
 * */

public final class RockFile {

	private static String sdPathstr = "";
	
	//检测SD卡是否存在
	public static String sdPath()
	{
		if(!Rock.isEmpt(sdPathstr))return sdPathstr;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        	File sdpath1 = Environment.getExternalStorageDirectory();
			sdPathstr	= ""+sdpath1+"";
            return sdPathstr;
        }else{
        	return "";
        }
	}
	
	/**
	 * 判断sd下的文件是否存在喽
	 * path String 文件路径
	 * */
	public static boolean isExists(String path)
	{
		boolean bool = false;
		File file	 = new File(path);
		if(file.exists())bool=true;
		return bool;
	}
	

	//获取目录下文件夹
	public static String[] getFolder(String path)
	{
		ArrayList<String> arr = new ArrayList<String>();
		String[] 	a = null;
		File file	= new File(path);
		if(file.exists()){
			File[] files = file.listFiles();  
			for(int i=0; i<files.length; i++){
				if(files[i].isDirectory()){
					arr.add(files[i].getName());
				}
			}	
			int len	= arr.size();
			a = new String[len];
			for(int i=0; i<len; i++)a[i]=arr.get(i);
		}
		return a;
	}

	/**
	 * 判断是不是图片
	 * */
	public static Boolean isimg(String ext)
	{
		return Rock.contain(",jpg,png,gif,jpeg,bmp,",","+ext+",");
	}

	/*
	* 下载路径
	* */
	public static String getDir(String str, int glx)
	{
		String sdpath = sdPath();
		if(Rock.isEmpt(sdpath))return "";
		String spath  = ""+sdpath+"/"+A.APPPAGE+"";
		File dir	  = new File(spath);
		if (!dir.exists())dir.mkdirs();
		if(!Rock.isEmpt(str)) {
			spath += "/" + str + "";
			dir = new File(spath);
			if (!dir.exists()) dir.mkdirs();
		}
		if(glx==1)spath = spath.replace(sdpath, "");
		return spath;
	}
	public static String getDir(String str)
	{
		return getDir(str, 0);
	}
	

	public static String getExt(String name){
    	String ext	= name;
    	if(name != null && name.length()>0){
    		int dot = name.lastIndexOf('.');
    		ext	= name.substring(dot + 1); 
    	}
    	ext	= ext.toLowerCase();
    	return ext;
    }

	/**
	 * 获取一个路径的文件名
	 * */
	public static String getFilename(String name){
		String ext	= name;
		if(name != null && name.length()>0){
			int dot = name.lastIndexOf('/');
			ext	= name.substring(dot + 1);
		}
		return ext;
	}

	public static String getFilepath(String name){
		String ext	= name;
		if(name != null && name.length()>0){
			int dot = name.lastIndexOf('/');
			ext	= name.substring(0, dot);
		}
		return ext;
	}

	public static String getFilesizecn(String spath){
		String ext	= "0KB";
		File file	  = new File(spath);
		if(!file.exists())return ext;
		long filesize = file.length();

		return formatsize(filesize);
	}

	public static String formatsize(long size)
	{
		String arr[] 	= {"Byte", "KB", "MB", "GB", "TB", "PB"};
		Double e		= Math.floor(Math.log(size)/Math.log(1024));
		Double fs		= size/Math.pow(1024,Math.floor(e));
		DecimalFormat df = new DecimalFormat(".##");
		return df.format(fs)+" "+arr[e.intValue()];
	}

	/**
	 * 保存文件到sd下
	 * params str 文件base64
	 * */
	public static String saveImgbase64(String str, String file)
	{
		String spath 	= getDir("temp");
		if(Rock.isEmpt(spath) || Rock.isEmpt(str))return "";
		spath		  	= spath+"/"+file+"";
		byte[] bitmapArray;
		bitmapArray = Base64.decode(str, Base64.DEFAULT);
		try {
			FileOutputStream fos = new FileOutputStream(spath,true);
			fos.write(bitmapArray);
			fos.close();
			file = spath;
		}catch (IOException e) {
			file = "";
		}
		return file;
	}

	public static String saveFiles(String str, String file)
	{
		String ext   = getExt(file);
		String nfile = "~"+Jiami.md5(file)+"";
		return saveImgbase64(str, nfile);
	}

	/**
	 * 判断缓存文件是否存在
	 * */
	public static String existsFile(String file)
	{
		String spath  = getDir("temp");
		String ext    = getExt(file);
		String nfile  = ""+spath+"/~"+Jiami.md5(file)+"";
		if(!new File(nfile).exists()){
			nfile = "";
		}
		return nfile;
	}

	/**
	 * 获取选择到的文件路径
	 * */
	public static String getChangePath(Context context, Uri uri)
	{
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;
			try {
				cursor = context.getContentResolver().query(uri, projection,null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		}
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	/**
	 * 递归删除 文件/文件夹
	 */
	public static void deleteDirFile(File file) {

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteDirFile(files[i]);
				}
			}
			file.delete();
		}
	}
	public static void deleteDirFile(String dir) {
		File file = new File(dir);
		deleteDirFile(file);
	}
}
