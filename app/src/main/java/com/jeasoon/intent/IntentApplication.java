package com.jeasoon.intent;

import android.annotation.SuppressLint;
import android.app.Application;

import com.jeasoon.intent.window.LogWindow;
import com.jeasoon.intent.window.StackWindow;

public class IntentApplication extends Application {

    private static final String TAG = "zjs";

    @SuppressLint("StaticFieldLeak")
    private static IntentApplication sInstance;

    private LogWindow   mLogWindow;
    private StackWindow mStackWindow;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mLogWindow = new LogWindow();
        mStackWindow = new StackWindow();
        mLogWindow.addFilterTag(TAG);
        mLogWindow.startLogTracker();
        mStackWindow.startStackTracker();
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

    public static void openStackWindow() {
        sInstance.mStackWindow.openStackWindow();
    }

    public static void closeStackWindow() {
        sInstance.mStackWindow.closeStackWindow();
    }

    public static boolean isStackWindowOpened() {
        return sInstance.mStackWindow.isStackWindowOpened();
    }

}
