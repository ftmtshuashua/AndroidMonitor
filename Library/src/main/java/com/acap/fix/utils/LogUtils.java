package com.acap.fix.utils;

import android.util.Log;

import com.acap.fix.WhoFinishMyActivity;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/13 10:17
 * </pre>
 */
public class LogUtils {
    static final String TAG = "WFMA"; // "WhoFinishMyActivity"

    public static void trim(Throwable tr) {
        if (WhoFinishMyActivity.IsPrintStack) {
            String stackTraceString = getStackTraceString(tr);
            String[] split = stackTraceString.split("\n");
            StringBuffer SB = new StringBuffer();
            for (String s : split) {
                String trim = s.trim();
                if (trim.startsWith("at com.acap.fix")) continue;
                if (trim.startsWith("at de.robv.android.xposed.XC_MethodHook")) continue;
                if (trim.startsWith("at com.swift.sandhook.")) continue;
                SB.append(s).append("\n");
            }
            Log.i(TAG, SB.toString());
        } else {
            Log.i(TAG, tr.getMessage());
        }
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void i(Throwable tr) {
        Log.i(TAG, getStackTraceString(tr));
    }

    public static void i(String msg, Throwable tr) {
        Log.i(TAG, msg + "\n" + getStackTraceString(tr));
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(Throwable tr) {
        Log.e(TAG, "", tr);
    }

    public static void e(String msg, Throwable tr) {
        Log.e(TAG, msg, tr);
    }


    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }
}
