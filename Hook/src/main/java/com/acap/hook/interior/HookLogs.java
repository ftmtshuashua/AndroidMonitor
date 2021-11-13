package com.acap.hook.interior;

import android.util.Log;

import java.text.MessageFormat;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/11/12 16:44
 * </pre>
 */
public class HookLogs {
    private static final String TAG = "HooK";

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


}
