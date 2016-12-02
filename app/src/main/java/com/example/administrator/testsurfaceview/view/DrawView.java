package com.example.administrator.testsurfaceview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Liu on 2016/12/1.
 */
public class DrawView extends View {

    private int mRarius = 0;

    private Paint mXfermodePaint;

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mXfermodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();

        mRarius = (mWidth > mHeight) ? (mHeight / 4) : (mWidth / 4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        Bitmap topBitmap = drawTopCircle();
        Bitmap bottomBitmap = drawBottomRect();

        int saveLayer = canvas.saveLayer(0, 0, mRarius * 4, mRarius * 4, null, Canvas.ALL_SAVE_FLAG);

        canvas.drawBitmap(bottomBitmap, mRarius, mRarius, mXfermodePaint);

        mXfermodePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));

        canvas.drawBitmap(topBitmap, 0, 0, mXfermodePaint);

        mXfermodePaint.setXfermode(null);

        canvas.restoreToCount(saveLayer);
    }

    private Bitmap drawTopCircle() {
        mXfermodePaint.setColor(Color.GREEN);
        Bitmap bitmap = Bitmap.createBitmap(2 * mRarius, 2 * mRarius, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bitmap);
        c.drawCircle(mRarius, mRarius, mRarius, mXfermodePaint);
        return bitmap;
    }

    private Bitmap drawBottomRect() {
        mXfermodePaint.setColor(Color.YELLOW);
        Bitmap bitmap = Bitmap.createBitmap(2 * mRarius, 2 * mRarius, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bitmap);
        c.drawRect(0, 0, 2 * mRarius, 2 * mRarius, mXfermodePaint);
        return bitmap;
    }
}
