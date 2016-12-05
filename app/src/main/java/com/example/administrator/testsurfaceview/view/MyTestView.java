package com.example.administrator.testsurfaceview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/10/25.
 */
public class MyTestView extends View {
    private Paint mPaint;

    public MyTestView(Context context) {
        this(context, null);
    }


    public MyTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 改变字体大小
     *
     * @param size
     */
    public void changeSize(int size) {
        mPaint.setTextSize(size);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /** 绘制FontMetrics对象的各种线 */
        mPaint.reset();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(80);
        // FontMetrics对象
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        String text = "abcdefg测试aa";
        // 计算每一个坐标
        float textWidth = mPaint.measureText(text);
        float baseX = 30;
        float baseY = 700;
        float topY = baseY + fontMetrics.top;
        float ascentY = baseY + fontMetrics.ascent;
        float descentY = baseY + fontMetrics.descent;
        float bottomY = baseY + fontMetrics.bottom;

        //        top: -84.49219
        //        ascent: -74.21875
        //        descent: 19.53125
        //        bottom: 21.679688

        Log.i("top", fontMetrics.top + "");
        Log.i("ascent", fontMetrics.ascent + "");
        Log.i("descent", fontMetrics.descent + "");
        Log.i("bottom", fontMetrics.bottom + "");
        // 绘制文本
        Log.i("text", text.length() + ";" + mPaint.getTextSize() + ";" + mPaint.getFontSpacing());

        mPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, baseX, baseY, mPaint);

//        canvas.drawLine(mPaint.getFontSpacing()*text.length(),0,mPaint.getFontSpacing()*text.length(),0,);

        // BaseLine描画
        mPaint.setColor(Color.RED);
        canvas.drawLine(baseX, baseY, baseX + textWidth, baseY, mPaint);
        mPaint.setTextSize(20);
        canvas.drawText("base:" + baseY, baseX + textWidth, baseY, mPaint);
        // Base描画
        canvas.drawCircle(baseX, baseY, 5, mPaint);
        // TopLine描画
        mPaint.setColor(Color.LTGRAY);
        canvas.drawLine(baseX, topY, baseX + textWidth, topY, mPaint);
        canvas.drawText("top:" + topY, baseX + textWidth, topY, mPaint);
        // AscentLine描画
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(baseX, ascentY, baseX + textWidth, ascentY, mPaint);
        canvas.drawText("ascent:" + (ascentY + 10), baseX + textWidth, ascentY + 10, mPaint);
        // DescentLine描画
        mPaint.setColor(Color.YELLOW);
        canvas.drawLine(baseX, descentY, baseX + textWidth, descentY, mPaint);
        canvas.drawText("descent:" + descentY, baseX + textWidth, descentY, mPaint);
        // ButtomLine描画
        mPaint.setColor(Color.MAGENTA);
        canvas.drawLine(baseX, bottomY, baseX + textWidth, bottomY, mPaint);
        canvas.drawText("buttom:" + (bottomY + 10), baseX + textWidth, bottomY + 10, mPaint);


        canvas.drawLine(0, 100, 200, 100, mPaint);
        canvas.drawText("呵呵", 0, 100 + mPaint.getTextSize() / 2, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i("mytextview", "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("mytextview", "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i("mytextview", "onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }
}
