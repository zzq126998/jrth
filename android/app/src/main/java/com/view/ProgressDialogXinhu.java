/**
 * 自定义加载
 * from http://xh829.com/
 * */

package com.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import android.view.WindowManager;

import com.rock.xinhuapk.R;


public class ProgressDialogXinhu extends ProgressDialog {

    public ProgressDialogXinhu(Context context)
    {
        super(context);
    }

    public ProgressDialogXinhu(Context context, int theme)
    {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init(getContext());

    }

    private Context mContext;


    private void init(Context context)
    {
        mContext = context;
        setCancelable(true);//设置不可取消，点击其他区域不能取消
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.load_dialog);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width    = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height   = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show()
    {
        super.show();
    }
}