package com.lib;

import android.content.Context;



public final class SqliteClass extends SqliteHelper{
	
	private static String Database	= A.APPPAGE;
	private static int version		= A.DB_VERSION;

	//首屏会话历史表
	public static final String tn_history				= "chathistory";
	public static final String[] tn_historyfields		=  {"face", "name", "optdt", "optdts", "cont", "type", "stotal", "sendid", "receid","iszd"};

	//聊天记录表
	public static final String tn_chatrecord			= "chatrecord";
	public static final String[] tn_recordfields		=  {"id","face", "sendname","optdt", "cont", "type", "sendid", "receid", "zt", "fileid"};

	//文件表
	public static final String tn_file					= "filerecord";
	public static final String[] tn_filefields			=  {"id","fileext","filepath","filename","thumbpath","filesizecn","filetype","optid","optname","adddt","filesize","downpath"};

	
	public SqliteClass(Context context) {
		super(context, Database, version);
	}
	

	//初始化时创建
	protected void initcreate(){
		//会话历史表
		createtable(tn_history,"name varchar(50),face varchar(100),type varchar(10),optdts varchar(10)," +
				"cont varchar(4000),stotal INTEGER DEFAULT 0,sendid INTEGER DEFAULT 0,receid INTEGER DEFAULT 0," +
				"optdt datetime,iszd INTEGER DEFAULT 0");

		//聊天记录表
		createtable(tn_chatrecord,"face varchar(100),sendname varchar(50),zt INTEGER DEFAULT 0,fileid INTEGER DEFAULT 0," +
				"cont varchar(4000),type varchar(10),sendid INTEGER DEFAULT 0,receid INTEGER DEFAULT 0,optdt datetime");

		//文件表
		createtable(tn_file,"fileext varchar(20),filepath varchar(50),downpath varchar(100),filename varchar(50),filetype varchar(50),thumbpath varchar(50)" +
				",filesizecn varchar(10),optid INTEGER DEFAULT 0,optname varchar(20),filesize INTEGER DEFAULT 0,adddt datetime");


	}

	/**
	 * 清除说有表记录
	 * */
	public void clearalltable()
	{
		delete(tn_history, "1=1");
		delete(tn_chatrecord, "1=1");
		delete(tn_file, "1=1");
		setOption("userarr", "");
		setOption("deptarr", "");
		setOption("grouparr", "");
		setOption("maxreid","");
		setOption("minreid","");
	}
	
}