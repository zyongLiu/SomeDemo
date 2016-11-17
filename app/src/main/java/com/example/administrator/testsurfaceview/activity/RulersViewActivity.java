package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.view.MnScaleBar;
import com.example.administrator.testsurfaceview.view.RulersView;
import com.sunfusheng.marqueeview.MarqueeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu on 2016/11/15.
 */
public class RulersViewActivity extends Activity{
    private RulersView rulersView;
    private TextView text;
    private MnScaleBar scale_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rulersview);
        initView();
    }

    private void initView() {
        rulersView= (RulersView) findViewById(R.id.rulersView);
        rulersView.setBigandSmallCount(5,10);
        rulersView.setRulersValueFormatter(new RulersView.RulersValueFormatter() {
            @Override
            public String getFormattedValue(int value) {
                return "第"+value+"天";
            }
        });

        scale_bar = (MnScaleBar)findViewById(R.id.scale_bar);
        text = (TextView)findViewById(R.id.text);

        scale_bar.setOnScrollListener(new MnScaleBar.OnScrollListener() {
            @Override
            public void onScrollScale(int scale) {
                text.setText("身高："+scale+"cm");
            }
        });
    }
}
