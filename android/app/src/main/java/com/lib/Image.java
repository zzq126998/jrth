/**
 * 说明：图片操作
 * 创建：雨中磐石  xh829.com
 * 时间：2014-11-28
 * 邮箱：qqqq2900@126.com
 * QQ：290802026
 * */

package com.lib;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public final class Image {


	/**
	 * 压缩图片为800*480,并保存起来
	 * */
	public static String  squashSize(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		//现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;//这里设置高度为800f
		float ww = 480f;//这里设置宽度为480f
		//缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;//be=1表示不缩放
		if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//设置缩放比例
		//重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		String savepath	= RockFile.getFilename(srcPath).replace(".","_s.");
		String slj		= RockFile.getFilepath(srcPath);
		File out 		= new File(slj, savepath);
		try {
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, new FileOutputStream(out));
			savepath = slj+"/"+savepath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			savepath = srcPath;
		}
		return savepath;
	}
	
}