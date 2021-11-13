package com.acap.hook;

import de.robv.android.xposed.XC_MethodHook;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/13 10:26
 * </pre>
 */
@FunctionalInterface
public interface OnBeforeHookedMethod {
    void call(XC_MethodHook.MethodHookParam param) throws Throwable;
}
