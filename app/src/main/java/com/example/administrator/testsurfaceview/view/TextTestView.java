package com.example.administrator.testsurfaceview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.testsurfaceview.utils.LogUtils;

/**
 * Created by Liu on 2016/11/30.
 */
public class TextTestView extends View {
    private Paint mPaint;
    private Paint mDrawPaint;
    private TextPaint mTextPaint;



    private static Rect mDrawTextRectBuffer = new Rect();
    private static Paint.FontMetrics mFontMetricsBuffer = new Paint.FontMetrics();

    public TextTestView(Context context) {
        this(context, null);
    }

    public TextTestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(80);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mDrawPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mDrawPaint.setColor(Color.RED);
        mDrawPaint.setStrokeWidth(1);

        mTextPaint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(Color.BLUE);
    }

    private int mHeight=0;
    private int mWidth=0;

    @Override
    protected void onDraw(Canvas canvas) {

        mHeight=getHeight();
        mWidth=getWidth();


        String text = "2时EeJjHh";
        float drawOffsetX = mWidth/2;
        float drawOffsetY = mHeight/2;

        canvas.save();
        String message = "paint,draw paint指用颜色画";
        canvas.translate(drawOffsetX,0);
        StaticLayout myStaticLayout = new StaticLayout(message, mTextPaint, (int) mTextPaint.measureText("一二三"),
                Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        myStaticLayout.draw(canvas);
        canvas.restore();


        //画辅助线
        canvas.drawLine(0,drawOffsetY,mWidth,drawOffsetY,mDrawPaint);
        canvas.drawLine(drawOffsetX,0,drawOffsetX,mHeight,mDrawPaint);

        float lineHeight = mPaint.getFontMetrics(mFontMetricsBuffer);
        mPaint.getTextBounds(text, 0, text.length(), mDrawTextRectBuffer);
        float textWidth=mPaint.measureText(text);

        canvas.drawLine(0,drawOffsetY,0,drawOffsetY-lineHeight,mDrawPaint);

        canvas.drawRect(new RectF(mDrawTextRectBuffer.left+drawOffsetX,mDrawTextRectBuffer.top+drawOffsetY,
                mDrawTextRectBuffer.right+drawOffsetX,mDrawTextRectBuffer.bottom+drawOffsetY),mDrawPaint);


        LogUtils.i("lineHeight:"+lineHeight+",mDrawTextRectBuffer.height:"+mDrawTextRectBuffer.height());
        LogUtils.i("textWidth:"+textWidth+",mDrawTextRectBuffer.width:"+mDrawTextRectBuffer.width());
        LogUtils.i("textSize:"+mPaint.getTextSize());


        canvas.drawLine(0,drawOffsetY+mFontMetricsBuffer.top,mWidth,drawOffsetY+mFontMetricsBuffer.top,mDrawPaint);
        canvas.drawLine(0,drawOffsetY+mFontMetricsBuffer.ascent,mWidth,drawOffsetY+mFontMetricsBuffer.ascent,mDrawPaint);
        canvas.drawLine(0,drawOffsetY+mFontMetricsBuffer.descent,mWidth,drawOffsetY+mFontMetricsBuffer.descent,mDrawPaint);
        canvas.drawLine(0,drawOffsetY+mFontMetricsBuffer.bottom,mWidth,drawOffsetY+mFontMetricsBuffer.bottom,mDrawPaint);
//        canvas.drawLine(0,drawOffsetY+mFontMetricsBuffer.leading,mWidth,drawOffsetY+mFontMetricsBuffer.leading,mDrawPaint);


        LogUtils.i("FontMetricsInt: top=" + mFontMetricsBuffer.top + " ascent=" + mFontMetricsBuffer.ascent +
                " descent=" + mFontMetricsBuffer.descent + " bottom=" + mFontMetricsBuffer.bottom +
                " leading=" + mFontMetricsBuffer.leading);
//        canvas.drawRect(mDrawTextRectBuffer,mDrawPaint);

//        drawOffsetX -= mDrawTextRectBuffer.left;
//        drawOffsetY += -mFontMetricsBuffer.ascent;
        canvas.drawText(text, drawOffsetX, drawOffsetY, mPaint);

        canvas.drawText("第二行",drawOffsetX,drawOffsetY+lineHeight,mPaint);



    }
}
