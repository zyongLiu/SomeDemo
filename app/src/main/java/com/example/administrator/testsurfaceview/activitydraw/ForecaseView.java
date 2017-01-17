package com.example.administrator.testsurfaceview.activitydraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.utils.DisplayUtil;
import com.example.administrator.testsurfaceview.utils.LinearGradientUtil;
import com.example.administrator.testsurfaceview.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Liu on 2017/1/16.
 */
public class ForecaseView extends View {
    private Context mContext;

    private Paint mPaint;

    private int numValue = 26;

    private TextPaint mTextPaint;
    //小块宽度
    private float cellWidth = 112;
    private float division = 3;

    private int mHeight, mWidth;
    //路径
    private Path path;
    //滑动百分比
    private float rate = 0f;

    private float temperatureHeight;
    private float aqiHeight;
    private float windHeight;
    private float timeHeight;

    private float marginLeft = 10, marginRight = 10, marginTop = 10, marginBottom;
    //内容区域宽度
    private float contentWidth;

    private LinearGradientUtil gradientUtil;
    //开始颜色
    private int colorOther = Color.parseColor("#6C6B45");
    //结束颜色
    private int colorNow = Color.parseColor("#AD951E");
    //温度块高度
    private float temBlockHeight = 40f;
    private float smoothness = 0.33f;
    //风力测试数据
    private List<Integer[]> windData;
    private List<PointF> mPoint;
    private List<PointF> drawPoint = new ArrayList<>();
    //风力块高度
    private float windBlockHeight = 40f;

    private static Paint.FontMetrics mFontMetricsBuffer = new Paint.FontMetrics();

    private PathMeasure pathMeasure;


    public ForecaseView(Context context) {
        this(context, null);
    }

    public ForecaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ForecaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        windData = new ArrayList<>();
        windData.add(new Integer[]{0, 1, 1});
        windData.add(new Integer[]{2, 2, 2});
        windData.add(new Integer[]{3, 22, 1});
        windData.add(new Integer[]{23, 24, 3});
        windData.add(new Integer[]{25, 25, 1});


        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.YELLOW);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(40f);
        cellWidth = mTextPaint.measureText("00:0000");

        gradientUtil = new LinearGradientUtil(colorOther, colorNow);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        contentWidth = numValue * (cellWidth + division);
        mWidth = getMeasuredWidth() == 0 ? (int) (contentWidth + marginLeft + marginRight) : getMeasuredWidth();
        mHeight = getMeasuredHeight() == 0 ? 900 : getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        temperatureHeight = (mHeight - marginTop - marginBottom) * 2 / 3;
        aqiHeight = windHeight = timeHeight = temperatureHeight / 6;

        if (mPoint == null) {
            mPoint = new ArrayList<>();
            for (int i = 0; i < numValue; i++) {
                mPoint.add(new PointF(i, (float) (Math.random() * 10f - 5f)));
            }
            computeBesselPoints();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.parseColor("#253D52"));
        //辅助块 四个区域
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(1f);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(new RectF(marginLeft, marginTop, mWidth - marginRight, marginTop + temperatureHeight), mPaint);
        canvas.drawRect(new RectF(marginLeft, marginTop + temperatureHeight, mWidth - marginRight, marginTop + temperatureHeight + aqiHeight), mPaint);
        canvas.drawRect(new RectF(marginLeft, marginTop + temperatureHeight + aqiHeight, mWidth - marginRight, marginTop + temperatureHeight + aqiHeight + windHeight), mPaint);
        canvas.drawRect(new RectF(marginLeft, marginTop + temperatureHeight + aqiHeight + windHeight, mWidth - marginRight, marginTop + temperatureHeight + aqiHeight + windHeight + timeHeight), mPaint);


        //空气质量 文字
        float textStart = cellWidth / 2 + marginLeft;
        float textEnd = mWidth - marginRight - cellWidth / 2;
        float centerTextX = textStart + rate * (textEnd - textStart);
        float textWidth = mTextPaint.measureText("24");
        mTextPaint.setTextSize(40);
        canvas.drawText("24", centerTextX, marginTop + temperatureHeight + temBlockHeight, mTextPaint);
        //空气质量#AD951E
        path = new Path();
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < numValue; i++) {
            RectF rectF = new RectF(
                    i * (cellWidth + division) + marginLeft,
                    marginTop + temperatureHeight + aqiHeight - temBlockHeight,
                    cellWidth * (i + 1) + division * i + marginLeft,
                    marginTop + temperatureHeight + aqiHeight);
            path.addRoundRect(rectF,
                    new float[]{15, 15, 15, 15, 0, 0, 0, 0}, Path.Direction.CW);
            //left
            float colorRate = (centerTextX + textWidth / 2 - cellWidth * (i + 1) - division * i - marginLeft) / (textWidth + division);
            //left
            if (centerTextX + textWidth / 2 > cellWidth * (i + 1) + division * i + marginLeft && centerTextX - textWidth / 2 < (i + 1) * (cellWidth + division) + marginLeft) {
                mPaint.setColor(gradientUtil.getColor(1 - colorRate));
            } else if (centerTextX > cellWidth * i + division * i + marginLeft && centerTextX < cellWidth * (i + 1) + division * i + marginLeft) {
                mPaint.setColor(colorNow);
            } else {
                mPaint.setColor(colorOther);
            }
            canvas.drawPath(path, mPaint);
            path.reset();
        }
        //风力
//        new Integer[]{0,1,1}
//        new Integer[]{2,2,2}
//        new Integer[]{3,22,1}
//        new Integer[]{23,24,3}
//        new Integer[]{25,25,1}
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        mTextPaint.setTextSize(30f);
        mTextPaint.getFontMetrics(mFontMetricsBuffer);
        for (int i = 0; i < windData.size(); i++) {
            Integer[] d = windData.get(i);
            RectF rectF = new RectF(d[0] * cellWidth + division * (d[0]) + marginLeft,
                    marginTop + temperatureHeight + aqiHeight + windHeight - windBlockHeight,
                    (d[1] + 1) * cellWidth + division * (d[1]) + marginLeft,
                    marginTop + temperatureHeight + aqiHeight + windHeight);
            path.addRoundRect(rectF,
                    new float[]{15, 15, 15, 15, 0, 0, 0, 0}, Path.Direction.CW);
            mPaint.setColor(Color.LTGRAY);
            canvas.drawPath(path, mPaint);
            canvas.drawText(d[2] + "级", marginLeft + cellWidth * (d[0] + d[1] + 1) / 2 + division * (d[0] + d[1]) / 2,
                    marginTop + temperatureHeight + aqiHeight + windHeight - mFontMetricsBuffer.descent
                    , mTextPaint);
            path.reset();
        }
        //时间
        mTextPaint.setTextSize(40f);
        mTextPaint.getFontMetrics(mFontMetricsBuffer);
        for (int i = 0; i < numValue; i++) {
            canvas.drawText("14:00", marginLeft + cellWidth * (2 * i + 1) / 2 + division * i,
                    marginTop + temperatureHeight + aqiHeight + windHeight + timeHeight - mFontMetricsBuffer.descent, mTextPaint);
        }
        //画曲线
        if (drawPoint != null && drawPoint.size() > 0) {
            drawBezier(canvas);
        }
        //画曲线上图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.w0);
        float bitmapPointX = marginLeft + 2.5f * cellWidth + 2 * division;
        float bitmapPointY = getRealY(mPoint.get(2).y);
        RectF rectF = new RectF(
                bitmapPointX - DisplayUtil.dip2px(mContext, 10),
                bitmapPointY - DisplayUtil.dip2px(mContext, 30),
                bitmapPointX + DisplayUtil.dip2px(mContext, 10),
                bitmapPointY - DisplayUtil.dip2px(mContext, 10));
        canvas.drawBitmap(bitmap, null, rectF, mPaint);
        //画温度、天气、背景框
        float[] f = new float[2];
        float lenth = rate * pathMeasure.getLength();
        pathMeasure.getPosTan(lenth, f, null);
        RectF rectWeather = new RectF(
                f[0] - DisplayUtil.dip2px(mContext, 10),
                f[1] - DisplayUtil.dip2px(mContext, 30),
                f[0] + DisplayUtil.dip2px(mContext, 10),
                f[1] - DisplayUtil.dip2px(mContext, 10));
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.w1), null, rectWeather, mPaint);

//        Path c=new Path();
//        pathMeasure.getSegment(rate * (textEnd - textStart),lenth,c,false);
//        PathMeasure p=new PathMeasure(c,false);
//        float[] ff=new float[2];
//        p.getPosTan(0.1f*p.getLength(),ff,null);
//        RectF rectWeather = new RectF(
//                centerTextX - DisplayUtil.dip2px(mContext, 10),
//                ff[1] - DisplayUtil.dip2px(mContext, 30),
//                centerTextX + DisplayUtil.dip2px(mContext, 10),
//                ff[1] - DisplayUtil.dip2px(mContext, 10));
//        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.w1), null, rectWeather, mPaint);

    }

    /**
     * 父ScrollView滚动时触发
     *
     * @param width
     * @param total
     */
    public void onScrollChanged(int width, int total) {
        rate = width * 1.0f / (mWidth - total);
        postInvalidate();
    }

    public void drawBezier(Canvas canvas) {
        Path curvePath = new Path();
        for (int i = 0; i < drawPoint.size(); i = i + 3) {
            if (i == 0) {
                curvePath.moveTo(getRealX(drawPoint.get(i).x), getRealY(drawPoint.get(i).y));
            } else {
                curvePath.cubicTo(
                        getRealX(drawPoint.get(i - 2).x), getRealY(drawPoint.get(i - 2).y),
                        getRealX(drawPoint.get(i - 1).x), getRealY(drawPoint.get(i - 1).y),
                        getRealX(drawPoint.get(i).x), getRealY(drawPoint.get(i).y));
            }
        }

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);
        mPaint.setColor(Color.YELLOW);
        canvas.drawPath(curvePath, mPaint);// 绘制光滑曲线

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        for (PointF point : mPoint) {
            canvas.drawCircle(getRealX(point.x), getRealY(point.y), 6f, mPaint);
        }

        pathMeasure = new PathMeasure(curvePath, false);

    }

    private float getRealX(float x) {
        return marginLeft + cellWidth * (2f * x + 1f) / 2f + division * x;
    }


    private float getRealY(float y) {
        return temperatureHeight * ((15f - y) / 30f) + marginTop;
    }

    /**
     * 计算贝塞尔结点
     */
    private void computeBesselPoints() {
        drawPoint.clear();
        for (int i = 0; i < mPoint.size(); i++) {
            if (i == 0 || i == mPoint.size() - 1) {
                computeUnMonotonePoints(i, mPoint, drawPoint);
            } else {
                PointF p0 = mPoint.get(i - 1);
                PointF p1 = mPoint.get(i);
                PointF p2 = mPoint.get(i + 1);
                if ((p1.y - p0.y) * (p1.y - p2.y) >= 0) {// 极值点
                    computeUnMonotonePoints(i, mPoint, drawPoint);
                } else {
                    computeMonotonePoints(i, mPoint, drawPoint);
                }
            }
        }
    }

    /**
     * 计算非单调情况的贝塞尔结点
     */
    private void computeUnMonotonePoints(int i, List<PointF> points, List<PointF> besselPoints) {
        if (i == 0) {
            PointF p1 = points.get(0);
            PointF p2 = points.get(1);
            besselPoints.add(p1);
            besselPoints.add(new PointF(p1.x + (p2.x - p1.x) * smoothness, p1.y));
        } else if (i == points.size() - 1) {
            PointF p0 = points.get(i - 1);
            PointF p1 = points.get(i);
            besselPoints.add(new PointF(p1.x - (p1.x - p0.x) * smoothness, p1.y));
            besselPoints.add(p1);
        } else {
            PointF p0 = points.get(i - 1);
            PointF p1 = points.get(i);
            PointF p2 = points.get(i + 1);
            besselPoints.add(new PointF(p1.x - (p1.x - p0.x) * smoothness, p1.y));
            besselPoints.add(p1);
            besselPoints.add(new PointF(p1.x + (p2.x - p1.x) * smoothness, p1.y));
        }
    }

    /**
     * 计算单调情况的贝塞尔结点
     *
     * @param i
     * @param points
     * @param besselPoints
     */
    private void computeMonotonePoints(int i, List<PointF> points, List<PointF> besselPoints) {
        PointF p0 = points.get(i - 1);
        PointF p1 = points.get(i);
        PointF p2 = points.get(i + 1);
        float k = (p2.y - p0.y) / (p2.x - p0.x);
        float b = p1.y - k * p1.x;
        PointF p01 = new PointF();
        p01.x = p1.x - (p1.x - (p0.y - b) / k) * smoothness;
        p01.y = k * p01.x + b;
        besselPoints.add(p01);
        besselPoints.add(p1);
        PointF p11 = new PointF();
        p11.x = p1.x + (p2.x - p1.x) * smoothness;
        p11.y = k * p11.x + b;
        besselPoints.add(p11);
    }

}
