package com.example.administrator.testsurfaceview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.example.administrator.testsurfaceview.utils.LogUtils;

/**
 * Created by Liu on 2016/11/8.
 */
public class MyTouchViewGroupFirst extends RelativeLayout{
    public MyTouchViewGroupFirst(Context context) {
        this(context,null);
    }

    public MyTouchViewGroupFirst(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTouchViewGroupFirst(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtils.i("onInterceptTouchEvent");
//        return super.onInterceptTouchEvent(ev);
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        LogUtils.i("dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.i("onTouchEvent");
        return super.onTouchEvent(event);
    }
}
