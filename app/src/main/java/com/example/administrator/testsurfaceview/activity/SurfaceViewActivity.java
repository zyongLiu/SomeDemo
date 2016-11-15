package com.example.administrator.testsurfaceview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.drawer.BaseDrawer;
import com.example.administrator.testsurfaceview.view.DynamicWeatherView;
import com.example.administrator.testsurfaceview.view.MyTestView;


public class SurfaceViewActivity extends AppCompatActivity {

    private DynamicWeatherView dyview;

    private MyTestView mytextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dyview= (DynamicWeatherView) findViewById(R.id.dyview);
        dyview.setDrawerType(BaseDrawer.Type.CLEAR_D);

        mytextview= (MyTestView) findViewById(R.id.mytextview);

    }

    public void onclick(View view){
        mytextview.changeSize((int) (Math.random()*100));
    }

    @Override
    protected void onResume() {
        super.onResume();
        dyview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dyview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dyview.onDestroy();
    }
}
