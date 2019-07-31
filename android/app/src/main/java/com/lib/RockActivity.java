/**
 * 说明：Activity使用
 * 创建：雨中磐石  from www.xh829.com
 * 时间：2014-02-10
 * 邮箱：qqqq2900@126.com
 * QQ：290802026
 * */


package com.lib;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.view.HandlerXinhu;
import com.view.ProgressDialogXinhu;

import java.util.Timer;
import java.util.TimerTask;

public class RockActivity extends Activity {
	
	protected static Rock rock;
	protected static SqliteClass Sqlite;
	protected final  RockActivity mActivity = this;
	protected static String appnum;
	private int msgtishiwhat 		= 16; //提示框的值

	public TextView msgview 		= null;
	public ProgressDialogXinhu dialogXinhu = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Context context	= getBaseContext();
		Sqlite 		= new SqliteClass(context);
		appnum		= Sqlite.getappnum();
		dialogXinhu = new ProgressDialogXinhu(this);
		initCreate();
	}

	protected void initCreate() {

	}
	
	protected View.OnClickListener OnViewClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			ViewClick(v);
		}
	};
	
	protected void ViewClick(View v) {
		
	}
	
	@Override
	protected void onDestroy(){
    	super.onDestroy();
    }

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}


	protected Handler myhandler = new HandlerXinhu(){
		//不是网络请求的
		public void onNotnet(Message msg){
			if(msg.what==msgtishiwhat){
				msgview.setVisibility(View.GONE);
			}else{
				handleCallback(msg, "");
			}
		}

		//成功后得到数据
		public void onSuccess(int what, String str) {
			Message msg = new Message();
			msg.arg1 = 1;
			msg.what = what;
			handleCallback(msg, str);
		}

		//网络请求失败
		public void onShibai(int what, String str){
			Message msg = new Message();
			msg.arg1 = 0;
			msg.what = what;
			handleCallback(msg, str);
		}
	};
    
    /**
     * 重写的handlecallback
	 * msg 整改对象，bstr返回数据{"code":"200","data":""}bstr就是data上的值
     * */
    protected  void handleCallback(Message msg, String bstr){
    	
    }

    
    //跳转
    protected Intent startSimpleActivity(Class<?> target, int requestCode, int Flags) {
		Intent nIntent = new Intent();
		nIntent.setClass(this, target);
		if(Flags!=-1)nIntent.addFlags(Flags);
		startActivityForResult(nIntent, requestCode);
		return nIntent;
	}
	protected Intent startSimpleActivity(Class<?> target) {
		return startSimpleActivity(target, 0, -1);
	}

	protected Intent startSimpleActivity(Class<?> target, int Flags) {
		return startSimpleActivity(target, 0, Flags);
	}
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
    	ActivityResult(requestCode, resultCode, data);
	}
    
    protected  void ActivityResult(int requestCode, int resultCode, Intent data){
    	
    }

	/**
	 * Runnable定时运行的
	 * runnable = new Runnable() {@Override public void run() {view.setVisibility(View.GONE);}}
	 * time 时间
	 * */
	private Runnable runnablechu = null;
	protected void RunnableTime(Runnable runnable, int time)
	{
		RunnableCancel();
		runnablechu = runnable;
		myhandler.postDelayed(runnable, time);
	}

	protected void RunnableCancel()
	{
		if(runnablechu == null)return;
		myhandler.removeCallbacks(runnablechu);
		runnablechu = null;
	}

	/**
	 * timer定时运行的
	 * */
	protected Timer RunTimer(final int lx, int time)
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				myhandler.sendEmptyMessage(lx);
			}
		}, time);
		return timer;
	}

	private Timer msgtishitime = null;
	public void msgtishi(String txt, String color, int time)
	{
		if(msgtishitime!=null){
			msgtishitime.cancel();
			msgtishitime=null;
		}
		if(msgview == null)return;
		if(Rock.isEmpt(txt)){
			msgview.setVisibility(View.GONE);
			return;
		}
		msgview.setText(txt);
		msgview.setVisibility(View.VISIBLE);
		msgview.setTextColor(Color.parseColor(color));
		msgtishitime = RunTimer(msgtishiwhat, time*1000);
	}
	public void msgtishi(String txt){msgtishi(txt, "#ffffff", 3);}
	public void msgerror(String txt){msgtishi(txt, "#ff0000", 3);}
	public void msgtishi(String txt, String color){msgtishi(txt, color,3);}
	public void msgtishi(String txt, int time){msgtishi(txt, "#ffffff", time);}
}
