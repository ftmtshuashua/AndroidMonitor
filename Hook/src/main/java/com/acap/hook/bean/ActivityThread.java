package com.acap.hook.bean;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;

import com.acap.hook.interior.HookLogs;
import com.swift.sandhook.xposedcompat.XposedCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import de.robv.android.xposed.XposedHelpers;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/11/13 0:54
 * </pre>
 *
 * @author A·Cap
 */
public class ActivityThread {
    private static ActivityThread mInstance;

    private Object mCurrentActivityThread;
    private Object mActivities;

    public ActivityThread() {
        try {
            mCurrentActivityThread = getActivityThread();
            this.mActivities = XposedHelpers.findField(mCurrentActivityThread.getClass(), "mActivities").get(mCurrentActivityThread);
        } catch (Throwable e) {
            HookLogs.e(e);
        }
    }

    public Activity getActivity(IBinder binder) {
        try {
            Object ActivityClientRecord = Map.class.getMethod("get", Object.class).invoke(mActivities, binder);
            Object activity = XposedHelpers.getObjectField(ActivityClientRecord, "activity");
            return (Activity) activity;
        } catch (Throwable e) {
            HookLogs.e(e);
        }
        return null;
    }

    public static ActivityThread getInstance() {
        if (mInstance == null) {
            synchronized (ActivityThread.class) {
                if (mInstance == null) {
                    mInstance = new ActivityThread();
                }
            }
        }
        return mInstance;
    }

    private static Object getActivityThread() {
        try {
            Context context = XposedCompat.context;
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method m = activityThread.getMethod("currentActivityThread", new Class[0]);
            m.setAccessible(true);
            Object mCurrentActivityThread = m.invoke(null, new Object[0]);
            if ((mCurrentActivityThread == null) && (context != null)) {
                Field mLoadedApk = context.getClass().getField("mLoadedApk");
                mLoadedApk.setAccessible(true);
                Object apk = mLoadedApk.get(context);
                Field mActivityThreadField = apk.getClass().getDeclaredField("mActivityThread");
                mActivityThreadField.setAccessible(true);
                mCurrentActivityThread = mActivityThreadField.get(apk);
            }
            return mCurrentActivityThread;
        } catch (Throwable ignore) {
            HookLogs.e(ignore);
        }
        return null;
    }
}
