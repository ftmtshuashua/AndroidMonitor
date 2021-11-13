package com.acap.hook;


import com.acap.hook.interior.HookLogs;
import com.swift.sandhook.xposedcompat.XposedCompat;

import java.util.Arrays;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

/**
 * <pre>
 * Tip:
 *      findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback)
 *
 * Created by A·Cap on 2021/10/13 10:24
 * </pre>
 *
 * @author A·Cap
 */
public class HookModel {
    private final Class<?> clazz;
    private String methodName;
    private Object[] methodParamsType;

    public static Class<?> getClass(String clazz) {
        return XposedHelpers.findClass(clazz, XposedCompat.classLoader);
    }

    public HookModel(String clazz) {
        this.clazz = getClass(clazz);
    }

    public HookModel(Class<?> clazz) {
        this.clazz = clazz;
    }

    public HookModel setMethod(String methodName, Object... parameterType) {
        this.methodName = methodName;
        this.methodParamsType = parameterType;
        return this;
    }

    public void findAndHookMethod(OnBeforeHookedMethod before) {
        findAndHookMethod(new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                try {
                    before.call(param);
                } catch (Throwable e) {
                    HookLogs.e(e);
                }
            }
        });
    }

    public void findAndHookMethod(OnAfterHookedMethod after) {
        findAndHookMethod(new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                try {
                    after.call(param);
                } catch (Throwable e) {
                    HookLogs.e(e);
                }
            }
        });
    }

    public void findAndHookMethod(XC_MethodHook xc) {
        try {
            Object[] objects = Arrays.copyOf(methodParamsType, methodParamsType.length + 1);
            objects[objects.length - 1] = xc;
            XposedHelpers.findAndHookMethod(clazz, methodName, objects);
        } catch (Throwable e) {
            HookLogs.e(e);
        }


    }

}
