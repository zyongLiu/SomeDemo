package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.utils.LogUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Liu on 2016/11/25.
 */
public class HeartViewActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartview);


        OkHttpUtils.get().url("http://192.168.1.128/test/updatademo-debug.apk").build().execute(new FileCallBack(
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/test", "heh.apk") {
            @Override
            public void inProgress(float progress, long total, int id) {
                LogUtils.i("progress" + progress + "total" + total);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                e.printStackTrace();
                LogUtils.i("onError" + e.getMessage());
            }

            @Override
            public void onResponse(File response, int id) {
                LogUtils.i(response.getAbsolutePath());
                Toast.makeText(getApplication().getApplicationContext(),"下载成功",Toast.LENGTH_SHORT).show();
                an(response);
            }
        });
    }

    public void an(File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }


}
