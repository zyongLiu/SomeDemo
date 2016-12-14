package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.bean.MovieEntity;
import com.example.administrator.testsurfaceview.callback.ProgressListener;
import com.example.administrator.testsurfaceview.http.ProgressResponseBody;
import com.example.administrator.testsurfaceview.utils.LogUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Liu on 2016/11/11.
 */
public class RetrofitActivity extends Activity {
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
//        getMovie();
        downloadFile();
    }

    public void getMovie() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
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
        }).build();


        String url = "https://api.github.com/repos/typecho-fans/plugins/contents/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .client(client)
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


    public void downloadFile() {
        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                System.out.println(bytesRead);
                System.out.println(contentLength);
                System.out.println(done);
                System.out.format("%d%% done\n", (100 * bytesRead) / contentLength);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                LogUtils.i("intercept" + Thread.currentThread().getName());
                okhttp3.Response response = chain.proceed(chain.request());
                return response.newBuilder().body(new ProgressResponseBody(response.body(), progressListener)).build();
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.111").client(client).build();
        FileWebService service = retrofit.create(FileWebService.class);
        service.getFile(2015).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                LogUtils.i("onResponse" + Thread.currentThread().getName());
                File file = null;
                OutputStream outputStream = null;
                BufferedOutputStream stream = null;
                try {
                    byte[] bytes = response.body().bytes();
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/aa.jpg");
                    outputStream = new FileOutputStream(file);
                    stream = new BufferedOutputStream(outputStream);
                    stream.write(bytes);
                    stream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


//    # Good Practise
//    base url: https://futurestud.io/api/
//    endpoint: my/endpoint
//    Result:   https://futurestud.io/api/my/endpoint
//
//            # Bad Practise
//    base url: https://futurestud.io/api
//    endpoint: /my/endpoint
//    Result:   https://futurestud.io/my/endpoint

//    # Example 1
//    base url: https://futurestud.io/api/v3/
//    endpoint: my/endpoint
//    Result:   https://futurestud.io/api/v3/my/endpoint
//
//            # Example 2
//    base url: https://futurestud.io/api/v3/
//    endpoint: /api/v2/another/endpoint
//    Result:   https://futurestud.io/api/v2/another/endpoint

//    # Example 3 — completely different url
//    base url: http://futurestud.io/api/
//    endpoint: https://api.futurestud.io/
//    Result:   https://api.futurestud.io/
//
//            # Example 4 — Keep the base url’s scheme
//    base url: https://futurestud.io/api/
//    endpoint: //api.futurestud.io/
//    Result:   https://api.futurestud.io/
//
//            # Example 5 — Keep the base url’s scheme
//    base url: http://futurestud.io/api/
//    endpoint: //api.github.com
//    Result:   http://api.github.com

//    public interface TaskService {
//        @POST("/tasks")
//        Call<Task> createTask(@Body Task task);
//    }

    public interface MovieService {
        @GET("top250")
        Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
    }

    //    20140814122633546.jpg
    public interface FileWebService {
        @GET("/test/{fileId}.jpg")
        @Headers({"Content-Type: image/jpeg"})
        Call<ResponseBody> getFile(@Path("fileId") int fileId);
    }

    public interface UserService {
        @GET
        public Call<ResponseBody> profilePicture(@Url String url);
    }

}
