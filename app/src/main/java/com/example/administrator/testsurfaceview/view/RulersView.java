package com.example.administrator.testsurfaceview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.utils.LogUtils;


/**
 * 自定义刻度尺
 * Created by Liu on 2016/10/28.
 */
public class RulersView extends View implements GestureDetector.OnGestureListener,ScaleGestureDetector.OnScaleGestureListener{
    //画线的笔
    private Paint mPaint;
    //写标注的笔
    private TextPaint mTextPaint;


    //一大格占多少小格
    private int smallSpace = 24;

    private float mWidth;
    private float mHeight;
    //刻度高度
    private float rulersHeight;
    //小刻度的距离
    private float smallDistance;
    //大刻度的距离 smallDistance*smallSpace
    private float bigDistance;
    //小刻度宽度
    private float smallWidth = 20;
    //大刻度宽度
    private float bigWidth = 30;
    //大刻度的个数
    private int bigCount=7;
    //雨块宽度
    private float rainWidth=50;
    //雨块颜色
    private int rainBolckColor;
    //标注字体大小
    private int textSize;

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    private OnRainChunkClickListener onRainChunkClickListener;


    //顶点的坐标
    private float topPointX;
    private float topPointY=10;
    private float scaledRatio=1;



    public interface OnRainChunkClickListener{
        void onClick(float f);
    }

    public RulersView(Context context) {
        this(context,null);
    }

    public RulersView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RulersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.rulers);
        rainBolckColor=a.getColor(R.styleable.rulers_blockColor,Color.BLUE);
        textSize=a.getInteger(R.styleable.rulers_textSize,25);
        a.recycle();
        init(context);
    }


    public void setOnRainChunkClickListener(OnRainChunkClickListener onRainChunkClickListener){
        this.onRainChunkClickListener=onRainChunkClickListener;
    }

    private void init(Context context) {
        mPaint=new Paint();
        mPaint.setColor(Color.BLACK);

        mTextPaint=new TextPaint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);

        mGestureDetector=new GestureDetector(context,this);
        mScaleGestureDetector=new ScaleGestureDetector(context,this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth=MeasureSpec.getSize(widthMeasureSpec);
        mHeight=MeasureSpec.getSize(heightMeasureSpec);
        rulersHeight = mHeight -10*2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //根据总长度求出一些常亮

        bigDistance=rulersHeight/bigCount;
        smallDistance=bigDistance/smallSpace;

        topPointX=mWidth/2;
        //初始状态为10 ？
//        topPointY=10;

        //画竖线
        mPaint.setTextSize(18F);
        canvas.drawLine(topPointX,topPointY,topPointX, topPointY+rulersHeight,mPaint);
        //画大刻度
        for (int i=0;i<=bigCount;i++){
            canvas.drawLine(topPointX-bigWidth,topPointY+i*bigDistance,topPointX,topPointY+i*bigDistance,mPaint);
        }
        //画标注
        for (int i=0;i<=bigCount;i++){
            canvas.drawText("11/"+(i+1)+"",
                    topPointX-bigWidth-smallWidth/2,
                    topPointY+i*bigDistance+mPaint.getTextSize()/2,mTextPaint);
        }
        //画小刻度
        for (int i=0;i<bigCount;i++){
            for (int j=1;j<smallSpace;j++){
                canvas.drawLine(topPointX-smallWidth,topPointY+i*bigDistance+j*smallDistance,mWidth/2,topPointY+i*bigDistance+j*smallDistance,mPaint);
            }
        }
        //画右侧雨块
        mPaint.setColor(rainBolckColor);
        RectF rect=new RectF(topPointX,topPointY+3*bigDistance+2*smallDistance,topPointX+rainWidth,topPointY+4*bigDistance+1*smallDistance);
        canvas.drawRect(rect,mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        RectF rect=new RectF(mWidth/2,10+3*bigDistance*scaledRatio+2*smallDistance*scaledRatio,mWidth/2+rainWidth,10+4*bigDistance*scaledRatio+1*smallDistance*scaledRatio);
        if (rect.contains(e.getX(),e.getY())&&onRainChunkClickListener!=null){
            onRainChunkClickListener.onClick((rect.top-10)/bigDistance);
        }
        return false;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scaledRatio=detector.getCurrentSpanY()/detector.getPreviousSpanY();

        float currentTopPointY=detector.getFocusY()-scaledRatio*(detector.getFocusY()-topPointY);
        float currentRulersHeight = scaledRatio*rulersHeight;

        //top离开上布局
        if (currentTopPointY>=10||currentTopPointY+currentRulersHeight+10<mHeight){
//            LogUtils.i("top离开上布局");
            topPointY=10;
            rulersHeight = mHeight -10*2;
        } else if (currentRulersHeight>2*(mHeight-20)){
//            LogUtils.i("放得太大了");
        } else {
            topPointY=currentTopPointY;
            rulersHeight=currentRulersHeight;
            postInvalidate();
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        LogUtils.i("onScroll");
        if (Math.abs(distanceY)>Math.abs(distanceX)){
            float currentTopPointY=topPointY-distanceY;
            if (currentTopPointY<=10&&currentTopPointY+rulersHeight>=mHeight-10){
                LogUtils.i((currentTopPointY+rulersHeight)+"="+mHeight);
                topPointY=currentTopPointY;
                postInvalidate();
            }
        }
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
    }


    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


}
