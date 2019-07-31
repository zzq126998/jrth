/**
 * 显示Handler
 * from http://xh829.com/
 * 来自信呼开发团队
 * */

package com.view;


import android.os.Handler;
import android.os.Message;

import com.lib.A;
import com.lib.Json;
import com.lib.Rock;
import com.rock.xinhuapk.Xinhu;

import java.util.Map;


public class HandlerXinhu extends Handler {

    public void handleMessage(Message msg) {
        int what = msg.what,arg1 = msg.arg1,arg2 = msg.arg2, ajacode = 0;
        String ajaxdata 	= "";
        if(arg2 == A.HTTPB_BACKCODE){
            String resultstr = msg.getData().get("result").toString();
            if (arg1 == A.HTTPB_SUCCESS) {
                Map<String, String> ret;
                ret         = Json.getJsonObject(resultstr);
                String code = ret.get("code");
                Xinhu.interajaxresult = ret.get("data");
                if(Rock.equals(code, "200")){
                    ajaxdata = ret.get("data");
                    ajacode	 = 1;
                    onSuccess(what, ajaxdata);
                }else{
                    ajaxdata = ret.get("msg");
                    if(ajaxdata==null)ajaxdata = resultstr;
                }
            } else {
                ajaxdata     = "ERR" + what + "：" + resultstr;
            }
            if(ajacode==0){
                onShibai(what, ajaxdata);
            }
        }else{
            onNotnet(msg);
        }
    }

    //不是网络请求
    public void onNotnet(Message msg)
    {

    }

    //成功后得到数据
    public void onSuccess(int what, String str)
    {

    }


    //成功后得到数据
    public void onShibai(int what, String str)
    {

    }
}