package com.acap.fix.utils;

import android.os.Build;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/13 18:08
 * </pre>
 */
public class BuildCompat {

    public static int getPreviewSDKInt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return Build.VERSION.PREVIEW_SDK_INT;
            } catch (Throwable e) {
                // ignore
            }
        }
        return 0;
    }

    public static boolean isOreo() {
        return Build.VERSION.SDK_INT > 25 || (Build.VERSION.SDK_INT == 25 && getPreviewSDKInt() > 0);
    }

    public static boolean isPie() {
        return Build.VERSION.SDK_INT > 27 || (Build.VERSION.SDK_INT == 27 && getPreviewSDKInt() > 0);
    }


    public static boolean isQ() {
        return Build.VERSION.SDK_INT > 28 || (Build.VERSION.SDK_INT == 28 && getPreviewSDKInt() > 0);
    }

    public static boolean isR() {
        return Build.VERSION.SDK_INT > 29 || (Build.VERSION.SDK_INT == 29 && getPreviewSDKInt() > 0);
    }

    public static boolean isSamsung() {
        return "samsung".equalsIgnoreCase(Build.BRAND) || "samsung".equalsIgnoreCase(Build.MANUFACTURER);
    }




}