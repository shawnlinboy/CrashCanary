package me.linshen.appcracker.demo;

import android.app.Application;

import me.linshen.android.crashcanary.CrashCanary;

/**
 * Created by linshen on 16-7-31.
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashCanary.install(this);
    }
}
