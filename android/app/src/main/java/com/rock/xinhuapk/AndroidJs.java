package com.rock.xinhuapk;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.lib.Rock;
import com.lib.SqliteClass;

import java.util.Map;

/**
 *  定义webview和js交互
 * */
public class AndroidJs{

    private Context mContext;
    private SqliteClass Sqlite;

    private QQLocation qqLocation = null;

    public AndroidJs(Context context, SqliteClass sqlite){
        mContext = context;
        Sqlite   = sqlite;
    }

    @JavascriptInterface
    public void Toast(String name) {
        Rock.Toast(mContext, name);
    }

    @JavascriptInterface
    public void alert(String cont) {
        Dialog.alert(mContext, cont);
    }

    //下载文件
    @JavascriptInterface
    public void openfile(String fileid) {
        DownLoad.openfile(mContext, fileid);
    }

    //定位
    @JavascriptInterface
    public void startLocation(final String fun) {
        qqLocation = new QQLocation(mContext, new CallBack(){
            public void backlocation(Map<String,String> a){
                Locationback(fun, a);
                stopLocation();
            }
        });
        qqLocation.start();
    }

    //定位好了后回调
    public void Locationback(String fun, Map<String,String> a){

    }

    //停止定位
    public void stopLocation()
    {
        if(qqLocation!=null)qqLocation.stop();
        qqLocation = null;
    }

    //拨打电话
    @JavascriptInterface
    public void callPhone(String phone)
    {
        Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
