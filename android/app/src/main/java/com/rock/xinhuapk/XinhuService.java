/**
 * 服务，用来下载聊天记录
 * from http://xh829.com/
 * 来自：信呼开发团队
 * */

package com.rock.xinhuapk;


import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.lib.Json;
import com.lib.Rock;
import com.lib.SqliteClass;
import com.view.HandlerXinhu;

import java.util.List;
import java.util.Map;


public class XinhuService extends Service {

    private int maxreid = 0,minreid=999999999,downbool=0,maxloadci = 2,nowlocaci = 0;
    private SqliteClass Sqlite  = null;
    private Context mContext    = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mContext        = this.getApplicationContext();
        Sqlite          = new SqliteClass(mContext);
        Bundle bundle   = intent.getExtras();
        String type     = bundle.getString(Xinhu.SERVICETYPE);
        if (Rock.equals(type, Xinhu.SERVICETYPE_DOWN)){
            downrecordss();//下载聊天记录
        }
        return super.onStartCommand(intent, flags, startId);
    }







    //下载聊天记录
    private void downrecordss()
    {
        if(downbool==1)return;
        downbool    = 1;
        nowlocaci   = 0;
        maxreid		= Integer.parseInt(Sqlite.getOption("maxreid", maxreid+""));
        minreid		= Integer.parseInt(Sqlite.getOption("minreid", minreid+""));
        downrecord();
    }
    private void downrecord()
    {
        Xinhu.ajaxget("reim", "downrecord","maxid="+maxreid+"&minid="+minreid+"",myhandler, 1);
    }
    protected Handler myhandler = new HandlerXinhu(){
        @Override
        public void onSuccess(int what, String str) {
            if(what==1)downrecords(str);
            downbool = 0;
        }
        @Override
        public void onShibai(int what, String str) {
            downbool = 0;
        }
    };
    private void downrecords(String bstr)
    {
        Map<String,String> ret          = Json.getJsonObject(bstr),lret = null;
        List<Map<String, String>> rows  = Json.getJsonArray(ret.get("rows"));
        int len  = rows.size(),i,j;
        for(i=0;i<len;i++) {
            Map<String, String> a = rows.get(i);
            String[] varr = new String[SqliteClass.tn_recordfields.length];
            for (j = 0; j < varr.length; j++) varr[j] = a.get(SqliteClass.tn_recordfields[j]);
            Boolean bo = Sqlite.record(SqliteClass.tn_chatrecord, SqliteClass.tn_recordfields, varr, "");
            lret	 	= a;
            addfilers(a.get("filers"));
        }
        Boolean isdown = Rock.equals(ret.get("isdown"),"1");
        nowlocaci++;
        if(len>0) {
            maxreid	= Integer.parseInt(ret.get("maxid"));
            minreid	= Integer.parseInt(ret.get("minid"));
            Sqlite.setOption("maxreid", ret.get("maxid"));
            Sqlite.setOption("minreid", ret.get("minid"));
            if(isdown && nowlocaci < maxloadci){
                downrecord();   //继续下载
            }
        }
        Xinhu.DWONLASTMAP = lret;
        //发送广播下载过来的
        if(lret != null) {
            Xinhu.sendBroadcast(mContext, Xinhu.ACTION_DOWNBACK);
        }
        if(!isdown)Xinhu.stopService(mContext); //不能下载就停止服务
    }
    //保存文件
    private void addfilers(String fstr)
    {
        if(fstr==null)return;
        Map<String, String> a = Json.getJsonObject(fstr);
        String[] varr = new String[SqliteClass.tn_filefields.length];
        for (int j = 0; j < varr.length; j++){
            varr[j] = a.get(SqliteClass.tn_filefields[j]);
            if(varr[j]==null)varr[j]="";
        }
        Boolean bo = Sqlite.record(SqliteClass.tn_file, SqliteClass.tn_filefields, varr, "");
    }

}