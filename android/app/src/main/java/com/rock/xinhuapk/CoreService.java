/**
 * 服务，用来下载聊天记录
 * from http://xh829.com/
 * 来自：信呼开发团队
 * */

package com.rock.xinhuapk;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import com.lib.CDate;
import com.lib.Json;
import com.lib.Rock;
import com.lib.SqliteClass;
import com.view.HandlerXinhu;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class CoreService extends Service {


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
        Rock.Toast(mContext,"笑死");
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mContext        = this.getApplicationContext();
        //Sqlite          = new SqliteClass(mContext);
        //Rock.Toast(mContext,"开启");

        //注册动态广播
        /*
        IntentFilter filters = new IntentFilter();
        filters.addAction(Intent.ACTION_SCREEN_OFF);
        filters.addAction(Intent.ACTION_SCREEN_ON);
        filters.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(new XinhuReceiver(), filters);*/
        startsss();

        return START_STICKY;
    }

    private void startsss()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Rock.Toast(mContext, CDate.now());
                startsss();
             }
        },5000);
    }

}