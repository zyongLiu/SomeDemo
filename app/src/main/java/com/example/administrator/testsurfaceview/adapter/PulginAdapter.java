package com.example.administrator.testsurfaceview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.bean.ApkItem;
import com.morgoo.droidplugin.pm.PluginManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu on 2016/11/10.
 */
public class PulginAdapter extends BaseAdapter {
    private Context mContext;
    private List<ApkItem> installedApks;
    private List<ApkItem> noinstallApks;

    public static final int ITEM_TITLE = 0;
    public static final int ITEM_CONTENT = 1;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClickListener(View v, int position,boolean installed);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PulginAdapter(Context context) {
        this.mContext = context;
        installedApks = new ArrayList<>();
        noinstallApks = new ArrayList<>();
    }

    public PulginAdapter(Context context, List<ApkItem> installed, List<ApkItem> noinstall) {
        this.mContext = context;
        this.installedApks = installed;
        this.noinstallApks = noinstall;
    }

    public void addApkItem(ApkItem apkItem, boolean installed) {
        if (installed) {
            installedApks.add(apkItem);
        } else {
            noinstallApks.add(apkItem);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == installedApks.size() + 1) {
            return ITEM_TITLE;
        } else {
            return ITEM_CONTENT;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return 2 + installedApks.size() + noinstallApks.size();
    }

    @Override
    public Object getItem(int position) {
        if (position > 0 && position < installedApks.size() + 1) {
            return installedApks.get(position - 1);
        } else if (position > installedApks.size() + 1) {
            return noinstallApks.get(position - installedApks.size() - 2);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == ITEM_TITLE) {
            TextView textView = new TextView(mContext);
            if (position == 0) {
                textView.setText("已安装");
            } else {
                textView.setText("未安装");
            }
            return textView;
        } else {
            if (position > installedApks.size() + 1){
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.apk_item, null);
                }
                ApkItem item = (ApkItem) getItem(position);

                ImageView icon = (ImageView) convertView.findViewById(R.id.imageView);
                icon.setImageDrawable(item.icon);

                TextView title = (TextView) convertView.findViewById(R.id.textView1);
                title.setText(item.title);

                TextView version = (TextView) convertView.findViewById(R.id.textView2);
                version.setText(String.format("%s(%s)", item.versionName, item.versionCode));

                TextView btn3 = (TextView) convertView.findViewById(R.id.button3);
                btn3.setText("删除");
                btn3.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClickListener(view, position,false);
                        }
                    }
                });
                TextView btn = (TextView) convertView.findViewById(R.id.button2);
                try {
                    if (item.installing) {
                        btn.setText("安装中ing");
                    } else {
                        if (PluginManager.getInstance().isConnected()) {
                            btn.setText(PluginManager.getInstance().getPackageInfo(item.packageInfo.packageName, 0) != null ? "已经安装" : "安装");
                        } else {
                            btn.setText("等待初始化服务");
                        }
                    }
                } catch (Exception e) {
                    btn.setText("安装1");
                }
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClickListener(view, position,false);
                        }
                    }
                });
                return convertView;
            }else {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.apk_item, null);
                }
                ApkItem item = (ApkItem) getItem(position);

                ImageView icon = (ImageView) convertView.findViewById(R.id.imageView);
                icon.setImageDrawable(item.icon);

                final TextView title = (TextView) convertView.findViewById(R.id.textView1);
                title.setText(item.title);

                final TextView version = (TextView) convertView.findViewById(R.id.textView2);
                version.setText(String.format("%s(%s)", item.versionName, item.versionCode));

                TextView btn = (TextView) convertView.findViewById(R.id.button2);
                btn.setText("打开");
                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClickListener(view, position,true);
                        }
                    }
                });

                btn = (TextView) convertView.findViewById(R.id.button3);
                btn.setText("卸载");
                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClickListener(view, position,true);
                        }
                    }
                });

                return convertView;
            }
        }
    }

    public void removeApkItem(ApkItem iremovedItem) {
        installedApks.remove(iremovedItem);
        notifyDataSetChanged();
    }
}
