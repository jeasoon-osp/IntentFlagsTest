package com.jeasoon.intent;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Build;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

class LogWindow {

    @BindView(R.id.jeasoon_tv_content)
    TextView tvLogView;

    private          Application                mApplication;
    private volatile Thread                     mLogTrackThread;
    private          WindowManager              mWindowManager;
    private          WindowManager.LayoutParams mWindowLayoutParams;
    private          FrameLayout                flContainer;
    private          Set<String>                mFilterTags;
    private          boolean                    logWindowOpened;

    @SuppressLint("InflateParams")
    LogWindow() {
        mFilterTags = new HashSet<>();
        mApplication = Util.getApplication();
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) mApplication.getSystemService(Context.WINDOW_SERVICE);
        flContainer = (FrameLayout) LayoutInflater.from(mApplication).inflate(R.layout.window_main_log, null);
        ButterKnife.bind(this, flContainer);
        tvLogView.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvLogView.setHorizontallyScrolling(true);
        initWindowLayoutParams();
    }

    private void initWindowLayoutParams() {
        DisplayMetrics             metrics      = mApplication.getResources().getDisplayMetrics();
        WindowManager.LayoutParams layoutParams = mWindowLayoutParams;
        layoutParams.x = 0;
        layoutParams.y = 140;
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = metrics.heightPixels / 3;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    @OnTouch({
            R.id.jeasoon_v_handle_bar_start,
            R.id.jeasoon_v_handle_bar_top,
            R.id.jeasoon_v_handle_bar_end,
            R.id.jeasoon_v_handle_bar_bottom
    })
    boolean onTouchHandle(View v, MotionEvent event) {
        PointF lastPoint = (PointF) v.getTag();
        if (lastPoint == null) {
            lastPoint = new PointF();
            v.setTag(lastPoint);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastPoint.x = event.getRawX();
                lastPoint.y = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float currentX = event.getRawX();
                float currentY = event.getRawY();
                float deltaX = currentX - lastPoint.x;
                float deltaY = currentY - lastPoint.y;
                lastPoint.x = currentX;
                lastPoint.y = currentY;
                mWindowLayoutParams.x += deltaX;
                mWindowLayoutParams.y += deltaY;
                mWindowManager.updateViewLayout(flContainer, mWindowLayoutParams);
                break;
        }
        return true;
    }

    void openLogWindow() {
        closeLogWindow();
        doOpenLogWindow();
    }

    void closeLogWindow() {
        doCloseLogWindow();
    }

    void startLogTracker() {
        doStartLogTracker();
    }

    void stopLogTracker() {
        doStopLogTracker();
    }

    boolean isLogWindowOpened() {
        return logWindowOpened;
    }

    void addFilterTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        mFilterTags.add(tag);
    }

    void removeFilterTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        mFilterTags.remove(tag);
    }

    private void doOpenLogWindow() {
        mWindowManager.addView(flContainer, mWindowLayoutParams);
        boolean shouldAutoScroll = !tvLogView.canScrollVertically(1);
        int     textHeight       = tvLogView.getLineHeight() * tvLogView.getLineCount();
        int     viewHeight       = tvLogView.getMeasuredHeight();
        if (shouldAutoScroll && viewHeight <= textHeight) {
            tvLogView.scrollBy(0, textHeight - tvLogView.getScrollY() - viewHeight);
        }
        logWindowOpened = true;
    }

    private void doCloseLogWindow() {
        if (!logWindowOpened) {
            return;
        }
        mWindowManager.removeViewImmediate(flContainer);
        logWindowOpened = false;
    }

    private void doStartLogTracker() {
        synchronized (this) {
            if (mLogTrackThread != null && !mLogTrackThread.isInterrupted() && mLogTrackThread.isAlive()) {
                return;
            }
        }
        Thread thread = new Thread(() -> {
            Process        clearProcess  = null;
            Process        logcatProcess = null;
            BufferedReader reader        = null;
            try {
                clearProcess = Runtime.getRuntime().exec("logcat -c ");
                clearProcess.waitFor();
                logcatProcess = Runtime.getRuntime().exec("logcat");
                reader = new BufferedReader(new InputStreamReader(logcatProcess.getInputStream()));
                String line;
                while (!Thread.currentThread().isInterrupted() && (line = reader.readLine()) != null) {
                    if (tvLogView != null) {
                        boolean hit = false;
                        for (String tag : mFilterTags) {
                            hit = line.contains(tag);
                            if (hit) {
                                break;
                            }
                        }
                        if (!hit) {
                            continue;
                        }
                        String nextLine = line;
                        tvLogView.post(() -> {
                            boolean shouldAutoScroll = !tvLogView.canScrollVertically(1);
                            tvLogView.append(nextLine);
                            tvLogView.append("\n");
                            int textHeight = tvLogView.getLineHeight() * tvLogView.getLineCount();
                            int viewHeight = tvLogView.getMeasuredHeight();
                            if (shouldAutoScroll && viewHeight <= textHeight) {
                                tvLogView.scrollBy(0, textHeight - tvLogView.getScrollY() - viewHeight);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException ignored) {
                    }
                }
                if (clearProcess != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        clearProcess.destroyForcibly();
                    } else {
                        clearProcess.destroy();
                    }
                }
                if (logcatProcess != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        logcatProcess.destroyForcibly();
                    } else {
                        logcatProcess.destroy();
                    }
                }
                synchronized (this) {
                    if (mLogTrackThread == Thread.currentThread()) {
                        mLogTrackThread = null;
                    }
                }
            }
        });
        synchronized (this) {
            mLogTrackThread = thread;
        }
        thread.start();
    }

    private void doStopLogTracker() {
        synchronized (this) {
            if (mLogTrackThread != null) {
                mLogTrackThread.interrupt();
                mLogTrackThread = null;
            }
        }
    }

}
