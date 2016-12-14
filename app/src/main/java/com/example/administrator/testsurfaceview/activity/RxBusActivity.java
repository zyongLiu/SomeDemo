package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.rx.RxBus;
import com.example.administrator.testsurfaceview.utils.LogUtils;

import rx.functions.Action1;

/**
 * Created by Liu on 2016/12/12.
 */
public class RxBusActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxbus);

        RxBus.getInstance().toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof String)
                    LogUtils.i("收到:" + o);
            }
        });
    }

    public void send(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s="send from thread:" + Thread.currentThread().getName();
                LogUtils.i(s);
                RxBus.getInstance().send(s);
            }
        }, "thread1").start();
    }
}
