package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.bean.MovieEntity;
import com.example.administrator.testsurfaceview.callback.ProgressListener;
import com.example.administrator.testsurfaceview.http.ProgressResponseBody;
import com.example.administrator.testsurfaceview.utils.LogUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Liu on 2016/11/10.
 */
public class RxReActivity extends Activity {
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
        downFile();
    }

    public interface MovieService {
        @GET("top250")
        Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
    }

    public void getMovie() {
        String url = "https://api.douban.com/v2/movie/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        MovieService movieService = retrofit.create(MovieService.class);


        final Subscription subscribe = movieService.getTopMovie(0, 10)
                .subscribeOn(Schedulers.io())

                .observeOn(Schedulers.computation())
                .flatMap(new Func1<MovieEntity, Observable<MovieEntity.SubjectsBean>>() {
                    @Override
                    public Observable<MovieEntity.SubjectsBean> call(MovieEntity movieEntity) {
                        return Observable.from(movieEntity.getSubjects());
                    }
                })
                .observeOn(Schedulers.newThread())
                .map(new Func1<MovieEntity.SubjectsBean, MovieEntity.SubjectsBean>() {
                    @Override
                    public MovieEntity.SubjectsBean call(MovieEntity.SubjectsBean subjectsBean) {
                        return subjectsBean;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieEntity.SubjectsBean>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(RxReActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        resultTV.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieEntity.SubjectsBean subjectsBean) {
                        LogUtils.i(subjectsBean.getTitle());
                        resultTV.setText(subjectsBean.getTitle());
                    }
                });

        resultTV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("泰坦尼克号")) {
                    subscribe.unsubscribe();
                    LogUtils.i("TextWatcher取消");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                subscriber.onNext("11");
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.newThread())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        subscribe.unsubscribe();
//                        LogUtils.i("取消");
//                    }
//                });


//        HttpMethods.getInstance().getTopMovie(new Subscriber<MovieEntity>() {
//            @Override
//            public void onCompleted() {
//                Toast.makeText(RxReActivity.this, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                resultTV.setText(throwable.getMessage());
//            }
//
//            @Override
//            public void onNext(MovieEntity movieEntity) {
//                resultTV.setText(movieEntity.toString());
//            }
//        }, 0, 10);
    }

    public void downFile() {
        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(final long bytesRead, final long contentLength, boolean done) {
//                LogUtils.i("update" + Thread.currentThread().getName());
//                System.out.println(bytesRead);
//                System.out.println(contentLength);
//                System.out.println(done);
//                System.out.format("%d%% done\n", (100 * bytesRead) / contentLength);
                Observable.just(1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer integer) {
                                Toast.makeText(getApplication(), ((100 * bytesRead) / contentLength) + "%", Toast.LENGTH_SHORT).show();
                            }
                        });

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
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.111").client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();

        FileWebService hehe = retrofit.create(FileWebService.class);
        hehe.getFile(2015)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<ResponseBody, Observable<File>>() {
                             @Override
                             public Observable<File> call(ResponseBody response) {
                                 LogUtils.i("onResponse" + Thread.currentThread().getName());
                                 File file = null;
                                 OutputStream outputStream = null;
                                 BufferedOutputStream stream = null;
                                 try {
                                     byte[] bytes = response.bytes();
                                     file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/aa.jpg");
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
                                 return Observable.just(file);
                             }
                         }
                ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.i("完成" + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i("onError" + e.getMessage() + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(File file) {
                        LogUtils.i("onNext" + file.getAbsolutePath() + Thread.currentThread().getName());
                    }
                });
    }

    public interface FileWebService {
        @GET("/files/{fileId}.jpg")
        @Headers({"Content-Type: image/jpeg"})
        Observable<ResponseBody> getFile(@Path("fileId") int fileId);
    }
}
