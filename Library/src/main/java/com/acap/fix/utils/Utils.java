package com.acap.fix.utils;

//import org.chickenhook.restrictionbypass.Unseal;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.List;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/14 14:15
 * </pre>
 */
public class Utils {

    // 如果需要，绕过隐藏的API强制策略
    public static void bypassHiddenAPIEnforcementPolicyIfNeeded() {
//        try {
//            Unseal.unseal();
//        } catch (Exception e) {
//            LogUtils.e(e);
//        }
//        if (BuildCompat.isPie()) {
//            try {
//                Method forNameMethod = Class.class.getDeclaredMethod("forName", String.class);
//                Class<?> clazz = (Class<?>) forNameMethod.invoke(null, "dalvik.system.VMRuntime");
//                Method getMethodMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
//                Method getRuntime = (Method) getMethodMethod.invoke(clazz, "getRuntime", new Class[0]);
//                Method setHiddenApiExemptions = (Method) getMethodMethod.invoke(clazz, "setHiddenApiExemptions", new Class[]{String[].class});
//                Object runtime = getRuntime.invoke(null);
//                setHiddenApiExemptions.invoke(runtime, new Object[]{new String[]{"L"}});
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 打印层级关系
     *
     * @param cls
     */
    public static void printHierarchy(Class cls) {
        if (cls == null || cls == Object.class) return;
        LogUtils.i(MessageFormat.format("-------------- {0} --------------", cls));
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            LogUtils.i("-->" + method);
        }
        printHierarchy(cls.getSuperclass());
    }


    public static Method exactMethod(Class<?> type, String name, Class<?>... types) throws NoSuchMethodException {

        try {
            return type.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            do {
                try {
                    return type.getDeclaredMethod(name, types);
                } catch (NoSuchMethodException ignore) {
                }

                type = type.getSuperclass();
            } while (type != null);

            throw new NoSuchMethodException();
        }
    }

    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getCurrentProcessName(context));
    }

    private static String getCurrentProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) return null;
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info == null || info.size() == 0) return null;
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
}
