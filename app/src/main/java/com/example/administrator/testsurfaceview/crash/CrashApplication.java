package com.example.administrator.testsurfaceview.crash;


import android.app.Application;

import com.morgoo.droidplugin.PluginApplication;

public class CrashApplication extends PluginApplication {
	@Override
	public void onCreate() {
		super.onCreate();

		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());


	}
}
