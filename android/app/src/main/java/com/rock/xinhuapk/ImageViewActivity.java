package com.rock.xinhuapk;



import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lib.Rock;
import com.lib.RockActivity;
import com.view.ImageViewXinhu;

import java.io.File;
import java.util.Map;

public class ImageViewActivity extends RockActivity {

	private ImageViewXinhu imageControl;
	private Map<String,String> fMap = null;
	private TextView filestobj;
	private String bstrnot;

	/**
	 * 初始化
	 * */
	protected void initCreate() {

		setContentView(R.layout.activity_imageview);
		imageControl = (ImageViewXinhu)findViewById(R.id.ImageControl);
		findViewById(R.id.back).setOnClickListener(this.OnViewClickListener);
		Bundle bundle = this.getIntent().getExtras();
		String fileid = bundle.getString("url");

		filestobj 		= (TextView) findViewById(R.id.downbtn);

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
		if(fMap!=null) {
			filestobj.setText("原图"+fMap.get("filesizecn")+",下载查看原图");
			String path = fMap.get("filepath");
			imageControl.setPath(fMap.get("thumbpath"));
			if(Rock.equals(fMap.get("thumbpath"), fMap.get("filepath"))){
				filestobj.setVisibility(View.GONE);
			}else{
				findViewById(R.id.downbtn).setOnClickListener(this.OnViewClickListener);
			}
		}
	}

	protected void ViewClick(View v) {
		int id = v.getId();
		if(id == R.id.back){
			finish();
		}
		if(id == R.id.downbtn){
			downfile();
		}
	}


	//开始下载
	private boolean dbool = false;
	private void downfile()
	{
		if(fMap==null || dbool)return;
		if(DownLoad.isexists()){
			openfile();
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
		openfile();
	}

	private void openfile()
	{
		try{
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(fMap.get("downpath"))), "image/*");
			startActivity(intent);
			finish();
		}catch (Exception e){
			Dialog.alert(this, "未知文件不能打开");
		}
	}
}
