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

    private BaseDrawer.Type[] types = new BaseDrawer.Type[]{
            BaseDrawer.Type.DEFAULT, BaseDrawer.Type.CLEAR_D, BaseDrawer.Type.CLEAR_N, BaseDrawer.Type.RAIN_D,
            BaseDrawer.Type.RAIN_N, BaseDrawer.Type.SNOW_D, BaseDrawer.Type.SNOW_N, BaseDrawer.Type.CLOUDY_D, BaseDrawer.Type.CLOUDY_N,
            BaseDrawer.Type.OVERCAST_D, BaseDrawer.Type.OVERCAST_N, BaseDrawer.Type.FOG_D, BaseDrawer.Type.FOG_N,
            BaseDrawer.Type.HAZE_D, BaseDrawer.Type.HAZE_N, BaseDrawer.Type.SAND_D, BaseDrawer.Type.SAND_N,
            BaseDrawer.Type.WIND_D, BaseDrawer.Type.WIND_N, BaseDrawer.Type.RAIN_SNOW_D, BaseDrawer.Type.RAIN_SNOW_N,
            BaseDrawer.Type.UNKNOWN_D, BaseDrawer.Type.UNKNOWN_N};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dyview = (DynamicWeatherView) findViewById(R.id.dyview);
        dyview.setDrawerType(BaseDrawer.Type.CLEAR_D);

        mytextview = (MyTestView) findViewById(R.id.mytextview);

        dyview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dyview.setDrawerType(types[(int) (Math.random() * types.length)]);
            }
        });

    }

    public void onclick(View view) {
        mytextview.changeSize((int) (Math.random() * 100));
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
