package com.example.administrator.testsurfaceview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Liu on 2016/11/25.
 */
public class HeartView extends View {
    private Paint mPaint;
    private float x, y, r;

    public HeartView(Context context) {
        this(context, null);
    }

    public HeartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.MAGENTA);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        setBackgroundColor(Color.BLACK);
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                r = (float) (Math.PI / 45 + Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j)) * 18);
                x = (float) (r * Math.cos(Math.PI / 45 * j) * Math.sin(Math.PI / 45 * i) + width / 2);
                y = (float) (-r * Math.sin(Math.PI / 45 * j) + height / 2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawOval(x, y, x + 2, y + 2, mPaint);
                }
            }
        }
    }
}
