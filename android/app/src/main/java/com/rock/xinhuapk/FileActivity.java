/**
 * 人员详细信息
 * from http://xh829.com/
 * 来自信呼开发团队
 * */

package com.rock.xinhuapk;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;


import com.lib.Rock;
import com.lib.RockActivity;

import java.io.File;
import java.util.Map;


public class FileActivity extends RockActivity {

	private Map<String,String> fMap = null;
	private TextView filestobj;
	private String bstrnot;

	/**
	 * 初始化
	 * */
	protected void initCreate() {
		setContentView(R.layout.activity_file);
		fMap		  = null;
		Bundle bundle = this.getIntent().getExtras();
		String name   = bundle.getString("name");
		String fileid = bundle.getString("url");
		setTitles(name);
		showfile();
		findViewById(R.id.back).setOnClickListener(this.OnViewClickListener);
		findViewById(R.id.filestatus).setOnClickListener(this.OnViewClickListener);
		filestobj 		= (TextView) findViewById(R.id.filestatus);

		DownLoad.start(this,Sqlite,fileid,new CallBack(){
			public void back(){
				fMap = DownLoad.getfMap();
				myhandler.sendEmptyMessage(1);
			}
			public void backnot(String str){
				bstrnot = str;
				myhandler.sendEmptyMessage(2);
			}
		});
	}
	protected void ViewClick(View v) {
		int id = v.getId();
		if(id == R.id.back){
			finish();
		}

		if(id == R.id.filestatus){
			downfile();
		}
	}
	private void setTitles(String tit)
	{
		TextView view = (TextView) findViewById(R.id.title);
		view.setText(tit);
	}

	protected  void handleCallback(Message msg, String bstr){
		int what = msg.what;
		if(what==1){
			showfile();
		}
		if(what==2){
			filestobj.setText(bstrnot);
			filestobj.setTextColor(Color.RED);
		}
	}

	private void showfile(){
		TextView tv = (TextView) findViewById(R.id.fileext);
		if(fMap==null) {
			tv.setText("");
		}else{
			tv.setText(fMap.get("fileext"));
		}

		tv = (TextView) findViewById(R.id.filename);
		if(fMap==null) {
			tv.setText("");
		}else{
			tv.setText(fMap.get("filename"));
		}

		tv = (TextView) findViewById(R.id.fileinfor);
		if(fMap==null){
			tv.setText("");
		}else {
			tv.setText("发送者："+fMap.get("optname")+"，大小："+fMap.get("filesizecn")+"\n时间："+fMap.get("adddt")+"");
			changestatus();
		}
	}

	private void changestatus()
	{
		fMap = DownLoad.getfMap();
		if(DownLoad.isexists()){
			filestobj.setText("已下载，点我打开");
			filestobj.setTextColor(getResources().getColor(R.color.darkgreen));
			TextView view = (TextView) findViewById(R.id.filepath);
			view.setText(fMap.get("downpath"));
		}else{
			filestobj.setText("未下载，点我下载");
			filestobj.setTextColor(getResources().getColor(R.color.red));
		}

	}

	//开始下载
	private boolean dbool = false;
	private void downfile()
	{
		if(fMap==null || dbool)return;
		if(DownLoad.isexists()){
			try{
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(new File(fMap.get("downpath"))), fMap.get("filetype"));
				startActivity(intent);
			}catch (Exception e){
				Dialog.alert(this, "未知文件不能打开");
			}
			return;
		}
		dbool=true;
		filestobj.setText("下载中...");
		filestobj.setTextColor(Color.parseColor("#ff6600"));
		DownLoad.startdown(new CallBack(){
			public void back(){
				downfileok();
			}
		});
	}
	//下载完成回调
	private void downfileok()
	{
		dbool= false;
		changestatus();
	}
}
