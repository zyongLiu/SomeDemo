package com.example.administrator.testsurfaceview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Liu on 2016/10/26.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MyThread mDrawThread;
    private SurfaceHolder holder;

    public MySurfaceView(Context context) {
        this(context, null);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        holder = getHolder();
        holder.addCallback(this);
        mDrawThread = new MyThread(holder);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("MySurfaceView", "surfaceCreated");
        mDrawThread.isRun = true;
        mDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("MySurfaceView", "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("MySurfaceView", "surfaceDestroyed");
        mDrawThread.isRun = false;
    }


    class MyThread extends Thread {

        private SurfaceHolder holder;
        public boolean isRun;

        public MyThread(SurfaceHolder holder) {
            this.holder = holder;
            isRun = true;
        }

        @Override
        public void run() {

            int count = 0;

            int i = 0;
            while (isRun) {
                Canvas c = null;
                try {
                    synchronized (holder) {
                        c = holder.lockCanvas();//锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
                        c.drawColor(Color.BLACK);//设置画布背景颜色
                        Paint p = new Paint(); //创建画笔
                        p.setColor(Color.WHITE);
                        Rect r = new Rect(100, 50, 300, 250);
//                        i = i+10;
                        c.rotate(i);
                        Log.i("degree",i+"°");
                        c.drawRect(r, p);
                        p.setTextSize(30);
                        c.drawText("这是第" + (count++) + "秒", 100, 310, p);
                        c.restore();
                        Thread.sleep(1000);//睡眠时间为1秒
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);//结束锁定画图，并提交改变。
                    }
                }
            }
        }
    }
}
