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
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.bean.Block;
import com.example.administrator.testsurfaceview.utils.LogUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * 自定义刻度尺
 * Created by Liu on 2016/10/28.
 */
public class RulersView extends View implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {
    //画线的笔
    private Paint mPaint;
    //写标注的笔
    private TextPaint mTextPaint;

    //一大格占多少小格
    private int smallCount = 24;

    private float mWidth;
    private float mHeight;
    //刻度高度
    private float rulersHeight;
    //小刻度的距离
    private float smallDistance;
    //大刻度的距离 smallDistance*smallCount
    private float bigDistance;
    //小刻度宽度
    private float smallWidth = 20;
    //大刻度宽度
    private float bigWidth = 30;
    //大刻度的个数
    private int bigCount = 7;
    //雨块宽度
    private float rainWidth = 50;
    //雨块颜色
    private int rainBolckColor;
    //选中的雨块颜色
    private int selectedRainBolckColor;
    //标注字体大小
    private int textSize;

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    //雨块点击事件
    private OnRainChunkClickListener onRainChunkClickListener;

    //顶点的坐标
    private float topPointX;
    private float topPointY = 10;
    private float scaledRatio = 1;

    //是否可滚动
    private boolean zoomable = true;
    //色块
    private List<Block> blocks;
    //选中的雨块
    private int blockSelectedIndex = 0;
    //最大放大的比例
    private float maxRatio = 2;

    private RulersValueFormatter rulersValueFormatter;

    private OverScroller mScroller;

    public interface OnRainChunkClickListener {
        void onClick(int position, Block block);
    }

    public interface RulersValueFormatter {
        String getFormattedValue(int value);
    }

    public RulersView(Context context) {
        this(context, null);
    }

    public RulersView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.rulers);
        rainBolckColor = a.getColor(R.styleable.rulers_blockColor, Color.BLUE);
        selectedRainBolckColor = a.getColor(R.styleable.rulers_selectedBlockColor, Color.RED);
        textSize = a.getInteger(R.styleable.rulers_textSize, 25);
        a.recycle();
        init(context);
    }


    public void setOnRainChunkClickListener(OnRainChunkClickListener onRainChunkClickListener) {
        this.onRainChunkClickListener = onRainChunkClickListener;
    }

    public void setRulersValueFormatter(RulersValueFormatter rulersValueFormatter) {
        this.rulersValueFormatter = rulersValueFormatter;
    }

    /**
     * 设置色块
     *
     * @param blocks
     */
    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
        postInvalidate();
    }

    /**
     * 增加色块
     *
     * @param block
     */
    public void addBlocks(Block block) {
        if (blocks == null) {
            blocks = new ArrayList<>();
        }
        blocks.add(block);
        postInvalidate();
    }

    /**
     * 增加色块
     *
     * @param blocks
     */
    public void addBlocks(List<Block> blocks) {
        if (blocks == null) {
            blocks = new ArrayList<>();
        }
        blocks.addAll(blocks);
        postInvalidate();
    }

    public void setBigandSmallCount(int bigCount, int smallCount) {
        this.bigCount = bigCount;
        this.smallCount = smallCount;
    }


    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);

        mGestureDetector = new GestureDetector(context, this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);

        mScroller = new OverScroller(context);
    }

    /**
     * 控件重置为初始状态
     */
    public void reset() {
        rulersHeight = mHeight - 10 * 2;
        topPointY = 10;
        postInvalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        rulersHeight = mHeight - 10 * 2;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //根据总长度求出一些常量

        bigDistance = rulersHeight / bigCount;
        smallDistance = bigDistance / smallCount;

        topPointX = mWidth / 2;
        //初始状态为10 ？
//        topPointY=10;

        //画竖线
        mPaint.setTextSize(18F);
        canvas.drawLine(topPointX, topPointY, topPointX, topPointY + rulersHeight, mPaint);
        //画大刻度
        for (int i = 0; i <= bigCount; i++) {
            canvas.drawLine(topPointX - bigWidth, topPointY + i * bigDistance, topPointX, topPointY + i * bigDistance, mPaint);
        }
        //画标注
        for (int i = 0; i <= bigCount; i++) {
            String text = null;
            if (rulersValueFormatter != null) {
                text = rulersValueFormatter.getFormattedValue(i);
            }
            canvas.drawText(text,
                    topPointX - bigWidth - smallWidth / 2,
                    topPointY + i * bigDistance + mPaint.getTextSize() / 2, mTextPaint);
        }
        //画小刻度
        for (int i = 0; i < bigCount; i++) {
            for (int j = 1; j < smallCount; j++) {

                //半天特殊标示
                if (j == smallCount / 2) {
                    canvas.drawLine(
                            topPointX - bigWidth,
                            topPointY + i * bigDistance + j * smallDistance,
                            mWidth / 2,
                            topPointY + i * bigDistance + j * smallDistance, mPaint);
                } else {
                    canvas.drawLine(
                            topPointX - smallWidth,
                            topPointY + i * bigDistance + j * smallDistance,
                            mWidth / 2,
                            topPointY + i * bigDistance + j * smallDistance, mPaint);
                }
            }
        }
        //画右侧雨块
        if (blocks != null && blocks.size() > 0) {
            for (int i = 0; i < blocks.size(); i++) {
                if (i == blockSelectedIndex) {
                    mPaint.setColor(selectedRainBolckColor);
                } else {
                    mPaint.setColor(rainBolckColor);
                }
                Block block = blocks.get(i);
                RectF rect = new RectF(topPointX,
                        topPointY + block.getStartBigIndex() * bigDistance + block.getStartSmallIndex() * smallDistance,
                        topPointX + rainWidth,
                        topPointY + block.getEndBigIndex() * bigDistance + block.getEndSmallIndex() * smallDistance);
                canvas.drawRect(rect, mPaint);
            }
        }

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

        if (blocks != null && blocks.size() > 0) {
            for (int i = 0; i < blocks.size(); i++) {
                Block block = blocks.get(i);
                RectF rect = new RectF(topPointX,
                        topPointY + block.getStartBigIndex() * bigDistance + block.getStartSmallIndex() * smallDistance,
                        topPointX + rainWidth,
                        topPointY + block.getEndBigIndex() * bigDistance + block.getEndSmallIndex() * smallDistance);
                if (rect.contains(e.getX(), e.getY()) && onRainChunkClickListener != null) {
                    blockSelectedIndex = i;
                    invalidate();
                    onRainChunkClickListener.onClick(i, blocks.get(i));
                    break;
                }
            }
        }
        return false;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (!zoomable)
            return false;

        scaledRatio = detector.getCurrentSpanY() / detector.getPreviousSpanY();

        float currentTopPointY = detector.getFocusY() - scaledRatio * (detector.getFocusY() - topPointY);
        float currentRulersHeight = scaledRatio * rulersHeight;

        //top离开上布局
        if (currentTopPointY >= 10 || currentTopPointY + currentRulersHeight + 10 < mHeight) {
//            LogUtils.i("top离开上布局");
            topPointY = 10;
            rulersHeight = mHeight - 10 * 2;
        } else if (currentRulersHeight > maxRatio * (mHeight - 20)) {
//            LogUtils.i("放得太大了");
        } else {
            topPointY = currentTopPointY;
            rulersHeight = currentRulersHeight;
            postInvalidate();
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Math.abs(distanceY) > Math.abs(distanceX)) {
            float currentTopPointY = topPointY - distanceY;
            if (currentTopPointY <= 0 && currentTopPointY + rulersHeight >= mHeight) {
                topPointY = currentTopPointY;
                postInvalidate();
            }
            if (currentTopPointY > 0) {
                topPointY = 10;
                postInvalidate();
            } else if (currentTopPointY + rulersHeight < mHeight) {
                topPointY = mHeight - 10 - rulersHeight;
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
//        mScroller.fling(0, (int) e1.getY(), 0, (int)velocityY, 0, 0, 0, 100);
        LogUtils.i("onFling");
        return false;
    }


//    @Override
//    public void computeScroll() {
//        if (mScroller.computeScrollOffset()) {
//            scrollTo(0, -mScroller.getCurrY());
//            postInvalidate();
//        }
//    }
}
