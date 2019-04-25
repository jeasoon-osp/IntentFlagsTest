package com.jeasoon.intent;

import android.annotation.SuppressLint;
import android.app.Application;

public class IntentApplication extends Application {

    private static final String TAG = "zjs";

    @SuppressLint("StaticFieldLeak")
    private static IntentApplication sInstance;

    private LogWindow mLogWindow;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mLogWindow = new LogWindow();
        mLogWindow.addFilterTag(TAG);
        mLogWindow.startLogTracker();
    }

    public static void openLogWindow() {
        sInstance.mLogWindow.openLogWindow();
    }

    public static void closeLogWindow() {
        sInstance.mLogWindow.closeLogWindow();
    }

    public static boolean isLogWindowOpened() {
        return sInstance.mLogWindow.isLogWindowOpened();
    }

    public static void addLogFilterTag(String tag) {
        sInstance.mLogWindow.addFilterTag(tag);
    }

    public static void removeLogFilterTag(String tag) {
        sInstance.mLogWindow.removeFilterTag(tag);
    }

}
