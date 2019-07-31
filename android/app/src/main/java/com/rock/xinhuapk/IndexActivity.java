package com.rock.xinhuapk;



import com.lib.A;
import com.lib.CDate;
import com.lib.CLog;
import com.lib.Jiami;
import com.lib.Json;
import com.lib.Rock;
import com.lib.RockActivity;
import com.lib.RockFile;
import com.lib.RockHttp;
import com.lib.SqliteClass;
import com.view.ImageViewXinhu;
import com.xiaomi.mipush.sdk.MiPushClient;


import android.annotation.TargetApi;

import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IndexActivity extends RockActivity {

	private LinearLayout showviewobj0,showviewobj1,showviewobj2,showviewobj3,showchatnot,showdeptdao;
	private LinearLayout[] showviewobj = new LinearLayout[4];
	private ImageView[]	   navimageview= new ImageView[4];
	private TextView[]	   navtextview = new TextView[5];
	private String[] 		titlearr   = {"消息","应用","联系人","我"};

	private int what_chat = 3001,what_ying = 3002,what_userget = 3003,what_exit = 3004,chatindex=-1,what_chatdown = 3002,what_notmsg=4000,nowchatindex=-1,nowindex=0;
	private IndexReceiver indexReceiver;


	private ArrayList<View> chatview = new ArrayList<View>(); //会话的
	private ArrayList<View> lianxrview = new ArrayList<View>(); //联系人上
	private ArrayList<View> liandaoview = new ArrayList<View>(); //联系人上
	private ArrayList<View> agentview = new ArrayList<View>(); //应用的
	private ArrayList<Map<String, String>> chatlist = new ArrayList<Map<String, String>>();//历史记录会话
	private Map<String, String> chatobj = new HashMap<String, String>();
	private boolean isboolone = false,isboolshow=true;//是否手动消息时加载记录

	private String oldagentstr= "";




	protected void initCreate() {
		setContentView(R.layout.activity_index);
		findViewById(R.id.add).setOnClickListener(OnViewClickListener);
		findViewById(R.id.search).setOnClickListener(OnViewClickListener);
		findViewById(R.id.nav_chat0).setOnClickListener(OnViewClickListener);
		findViewById(R.id.nav_ying0).setOnClickListener(OnViewClickListener);
		findViewById(R.id.nav_user0).setOnClickListener(OnViewClickListener);
		findViewById(R.id.nav_my0).setOnClickListener(OnViewClickListener);

		msgview = (TextView) findViewById(R.id.msg);




		//只要调用一次就可以了
		Xinhu.adminname   	= Sqlite.getOption("adminname");
		Xinhu.adminid   	= Sqlite.getOption("adminid");
		Xinhu.admintoken   	= Sqlite.getOption("admintoken");
		Xinhu.adminmap   	= Json.getJsonObject(Sqlite.getOption("adminjson"));
		Xinhu.APIURL    	= Sqlite.getOption("apiurl", Xinhu.APIURL);
		CDate.SPLITTIME 	= Integer.parseInt(Sqlite.getOption("splittime","0")); //跟服务器时间差
		//titlearr[3]			= Xinhu.adminname;
		Xinhu.device		= Jiami.md5(appnum);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Xinhu.ACTION_MESSAGE);
		filter.addAction(Xinhu.ACTION_CONNECTION);
		filter.addAction(Xinhu.ACTION_DOWNBACK);
		indexReceiver		= new IndexReceiver();
		registerReceiver(indexReceiver, filter);

		//TextView v = (TextView)findViewById(R.id.nav_my2);
		//v.setText(Xinhu.adminname);

		showviewobj0 	= (LinearLayout) findViewById(R.id.showview0);
		showviewobj1 	= (LinearLayout) findViewById(R.id.showview1);
		showviewobj2 	= (LinearLayout) findViewById(R.id.showview2);
		showviewobj3 	= (LinearLayout) findViewById(R.id.showview3);
		showchatnot 	= (LinearLayout) findViewById(R.id.showchatnot);
		showviewobj[0]	= showviewobj0;
		showviewobj[1]	= showviewobj1;
		showviewobj[2]	= showviewobj2;
		showviewobj[3]	= showviewobj3;

		navimageview[0] = (ImageView) findViewById(R.id.nav_chat1);
		navimageview[1] = (ImageView) findViewById(R.id.nav_ying1);
		navimageview[2] = (ImageView) findViewById(R.id.nav_user1);
		navimageview[3] = (ImageView) findViewById(R.id.nav_my1);

		navtextview[0] = (TextView) findViewById(R.id.nav_chat2);
		navtextview[1] = (TextView) findViewById(R.id.nav_ying2);
		navtextview[2] = (TextView) findViewById(R.id.nav_user2);
		navtextview[3] = (TextView) findViewById(R.id.nav_my2);
		navtextview[4] = (TextView) findViewById(R.id.title);

		Rock.setStatusBar(this, R.id.headertopzt); //设置沉淀

		getchathistory();
		initindex();
		showagentlist(""); //加载应用
		initPush(); //初始化推送
	}


	//点击
	protected void ViewClick(View v) {
		int id = v.getId();
		if(id == R.id.add){
			showaddmenu();
		}
		if(id == R.id.search){
			showaddmenu();
		}
		if(id == R.id.nav_chat0){
			changenav(0);
		}
		if(id == R.id.nav_ying0){
			changenav(1);
		}
		if(id == R.id.nav_user0){
			changenav(2);
		}
		if(id == R.id.nav_my0){
			changenav(3);
		}
		if(id == R.id.exitbtn){
			exit(0);
		}
		if(id == R.id.mya_shatebtn){
			gotoshare();
		}
		if(id == R.id.mya_cachebtn){
			clearWebViewCache();
		}
		if(id==R.id.mya_versionbtn){
			Rock.Toast(this, "已是最新版本");
		}

		if(id==R.id.mya_clearbtn){
			Sqlite.clearalltable();
			Rock.Toast(this, "已清空本地数据库");
		}
		//打开个人档案
		//if(id==R.id.mya_danganbtn){

		//}
	}

	//打开人员聊天
	private void openchat(int i)
	{
		Map<String, String> a = chatlist.get(i);
		nowchatindex 		= i;
		
		Xinhu.recechatname	= a.get("name");
		Xinhu.recechattype 	= a.get("type");
		Xinhu.recechatid 	= a.get("receid");
		
		chatyidu();
		if(Rock.equals(Xinhu.recechattype, Xinhu.CHAT_AGENTVAL)) {
			openagent(Xinhu.recechatid);
		}else {
			//Rock.alert(this,a.toString());
			startSimpleActivity(ChatActivity.class);
		}
	}

	//将会话标识已读
	private void chatyidu()
	{
		if(nowchatindex<0)return;
		View v 		= chatview.get(nowchatindex);
		if(v!=null) {
			TextView av = (TextView) v.findViewById(R.id.stotal);
			av.setText("");
			av.setVisibility(View.GONE);
			chatlist.get(nowchatindex).put("stotal","0");
			totaochat();
		}
		Sqlite.update(SqliteClass.tn_history,"stotal=0","`type`='"+Xinhu.recechattype+"' and `receid`="+Xinhu.recechatid+"");
		Xinhu.ajaxget("reim","yiduall","type="+Xinhu.recechattype+"&gid="+Xinhu.recechatid+"", null, what_notmsg);
	}

	//统计会话未读数
	private void totaochat()
	{
		int i,size = chatlist.size(),stotal=0;
		for(i=0;i<size;i++){
			Map<String,String> a = chatlist.get(i);
			if(a!=null){
				stotal+=Integer.parseInt(a.get("stotal"));
			}
		}
		showshowstotal(stotal+"",1);
	}

	//清除素有会话记录
	private void clearallchat()
	{
		chatview.clear();
		chatlist.clear();
		chatobj.clear();
		showviewobj0.removeAllViews();
		nowchatindex=-1;
		Sqlite.delete(SqliteClass.tn_history,"1=1");
		if(nowindex==0)showchatnot.setVisibility(View.VISIBLE);
	}

	//读取消息记录列表
	private void initindex()
	{
		String ntime = CDate.gettime();
		Xinhu.ajaxget("index", "indexapp","ntime="+ntime+"",myhandler, what_chat);
	}
	private void showchatlist(String str)
	{
		Map<String,String> ret = Json.getJsonObject(str);
		List<Map<String, String>> lista = Json.getJsonArray(ret.get("reimarr"));
		int len  = lista.size(),i;
		clearallchat();
		for(i=0;i<len;i++){
			this.addchatlist(lista.get(i), 1);
		}
		if(len>0)showchatnot.setVisibility(View.GONE);
		totaochat();
		Sqlite.setOption("splittime", ret.get("splittime"));
		//设置跟服务器时间差
		CDate.SPLITTIME = Integer.parseInt(ret.get("splittime"));
		if(ret.get("maxupsize")!=null)A.MAXUPSIZE = Integer.parseInt(ret.get("maxupsize"));
		if(nowindex==1)oldagentstr = "";//在应用页刷新
		showagentlist(ret.get("agentarr"));//显示应用
		showshowstotal(ret.get("agentstotal"),0);//显示应用未读

		if(!Rock.equals(ret.get("appversion"),"android"+A.VERSION+"")){
			msgerror("不是最新系统，可能会影响APP正常使用");
		}

		//Xinhu.startWebSocket(this, ret.get("wsconfig")); //启用服务
	}
	//加到列表中
	private void addchatlist(Map<String, String> a, int glx)
	{
		String keys 	= "" + a.get("type") + "_"+a.get("receid")+"";
		String oii  	= chatobj.get(keys);
		int oldi 		= -1,i=0;
		if(oii!=null)oldi = Integer.parseInt(oii);
		if(oldi==-1) {
			chatlist.add(a);
			i = chatlist.size() - 1;
		}else{
			removechatlist(oldi); //删除原来的
			chatlist.set(oldi,a);
			i = oldi;
		}

		View view 	= Rock.getView(this,R.layout.index_chat0);
		String str 	= "";
		TextView av = (TextView) view.findViewById(R.id.title);
		av.setText(a.get("name"));

		av = (TextView) view.findViewById(R.id.titles);
		av.setText(Jiami.base64decode(a.get("cont")));

		av = (TextView) view.findViewById(R.id.dt);
		av.setText(a.get("optdts"));

		av = (TextView) view.findViewById(R.id.xu);
		av.setText(i+"");

		str = a.get("stotal");
		av = (TextView) view.findViewById(R.id.stotal);
		if(!str.equals("0")){
			av.setText(str);
			av.setVisibility(View.VISIBLE);
		}

		final int oi = i;
		View chatv =  (View)view.findViewById(R.id.chatlist);
		chatv.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				openchat(oi);
			}
		});
		chatv.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View view) {
				chatindex = oi;
				return false;
			}
		});
		if(oldi==-1) {
			chatview.add(view);
		}else{
			chatview.set(oldi,view);
		}

		ImageViewXinhu imgu = (ImageViewXinhu)view.findViewById(R.id.icons);
		imgu.setPath(a.get("face"));

		registerForContextMenu(chatv); //注册长按显示菜单
		if(Rock.equals(a.get("iszd"),"1") || glx==2) {
			showviewobj0.addView(view,0); //置顶
		}else{
			showviewobj0.addView(view);
		}

		String where 	= "`type`='" + a.get("type") + "' and `receid`=" + a.get("receid") + "";

		if(oldi==-1) {
			where = "";
		}
		chatobj.put(keys, i+"");

		//保存到表里
		if(glx>0) {
			String[] varr = new String[SqliteClass.tn_historyfields.length];
			for(i=0;i<varr.length;i++)varr[i] = a.get(SqliteClass.tn_historyfields[i]);
			Boolean bo=Sqlite.record(SqliteClass.tn_history, SqliteClass.tn_historyfields, varr, where);
			if(!bo)Rock.Toast(this,Sqlite.nowsql);
		}
	}

	/**
	 * 读取会话历史记录
	 * */
	private void getchathistory()
	{
		String [] fields= SqliteClass.tn_historyfields;
		String fstr = "";
		for(String fstr1:fields){
			fstr+=",`"+fstr1+"`";
		}
		String [][] rows = Sqlite.getall("select "+fstr.substring(1)+" from "+SqliteClass.tn_history+" order by `iszd` desc,`optdt` desc");
		if(rows!=null) {
			int i, len = rows.length,j=0;
			for(i=0;i<len;i++){
				Map<String, String> a = new HashMap<String, String>();
				j = 0;
				for(String fstr1:fields){
					a.put(fstr1, rows[i][j]);
					j++;
				}
				addchatlist(a,0);
			}
			if(len>0)showchatnot.setVisibility(View.GONE);
		}
		totaochat();
	}

	protected  void handleCallback(Message msg, String bstr)
	{
		int what = msg.what,wlcode = msg.arg1;
		if(what==what_notmsg)return;

		if(what>3000){
			dialogXinhu.dismiss();
		}

		//退出
		if(what==what_exit){
			exit(2);
			return;
		}

		//网络请求失败
		if(what>3000 && wlcode==0){
			msgtishi(bstr, "#ff0000", 10);

			return;
		}

		//首页第一个页面读取
		if(what==what_chat) {
			showchatlist(bstr);
			downrecord();
		}

		//首页加载联系人
		if(what==what_userget) {
			showuserlistback(bstr);
		}

	}


	@Override
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(indexReceiver);//注销广播
	}

	@Override
	protected void onResume() {
		super.onResume();
		nowchatindex = -1;
		isboolshow	 = true;
		//我有发送消息加载
		if(!Rock.isEmpt(Xinhu.recechatsend)){
			initindex();
		}
		Xinhu.NotificationcancelAll(this); //清除所有通知
		MiPushClient.clearNotification(this);
		Xinhu.clearchat();
		Xinhu.sendBroadcast(this, Xinhu.ACTION_ALLCLOSE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		isboolshow = false;
	}

	//聆听推送过来的消息
	public class IndexReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action   = intent.getAction(),title,content,type;
			Bundle bundle   = intent.getExtras();
			//Rock.Toast(context, "action："+action+"");

			//有收到消息
			if(action.equals(Xinhu.ACTION_MESSAGE)) {
				title    = bundle.getString("title");
				content  = bundle.getString("content");
				type 	 = bundle.getString(Xinhu.PUSH_MESSAGE_TYPE);
				pushmessage(content, type);
				//Rock.alert(context, "标题12："+title+"内容："+content+"");
			}

			//网络发送变化
			if(action.equals(Xinhu.ACTION_CONNECTION)) {

			}

			//下载回传过来的
			if(action.equals(Xinhu.ACTION_DOWNBACK)) {
				downrecords();
			}
		}
	}
	/**
	 * 有收到消息时处理
	 * ptype xiaomi推送 jpush 极光
	 * */
	private Map<String, String> pushmessagemap;
	private void pushmessage(String bstr, String ptype)
	{
		Map<String, String> a = Json.getJsonObject(bstr),a1;
		String pushtype = a.get("pushtype");
		a.put("ptype", ptype);//推送类型
		pushmessagemap	= a;

		//收到会话的消息
		if(Rock.equals(pushtype, "chat") && !RockHttp.isbool()){
			String keys 	= "" + a.get("type") + "_"+a.get("sendid")+"";
			if(Rock.equals(a.get("type"),Xinhu.CHAT_GROUPVAL))keys 	= "" + a.get("type") + "_"+a.get("receid")+"";
			String oii  	= chatobj.get(keys);
			isboolone		= true;
			if(oii==null) {
				initindex();
			}else {
				int i = Integer.parseInt(oii);
				a1 = chatlist.get(i);
				a.put("stotal", (Integer.parseInt(a1.get("stotal")) + 1) + "");
				if (Rock.equals(a.get("type"), Xinhu.CHAT_USERVAL))
					a.put("receid", a.get("sendid"));
				a.put("face", a1.get("face"));
				addchatlist(a, 2);
				totaochat();
				showchatnot.setVisibility(View.GONE);
				downrecord();//下载聊天记录
			}
		}

		//推送过来的应用通知
		if(Rock.equals(pushtype, Xinhu.CHAT_AGENTVAL)){
			String cont = Jiami.base64decode(a.get("cont"));
			final String url = a.get("url");
			final String tit = a.get("title");

			//未激活在前台，由于小米没在前台会自动到任务栏的，所以不需要
			if(!isboolshow) {
				//Xinhu.Notification(mActivity, 0, tit, cont);
			}

			Dialog.confirm(this, cont);
			Dialog.setTitle(tit);
			if(!Rock.isEmpt(url)){
				Dialog.setOkbtn("打开", new CallBack() {
					@Override
					public void back() {
						openurl(tit, url);
					}
				});
			}
			initindex();//已推送就加载
		}
	}



	//删除会话列表上
	private void removechatlist(int oi)
	{
		showviewobj0.removeView(chatview.get(oi));
		chatview.set(oi,null);
		chatlist.set(oi,null);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
	{
		TextView av = (TextView) v.findViewById(R.id.xu);
		int i 		= Integer.parseInt(av.getText().toString());
		Map<String, String> a = chatlist.get(i);
		if(Rock.equals(a.get("iszd"),"1")){
			//menu.add(0, 0, Menu.NONE, "取消置顶");
		}else {
			//menu.add(0, 1, Menu.NONE, "↑置顶");
		}
		menu.add(0, 5, Menu.NONE, "删除此记录");
	}

	//删除历史记录
	private void delhistory()
	{
		Map<String, String> a = chatlist.get(chatindex);
		if(a != null) {
			String type = a.get("type"),receid = a.get("receid");
			Xinhu.ajaxget("reim", "delhistory","type="+type+"&gid="+receid+"",null, what_notmsg);
			Sqlite.delete(SqliteClass.tn_history,"`type`='"+type+"' and `receid`="+receid+"");
			String keys = ""+type+"_"+receid+"";
			chatobj.remove(keys); //清除
		}
		removechatlist(chatindex);
		shownotchat();
	}
	private void shownotchat()
	{
		int i,len=chatview.size(),zoi=0;
		for(i=0;i<len;i++)if(chatview.get(i)!=null)zoi++;
		if(zoi==0){
			showchatnot.setVisibility(View.VISIBLE);
		}else{
			showchatnot.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id==5){
			delhistory();
		}
		//取消置顶
		if(id==0){

		}
		//置顶
		if(id==1){

		}
		return super.onContextItemSelected(item);
	}


	//下载聊天记录
	private void downrecord()
	{
		Xinhu.startService(this, Xinhu.SERVICETYPE_DOWN);
	}
	private void downrecords()
	{
		Map<String,String> lret = Xinhu.DWONLASTMAP;
		//推送过来的
		if(isboolone && lret!=null) {
			if(Rock.equals(lret.get("type"),Xinhu.CHAT_USERVAL) && Rock.equals(lret.get("sendid"),Xinhu.recechatid) ||
					Rock.equals(lret.get("type"),Xinhu.CHAT_GROUPVAL) && Rock.equals(lret.get("receid"),Xinhu.recechatid)) {
				chatyidu();
				//Rock.alert(this, lret.toString());
				Xinhu.sendBroadcast(this, Xinhu.ACTION_CHATRECEMESS);
			}else if(!isboolshow){
				//没有打开才发送通知(非小米)
				//if(!Rock.equals(pushmessagemap.get("ptype"),Xinhu.PUSH_MESSAGE_TYPE_XIAOMI)){
				//Xinhu.Notification(this, R.drawable.xinhu, pushmessagemap.get("name"), Jiami.base64decode(pushmessagemap.get("cont")));
				//}
			}
		}
		isboolone= false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false); //返回不退出
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}












	//------------以下是应用相关的喽----------------------

	//选择卡切换
	private void changenav(int index)
	{
		showviewobj[nowindex].setVisibility(View.GONE);
		navtextview[nowindex].setTextColor(Color.parseColor("#888888"));

		showviewobj[index].setVisibility(View.VISIBLE);
		navtextview[index].setTextColor(Color.parseColor("#1389D3"));

		navtextview[4].setText(titlearr[index]);

		if(index==0){
			navimageview[0].setImageResource(R.drawable.nav_chat1);
			shownotchat();
			//initindex();
		}else{
			navimageview[0].setImageResource(R.drawable.nav_chat0);
			showchatnot.setVisibility(View.GONE);
		}
		if(index==1){
			navimageview[1].setImageResource(R.drawable.nav_ying1);
		}else{
			navimageview[1].setImageResource(R.drawable.nav_ying0);
		}
		if(index==2){
			navimageview[2].setImageResource(R.drawable.nav_user1);
			if(userarr==null)showuserlist();
		}else{
			navimageview[2].setImageResource(R.drawable.nav_user0);
		}
		if(index==3){
			navimageview[3].setImageResource(R.drawable.nav_my1);
			showmylist();
		}else{
			navimageview[3].setImageResource(R.drawable.nav_my0);
		}
		nowindex = index;
	}

	//加载应用
	private List<Map<String, String>> agentarr,userarr = null,deptarr=null,grouparr = null;
	private void showagentlist(String bstr)
	{
		String string = bstr;
		if(Rock.isEmpt(string))string = Sqlite.getOption("agentarr");
		if(Rock.isEmpt(string))return;
		if(!Rock.isEmpt(string) && Rock.equals(oldagentstr, string))return;

		showviewobj1.removeAllViews();
		//Rock.alert(this,string);

		String types = "",str;
		agentarr 	= Json.getJsonArray(string);
		int len 	= agentarr.size(),xu = 0, clumn=4;//几列
		View view	= null;
		agentview.clear();
		int[] vint 	= {R.id.grid0,R.id.grid1,R.id.grid2,R.id.grid3};
		TextView av;
		for(int i=0;i<len; i++){
			Map<String, String> a = agentarr.get(i);
			String typess = a.get("types");
			if(i==0)types = typess;

			if(!Rock.equals(typess, types))xu = 0; //分类不相同
			if(!Rock.equals(typess, types) || i==0){
				View view2 	= Rock.getView(this,R.layout.index_yingtitle);
				av 	= (TextView)view2.findViewById(R.id.title);
				av.setText(typess);
				showviewobj1.addView(view2);
			}

			if(xu==0) {

				view 	= Rock.getView(this,(clumn==3) ? R.layout.index_yinggrid0: R.layout.index_yinggrid1);
				showviewobj1.addView(view);
			}

			LinearLayout lv = (LinearLayout)view.findViewById(vint[xu]);
			View view1 = Rock.getView(this,(clumn==3) ? R.layout.index_ying0:R.layout.index_ying1);
			agentview.add(view1);
			av 	= (TextView)view1.findViewById(R.id.title);
			av.setText(a.get("name"));

			ImageViewXinhu imas = (ImageViewXinhu)view1.findViewById(R.id.icons);
			imas.setPath(a.get("face"));

			str = a.get("stotal");
			av = (TextView) view1.findViewById(R.id.stotal);
			if(!str.equals("0")){
				av.setText(str);
				av.setVisibility(View.VISIBLE);
			}
			final int oi = i;
			view1.findViewById(R.id.item).setOnClickListener(new View.OnClickListener(){
				public void onClick(View v) {
					openargent(oi);
				}
			});;

			lv.addView(view1);


			xu++;
			if(xu==clumn)xu=0;
			types = typess;
		}
		if(!Rock.isEmpt(bstr))Sqlite.setOption("agentarr", bstr);
		oldagentstr = string;
	}
	//点击应用
	private void openargent(int i)
	{
		Map<String, String> a = agentarr.get(i);
		Xinhu.nowagentnum 	= a.get("num");
		Xinhu.nowagentname 	= a.get("name");
		View view = agentview.get(i);
		if(view!=null){
			TextView av = (TextView) view.findViewById(R.id.stotal);
			int ois = Integer.parseInt(a.get("stotal").toString());
			a.put("stotal","0");
			av.setText("0");
			av.setVisibility(View.GONE);
			av = (TextView)findViewById(R.id.nav_yingstotal);
			int oi = Integer.parseInt(av.getText().toString());
			int oi1= oi-ois;
			showshowstotal(oi1+"",0);
		}
		String urls= a.get("url"),url = "",params="token="+Xinhu.admintoken+"&adminid="+Xinhu.adminid+"";
		if(Rock.equals(urls, "link") && !Rock.isEmpt(a.get("urlm"))){
			url = Xinhu.geturl(a.get("urlm"));
			if(Rock.contain(url, "?")){
				url+="&"+params+"";
			}else{
				url+="?"+params+"";
			}
		}else {
			url = Xinhu.APIURL + "?d=we&m=ying&num=" + Xinhu.nowagentnum + "&" + params + "";
		}
		openurl(Xinhu.nowagentname, url);
	}
	//根据编号打开应用
	private void openagent(String id)
	{
		int len 	= agentarr.size(),i=0,oi=-1;
		for(i=0;i<len;i++){
			Map<String, String> a = agentarr.get(i);
			if(Rock.equals(a.get("id"), id)){
				oi = i;
				break;
			}
		}
		if(oi>0){
			openargent(oi);
		}else{
			Rock.Toast(this,"应用Id["+id+"]不存在");
		}
	}

	private void openurl(String name, String url)
	{
		if(Rock.contain(url, "?")){
			url+="&openfrom=androidapp";
		}else{
			url+="?openfrom=androidapp";
		}
		Xinhu.startActivity(this,YingActivity.class, name, url);
	}


	private void showshowstotal(String stotal, int lx)
	{
		int id = R.id.nav_yingstotal;
		if(Integer.parseInt(stotal)<0)stotal="0";
		if(lx==1)id = R.id.nav_chatstotal;
		TextView tv = (TextView)findViewById(id);
		tv.setText(stotal);
		if(Rock.equals(stotal,"0")){
			tv.setVisibility(View.GONE);
		}else{
			tv.setVisibility(View.VISIBLE);
		}
	}

	private void opendingwei()
	{
		String url = ""+Xinhu.APIURL+"?d=we&m=ying&a=daka&token="+Xinhu.admintoken+"&adminid="+Xinhu.adminid+"";
		openurl("考勤打卡",url);
	}



	private PopupMenu popupMenu = null;
	//显示操作菜单this.openOptionsMenu();
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void showaddmenu()
	{
		if(popupMenu!=null)popupMenu.dismiss();
		View v 		= (View)findViewById(R.id.add);
		popupMenu 	= new PopupMenu(this, v);
		Menu menu 	= popupMenu.getMenu();
		menu.add(0,0,0,"刷新");
		//menu.add(0,3,0,"创建会话");
		if(Rock.equals(Xinhu.adminmap.get("iskq"),"1"))menu.add(0,2,0,"＋考勤打卡");
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				onOptionsItemSelected(item);
				return false;
			}
		});
		popupMenu.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		showaddmenu();
		super.onCreateOptionsMenu(menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case 0: //刷新
				if(nowindex==2) {
					Sqlite.setOption("userarr", "");
					Sqlite.setOption("deptarr", "");
					showuserlist();
				}else{
					dialogXinhu.show();
					initindex();
				}
			break;
			case 2://考勤定位
				opendingwei();
			break;
			case 1:
				startSimpleActivity(MapActivity.class);
				break;
			case 3:
				Rock.Toast(this,"待开发");
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}



	//加载联系人
	private void showuserlist()
	{

		String dstr = Sqlite.getOption("deptarr");
		String ustr = Sqlite.getOption("userarr");
		if(Rock.isEmpt(dstr)) {
			dialogXinhu.show();
			Xinhu.ajaxget("dept", "dataapp", myhandler, what_userget);
		}else{
			deptarr = Json.getJsonArray(dstr);
			userarr = Json.getJsonArray(ustr);
			showdeptlist("0");
		}
		/*
		return;

		if(Rock.isEmpt(ustr)) {
			dialogXinhu.show();
			Xinhu.ajaxget("dept", "dataapp", myhandler, what_userget);
		}else{
			showuserlistadd(ustr);
		}*/
	}
	private void showuserlistback(String bstr)
	{
		Map<String,String> a = Json.getJsonObject(bstr);
		Sqlite.setOption("userarr", a.get("userarr"));
		Sqlite.setOption("grouparr", a.get("grouparr"));
		Sqlite.setOption("deptarr", a.get("deptarr"));

		deptarr = Json.getJsonArray(a.get("deptarr"));
		userarr = Json.getJsonArray(a.get("userarr"));
		showdeptlist("0");
		//Rock.alert(this, a.get("deptarr"));
		//showuserlistadd(a.get("userarr"));
		//showdeptlist(a.get("deptarr"), "0");
	}
	//加载到人员列表上
	private void showuserlistadd(String str)
	{
		userarr = Json.getJsonArray(str);
		int i,len=userarr.size();
		showviewobj2.removeAllViews();
		showgrouplist(Sqlite.getOption("grouparr"));
		View view2 	= Rock.getView(this,R.layout.index_yingtitle);
		TextView av = (TextView)view2.findViewById(R.id.title);
		av.setText("联系人("+len+")");
		showviewobj2.addView(view2);

		for(i=0;i<len;i++){
			showuserlistadds(userarr.get(i),i);
		}
	}

	//显示组织结构
	private View  daoveiw;
	private void showdeptlist(String id)
	{
		if(Rock.equals(id, "0"))id = deptarr.get(0).get("id");
		showviewobj2.removeAllViews();
		showgrouplist(Sqlite.getOption("grouparr"));
		View view2 	= Rock.getView(this,R.layout.index_yingtitle);
		TextView av = (TextView)view2.findViewById(R.id.title);
		av.setText("联系人("+userarr.size()+")");
		showviewobj2.addView(view2);

		daoveiw 	= Rock.getView(this,R.layout.index_deptdao);
		showviewobj2.addView(daoveiw);

        showviewobj2.addView(Rock.getView(this,R.layout.line));

		showdeptdao	= (LinearLayout)daoveiw.findViewById(R.id.deptdao);
		showdeptdao.removeAllViews();
        liandaoview.clear();
		showdeptlists(id, 0, -1);
	}
	private void showdeptlists(final String id, int level, int clidao)
	{
		int i,len=deptarr.size();

        for(i=0;i<lianxrview.size();i++)showviewobj2.removeView(lianxrview.get(i));
        lianxrview.clear();
		Map<String,String> nb = null,b;
		for(i=0;i<len;i++){
			b = deptarr.get(i);
			if(Rock.equals(b.get("id"), id))nb = b;
			if(Rock.equals(b.get("pid"), id)){
				showdeptlistadds(b,i);
			}
		}
		String name = nb.get("name");
		TextView at = null;
		if(level==0) {
			at = (TextView) daoveiw.findViewById(R.id.title);
			at.setText(name);
			at.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    showdeptlist("0");
                }
            });
		}else{
            if(clidao>-1)for(i = clidao;i<liandaoview.size();i++)showdeptdao.removeView(liandaoview.get(i));//点击导航时清空原来
            View dview = Rock.getView(this,R.layout.index_depttit);
            liandaoview.add(dview);
            final int aoi = liandaoview.size();

            at = (TextView) dview.findViewById(R.id.title);
            at.setText(name);
            at.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    showdeptlists(id, 1, aoi-1);
                }
            });

            showdeptdao.addView(dview);
		}

		len = userarr.size();
        int lxr=0;
		for(i=0;i<len;i++) {
			b = userarr.get(i);
			if(Rock.equals(b.get("deptid"), id)){
				showuserlistadds(b,i);
                lxr++;
			}
		}
        if(lxr==0){
            View vieno = Rock.getView(this,R.layout.index_user2);
            at = (TextView) vieno.findViewById(R.id.title);
            at.setText(""+name+" 下无联系人");
            lianxrview.add(vieno);
            showviewobj2.addView(vieno);
        }
	}

	//显示组织结构
	private void showdeptlistadds(Map<String,String> a, int i)
	{
		View view 	= Rock.getView(this,R.layout.index_user1);
		String str 	= "";
		TextView av = (TextView) view.findViewById(R.id.title);
		av.setText(a.get("name"));

		av = (TextView) view.findViewById(R.id.titles);
		av.setVisibility(View.VISIBLE);
        String rs = a.get("ntotal");
        String st = rs+"人";
        if(Rock.equals(rs,"0"))st="〉";
		av.setText(st);

		final int oi = i;
		view.findViewById(R.id.chatlist).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				opendept(oi);
			}
		});
        lianxrview.add(view);
		showviewobj2.addView(view);
	}
	private void opendept(int oi)
	{
		showdeptlists(deptarr.get(oi).get("id"), 1, -1);
	}

	//显示组会话
	private void showgrouplist(String str)
	{
		grouparr = Json.getJsonArray(str);
		int i,len=grouparr.size();
		if(len>0){
			View view2 	= Rock.getView(this,R.layout.index_yingtitle);
			TextView av = (TextView)view2.findViewById(R.id.title);
			av.setText("会话("+len+")");
			showviewobj2.addView(view2);
		}
		for(i=0;i<len;i++){
			showgrouplistadds(grouparr.get(i),i);
		}
	}
	//人员详情
	private void showuserlistadds(Map<String,String> a, int i)
	{
		View view 	= Rock.getView(this,R.layout.index_user0);
		String str 	= "";
		TextView av = (TextView) view.findViewById(R.id.title);
		av.setText(a.get("name"));

		av = (TextView) view.findViewById(R.id.titles);
		av.setText(a.get("deptname")+"("+a.get("ranking")+")");

		ImageViewXinhu imgu = (ImageViewXinhu)view.findViewById(R.id.icons);
		imgu.setPath(a.get("face"));
		final int oi = i;
		view.findViewById(R.id.chatlist).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				openuser(oi);
			}
		});
        lianxrview.add(view);
		showviewobj2.addView(view);
	}
	//会话详情
	private void showgrouplistadds(Map<String,String> a, int i)
	{
		View view 	= Rock.getView(this,R.layout.index_user1);
		String str 	= "";
		TextView av = (TextView) view.findViewById(R.id.title);
		av.setText(a.get("name"));

		ImageViewXinhu imgu = (ImageViewXinhu)view.findViewById(R.id.icons);
		imgu.setPath(a.get("face"));
		final int oi = i;
		view.findViewById(R.id.chatlist).setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				opengroup(oi);
			}
		});
		showviewobj2.addView(view);
	}

	//打开联系人详情
	private void openuser(int i)
	{
		Map<String,String> a = userarr.get(i);
		Xinhu.startActivity(this,UserActivity.class, a.get("name"), a.get("id"));
	}

	private void opengroup(int i)
	{
		Map<String,String> a = grouparr.get(i);

		String keys 	= "group_"+a.get("id")+"";
		String oii  	= chatobj.get(keys);
		if(oii==null){
			nowchatindex= -1;
		}else{
			nowchatindex= Integer.parseInt(oii);
		}
		Xinhu.recechatname = a.get("name");
		Xinhu.recechattype = Xinhu.CHAT_GROUPVAL;
		Xinhu.recechatid   = a.get("id"); //人员Id/会话Id
		chatyidu();
		startSimpleActivity(ChatActivity.class);
	}


	//显示我的
	private Boolean myshow = false;
	private void showmylist()
	{
		if(myshow)return;
		myshow		= true;
		View view 	= Rock.getView(this,R.layout.index_my);
		TextView av;
		av = (TextView)view.findViewById(R.id.name);
		av.setText(Xinhu.adminname+"("+Xinhu.adminmap.get("user")+")");

		av = (TextView)view.findViewById(R.id.titles);
		av.setText("部门："+Xinhu.adminmap.get("deptallname")+"\n职位："+Xinhu.adminmap.get("ranking")+"");

		ImageViewXinhu imgu = (ImageViewXinhu)view.findViewById(R.id.icons);
		imgu.setPath(Xinhu.adminmap.get("face"));

		view.findViewById(R.id.exitbtn).setOnClickListener(OnViewClickListener);
		view.findViewById(R.id.mya_clearbtn).setOnClickListener(OnViewClickListener);
		view.findViewById(R.id.mya_aboutbtn).setOnClickListener(OnViewClickListener);
		view.findViewById(R.id.mya_versionbtn).setOnClickListener(OnViewClickListener);
		view.findViewById(R.id.mya_cachebtn).setOnClickListener(OnViewClickListener);
		//view.findViewById(R.id.mya_helpbtn).setOnClickListener(OnViewClickListener);
		view.findViewById(R.id.mya_shatebtn).setOnClickListener(OnViewClickListener);
		showviewobj3.addView(view);
	}

	//退出
	private void exit(int lx)
	{
		if(lx==0) {
			Dialog.confirm(this, "确定要退出系统吗？");
			Dialog.setTitle("退出确认");
			Dialog.setOkbtn("退出", new CallBack() {
				@Override
				public void back() {
					exit(1);
				}
			});
		}
		if(lx==1){
			dialogXinhu.show();
			Xinhu.ajaxget("login","loginexit",myhandler, what_exit);
		}
		if(lx==2){
			Sqlite.setOption("adminid", "");
			Sqlite.setOption("adminname", "");
			Sqlite.setOption("admintoken", "");
			XiaomiApplication.stop(this); //关闭推送
			Xinhu.clearlogin();
			startSimpleActivity(LoginActivity.class);
			finish();
		}
	}

	//分享
	private void gotoshare()
	{
		String ym = Rock.getstring(this, R.string.appgwurl);
		String ann = Rock.getstring(this, R.string.appname);
		Intent intent=new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, ann);
		intent.putExtra(Intent.EXTRA_TEXT, "与你分享【"+ann+"】开源办公系统APP，官网地址：http://"+ym+"/");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, getTitle()));
	}

	//初始化小米推送
	private void initXiaoMi()
	{
		XiaomiApplication.init(this);
	}





	//推送
	private void initPush()
	{
		initXiaoMi();
	}

	/**
	 * 清除WebView缓存
	 */
	private void clearWebViewCache(){

		//清理Webview缓存数据库
		try {
			deleteDatabase("webview.db");
			deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String chedir = getFilesDir().getAbsolutePath();
		RockFile.deleteDirFile(chedir+Xinhu.CACAHE_DIRNAME); //WebView 缓存文件
		RockFile.deleteDirFile(chedir+"/webviewCache"); //删除webview 缓存目录
		RockFile.deleteDirFile(RockFile.getDir("temp"));//删除临时图片
		Rock.Toast(this,"清理完成");
	}
}