package com.acap.fix.hook;

import com.acap.fix.utils.LogUtils;

import java.lang.reflect.Method;
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
 */
public class HookModel {
    private Class<?> clazz;
    private String methodName;
    private Object[] methodParamsType;


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
                    LogUtils.e(e);
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
                    LogUtils.e(e);
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
            LogUtils.e(e);
//            try {
//                checks();
//            } catch (Exception e1) {
//            }
        }


    }

    private void checks() {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            LogUtils.e(declaredMethod.toString());
        }
    }
}
