/**
 * 自定义图片的，可自动缓存到本地
 * from http://xh829.com/
 * */

package com.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;


import com.lib.A;
import com.lib.Json;
import com.lib.Rock;
import com.lib.RockFile;
import com.rock.xinhuapk.Xinhu;

import java.util.Map;

public class ImageViewXinhu extends ImageView {

	private String nowpath;

	public ImageViewXinhu(Context context) {
		super(context);
	}

	public ImageViewXinhu(Context context, AttributeSet attrs,
						  int defStyle) {
		super(context, attrs, defStyle);
	}

	public ImageViewXinhu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	/**
	 * 设置图片地址
	 * */
	public void setPath(String face)
	{
		nowpath		  = face;
		Bitmap bitmap = Xinhu.downface(nowpath, myhandlers, 1);
		if(bitmap != null){
			this.setImageBitmap(bitmap);
		}
	}

	private void setPaths(String str)
	{
		Map<String, String> b = Json.getJsonObject(str);
		String nface 	   = RockFile.saveFiles(b.get("result"), b.get("path"));
		if(!Rock.isEmpt(nface)){
			Bitmap bitmap   = BitmapFactory.decodeFile(nface);
			this.setImageBitmap(bitmap);
		}
	}

	protected Handler myhandlers = new HandlerXinhu(){
		public void onSuccess(int what, String str) {
			setPaths(str);
		}
	};
}