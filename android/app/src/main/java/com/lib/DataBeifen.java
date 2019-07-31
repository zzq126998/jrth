/**
 * 说明：数据库备份
 * 创建：雨中磐石  from www.rili123.cn
 * 时间：2014-02-10
 * 邮箱：qqqq2900@126.com
 * QQ：290802026/1073744729
 * */

package com.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;




public final class DataBeifen{

	
	private SqliteClass Sqlite;
	
	private String path;
	private String sdpath;
	
	public ArrayList<String[]> beitable = new ArrayList<String[]>();//备份的表数组
	
	
	public DataBeifen(Context cont)
	{
		Sqlite		= new SqliteClass(cont);
		path		= A.APPPAGE;
		sdpath		= RockFile.sdPath();
		beitable.add(new String[]{"option","name,num,value,pnum,sort","and `num`<>'appnum'"});
	}
	
	public String[][] getbeifen()
	{
		if(Rock.isEmpt(sdpath))return null;
		File file	= new File(sdpath+"/rock/"+path+"");
		if(!file.exists())return null;
		ArrayList<String[]> list = new ArrayList<String[]>();
		int len	= 0;
		File[] files = file.listFiles();
		int lena	= files.length;
		for(int i=0; i<lena; i++){
			if(files[i].isFile()){
				String name	= files[i].getName();//文件名
				String ext	= RockFile.getExt(name);
				String dts	= name.replaceAll(""+path+"_", "").replaceAll(".txt", "");
				dts			= CDate.now("Y-m-d H:i:s","", Long.parseLong(dts));
				String abs[] =  dts.split(" ");
				String[] str = {"文件名","扩展名","创建时间","创建日期"};
				str[0]		= name;
				str[1]		= ext;
				str[2]		= dts;
				str[3]		= abs[0];
				list.add(str);
				len++;
			}
		}
		if(len==0)return null;
		String arr[][] = new String[len][4];
		
		int oi = 0;
		for(int i=len-1;i>=0;i--) {
			arr[oi] = list.get(i);
			oi++;
		}
		return arr;
	}
	
	/**
	 * 添加备份
	 * */
	@SuppressWarnings("deprecation")
	public String addbeifen()
	{
		if(Rock.isEmpt(sdpath))return "没有sd卡无法备份";
		
		String str	= "";
		File dir	= new File(""+sdpath+"/rock/"+path+"");
		
		if (!dir.exists())dir.mkdirs(); //创建文件夹
		String now = CDate.now("time","");	
		
		File file = new File(sdpath+"/rock/"+path+"/"+path+"_"+now+".txt");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				str = "无权限创建SD文件";
			}
		}
		if(!Rock.isEmpt(str))return str;
		
		int i= 0;
		String bstr = "",fiels="",fielss="",t_str="";
		int len = beitable.size();
		for(i=0; i<len; i++){
			String a[] 	= beitable.get(i);
			String fi[]	= a[1].split(",");
			fiels		= "";
			fielss		= "\"databasetable_"+a[0]+"\"";
			for(int j=0;j<fi.length;j++){
				fiels+=",`"+fi[j]+"`";
				fielss+=",\""+fi[j]+"\"";
			}
			fiels	= fiels.substring(1);
			String arr[][] = Sqlite.getall("select "+fiels+" from "+a[0]+" where `id`>0 "+a[2]+"");
			if(arr != null){
				bstr+=",["+fielss+"]";//保存字段
				for(int k=0; k<arr.length;k++){
					t_str 	= "";
					for(int j=0;j<fi.length;j++){
						t_str+=",\""+arr[k][j]+"\"";
					}
					bstr+=",["+t_str.substring(1)+"]";
				}
			}
		}
		
		if(!Rock.isEmpt(bstr)){
			bstr	= "["+bstr.substring(1)+"]";
			bstr	= bstr.replaceAll("null", "");
			bstr 	= URLEncoder.encode(bstr);
			
			try {
				FileOutputStream fos = new FileOutputStream(file,true);
				fos.write(bstr.getBytes());
				fos.close();
			}catch (IOException e) {
				str = "备份失败，可能无写入SD卡权限";
			}
		}
		return str;
	}
	
	/**
	 * 删除备份
	 * */
	public void delbeifen(String name)
	{
		String paths	= sdpath+"/rock/"+path+"/"+name+"";
		File file	= new File(paths);
		file.delete();
	}
	
	/**
	 * 恢复备份
	 * */
	@SuppressWarnings("deprecation")
	public String huibeifen(String name)
	{
		String str = "无法读取SD内容上文件",bstr="";
		
		String paths	= sdpath+"/rock/"+path+"/"+name+"";
		File file	= new File(paths);
		
		StringBuffer sb = new StringBuffer();
		try {
			FileInputStream fis = new FileInputStream(file);
			int c;
			while ((c = fis.read()) != -1) {
				sb.append((char) c);
			}
			fis.close();
			str  = "";
		}catch (FileNotFoundException e) {
		}catch (IOException e) {
		} 
		if(!Rock.isEmpt(str))return str;
		
		bstr 	= sb.toString();
		bstr	= URLDecoder.decode(bstr);
		
		if(Rock.isEmpt(bstr))return "没有文件内容";
		
		try {
			JSONArray json = new JSONArray(bstr);
			String table  = "",fields[] = null,fiesss="";
			for (int i = 0; i < json.length(); i++) {
				JSONArray data	= json.getJSONArray(i);
				String ftable = data.getString(0);
				int len		  = data.length();
				if(Rock.contain(ftable, "databasetable_")){
					table	 = ftable.replaceAll("databasetable_", "");
					fiesss	 = "";
					for(int j=1;j<len;j++){
						fiesss+=","+data.getString(j)+"";
					}
					fields	= fiesss.substring(1).split(",");
					Sqlite.delete(table, "`id`>0");
				}else if(!Rock.isEmpt(table)){
					String[] valuess = new String[len];
					for(int j=0;j<len;j++)valuess[j]=data.getString(j);
					Sqlite.record(table, fields, valuess, "");
				}
			}	
		} catch (JSONException e) {
			str	= "文件有误";
		}
		
		return str;
	}
}