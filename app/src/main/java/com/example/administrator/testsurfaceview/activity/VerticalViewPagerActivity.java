package com.example.administrator.testsurfaceview.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.adapter.DummyAdapter;
import com.example.administrator.testsurfaceview.view.FixedSpeedScroller;
import com.example.administrator.testsurfaceview.view.RollViewPager;
import com.example.administrator.testsurfaceview.view.VerticalViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu on 2016/11/17.
 */
public class VerticalViewPagerActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verticalviewpager);
        VerticalViewPager verticalViewPager = (VerticalViewPager) findViewById(R.id.verticalviewpager);
        verticalViewPager.setAdapter(new DummyAdapter(getSupportFragmentManager()));
    }


}
