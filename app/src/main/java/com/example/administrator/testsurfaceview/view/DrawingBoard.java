package com.example.administrator.testsurfaceview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu on 2016/12/2.
 */
public class DrawingBoard extends View {
    private Paint mPaint;

    private float drawPointLastX;
    private float drawPointLastY;

    private float drawPointX;
    private float drawPointY;

    private int mWidth;
    private int mHeight;

    private List<Path> mPaths;

    private Path mPath;

    public DrawingBoard(Context context) {
        this(context, null);
    }

    public DrawingBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawingBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaths = new ArrayList<>();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2f);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLUE);

        int layer = canvas.saveLayer(0, 0, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawColor(Color.DKGRAY);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
        mPaint.setXfermode(null);

        canvas.restoreToCount(layer);


//        mPaint.setColor(Color.DKGRAY);
//
//        if (mPath!=null) {
//            canvas.drawPath(mPath, mPaint);
//        }
//
//        for (Path path:mPaths){
//            canvas.drawPath(path,mPaint);
//        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                drawPointLastX = event.getX();
                drawPointLastY = event.getY();
                mPath.moveTo(drawPointLastX, drawPointLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(event);
                break;
            case MotionEvent.ACTION_UP:
                mPaths.add(mPath);
                break;
        }
        invalidate();

        super.onTouchEvent(event);
        return true;
    }


    private void onMove(MotionEvent event) {
        drawPointX = event.getX();
        drawPointY = event.getY();

        mPath.quadTo((drawPointLastX + drawPointX) / 2,
                (drawPointLastY + drawPointY) / 2
                , drawPointX, drawPointY);

        drawPointLastX = event.getX();
        drawPointLastY = event.getY();

    }
}
