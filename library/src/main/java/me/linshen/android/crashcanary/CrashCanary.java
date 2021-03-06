package me.linshen.android.crashcanary;

import android.app.Application;

/**
 * Created by linshen on 16-7-31.
 */
public class CrashCanary {

    private static final String TAG = "CrashCanary";

    private CrashCanary() {
    }

    public static void install(Application application) {
        install(application, null);
    }

    public static void install(Application application, CrashListener listener) {
        if (!FireLooper.isSafe()) {
            CrashHandler crashHandler = new CrashHandler();
            crashHandler.setCrashListener(listener);
            application.registerActivityLifecycleCallbacks(crashHandler.getLifecycleCallbacks());
            FireLooper.install();
            FireLooper.setUncaughtExceptionHandler(crashHandler);
            Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        }
    }

}
