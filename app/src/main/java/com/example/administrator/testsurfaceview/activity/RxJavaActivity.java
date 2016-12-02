package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.bean.Student;
import com.example.administrator.testsurfaceview.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Liu on 2016/11/11.
 */
public class RxJavaActivity extends Activity {
    @BindView(R.id.click_me_BN)
    Button clickMeBN;
    @BindView(R.id.result_TV)
    TextView resultTV;
    @BindView(R.id.imageView)
    ImageView imageView;

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

    public void getMovie() {
//        //        Observable 即被观察者
//        Observable observer = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("Hello");
//                subscriber.onNext("Liu");
//                subscriber.onNext("Hi");
//                subscriber.onCompleted();
//            }
//        });
//        //        implements Observer 观察者
//        Subscriber<String> subscriber = new Subscriber<String>() {
//            @Override
//            public void onStart() {
//                super.onStart();
//            }
//
//            @Override
//            public void onCompleted() {
//                LogUtils.i("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                LogUtils.i("Error!");
//            }
//
//            @Override
//            public void onNext(String s) {
//                LogUtils.i("Item:" + s);
//            }
//        };
//        // 观察者 与 被观察者绑定
//        observer.subscribe(subscriber);

////////////////////////////////////////////////////////////////////////////////
//        Action1<String> onNextAction=new Action1<String>() {
//            @Override
//            public void call(String s) {
//                LogUtils.i(s);
//            }
//        };
//        Action1<Throwable> onErrorAction=new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//
//            }
//        };
//        Action0 onCompletedAction = new Action0() {
//            @Override
//            public void call() {
//                LogUtils.i("completed");
//            }
//        };
//        //将观察者的方法分开了
//        observer.subscribe(onNextAction);
//        observer.subscribe(onNextAction,onErrorAction);
//        observer.subscribe(onNextAction,onErrorAction,onCompletedAction);
////////////////////////////////////////////////////////////////////////////////


//        String[] names=new String[]{"AA","BB","CC","DD"};
//
//        Observable.from(Arrays.asList(names))
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String s) {
//                        LogUtils.i(s);
//                    }
//                });
////////////////////////////////////////////////////////////////////////////////////////////////

//        Observable.create(new Observable.OnSubscribe<Drawable>() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void call(Subscriber<? super Drawable> subscriber) {
//                LogUtils.i("Call:"+Thread.currentThread().getName());
//                Drawable dr=getTheme().getDrawable(R.mipmap.ic_launcher);
//                subscriber.onNext(dr);
//                subscriber.onCompleted();
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Drawable>() {
//            @Override
//            public void onCompleted() {
//                LogUtils.i("onCompleted"+Thread.currentThread().getName());
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(RxJavaActivity.this, "Error!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNext(Drawable drawable) {
//                LogUtils.i("NEXT:"+Thread.currentThread().getName());
//                imageView.setImageDrawable(drawable);
//            }
//        });

////////////////////////////////////////////////////////////////////////////////////////////////

//        Observable.just(1,2,3,3)
//                .subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
//                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        LogUtils.i("CallThread:"+Thread.currentThread().getName()+",number:"+integer);
//                    }
//                });


//////////////////////变换//////////////////////////////////////////////////////////////////////////
//        Observable.just("http://192.168.1.144:89/PICS/MOSAICHREF000.20160913.190000.png")
//                .map(new Func1<String, Bitmap>() {
//                    @Override
//                    public Bitmap call(String s) {
//                        try {
//                            return Picasso.with(getApplicationContext()).load(s).config(Bitmap.Config.RGB_565).get();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            return null;
//                        }
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Bitmap>() {
//                    @Override
//                    public void call(Bitmap bitmap) {
//                        LogUtils.i((bitmap==null)+"");
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });
//////////////////////变换//////////////////////////////////////////////////////////////////////////
        final Student[] students = new Student[]{
                new Student("张三", new String[]{"语文", "数学", "外语"}),
                new Student("李四", new String[]{"语文", "数学"})
        };
//        Observable.from(students).map(new Func1<Student, String>() {
//            @Override
//            public String call(Student student) {
//                return student.getName();
//            }
//        }).subscribe(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                LogUtils.i(s);
//            }
//        });
//        Observable.from(students).flatMap(new Func1<Student, Observable<String>>() {
//            @Override
//            public Observable<String> call(Student student) {
//                return Observable.from(student.getCourses());
//            }
//        }).subscribe(new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//                LogUtils.i("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                LogUtils.i(s);
//            }
//        });

/////////////////////////////////////////////////////////////////////////////////////////////////////////
        Observable.just(1, 2, 3, 4) // IO 线程，由 subscribeOn() 指定
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        LogUtils.i("1线程:" + Thread.currentThread().getName());
                        return integer + "呵呵";
                    }
                }) // 新线程，由 observeOn() 指定
                .observeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        LogUtils.i("2线程:" + Thread.currentThread().getName());
                        return "哈哈" + s;
                    }
                }) // IO 线程，由 observeOn() 指定
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i("onError:" + e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        LogUtils.i("onNext:" + s);
                    }
                });  // Android 主线程，由 observeOn() 指定
    }
}
