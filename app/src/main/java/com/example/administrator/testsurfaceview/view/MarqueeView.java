package com.example.administrator.testsurfaceview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;


import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.utils.DisplayUtil;
import com.example.administrator.testsurfaceview.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class MarqueeView extends ViewFlipper implements GestureDetector.OnGestureListener {

    private Context mContext;
    private List<String> notices;
    private boolean isSetAnimDuration = false;
    private OnItemClickListener onItemClickListener;

    private int interval = 2000;
    private int animDuration = 500;
    private int textSize = 14;
    private int textColor = 0xffffffff;

    private boolean singleLine = false;
    private int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private static final int TEXT_GRAVITY_LEFT = 0, TEXT_GRAVITY_CENTER = 1, TEXT_GRAVITY_RIGHT = 2;

    private GestureDetector detector;

    private boolean allowTouchToFling = true;
    //是否是自动切屏
    private boolean isAutoFling=true;

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (notices == null) {
            notices = new ArrayList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        singleLine = typedArray.getBoolean(R.styleable.MarqueeViewStyle_mvSingleLine, false);
        animDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);
        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DisplayUtil.px2sp(mContext, textSize);
        }
        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        int gravityType = typedArray.getInt(R.styleable.MarqueeViewStyle_mvGravity, TEXT_GRAVITY_LEFT);
        switch (gravityType) {
            case TEXT_GRAVITY_CENTER:
                gravity = Gravity.CENTER;
                break;
            case TEXT_GRAVITY_RIGHT:
                gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        typedArray.recycle();

        setFlipInterval(interval);

        myAnimationListener = new myAnimationListener();

        resetAnimation();

        detector = new GestureDetector(context, this);
    }

    private void resetAnimation() {
        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        animIn.setAnimationListener(myAnimationListener);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    // 根据公告字符串启动轮播
    public void startWithText(final String notice) {
        if (TextUtils.isEmpty(notice)) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    // 根据公告字符串列表启动轮播
    public void startWithList(List<String> notices) {
        setNotices(notices);
        start();
    }

    // 根据宽度和公告字符串启动轮播
    private void startWithFixedWidth(String notice, int width) {
        int noticeLength = notice.length();
        int dpW = DisplayUtil.px2dip(mContext, width);
        int limit = dpW / textSize;
        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width !");
        }

        if (noticeLength <= limit) {
            notices.add(notice);
        } else {
            int size = noticeLength / limit + (noticeLength % limit != 0 ? 1 : 0);
            for (int i = 0; i < size; i++) {
                int startIndex = i * limit;
                int endIndex = ((i + 1) * limit >= noticeLength ? noticeLength : (i + 1) * limit);
                notices.add(notice.substring(startIndex, endIndex));
            }
        }
        start();
    }

    // 启动轮播
    public boolean start() {
        if (notices == null || notices.size() == 0) return false;
        removeAllViews();

        for (int i = 0; i < notices.size(); i++) {
            final TextView textView = createTextView(notices.get(i), i);
            final int finalI = i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(finalI, textView);
                    }
                }
            });
            addView(textView);
        }

        if (notices.size() > 1) {
            startFlipping();
            isAutoFling=true;
        }
        return true;
    }

    // 创建ViewFlipper下的TextView
    private TextView createTextView(String text, int position) {
        TextView tv = new TextView(mContext);
        tv.setGravity(gravity);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setTextSize(textSize);
        tv.setSingleLine(singleLine);
        tv.setTag(position);
        tv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (allowTouchToFling) {
                    stopFlipping();
                    detector.onTouchEvent(event);
                }
                return false;
            }
        });
        return tv;
    }

    public int getPosition() {
        return (int) getCurrentView().getTag();
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices) {
        this.notices = notices;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (isAnimaitonRun) {
            return false;
        }

        if (e2.getY() - e1.getY() > 120) {            // 从上向下滑动（上进下出）
            Animation rInAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_back_in);  // 向右滑动左侧进入的渐变效果（alpha  0.1 -> 1.0）
            Animation rOutAnim = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_back_out); // 向右滑动右侧滑出的渐变效果（alpha 1.0  -> 0.1）
            rInAnim.setAnimationListener(myAnimationListener);

            if (isSetAnimDuration) {
                rInAnim.setDuration(animDuration);
                rOutAnim.setDuration(animDuration);
            }
            setInAnimation(rInAnim);
            setOutAnimation(rOutAnim);
            showPrevious();
            isAutoFling=false;
            return true;
        } else if (e2.getY() - e1.getY() < -120) {        // 从下向上滑动（下进上出）
            resetAnimation();
            showNext();
            isAutoFling=false;
            return true;
        }

        return false;
    }

    private boolean isAnimaitonRun = false;
    private myAnimationListener myAnimationListener;

    public interface OnItemClickListener {
        void onItemClick(int position, TextView textView);
    }

    public class myAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            isAnimaitonRun = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isAnimaitonRun = false;
            if (!isAutoFling){
                postDelayed(mRunnable, (long) (animDuration*1.5));
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            if (!isAnimaitonRun){
                resetAnimation();
                showNext();
                startFlipping();
                isAutoFling=true;
            }
        }
    };

}
