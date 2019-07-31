package com.rock.xinhuapk;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import android.webkit.WebView;

import com.lib.Rock;

public class YingActivity extends WebActivity {


	/**
	 * 初始化
	 * */
	protected void initCreate() {
		setContentView(R.layout.activity_ying);

		Bundle bundle = this.getIntent().getExtras();
		String name   = bundle.getString("name");
		String url    = bundle.getString("url");
		setTitles(name);

		findViewById(R.id.back).setOnClickListener(this.OnViewClickListener);
		findViewById(R.id.more).setOnClickListener(this.OnViewClickListener);
		webview = (WebView)findViewById(R.id.forum_context);
		gotoUrl(webview, url);
	}

	protected void ViewClick(View v) {
		int id = v.getId();
		if(id == R.id.back){
			finish();
		}

		if(id == R.id.more){
			more();
		}
	}

	private void viewurl()
	{
		final String url = getUrl();
		Dialog.confirm(this, url);
		Dialog.setTitle("当前页网址");
		Dialog.setOkbtn("复制",new CallBack(){
			public void back(){
				Rock.copy(mActivity,url);
				Rock.Toast(mActivity, "网址已复制");
			}
		});
	}

	//更多操作
	private PopupMenu popupMenu = null;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void more()
	{
		if(popupMenu!=null)popupMenu.dismiss();
		View v 		= (View)findViewById(R.id.more);
		popupMenu 	= new PopupMenu(this, v);
		Menu menu 	= popupMenu.getMenu();
		menu.add(0,0,0,"刷新");
		menu.add(0,1,0,"查看网址");
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				onOptionsItemSelected(item);
				return false;
			}
		});
		popupMenu.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		more();
		super.onCreateOptionsMenu(menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case 0: //刷新
				webview.reload();
				break;
			case 1: //
				viewurl();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}


	private void setTitles(String tit)
	{
		TextView view = (TextView) findViewById(R.id.title);
		view.setText(tit);
	}

	protected  void onChangeTitle(String title)
	{
		setTitles(title);
	}
}
