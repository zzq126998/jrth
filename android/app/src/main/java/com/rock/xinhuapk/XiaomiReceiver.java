package com.rock.xinhuapk;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;

import com.lib.CLog;
import com.lib.Rock;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;




public class XiaomiReceiver extends PushMessageReceiver {

    private String mRegId;
    private String mTopic;
    private String mAlias;
    private String mAccount;
    private String mStartTime;
    private String mEndTime;

    //推送
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

        //String tit   = "message";
        //String cont  = message.getContent();
        //Rock.Toast(context, "w");
        //CLog.debug(cont);
       // Xinhu.Notification(context, 0, tit, cont);
    }

    //点击通知拦
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {


        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }

        Message msg = Message.obtain();
        if (message.isNotified()) {
            msg.obj = "onNotificationMessageClicked";
        }
        XiaomiApplication.getHandler().sendMessage(msg);
    }

    //有收到通知
    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
        //CLog.debug("xiao:"+message.getTitle()+"");
        String content = message.getContent();
        Intent ent = new Intent();
        ent.setAction(Xinhu.ACTION_MESSAGE);
        ent.putExtra("title", message.getTitle());
        ent.putExtra("content", content);
        ent.putExtra(Xinhu.PUSH_MESSAGE_TYPE, Xinhu.PUSH_MESSAGE_TYPE_XIAOMI);
        context.sendBroadcast(ent);

        //Message msg = Message.obtain();
       // msg.obj = "onNotificationMessageArrived："+message.getContent()+"";
        //XiaomiApplication.getHandler().sendMessage(msg);
    }


    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        //CLog.debug("onReceiveRegisterResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = "success";
            } else {
                log = "fail";
            }
        } else {
            log = message.getReason();
        }

        Message msg = Message.obtain();
        msg.obj = log;
        XiaomiApplication.getHandler().sendMessage(msg);
    }

    @SuppressLint("SimpleDateFormat")
    private static String getSimpleDate() {
        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
    }

}