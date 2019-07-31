/**
 * 显示表情和图片
 * from http://xh829.com/
 * */

package com.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lib.Rock;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextViewChat extends TextView {

    private Context mContext;
    private String oldString;

    public TextViewChat(Context context) {
        super(context);
    }

    public TextViewChat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewChat(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 获取原始字符串
     * */
    public String getTextOld()
    {
        return oldString;
    }

    public void setTextChat(String str)
    {
        this.setText(str);
    }

    //设置内容匹配表情
    public void setTextChat(Context context,String str, Map<String,String> emotsMap)
    {
        Pattern p   = Pattern.compile("\\[(.*?)\\]");
        Matcher m   = p.matcher(str);
        mContext        = context;
        oldString       = str;
        String neirs    = "",allstr="",oj;
        int j = 0;
        while(m.find()){
            j++;
            neirs   = m.group();
            allstr+=","+neirs+"";
            oj      = emotsMap.get(neirs);
            if(oj!=null)str = str.replace(neirs, "<img src='emots/Expression_"+oj+".png'>");
        }
        if(j==0) {
            this.setText(str);
        }else{
            CharSequence richText = Html.fromHtml(str, assetsImageGetter, null);
            this.setText(richText);
        }
    }

    private Html.ImageGetter assetsImageGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            return loadAssetsimg(mContext, source);
        }
    };

    private Drawable loadAssetsimg(Context context, String path)
    {
        Drawable drawable = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            drawable = new BitmapDrawable(bitmap);
            drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            bitmap = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return drawable;
    }

}