/**
 * 图片查看
 * from http://xh829.com/
 * */

package com.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ImageControl extends ImageView {
	public ImageControl(Context context) {
		super(context);
	}

	public ImageControl(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}