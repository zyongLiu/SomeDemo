package com.example.administrator.testsurfaceview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.example.administrator.testsurfaceview.adapter.DummyAdapter;
import com.example.administrator.testsurfaceview.fragment.PlaceholderFragment;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

/**
 * 循环滚动切换图片(支持带标题,不带标题传null即可),自带适配器 支持显示本地res图片和网络图片，指定uri的图片
 * OnPagerClickCallback onPagerClickCallback 处理page被点击的回调接口, 被用户手动滑动时，暂停滚动，增强用户友好性
 *
 * @author dance
 */
public class RollViewPager extends VerticalViewPager {
    private Context context;
    private int currentItem;
    private List<String> strings;// 图片地址
    private OnPagerClickCallback onPagerClickCallback;
    MyOnTouchListener myOnTouchListener;
    ViewPagerTask viewPagerTask;
    private int max_count;

    private long start = 0;


    public class MyOnTouchListener implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    start = System.currentTimeMillis();
                    handler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_MOVE:
                    handler.removeCallbacks(viewPagerTask);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    startRoll();
                    break;
                case MotionEvent.ACTION_UP:
                    long duration = System.currentTimeMillis() - start;
                    if (duration <= 400) {
                        if (onPagerClickCallback != null)
                            onPagerClickCallback.onPagerClick(currentItem);
                    }
                    startRoll();
                    break;
            }
            return true;
        }
    }


    public class ViewPagerTask implements Runnable {
        @Override
        public void run() {
            if (currentItem + 1 == max_count) {
                currentItem = (currentItem + 1) % strings.size();
            } else {
                currentItem = currentItem + 1;
            }
            handler.obtainMessage().sendToTarget();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (currentItem==0){
                setCurrentItem(currentItem,false);
            }else {
                setCurrentItem(currentItem);
            }

            startRoll();
        }
    };

    /**
     * 循环滚动切换图片，被触摸时，暂停滚动，增强用户友好性 ，支持带标题,设置标题用setTitle，
     * 支持显示本地res图片和网络图片，指定uri的图片
     *
     * @param context
     * @param strings              图片的点的集合，之所以不自动生成，是因为点的位置和大小不确定，所以由调用者传入
     * @param onPagerClickCallback 页面点击回调
     */
    public RollViewPager(Context context, List<String> strings,
                         OnPagerClickCallback onPagerClickCallback) {
        super(context);
        this.context = context;
        this.strings = strings;
        this.onPagerClickCallback = onPagerClickCallback;


        this.max_count = strings.size() * 2;
        this.currentItem = 0;

        viewPagerTask = new ViewPagerTask();
        myOnTouchListener = new MyOnTouchListener();

    }


    private boolean hasSetAdapter = false;
    private final int SWITCH_DURATION = 4000;

    /**
     * 开始滚动
     */
    public void startRoll() {
        if (!hasSetAdapter) {
            hasSetAdapter = true;
            this.setOnPageChangeListener(new MyOnPageChangeListener());
            this.setAdapter(new DummyAdapter(((FragmentActivity) context).getSupportFragmentManager()));
        }
        if (currentItem==0){
            setCurrentItem(currentItem,false);
        }else {
            setCurrentItem(currentItem);
        }

        /**
         * 是否自动滚动
         */
        handler.postDelayed(viewPagerTask, SWITCH_DURATION);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        int oldPosition = 0;

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            oldPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        handler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    /**
     * 处理page点击的回调接口
     *
     * @author dance
     */
    public interface OnPagerClickCallback {
        public abstract void onPagerClick(int position);
    }

    public class DummyAdapter extends FragmentPagerAdapter {

        public DummyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position%3 + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return max_count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position%3) {
                case 0:
                    return "PAGE 1";
                case 1:
                    return "PAGE 2";
                case 2:
                    return "PAGE 3";
            }
            return null;
        }

    }
}
