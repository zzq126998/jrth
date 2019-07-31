/**
 * 小米推送
 * from http://www.rockoa.com/
 * 来自信呼开发团队
 * 雨中磐石(rainrock)
 * */

package com.rock.xinhuapk;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;


import com.lib.CLog;
import com.lib.Rock;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * 1、为了打开客户端的日志，便于在开发过程中调试，需要自定义一个 Application。
 * 并将自定义的 application 注册在 AndroidManifest.xml 文件中。<br/>
 * 2、为了提高 push 的注册率，您可以在 Application 的 onCreate 中初始化 push。你也可以根据需要，在其他地方初始化 push。
 *
 * @author rainrock
 */
public class XiaomiApplication{


    private static XiaomiHandler sHandler = null;


    public static void init(Context context)
    {
        if (shouldInit(context)) {
            MiPushClient.registerPush(context, Xinhu.XIAOMI_APP_ID, Xinhu.XIAOMI_APP_KEY);
        }

        if (sHandler == null) {
            sHandler = new XiaomiHandler(context);
        }
    }


    public static void stop(Context context)
    {
        MiPushClient.unregisterPush(context);
    }


    private  static boolean shouldInit(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static XiaomiHandler getHandler() {
        return sHandler;
    }



    public static class XiaomiHandler extends Handler {

        private Context context;

        public XiaomiHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            if (!TextUtils.isEmpty(s)) {

                //连接成功设置别名
                if(Rock.equals(s,"success")){
                    MiPushClient.setAlias(context, Xinhu.admintoken, null);
                }
                if(Rock.equals(s,"fail")){
                    Rock.Toast(context,"小米推送连接失败");
                }
            }
        }
    }
}