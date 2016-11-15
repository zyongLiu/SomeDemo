package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import com.example.administrator.testsurfaceview.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liu on 2016/11/15.
 */
public class OkhttpActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void testDownLoad(){
        OkHttpClient httpClient=new OkHttpClient();
        String url="http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        Request request=new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream =response.body().byteStream();
                FileOutputStream fileOutputStream=null;
                try {
                    fileOutputStream=new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/download.jpg"));
                    byte[] buffer=new byte[2048];
                    int len=0;
                    while ((len=inputStream.read(buffer))!=-1){
                        fileOutputStream.write(buffer,0,len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    LogUtils.e("IOException");
                    e.printStackTrace();
                }
                LogUtils.d("文件下载成！");
            }
        });
    }
}
