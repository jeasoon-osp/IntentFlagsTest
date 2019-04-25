package com.jeasoon.intent.window;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.jeasoon.intent.R;
import com.jeasoon.intent.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.OnTouch;

public class StackWindow {

    private volatile Thread                     mStackTrackThread;
    private          Application                mApplication;
    private          ActivityManager            mActivityManager;
    private          WindowManager              mWindowManager;
    private          WindowManager.LayoutParams mWindowLayoutParams;
    private          FrameLayout                flContainer;
    private          List<Proc>                 mProcList;
    private          ExecutorService            mExecutorService;
    private          boolean                    stackWindowOpened;

    @SuppressLint("InflateParams")
    public StackWindow() {
        mProcList = new ArrayList<>();
        mApplication = Util.getApplication();
        mExecutorService = Executors.newSingleThreadExecutor();
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowManager = (WindowManager) mApplication.getSystemService(Context.WINDOW_SERVICE);
        mActivityManager = (ActivityManager) mApplication.getSystemService(Context.ACTIVITY_SERVICE);
        flContainer = (FrameLayout) LayoutInflater.from(mApplication).inflate(R.layout.window_main_stack, null);
        ButterKnife.bind(this, flContainer);
        initWindowLayoutParams();
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            startStackTracker();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            startStackTracker();
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

    public void openStackWindow() {
        closeStackWindow();
        doOpenStackWindow();
    }

    public void closeStackWindow() {
        doCloseStackWindow();
    }

    public void startStackTracker() {
        doStartStackTracker();
    }

    public void stopStackTracker() {
        doStopStackTracker();
    }

    public void destroy() {
        mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
        closeStackWindow();
        stopStackTracker();
        mExecutorService.shutdownNow();
    }

    public boolean isStackWindowOpened() {
        return stackWindowOpened;
    }

    private void doOpenStackWindow() {
        mWindowManager.addView(flContainer, mWindowLayoutParams);
        stackWindowOpened = true;
    }

    private void doCloseStackWindow() {
        if (!stackWindowOpened) {
            return;
        }
        mWindowManager.removeViewImmediate(flContainer);
        stackWindowOpened = false;
    }

    private void doStopStackTracker() {
        synchronized (this) {
            if (mStackTrackThread != null) {
                mStackTrackThread.interrupt();
                mStackTrackThread = null;
            }
        }
    }

    @SuppressWarnings("all")
    private void doStartStackTracker() {
        synchronized (this) {
            if (mStackTrackThread != null && !mStackTrackThread.isInterrupted() && mStackTrackThread.isAlive()) {
                return;
            }
        }
        mExecutorService.execute(() -> {
            synchronized (StackWindow.this) {
                mStackTrackThread = Thread.currentThread();
            }
            List<Proc> stackInfo = doCollectStackInfo();
            synchronized (StackWindow.this) {
                if (!Thread.currentThread().isInterrupted() && mStackTrackThread != null) {
                    postProcData(stackInfo);
                }
                mStackTrackThread = null;
            }
            Thread.interrupted();
        });

    }

    private void postProcData(List<Proc> stackInfo) {
        Log.e("zjs", "stackInfo: " + stackInfo.toString());
    }

    @SuppressLint("UseSparseArrays")
    private List<Proc> doCollectStackInfo() {
        Process        stackProcess = null;
        BufferedReader reader       = null;
        List<Proc>     procList     = new ArrayList<>();
        try {
            HashMap<Integer, Proc>                                  myProcessMap  = new HashMap<>();
            HashMap<Integer, ActivityManager.RunningAppProcessInfo> allProcessMap = new HashMap<>();
            List<ActivityManager.RunningAppProcessInfo>             processes     = mActivityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info : processes) {
                allProcessMap.put(info.pid, info);
            }
            stackProcess = Runtime.getRuntime().exec("cat /sdcard/tmp.txt");
//            stackProcess = Runtime.getRuntime().exec(String.format(Locale.ENGLISH, "dumpsys activity %s", mApplication.getPackageName()));
            reader = new BufferedReader(new InputStreamReader(stackProcess.getInputStream()));
            String line;
            String overReadLine = null;
            while (!Thread.currentThread().isInterrupted() && ((line = overReadLine) != null || (line = reader.readLine()) != null)) {
                overReadLine = null;
                line = line.trim();
                if (line.startsWith("TASK")) {
                    Task task = parseTask(line);
                    if (task == null) {
                        continue;
                    }
                    int pid = 0;
                    while (!Thread.currentThread().isInterrupted() && (line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.startsWith("ACTIVITY")) {
                            String activityLine = line;
                            while (!Thread.currentThread().isInterrupted() && (line = reader.readLine()) != null) {
                                line = line.trim();
                                if (line.startsWith("mResumed")) {
                                    Acti activity = parseActivity(activityLine, line);
                                    if (activity == null) {
                                        continue;
                                    }
                                    pid = activity.pid;
                                    task.pid = activity.pid;
                                    task.activityList.offer(activity);
                                    break;
                                } else if (line.startsWith("TASK")) {
                                    overReadLine = line;
                                    break;
                                }
                            }
                            if (overReadLine != null) {
                                break;
                            }
                        }
                    }
                    if (pid == 0) {
                        continue;
                    }
                    Proc proc;
                    proc = myProcessMap.get(pid);
                    if (proc == null) {
                        proc = RecycleContainer.obtain(Proc.class);
                        proc.pid = pid;
                        myProcessMap.put(pid, proc);
                        procList.add(proc);
                        ActivityManager.RunningAppProcessInfo process = allProcessMap.get(pid);
                        if (process != null) {
                            proc.processName = process.processName;
                        }
                    }
                    proc.taskList.push(task);
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
            if (stackProcess != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    stackProcess.destroyForcibly();
                } else {
                    stackProcess.destroy();
                }
            }
        }
        return procList;
    }

    private Task parseTask(String line) {
        String[] taskMsg = line.split("\\s+");
        if (taskMsg.length < 3) {
            return null;
        }
        String[] idMsg = taskMsg[2].split("=");
        if (idMsg.length < 2) {
            return null;
        }
        Task task = RecycleContainer.obtain(Task.class);
        task.taskAffinity = taskMsg[1];
        task.taskId = Integer.parseInt(idMsg[1]);
        return task;
    }

    private Acti parseActivity(String activity, String status) {
        String[] activityMsg = activity.split("\\s+");
        if (activityMsg.length < 4) {
            return null;
        }
        String[] pid = activityMsg[3].split("=");
        if (pid.length < 2) {
            return null;
        }
        String[] activityStatus = status.split("\\s+");
        if (activityStatus.length < 3) {
            return null;
        }
        Acti acti = RecycleContainer.obtain(Acti.class);
        acti.className = activityMsg[1];
        acti.pid = Integer.parseInt(pid[1]);
        acti.id = Integer.parseInt(activityMsg[2], 16);
        acti.isResumed = activityStatus[0].endsWith("true");
        acti.isStopped = activityStatus[1].endsWith("true");
        acti.isFinished = activityStatus[2].endsWith("true");
        return acti;
    }

    private static class RecycleContainer {
        private static final HashMap<Class, LinkedList<WeakReference<Object>>> RECYCLABLE_MAP = new HashMap<>();

        @SuppressWarnings("all")
        static synchronized <T> T obtain(Class<T> clazz) {
            LinkedList<WeakReference<Object>> recyclableList = RECYCLABLE_MAP.get(clazz);
            if (recyclableList == null || recyclableList.isEmpty()) {
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            WeakReference<Object> ref    = recyclableList.pop();
            Object                target = ref.get();
            if (target != null) {
                return (T) target;
            }
            return obtain(clazz);
        }

        static synchronized void recycle(Object obj) {
            LinkedList<WeakReference<Object>> recyclableList = RECYCLABLE_MAP.get(obj.getClass());
            if (recyclableList == null) {
                recyclableList = new LinkedList<>();
                RECYCLABLE_MAP.put(obj.getClass(), recyclableList);
            }
            if (OnRecycle.class.isAssignableFrom(obj.getClass())) {
                ((OnRecycle) obj).onRecycle();
            }
            recyclableList.offer(new WeakReference<>(obj));
        }

        interface OnRecycle {
            void onRecycle();
        }

    }

    static class Proc implements RecycleContainer.OnRecycle {
        int    pid;
        String processName;
        final LinkedList<Task> taskList = new LinkedList<>();

        @Override
        public void onRecycle() {
            pid = 0;
            processName = null;
            taskList.clear();
        }

        @Override
        public String toString() {
            return "Proc{" +
                    "pid=" + Integer.toHexString(pid) +
                    ", processName='" + processName + '\'' +
                    ", taskList=" + taskList +
                    '}';
        }
    }

    static class Task implements RecycleContainer.OnRecycle {
        int    pid;
        int    taskId;
        String taskAffinity;
        final LinkedList<Acti> activityList = new LinkedList<>();

        @Override
        public void onRecycle() {
            pid = 0;
            taskId = 0;
            taskAffinity = null;
            activityList.clear();
        }

        @Override
        public String toString() {
            return "Task{" +
                    "pid=" + Integer.toHexString(pid) +
                    ", taskId=" + Integer.toHexString(taskId) +
                    ", taskAffinity='" + taskAffinity + '\'' +
                    ", activityList=" + activityList +
                    '}';
        }
    }

    static class Acti implements RecycleContainer.OnRecycle {
        int     id;
        int     pid;
        boolean isResumed;
        boolean isStopped;
        boolean isFinished;
        String  className;

        @Override
        public void onRecycle() {
            id = 0;
            pid = 0;
            isResumed = false;
            isStopped = false;
            isFinished = false;
            className = null;
        }

        @Override
        public String toString() {
            return "Acti{" +
                    "id=" + Integer.toHexString(id) +
                    ", pid=" + Integer.toHexString(pid) +
                    ", isResumed=" + isResumed +
                    ", isStopped=" + isStopped +
                    ", isFinished=" + isFinished +
                    ", className='" + className + '\'' +
                    '}';
        }
    }

}
