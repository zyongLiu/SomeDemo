package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.utils.LogUtils;
import com.example.administrator.testsurfaceview.view.MarqueeView;
import com.example.administrator.testsurfaceview.view.RollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu on 2016/11/17.
 */
public class ScrollActivitiy extends FragmentActivity {
    AccelerateInterpolator accelerateInterpolator;


    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        initView();
    }

    private void initView() {

        MarqueeView marqueeView = (MarqueeView) findViewById(R.id.marqueeView);
        List<String> info = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            info.add(i + "、预计开始时间10月29日19:00，结束时间10月29日23:00，预计累计降雨量50mm。");
        }
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                LogUtils.i(position + textView.getText().toString());
            }
        });
        marqueeView.startWithList(info);
///////////////////////////////////////////////////////////////
        TextView tv = (TextView) findViewById(R.id.tv_scroll);
        tv.setText("1、预计开始时间10月29日19:00，结束时间10月29日23:00，预计累计降雨量50mm。\n" +
                "2、预计开始时间10月29日19:00，结束时间10月29日23:00，预计累计降雨量50mm。\n" +
                "3、预计开始时间10月29日19:00，结束时间10月29日23:00，预计累计降雨量50mm。\n");

        accelerateInterpolator = new AccelerateInterpolator();
        iniHolder();
///////////////////////////////////////////////////////////////////
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rll);

        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        RollViewPager rollViewPager = new RollViewPager(this, strings, new RollViewPager.OnPagerClickCallback() {
            @Override
            public void onPagerClick(int position) {

            }
        });
        rollViewPager.setId(View.generateViewId());
        rollViewPager.startRoll();
        rl.addView(rollViewPager);
    }

    private void iniHolder() {
        Holder.scroll = (ScrollView) findViewById(R.id.scroll);
//        Holder.mlayout = (LinearLayout) findViewById(R.id.l_test);
        Holder.tv_null = (TextView) findViewById(R.id.tv_scroll);
//        Holder.tv_null.setHeight(getWindowManager().getDefaultDisplay()
//                .getHeight() - 50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Holder.scroll.post(ScrollRunnable);
    }

    private Runnable ScrollRunnable = new Runnable() {
        @Override
        public void run() {
//            accelerateInterpolator.getInterpolation()

            int off = Holder.tv_null.getMeasuredHeight()
                    - Holder.scroll.getHeight();// 判断高度,ScrollView的最大高度，就是屏幕的高度
            if (off > 0) {
                Holder.scroll.scrollBy(0, 1);
                //滚动到底端
                if (Holder.scroll.getScrollY() == off) {
//                    Thread.currentThread().interrupt();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Holder.scroll.scrollTo(0, 0);
                    mHandler.postDelayed(this, 3000);
                } else {
                    mHandler.postDelayed(this, 15);
                }
            }
        }
    };

    static class Holder {
        static ScrollView scroll;
        //        static LinearLayout mlayout;
        static TextView tv_null;
    }
}
