package com.acap.compliance.interior;

import android.util.Log;

import java.text.MessageFormat;

import stack.PrivacyComplianceStack;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/11/12 16:44
 * </pre>
 */
public class Logs {

    //  Who finish my activity !
    private static final String TAG = "HooK-Compliance";

    private static void i(String tag, String msg) {
        Log.i(MessageFormat.format("{0}-{1}", TAG, tag), msg);
    }

    private static void e(String tag, String msg, Throwable e) {
        Log.e(MessageFormat.format("{0}-{1}", TAG, tag), msg, e);
    }

    public static void i(String msg) {
        i("i", msg);
    }

    public static void e(Throwable e) {
        e("e", "", e);
    }

    /**
     * 清理 Throwable 中的无关调用栈
     */
    public static void trim(Throwable tr) {
        String[] split = getString(tr).split("\n");
        StringBuffer SB = new StringBuffer();
        for (String s : split) {
            String trim = s.trim();
            if (trim.startsWith("at com.acap.compliance")) {
                continue;
            }
            if (trim.startsWith("at de.robv.android.xposed.XC_MethodHook")) {
                continue;
            }
            if (trim.startsWith("at com.swift.sandhook.")) {
                continue;
            }
            if (trim.startsWith("at com.acap.hook.")) {
                continue;
            }
            SB.append(s).append("\n");
        }
        i(SB.toString());
    }

    public static String getString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    public static void call(String msg) {
        trim(new PrivacyComplianceStack(msg));
    }

}
