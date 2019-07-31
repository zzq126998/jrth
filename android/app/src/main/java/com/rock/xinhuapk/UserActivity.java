/**
 * 人员详细信息
 * from http://xh829.com/
 * 来自信呼开发团队
 * */

package com.rock.xinhuapk;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lib.Json;
import com.lib.Rock;
import com.lib.RockActivity;
import com.view.ImageViewXinhu;

import java.util.List;
import java.util.Map;

public class UserActivity extends RockActivity {



	private Map<String, String> my;

	/**
	 * 初始化
	 * */
	protected void initCreate() {
		setContentView(R.layout.activity_user);

		Bundle bundle = this.getIntent().getExtras();
		String name   = bundle.getString("name");
		String id     = bundle.getString("url");
		setTitles(name);

		findViewById(R.id.back).setOnClickListener(this.OnViewClickListener);
		findViewById(R.id.btn).setOnClickListener(this.OnViewClickListener);
		findViewById(R.id.telbtn).setOnClickListener(this.OnViewClickListener);
		findViewById(R.id.mobilebtn).setOnClickListener(this.OnViewClickListener);

		TextView av = (TextView) findViewById(R.id.name);
		av.setText(name);

		List<Map<String, String>> userarr = Json.getJsonArray(Sqlite.getOption("userarr"));
		int i,len=userarr.size();
		Map<String, String> a = null;
		for (i=0;i<len;i++){
			a= userarr.get(i);
			if(Rock.equals(a.get("id"), id)){
				my = a;
				break;
			}
		}
		if(my!=null)showInfor(my);
	}

	private void showInfor(Map<String, String> a)
	{
		TextView av = (TextView) findViewById(R.id.name);
		av.setText(a.get("name"));


		av = (TextView) findViewById(R.id.titles);
		av.setText("部门："+a.get("deptallname")+"\n职位："+a.get("ranking")+"");

		ImageViewXinhu imgu = (ImageViewXinhu)findViewById(R.id.icons);
		imgu.setPath(a.get("face"));

		av = (TextView) findViewById(R.id.sex);
		av.setText(a.get("sex"));

		av = (TextView) findViewById(R.id.tel);
		av.setText(a.get("tel"));

		av = (TextView) findViewById(R.id.mobile);
		av.setText(a.get("mobile"));

		av = (TextView) findViewById(R.id.email);
		av.setText(a.get("email"));
	}


	protected void ViewClick(View v) {
		int id = v.getId();
		if(id == R.id.back){
			finish();
		}

		if(id == R.id.btn){
			sendxiaoxi();
		}

		if(id == R.id.telbtn){
			callmobile("tel");
		}
		if(id == R.id.mobilebtn){
			callmobile("mobile");
		}
	}

	//发消息
	private void sendxiaoxi()
	{
		if(my==null)return;
		Xinhu.recechatname = my.get("name");
		Xinhu.recechattype = Xinhu.CHAT_USERVAL;
		Xinhu.recechatid   = my.get("id"); //人员Id/会话Id
		startSimpleActivity(ChatActivity.class);
	}


	private void setTitles(String tit)
	{
		TextView view = (TextView) findViewById(R.id.title);
		view.setText(tit);
	}


	/**
	 * 调用拨号界面
	 * @param phone 电话号码
	 */
	private void call(String phone) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void callmobile(String str)
	{
		if(my==null)return;
		String phone = my.get(str);
		if(Rock.isEmpt(phone))return;
		call(phone);
	}
}
