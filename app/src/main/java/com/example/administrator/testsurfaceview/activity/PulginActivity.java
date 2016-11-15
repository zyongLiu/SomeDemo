package com.example.administrator.testsurfaceview.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.testsurfaceview.R;
import com.example.administrator.testsurfaceview.bean.ApkItem;
import com.example.administrator.testsurfaceview.adapter.PulginAdapter;
import com.morgoo.droidplugin.pm.PluginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.morgoo.helper.compat.PackageManagerCompat.INSTALL_FAILED_NOT_SUPPORT_ABI;
import static com.morgoo.helper.compat.PackageManagerCompat.INSTALL_SUCCEEDED;

/**
 * Created by Liu on 2016/11/8.
 */
public class PulginActivity extends Activity implements ServiceConnection {

    private ListView lv;
    final Handler handler = new Handler();
    private String[] datas;

    private PulginAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_list_activity_view);
        mMyBroadcastReceiver.registerReceiver(getApplication());
        lv = (ListView) findViewById(R.id.lv);

        mAdapter = new PulginAdapter(this);
        mAdapter.setOnItemClickListener(new PulginAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position, boolean installed) {
                final ApkItem item = (ApkItem) mAdapter.getItem(position);

                if (installed) {
                    if (v.getId() == R.id.button2) {

                        PackageManager pm = getPackageManager();
                        Intent intent = pm.getLaunchIntentForPackage(item.packageInfo.packageName);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (v.getId() == R.id.button3) {
                        doUninstall(item);
                    }
                } else {


                    if (v.getId() == R.id.button2) {

//                    PackageManager pm = getPackageManager();
//                    Intent intent = pm.getLaunchIntentForPackage(item.packageInfo.packageName);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);


                        if (item.installing) {
                            return;
                        }
                        if (!PluginManager.getInstance().isConnected()) {
                            Toast.makeText(getApplication(), "插件服务正在初始化，请稍后再试。。。", Toast.LENGTH_SHORT).show();
                        }
                        try {
                            if (PluginManager.getInstance().getPackageInfo(item.packageInfo.packageName, 0) != null) {
                                Toast.makeText(getApplication(), "已经安装了，不能再安装", Toast.LENGTH_SHORT).show();
                            } else {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        doInstall(item);
                                    }
                                }.start();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                PluginManager.getInstance().installPackage(item.apkfile, 0);
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                            mAdapter.removeApkItem(item);
                        }


                    } else if (v.getId() == R.id.button3) {
                        doUninstall(item);
                    }
                }
            }
        });
        lv.setAdapter(mAdapter);
        if (PluginManager.getInstance().isConnected()) {
            startLoad();
        } else {
            PluginManager.getInstance().addServiceConnection(this);
        }
    }

    private synchronized void doInstall(ApkItem item) {
        item.installing = true;

        handler.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
        try {
            final int re = PluginManager.getInstance().installPackage(item.apkfile, 0);
            item.installing = false;

            handler.post(new Runnable() {
                @Override
                public void run() {
                    switch (re) {
                        case PluginManager.INSTALL_FAILED_NO_REQUESTEDPERMISSION:
                            Toast.makeText(getApplication(), "安装失败，文件请求的权限太多", Toast.LENGTH_SHORT).show();
                            break;
                        case INSTALL_FAILED_NOT_SUPPORT_ABI:
                            Toast.makeText(getApplication(), "宿主不支持插件的abi环境，可能宿主运行时为64位，但插件只支持32位", Toast.LENGTH_SHORT).show();
                            break;
                        case INSTALL_SUCCEEDED:
                            Toast.makeText(getApplication(), "安装完成", Toast.LENGTH_SHORT).show();
                            mAdapter.notifyDataSetChanged();
                            break;
                    }

                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void startLoad() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            startLoadInner();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x1) {
            if (permissions != null && permissions.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    String permisson = permissions[i];
                    int grantResult = grantResults[i];
                    if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permisson)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            startLoadInner();
                        } else {
                            Toast.makeText(this, "没有授权，无法使用", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                for (String permisson : permissions) {

                }
            }
        }
    }

    private void startLoadInner() {
        //查找未安装
        new Thread("ApkScanner") {
            @Override
            public void run() {
                File file = Environment.getExternalStorageDirectory();

                List<File> apks = new ArrayList<File>(10);
                File[] files = file.listFiles();
                if (files != null) {
                    for (File apk : files) {
                        if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                            apks.add(apk);
                        }
                    }
                }
                file = new File(Environment.getExternalStorageDirectory(), "360Download");
                if (file.exists() && file.isDirectory()) {
                    File[] files1 = file.listFiles();
                    if (files1 != null) {
                        for (File apk : files1) {
                            if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                                apks.add(apk);
                            }
                        }
                    }
                }
                PackageManager pm = getPackageManager();
                for (final File apk : apks) {
                    try {
                        if (apk.exists() && apk.getPath().toLowerCase().endsWith(".apk")) {
                            final PackageInfo info = pm.getPackageArchiveInfo(apk.getPath(), 0);
                            if (info != null) {
                                try {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.addApkItem(new ApkItem(getApplication(), info, apk.getPath()), false);
                                        }
                                    });
                                } catch (Exception e) {
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }.start();
        //查找已安装
        new Thread("ApkScannerInstalled") {
            @Override
            public void run() {
                try {
                    final List<PackageInfo> infos = PluginManager.getInstance().getInstalledPackages(0);
                    final PackageManager pm = getPackageManager();
                    for (final PackageInfo info : infos) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.addApkItem(new ApkItem(pm, info, info.applicationInfo.publicSourceDir), true);
                            }
                        });
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        startLoad();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onDestroy() {
        PluginManager.getInstance().removeServiceConnection(this);
        mMyBroadcastReceiver.unregisterReceiver(getApplication());
        super.onDestroy();
    }


    private void doUninstall(final ApkItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("警告，你确定要删除么？");
        builder.setMessage("警告，你确定要删除" + item.title + "么？");
        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!PluginManager.getInstance().isConnected()) {
                    Toast.makeText(getApplicationContext(), "服务未连接", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        PluginManager.getInstance().deletePackage(item.packageInfo.packageName, 0);
                        Toast.makeText(getApplicationContext(), "删除完成", Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.setNeutralButton("取消", null);
        builder.show();
    }

    private MyBroadcastReceiver mMyBroadcastReceiver = new MyBroadcastReceiver();


    private class MyBroadcastReceiver extends BroadcastReceiver {

        void registerReceiver(Context con) {
            IntentFilter f = new IntentFilter();
            f.addAction(PluginManager.ACTION_PACKAGE_ADDED);
            f.addAction(PluginManager.ACTION_PACKAGE_REMOVED);
            f.addDataScheme("package");
            con.registerReceiver(this, f);
        }

        void unregisterReceiver(Context con) {
            con.unregisterReceiver(this);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (PluginManager.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                try {
                    PackageManager pm = getPackageManager();
                    String pkg = intent.getData().getAuthority();
                    PackageInfo info = PluginManager.getInstance().getPackageInfo(pkg, 0);
                    mAdapter.addApkItem(new ApkItem(pm, info, info.applicationInfo.publicSourceDir), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (PluginManager.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
                String pkg = intent.getData().getAuthority();
                int N = mAdapter.getCount();
                ApkItem iremovedItem = null;
                for (int i = 0; i < N; i++) {
                    ApkItem item = (ApkItem) mAdapter.getItem(i);
                    if (item != null && TextUtils.equals(item.packageInfo.packageName, pkg)) {
                        iremovedItem = item;
                        break;
                    }
                }
                if (iremovedItem != null) {
                    mAdapter.removeApkItem(iremovedItem);
                }
            }
        }
    }
}
