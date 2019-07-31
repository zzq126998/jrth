/**
 * 定义一下方法
 * from http://xh829.com/
 * 来自信呼开发团队
 * */

package com.rock.xinhuapk;

import android.app.AlertDialog;

import android.content.Context;

import android.view.View;
import android.widget.TextView;


import com.lib.Rock;



public class Dialog {

    private static View view = null;
    private static AlertDialog dialog = null;

    //自定义alert
    public static void alert(Context context, String cont, String tit, String btnqz, String btnqx,  CallBack queclick,  CallBack qxclick)
    {
        if(dialog!=null){
            dialog.dismiss();
            dialog  = null;
            view    = null;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        view = Rock.getView(context,R.layout.dialog_view0);
        setTitle(tit);
        setContent(cont);
        setOkbtn(btnqz, queclick);
        setCancelbtn(btnqx, qxclick);

        builder.setView(view);
        dialog = builder.show();
    }
    public static void alert(Context context, String cont)
    {
        alert(context, cont, "系统提示", "确定", null,  null, null);
    }


    public static void confirm(Context context, String cont)
    {
        alert(context, cont, "系统提示", "确定", "取消",  null, null);
    }

    //设置确定按钮文字
    public static void setOkbtn(String str, final CallBack queclick)
    {
        TextView tv = (TextView)view.findViewById(R.id.btn_comfirm);
        tv.setText(str);
        view.findViewById(R.id.btn_comfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(queclick!=null)queclick.back();
            }
        });
    }
    public static void setOkbtn(String str)
    {
        setOkbtn(str,null);
    }

    //设置确定取消文字
    public static void setCancelbtn(String str, final CallBack qxclick)
    {
        TextView tv = (TextView)view.findViewById(R.id.btn_cancel);
        if (str==null){
            tv.setVisibility(View.GONE);
        }else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(str);
            view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (qxclick != null) qxclick.back();
                }
            });
        }
    }
    public static void setCancelbtn(String str)
    {
        setCancelbtn(str,null);
    }

    //设置标题
    public static void setTitle(String str)
    {
        TextView tv = (TextView)view.findViewById(R.id.title);
        if(Rock.isEmpt(str)){
            tv.setVisibility(View.GONE);
        }else{
            tv.setVisibility(View.VISIBLE);
            tv.setText(str);
        }
    }

    //设置内容
    public static void setContent(String str)
    {
        TextView tv = (TextView)view.findViewById(R.id.content);
        tv.setText(str);
    }
}