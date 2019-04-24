package com.jeasoon.intent;

import android.app.Application;
import android.content.Context;

public class Util {

    private static final Application sApplication;

    static {
        sApplication = getApplicationForce();
    }

    public static Application getApplicationForce() {
        Application app = (Application) ReflectUtil.invokeStatic("android.app.ActivityThread", "currentApplication");
        if (app != null) {
            return app;
        }
        Context context = (Context) ReflectUtil.fieldStatic("android.app.ActivityThread", "mSystemContext");
        if (context != null) {
            app = (Application) context.getApplicationContext();
        }
        return app;
    }

    public static Application getApplication() {
        return sApplication;
    }
}
