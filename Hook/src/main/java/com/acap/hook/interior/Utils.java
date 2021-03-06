package com.acap.hook.interior;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.acap.hook.runtime.LifecycleStateRequest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;

import de.robv.android.xposed.XposedHelpers;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/11/12 18:05
 * </pre>
 */
public class Utils {


    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getCurrentProcessName(context));
    }

    private static String getCurrentProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            return null;
        }
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) {
            return null;
        }
        int pid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo aInfo : info) {
            if (aInfo.pid == pid) {
                if (aInfo.processName != null) {
                    return aInfo.processName;
                }
            }
        }
        return "";
    }

    public static void print(Object o) {
        try {
            print(o, o.getClass());
        } catch (Throwable e) {
            HookLogs.e(e);
        }
    }

    public static void print(Class<?> cls) {
        try {
            print(null, cls);
        } catch (Throwable e) {
            HookLogs.e(e);
        }
    }

    private static void print(Object o, Class<?> cls) throws Throwable {
        if (cls == null || cls == Object.class) {
            return;
        }

        HookLogs.i(MessageFormat.format("-------------------------------- {0} --------------------------------", cls.getName()));
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field item : declaredFields) {
            if (o == null) {
                HookLogs.i(MessageFormat.format("{0} {1} ", item.getType().getSimpleName(), item.getName()));
            } else {
                item.setAccessible(true);
                HookLogs.i(MessageFormat.format("{0} {1} = {2}", item.getType().getSimpleName(), item.getName(), item.get(o)));

                if ("mActivityToken".equals(item.getName())) {
                    print(item.get(o));
                }
            }
        }
        Method[] declaredMethods = cls.getDeclaredMethods();
        for (Method item : declaredMethods) {
            HookLogs.i(MessageFormat.format("fun {0} {1}()", item.getReturnType().getSimpleName(), item.getName()));
        }
        print(o, cls.getSuperclass());
    }

}
