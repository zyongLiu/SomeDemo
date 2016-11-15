package com.example.administrator.testsurfaceview.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.testsurfaceview.R;

/**
 * Created by Liu on 2016/11/10.
 */
public class MainActivity extends Activity{
    private ListView lv;
    private String[] datas=new String[]{"Pulgin","SurfaceView","Touch","RetrofitActivity","RxJavaActivity","RxJava + Retrofit","Okhttp"};
    private MainAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_activity_view);
        lv= (ListView) findViewById(R.id.lv);
        mAdapter=new MainAdapter();
        lv.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=null;
                switch (position){
                    case 0:
                        intent=new Intent(getApplicationContext(),PulginActivity.class);
                        break;
                    case 1:
                        intent=new Intent(getApplicationContext(),SurfaceViewActivity.class);
                        break;
                    case 2:
                        intent=new Intent(getApplicationContext(),TouchActivity.class);
                        break;
                    case 3:
                        intent=new Intent(getApplicationContext(),RetrofitActivity.class);
                        break;
                    case 4:
                        intent=new Intent(getApplicationContext(),RxJavaActivity.class);
                        break;
                    case 5:
                        intent=new Intent(getApplicationContext(),RxReActivity.class);
                        break;
                    case 6:
                        intent=new Intent(getApplicationContext(),OkhttpActivity.class);
                        break;
                    default:

                }
                startActivity(intent);
            }
        });
    }

    public class MainAdapter extends BaseAdapter{

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
            TextView tv =new TextView(getApplication());
            tv.setText(datas[position]);
            tv.setTextSize(18);
            tv.setTextColor(Color.BLACK);
            return tv;
        }
    }
}
