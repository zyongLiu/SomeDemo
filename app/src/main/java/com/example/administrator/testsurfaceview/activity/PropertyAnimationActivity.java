package com.example.administrator.testsurfaceview.activity;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.testsurfaceview.R;

/**
 * Created by Liu on 2016/11/24.
 */
public class PropertyAnimationActivity extends Activity {
    private Button btn_change;
    private TextView tv_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_property);
        initView();
    }

    private void initView() {
        btn_change = (Button) findViewById(R.id.btn_change);
        tv_change = (TextView) findViewById(R.id.tv_change);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                tv_change.animate().alpha(0).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(5000).start();
                paowuxian(tv_change);
            }
        });
    }

    /**
     * 抛物线
     *
     * @param mBlueBall
     */
    public void paowuxian(final View mBlueBall) {

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue) {
                // x方向200px/s ，则y方向0.5 * 10 * t
                //重点位置
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                mBlueBall.setX(point.x);
                mBlueBall.setY(point.y);

            }
        });
    }
}
