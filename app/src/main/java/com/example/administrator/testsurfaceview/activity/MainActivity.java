package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.utils.DisplayUtil;
import com.example.administrator.testsurfaceview.utils.LogUtils;
import com.example.administrator.testsurfaceview.view.VerticalViewPager;

/**
 * Created by Liu on 2016/11/10.
 */
public class MainActivity extends Activity {
    private ListView lv;
    private WindowManager windowManager;
    private ImageView img;

    private String[] datas = new String[]{
            "Pulgin", "SurfaceView", "Touch", "RetrofitActivity", "RxJavaActivity",
            "RxJava + Retrofit", "Okhttp", "RulersViewActivity", "竖直滚动文字", "竖立ViewPager",
            "属性动画", "Heart", "DrawText","XfermodesActivity","画板"};
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_activity_view);

        addCoverLayer();

        lv = (ListView) findViewById(R.id.lv);
        mAdapter = new MainAdapter();
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), PulginActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), SurfaceViewActivity.class);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), TouchActivity.class);
                        break;
                    case 3:
                        intent = new Intent(getApplicationContext(), RetrofitActivity.class);
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(), RxJavaActivity.class);
                        break;
                    case 5:
                        intent = new Intent(getApplicationContext(), RxReActivity.class);
                        break;
                    case 6:
                        intent = new Intent(getApplicationContext(), OkhttpActivity.class);
                        break;
                    case 7:
                        intent = new Intent(getApplicationContext(), RulersViewActivity.class);
                        break;
                    case 8:
                        intent = new Intent(getApplicationContext(), ScrollActivitiy.class);
                        break;
                    case 9:
                        intent = new Intent(getApplicationContext(), VerticalViewPagerActivity.class);
                        break;
                    case 10:
                        intent = new Intent(getApplicationContext(), PropertyAnimationActivity.class);
                        break;
                    case 11:
                        intent = new Intent(getApplicationContext(), HeartViewActivity.class);
                        break;
                    case 12:
                        intent = new Intent(getApplicationContext(), TextTestActivity.class);
                        break;
                    case 13:
                        intent = new Intent(getApplicationContext(), XfermodesActivity.class);
                        break;
                    case 14:
                        intent = new Intent(getApplicationContext(), DrawBoardActivity.class);
                        break;
                    default:

                }
                startActivity(intent);
            }
        });
    }

    public class MainAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.length;
        }

        @Override
        public Object getItem(int position) {
            return datas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(getApplication());
            tv.setText(datas[position]);
            tv.setTextSize(18);
            tv.setTextColor(Color.BLACK);
            return tv;
        }
    }

    public void addCoverLayer() {
        windowManager = getWindowManager();

        // 动态初始化图层
        img = new ImageView(this);
        img.setLayoutParams(new FrameLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        img.setImageResource(R.drawable.guide);

        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtils.i("坐标:"+event.getX()+","+event.getY());
                return false;
            }
        });

        // 设置LayoutParams参数
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 设置显示的类型，TYPE_PHONE指的是来电话的时候会被覆盖，其他时候会在最前端，显示位置在stateBar下面，其他更多的值请查阅文档
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        // 设置显示格式
        params.format = PixelFormat.RGBA_8888;
        // 设置对齐方式
        params.gravity = Gravity.LEFT | Gravity.TOP;
        // 设置宽高
        params.width = DisplayUtil.getWindowWidth(this);
        params.height = DisplayUtil.getWindowHeight(this);


        // 添加到当前的窗口上
        windowManager.addView(img, params);

//        Button button=new Button(this);
//        button.setText("click");

        params.x=360;
        params.y=555;

        params.width = 200;
        params.height = 100;

//        params.gravity=Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                windowManager.removeView(img);
//            }
//        });
//
//
//        windowManager.addView(button,params);

        // 点击图层之后，将图层移除
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                windowManager.removeView(img);
            }
        });
    }
}
