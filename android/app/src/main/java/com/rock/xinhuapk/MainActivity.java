package com.rock.xinhuapk;



import android.content.Intent;
import android.os.Message;
import android.view.WindowManager;
import android.widget.TextView;

import com.lib.A;
import com.lib.CDate;
import com.lib.Rock;
import com.lib.RockActivity;
import com.view.ImageViewXinhu;

public class MainActivity extends RockActivity {

	/**
	 * 初始化
	 * */
	protected void initCreate() {
		setContentView(R.layout.activity_main);
		Xinhu.APIURL    = Sqlite.getOption("apiurl", Xinhu.APIURL);

		String appname	= Rock.getstring(this, R.string.appname);

		String apptitle = Sqlite.getOption("apptitle", appname);

		TextView at = (TextView) findViewById(R.id.appnameval);
		at.setText(apptitle);
		setface();


		String footer = "Copyright ©"+ CDate.now("Y","")+" "+Rock.getstring(this, R.string.appname)+" "+Rock.getstring(this, R.string.appgwurl)+"";
		at = (TextView) findViewById(R.id.banquan);
		at.setText(footer);

		//设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		RunTimer(15, 1000);//启动页面1秒就好了
		//openmain();
	}

	protected  void handleCallback(Message msg, String bstr){
		if(msg.what==15){
			openmain();
		}
	}

	private void openmain()
	{
		String uid = Sqlite.getOption("adminid");
		if(!Rock.isEmpt(uid)){
			startSimpleActivity(IndexActivity.class, Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		}else {
			startSimpleActivity(LoginActivity.class);
		}
		finish();
	}

	private void setface()
	{
		ImageViewXinhu imga = (ImageViewXinhu)findViewById(R.id.myicons);
		imga.setPath("images/logo.png");
	}
}
