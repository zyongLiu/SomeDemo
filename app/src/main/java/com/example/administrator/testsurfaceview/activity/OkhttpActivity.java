package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.administrator.testsurfaceview.bean.Message;
import com.example.administrator.testsurfaceview.utils.LogUtils;
import com.example.administrator.testsurfaceview.utils.OkHttp3Downloader;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Liu on 2016/11/15.
 */
public class OkhttpActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .cache(new Cache(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/atest")
//                        ,10*1024*1024))
//                .build();
//
//        OkHttpUtils.initClient(okHttpClient);
//
//        Picasso picasso = new Picasso.Builder(this)
//                .downloader(new OkHttp3Downloader(okHttpClient))
//                .build();
//        Picasso.setSingletonInstance(picasso);


//        testDownLoad();

        testText();
    }

    private void testText() {
        OkHttpUtils.get().url("http://192.168.1.128/test/update-text.txt").build().execute(new com.zhy.http.okhttp.callback.Callback<Message>() {

            @Override
            public Message parseNetworkResponse(Response response, int id) throws Exception {
                Gson gson = new Gson();
                return gson.fromJson(response.body().string(), Message.class);
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Message response, int id) {
                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            }

        });
    }

    private void testDownLoad() {
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                long t1 = System.nanoTime();
                LogUtils.i(String.format("Sending request %s on %s%n%s",
                        request.url(), chain.connection(), request.headers()));

                okhttp3.Response response = chain.proceed(request);

                long t2 = System.nanoTime();
                LogUtils.i(String.format("Received response for %s in %.1fms%n%s",
                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));

                return response;
            }
        })
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        LogUtils.i("just test");
//                return originalResponse.newBuilder()
//                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
//                        .build();
                        return originalResponse;
                    }
                })
                .build();
        String url = "http://192.168.1.144/HB/MOSAICHREF000.20161021.001000.png";
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/download.jpg"));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
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
