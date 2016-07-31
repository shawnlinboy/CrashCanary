package me.linshen.android.crashcanary;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by linshen on 16-7-31.
 */
public class CrashLogger {

    private static final String TAG = "CrashLogger";

    public static final String CRASH_LOG_FILE_PATH = Environment.getExternalStorageDirectory()
            .getPath() + File.separator + "Android" + File.separator + "data";
    private static final String CRASH_LOG_FILE_NAME = "crashLog.txt";
    private String mPath;

    public CrashLogger(@NonNull Context context) {
        final String path = CRASH_LOG_FILE_PATH + File.separator + context.getPackageName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initDir(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void writeLog(final Throwable throwable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    writeToFile(throwable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initDir(String path) throws IOException {
        if (isExternalStorageWritable()) {
            File file = new File(path);
            File crashLog = new File(file, CRASH_LOG_FILE_NAME);
            mPath = crashLog.getPath();
            if (!file.exists()) {
                if (file.mkdirs()) {
                    if (!crashLog.exists()) {
                        crashLog.createNewFile();
                    }
                } else {
                    Log.e(TAG, "error creating crash log folder");
                }
            }

        }
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private String generateDeviceInfo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date(System.currentTimeMillis()));
        return "Crashed at " + time + " on " + Build.MODEL + ", trace is:" + "\n";
    }

    private void writeToFile(Throwable throwable) throws IOException {
        Writer stringBuffSync = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringBuffSync);
        throwable.printStackTrace(printWriter);
        StringBuilder sb = new StringBuilder(generateDeviceInfo());
        sb.append(stringBuffSync.toString());
        sb.append("\n\n");
        printWriter.close();
        BufferedWriter bos = null;
        try {
            bos = new BufferedWriter(new FileWriter(mPath, true));
            bos.write(sb.toString());
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

}
