/**
 * 说明：Sqlite数据库的使用公共类,盗用可耻啊
 * 创建：雨中磐石  from www.rili123.cn
 * 时间：2014-02-10
 * 邮箱：qqqq2900@126.com
 * QQ：290802026
 * */


package com.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;


abstract class SqliteHelper extends SQLiteOpenHelper{
	
	public String nowsql		= "";
	public int count			= 0;
	
	protected Boolean	bool	= true,
					fistbool 	= false;	//是否第一次运行
	
	protected SQLiteDatabase db = null,
							rdb = null;

	protected Context mContext	= null;
	private Uri nowuri;
	private ContentResolver contResolver;
	private String Database,basenum;
	
	
    /** 
     * 在SQLiteOpenHelper的子类当中，必须有该构造函数 
     * @param context   上下文对象 
     * @param dbname    数据库名称 
     * @param version   当前数据库的版本，值必须是整数并且是递增的状态 
     **/
	public SqliteHelper(Context context, String dbname, int version){
		super(context, dbname, null, version);
		mContext 	= context;
		Database 	= dbname;
		basenum		= dbname.replaceAll(".db", "");
	}


	/**
	 * 第一次调用创建数据库
	 * */
	@Override
	public void onCreate(SQLiteDatabase dbs) {
		db 			= dbs;
		rdb			= dbs;
		fistbool	= true;
		createtable("option","`name` varchar(50),`num` varchar(100),`pnum` varchar(50),`value` text,`sort` INTEGER DEFAULT 0");
		query("CREATE UNIQUE INDEX num_index on `option`(num)");//创建唯一索引
		getappnum();
		initcreate();
		onVersion2();onVersion3();onVersion4();onVersion5();
		Toast("第一次运行初始化完成了");
	}
	
	/**
	 * 初始化创建
	 * */
	protected void initcreate(){}
	
	/**
	 * 版本升级
	 * */
	protected void onVersion2(){}
	protected void onVersion3(){}
	protected void onVersion4(){}
	protected void onVersion5(){}
	
	/**
	 * 数据库的版本升级时触发
	 * */
	@Override
	public void onUpgrade(SQLiteDatabase dbs, int oldVersion, int newVersion) {
		db 	= dbs;
		rdb	= dbs;
		if(newVersion >= 2 && oldVersion == 1)onVersion2();
		if(newVersion >= 3 && oldVersion <= 2)onVersion3();
		if(newVersion >= 4 && oldVersion <= 3)onVersion4();
		if(newVersion >= 5 && oldVersion <= 4)onVersion5();
		Toast("系统升级成功");
	}
	

	/**
	 * 注销
	 * */
	protected void onDestroy(){
        if(db != null)db.close();
        if(rdb!=null)rdb.close(); 
    }	
	
	/**
	 * 创建序列号
	 * */
	@SuppressLint("DefaultLocale")
	private String getxulie(){
		String str = android.os.Build.BRAND+"_";
		str+= android.os.Build.MODEL+"_"+basenum+"_";
		str+= Secure.getString(mContext.getContentResolver(), Secure.ANDROID_ID);
		str= str.replaceAll(" ", "");
		str= str.toLowerCase();
		return str;
	}
	
	/**
	 * 获取应用编号，没有就创建
	 * */
	public String getappnum(){
		String str = getOption("appnum");
		if(isEmpt(str)){
			str = getxulie();
			setOption("appnum", str, "应用编号");
		}
		return str;
	}
	

	/**
	 * 初始信息
	 * */
	public void initdb(){
		getWritableDatabase();//用来初始化数据库的
		if(fistbool){//添加欢迎信息
			//record("infor", new String[]{"title","content","dt","zt"},
			//new String[]{"欢迎使用","欢迎您使用本软件，如有问题请反馈给我们，我们也会通过信息回复给您，谢谢！", CDate.now(),"1"}, "");
		}
	}
	

	/**
	 * 事务执行处理
	 * */
	private Boolean TranQuery(String sql){
		//if(!bool)return false;//有错误了
		if(db == null){
			db = getWritableDatabase();//获取读写对象
		}
		Boolean bo 	= true;
		try{
			db.execSQL(sql);
		}catch(SQLiteException e){
			bo 		= false;
			bool	= false;
			Log.v("ErrorSql", sql);
		}
		nowsql = sql;
		return bo;
	}
	
	/**
	 * 更新删除记录操作
	 * @param	table		表名
	 * @param	fields		字段数组：new String[] {"name","a"}
	 * @param	values		对应值数组：new String[] {"chenxihu","abc"}
	 * @param	where		条件 为空添加，不为空为修改
	 * @return	Boolean
	 * */
	public Boolean record(String table,String[] fields,String[] values, String where){
		//if(!bool)return false;//有错误了
		
		String sql = "";
		String cont	= "",cont1="",cont2="";
		int len1	= fields.length,len2	= values.length;
		if(len1 != len2){
			return false;
		}
		for(int i=0; i<len1; i++){
			cont+=",`"+fields[i]+"`='"+values[i]+"'";
			cont1+=",`"+fields[i]+"`";
			cont2+=",'"+values[i]+"'";
		}
		cont		= cont.substring(1);
		cont1		= cont1.substring(1);
		cont2		= cont2.substring(1);
		Boolean bo	= true;
		if(where.length()==0){
			sql = "insert into `"+table+"`("+cont1+")values("+cont2+")";
		}else{
			sql = "update `"+table+"` set "+cont+" where "+where+" ";
		}
		bo	= TranQuery(sql);
		return bo;
	}
	
	/**
	 * 删除处理
	 * @param	table		表名
	 * @param	where		条件
	 * @return	Boolean
	 * */
	public Boolean delete(String table,String where){
		return TranQuery("delete from `"+table+"` where "+where+" ");
	}
	
	/**
	 * 更新
	 * @param	table		表名
	 * @param	where		条件
	 * @return	Boolean
	 * */
	public Boolean update(String table,String cont,String where){
		return TranQuery("update `"+table+"` set "+cont+" where "+where+" ");
	}
	

	/**
	 * 表添加字段
	 * */
	public Boolean addfields(String table,String cont){
		return TranQuery("alter table `"+table+"` add "+cont+" ");
	}
	
	
	/**
	 * 读取表记录
	 * @param	table		表名
	 * @param	where		条件 
	 * @return	int
	 * */
	public int rows(String table,String where){
		int rows	= 0;
		String s	= getmou(table, "count(*)", where);
		rows		= Integer.parseInt(s);
		count		= rows;
		return rows;
	}

	/**
	 * 读取记录某行对应字段的记录
	 * @param	table		表名
	 * @param	fields		字段名称 (*,count(1))
	 * @param	where		条件 
	 * @return	int
	 * */
	public String getmou(String table, String fields, String where){
		String s = "";
		String sql = "select "+fields+" from "+table+" where "+where+" limit 1";
		Cursor cur = getQuery(sql);
		if(cur != null){
			if(cur.moveToNext()){
				s = cur.getString(0);
			}
		}
		if(isEmpt(s))s="";
		return s;
	}

	public Map<String, String> getmap(String[] arr, String fields)
	{
		Map<String, String> a 	= new HashMap<String, String>();
		String[] farr 			= fields.replace("`","").split(",");
		int len	= arr.length,i;
		if(farr.length<len)len = farr.length;
		for(i=0;i<len;i++)a.put(farr[i], arr[i]);
		return a;
	}

	
	/**
	 * 读取记录某行对应字段的记录
	 * @param	table		表名
	 * @param	where		条件 
	 * @param	fields		字段名称
	 * @return	String[]
	 * */
	public String[] getone(String table, String where, String fields){
		String[] str = null;
		String sql 	= "select "+fields+" from "+table+" where "+where+" ";
		Cursor cur 	= getQuery(sql);
		count 		= 0;
		String s	= "";
		if(cur == null)return str;
		if(cur.moveToNext()){
			count = cur.getCount();
			int ls	= cur.getColumnCount();
			str		= new String[ls];
			for(int i=0;i<ls;i++){
				s		= cur.getString(i);
				if(s == null)s="";
				str[i] 	= s;
			}
		}
		return str;
	}
	
	public String[][] getall(String sql){
		Cursor cur = getQuery(sql);
		return getarr(cur);
	}
	
	public String[][] getarr(Cursor cur){
		count = 0;
		int ls=0;
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		String s= "";
		if(cur != null){
			count = cur.getCount();
			ls	= cur.getColumnCount();
			cur.moveToFirst();//移动到第一行
			while(cur.isAfterLast()==false){
				String[] str = {"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",""};
				for(int i=0;i<ls;i++){
					s		= cur.getString(i);
					if(s == null)s="";
					str[i] 	= s;
				}
				arrayList.add(str);
				cur.moveToNext();
			}
		}
		String arr[][] = new String[count][ls];
		if(count != 0){
			for(int i=0; i<count; i++){
				String[] str1 = arrayList.get(i);
				for(int j=0;j<ls;j++){
					arr[i][j] = str1[j];
				}
			}
		}else{
			arr = null;
		}
		return arr;
	}
	
	private Cursor getQuery(String sql){
		if(rdb==null)rdb = getReadableDatabase();
		Cursor cursor = null;
		try{
			cursor = rdb.rawQuery(sql, null);
		}catch(SQLiteException e){
		}
		nowsql	= sql;
		return cursor;
	}
	
	/**
	 * 创建表
	 * @param	table		表名
	 * @param	cont		创建内容，如(name varchar(50))
	 * @return	Boolean
	 * */
	protected Boolean createtable(String name,String cont){
		String sql = "CREATE TABLE IF NOT EXISTS "+name+"(id INTEGER PRIMARY KEY AUTOINCREMENT,"+cont+")";
		return TranQuery(sql);
	}

	/**
	 * 执行sql
	 * @param	sql		原生sql语句
	 * */
	public boolean query(String sql){
		return TranQuery(sql);
	}

	/**
	 * 删除表
	 * @param	name		表名
	 * @return	Boolean
	 * */
	protected Boolean droptable(String name){
		return TranQuery("drop table "+name+"");
	}
	
	/**
	 * 删除数据库啊
	 * */
	public boolean deleteDatabase(){
		boolean bo= mContext.deleteDatabase(Database);
		Toast("成功删除数据库，请重新软件");
		return bo;
	}
	
	/**
	 * 获取选择值
	 * */
	public String getOption(String num, String dev){
		String str = getmou("option","value","`num`='"+num+"'");
		if(Rock.isEmpt(str))str = dev;
		return str;
	}
	public String getOption(String num){
		return getOption(num, "");
	}
	
	/**
	 * 设置选项
	 * */
	public boolean setOption(String num,String value,String name){
		//先判断是否存在啦
		String where = "`num`='"+num+"'";
		int to	= rows("option", where);
		if(to == 0)where = "";
		return record("option", new String[]{"name","num","value"}, new String[]{name,num,value}, where);
	}
	public boolean setOption(String num,String value){return setOption(num,value,"");}
	public boolean setOptions(String num,String value){
		//先判断是否存在啦
		String where = "`num`='"+num+"'";
		int to	= rows("option", where);
		if(to == 0)where = "";
		return record("option", new String[]{"num","value"}, new String[]{num,value}, where);
	}
	
	
	/**
	 * 判断变量是否为空
	 * */
	public boolean isEmpt(String str){
		boolean bo = false;
		if(str == null)bo=true;
		if(!bo)if(str.length()==0 || str == "" || str.equals("") || str.equals("null"))bo = true;
		return bo;
	}
	
	public void Toast(String s){
		Toast.makeText(mContext, s, Toast.LENGTH_LONG).show();   
	}
	
	
	
	
	
	
	/**
	 * uri操作相关的
	 * */
	public void uriselect(Uri uri)
	{
		nowuri 		= uri;
		contResolver	= mContext.getContentResolver();
	}
	
	/**
	 * 执行
	 * */
	public String[][] urigetall(String[] fields, String where, String[] wheres, String order)
	{
		String arr[][] = null;
		if(nowuri==null)return arr;
		try{
			Cursor cur = contResolver.query(nowuri, fields, where, wheres, order);
			arr	= getarr(cur);
		}catch(Exception e){}
		return arr;
	}
	
	/**
	 * 总数
	 * */
	public int urirows(String where, String[] wheres)
	{
		int oi = 0;
		if(nowuri==null)return 0;
		try{
			Cursor cur = contResolver.query(nowuri, new String[]{"_id"}, where, wheres, null);
			if(cur != null){
				oi = cur.getCount();
			}
		}catch(Exception e){}
		return oi;
	}
	
	/**
	 * 删除
	 * */
	public boolean uridelete(String where, String[] wheres)
	{
		Boolean bo = false;
		if(nowuri==null)return bo;
		try{
			contResolver.delete(nowuri, where, wheres);
			bo = true;
		}catch(Exception e){}
		return bo;
	}
	
	/**
	 * 更新
	 * */
	public boolean urirecord(String[] fields,String[] values, String where, String[] wheres)
	{
		ContentValues uparr = new ContentValues();
		Boolean bo = false;
		for(int i=0; i<fields.length; i++){
			String val = values[i];
			if(isEmpt(val)){
				uparr.putNull(fields[i]);
			}else{
				uparr.put(fields[i], val);
			}
		}
		try{
			if(!isEmpt(where)){
				contResolver.update(nowuri, uparr, where, wheres);
			}else{
				contResolver.insert(nowuri, uparr);
			}
			bo = true;
		}catch(Exception e){}
		return bo;
	}
	
	/**
	 * 插入
	 * */
	public boolean uriinsert(String[] fields,String[] values)
	{
		return urirecord(fields, values, "", null);
	}
}