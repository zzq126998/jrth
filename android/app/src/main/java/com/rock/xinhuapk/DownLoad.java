/**
 * 使用系统下载器下载文件
 * from http://xh829.com/
 * 来自信呼开发团队
 * 创建时间：2017-03-22
 * */

package com.rock.xinhuapk;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;

import com.lib.A;
import com.lib.CLog;
import com.lib.Json;
import com.lib.Rock;
import com.lib.RockFile;
import com.lib.SqliteClass;
import com.view.HandlerXinhu;

import java.io.File;
import java.util.Map;

public class DownLoad {

    private static Context mContext;
    private static SqliteClass Sqlite;
    private static CallBack callBack;

    private static Map<String,String> fMap  = null;

    /**
     * 打开文件详情页
     * */
    public static void openfile(Context context,String fileid)
    {
        Xinhu.startActivity(context,FileActivity.class, "文件", fileid);
    }

    public static void init(Context context, SqliteClass sqlite)
    {
        mContext    = context;
        Sqlite      = sqlite;
    }


    /**
     * 获取当前的数据
     * */
    public static Map<String,String> getfMap()
    {
        return fMap;
    }

    /**
     * 下载发起
     * */
    public static void start(Context context, SqliteClass sqlite, String fileid, CallBack calls)
    {
        mContext    = context;
        Sqlite      = sqlite;
        callBack    = calls;
        initfMap(fileid);
        if(fMap==null) {
            Xinhu.ajaxget("file", "getfile", "id=" + fileid + "", myhandlers, 1);
        }else{
            if(callBack!=null)callBack.back();
        }
    }

    public static void initfMap(String fileid)
    {
        String fields   = "`id`,`downpath`,`filepath`,`thumbpath`,`filename`,`filesizecn`,`filetype`,`optname`,`adddt`,`fileext`";
        String[]frs     = Sqlite.getone(SqliteClass.tn_file, "`id`="+fileid+"", fields);
        fMap            = null;
        if(frs==null) {
        }else{
            fMap = Sqlite.getmap(frs, fields);
        }
    }

    /*
    * 判断下载的文件是否存在了
    * */
    public static Boolean isexists()
    {
        Boolean bo = false;
        String dpath = fMap.get("downpath");
        String fileid = fMap.get("id");
        if(Rock.isEmpt(dpath)){
            dpath = RockFile.getDir("downfile")+"/"+fileid+"_"+fMap.get("filename");
        }
        File f=new File(dpath);
        bo = f.exists();
        if(bo){
            fMap.put("downpath", dpath);
        }else{
            dpath = "";
            fMap.put("downpath", "");
        }
        Sqlite.update(SqliteClass.tn_file,"`downpath`='"+dpath+"'", "`id`="+fileid+"");
        return bo;
    }

    /*
    * 判断下载的文件是否存在了,返回路径
    * */
    public static String isexists(String fileid,String filename)
    {
        String  dpath = RockFile.getDir("downfile")+"/"+fileid+"_"+filename;
        File f  = new File(dpath);
        if(f.exists()){
            return dpath;
        }else {
            return "";
        }
    }

    /*
    public static void downfinish()
    {
        Dialog.confirm(mContext, "文件["+fMap.get("filename")+"]已下载到/"+ A.APPPAGE+"/downfile下，是否打开它？");
        Dialog.setOkbtn("打开", new CallBack() {
            @Override
            public void back() {

            }
        });
    }*/

    /**
     * 开始下载
     * */
    public static void startdown(final CallBack calls)
    {
        if(fMap==null)return;
        final String fileid   = fMap.get("id");
        String filepath = fMap.get("filepath");
        String filename = fileid+"_"+fMap.get("filename");
        String url = Xinhu.getapiurl("file","down")+"&id="+fileid+"";
        String ext = RockFile.getExt(filepath);
        if(!Rock.equals(ext, "uptemp")){
            url     = ""+Xinhu.APIURL+""+filepath+"";
        }
        String dspath = ""+ RockFile.getDir("downfile",1)+"/"; //保存路径
        final String downpath = ""+RockFile.sdPath()+""+dspath+""+filename+"";
        fMap.put("downpath", downpath);
        CLog.debug(url+","+downpath);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDestinationInExternalPublicDir(dspath, filename);//指定下载路径和下载文件
        request.setVisibleInDownloadsUi(true);// 设置为可见和可管理
        DownloadManager downloadManager= (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);//获取下载管理器
        final long refernece = downloadManager.enqueue(request);//将下载任务加入下载队列，否则不会进行下载

        // 注册广播接收器，当下载完成时自动安装
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (refernece == myDwonloadID) {
                    mContext.unregisterReceiver(this);
                    isexists();
                    calls.back();
                }
            }
        };
        mContext.registerReceiver(receiver, filter);
    }

    protected static Handler myhandlers = new HandlerXinhu(){
        public void onSuccess(int what, String str) {
            savefilerecord(str);
            if(callBack!=null)callBack.back();
        }
        public void onShibai(int what, String str) {
            if(callBack!=null)callBack.backnot(str);
        }
    };

    public static void savefilerecord(String fstr)
    {
        Map<String, String> a = Json.getJsonObject(fstr);
        String[] varr = new String[SqliteClass.tn_filefields.length];
        for (int j = 0; j < varr.length; j++){
            varr[j] = a.get(SqliteClass.tn_filefields[j]);
            if(varr[j]==null)varr[j]="";
        }
        Boolean bo = Sqlite.record(SqliteClass.tn_file, SqliteClass.tn_filefields, varr, "");
        fMap       = a;
    }

    /**
     * 打开图片预览
     * */
    public static void openimageview(String fileid)
    {
        initfMap(fileid);
        boolean bo  = false;
        if(fMap!=null){
            if(isexists()){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(fMap.get("downpath"))), "image/*");
                mContext.startActivity(intent);
                bo = true;
            }
        }
        if(!bo){
            Xinhu.startActivity(mContext,ImageViewActivity.class, "imageview", fileid);
        }
    }
}