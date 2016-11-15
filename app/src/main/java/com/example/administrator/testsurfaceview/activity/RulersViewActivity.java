package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.view.RulersView;

/**
 * Created by Liu on 2016/11/15.
 */
public class RulersViewActivity extends Activity{
    private RulersView rulersView;
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
    }
}
