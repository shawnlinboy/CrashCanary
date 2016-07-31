package me.linshen.android.crashcanary;

/**
 * Created by linshen on 16-7-31.
 */
public abstract class CrashListener {
    public abstract void onCrash(Thread thread, Throwable throwable);

}
