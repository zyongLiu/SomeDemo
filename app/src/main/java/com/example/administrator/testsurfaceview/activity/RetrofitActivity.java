package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.bean.MovieEntity;
import com.example.administrator.testsurfaceview.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Liu on 2016/11/11.
 */
public class RetrofitActivity extends Activity{
    @BindView(R.id.click_me_BN)
    Button clickMeBN;
    @BindView(R.id.result_TV)
    TextView resultTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxre);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.click_me_BN)
    public void onClick() {
        getMovie();
    }

    public interface MovieService {
        @GET("top250")
        Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
    }

    public void getMovie() {
//        OkHttpClient client=new OkHttpClient();
//        client.interceptors().add(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                long t1 = System.nanoTime();
//                LogUtils.i(String.format("Sending request %s on %s%n%s",
//                        request.url(), chain.connection(), request.headers()));
//
//                okhttp3.Response response = chain.proceed(request);
//
//                long t2 = System.nanoTime();
//                LogUtils.i(String.format("Received response for %s in %.1fms%n%s",
//                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//
//                return response;
//
//            }
//        });


        String url = "https://api.github.com/repos/typecho-fans/plugins/contents/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
//                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MovieService movieService = retrofit.create(MovieService.class);
        Call<MovieEntity> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<MovieEntity>() {
            @Override
            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                resultTV.setText(response.body().getSubjects().get(0).getTitle());
            }

            @Override
            public void onFailure(Call<MovieEntity> call, Throwable throwable) {
                resultTV.setText("WRONG");
            }
        });
        call.cancel();
    }



}
