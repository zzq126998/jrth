/**
 * 显示ScrollView,带聆听滚动条
 * from http://xh829.com/
 * 来自信呼开发团队
 * */

package com.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollViewXinhu extends ScrollView {

    private ScrollListener scrollListener;

    public ScrollViewXinhu(Context context) {
        super(context);
    }

    public ScrollViewXinhu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewXinhu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(scrollListener != null){
            scrollListener.onScrollChanged(l,t,oldl,oldt,computeVerticalScrollRange());
        }
    }

    public void setScrollViewListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    /**
     * 滑动监听器
     */
    public interface ScrollListener {
        void onScrollChanged(int x, int y, int oldx, int oldy,int computeVerticalScrollRange);
    }


}