package me.linshen.android.crashcanary;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by linshen on 16-7-31.
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {
    private WeakReference<Activity> mActivity;
    private Application.ActivityLifecycleCallbacks lifecycleCallbacks;
    private CrashListener crashListener;
    private CrashLogger mLogger;

    public CrashHandler() {
        lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                mActivity = new WeakReference<>(activity);
                if (mLogger == null) {
                    mLogger = new CrashLogger(mActivity.get());
                }
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        };
    }

    public void setCrashListener(CrashListener crashListener) {
        this.crashListener = crashListener;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (crashListener != null) {
            crashListener.onCrash(thread, throwable);
        }
        if (mLogger != null) {
            mLogger.writeLog(throwable);
        }
        if (mActivity.get() != null) {
            Log.e(mActivity.get().getPackageName(), Log.getStackTraceString(throwable));
            mActivity.get().finish();
        }
    }


    public Application.ActivityLifecycleCallbacks getLifecycleCallbacks() {
        return lifecycleCallbacks;
    }
}
