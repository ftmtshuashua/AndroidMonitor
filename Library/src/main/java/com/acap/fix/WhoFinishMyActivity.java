package com.acap.fix;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.acap.fix.hook.HookModel;
import com.acap.fix.hook.OnBeforeHookedMethod;
import com.acap.fix.hook.node.HookActivityFinishCall;
import com.acap.fix.hook.node.HookActivityOnDestroy;
import com.acap.fix.hook.node.HookMessageDispatch;
import com.acap.fix.jg.HookEvent;
import com.acap.fix.jg.MessageEnqueue;
import com.acap.fix.runtime.LifecycleStateRequest;
import com.acap.fix.utils.LogUtils;
import com.acap.fix.utils.Utils;
import com.acap.fix.why.ActivityFinishWhyException;
import com.acap.fix.why.OnUnknownCause;
import com.swift.sandhook.xposedcompat.XposedCompat;
import com.swift.sandhook.xposedcompat.utils.ApplicationUtils;

import java.text.MessageFormat;

import de.robv.android.xposed.XposedHelpers;

/**
 * <pre>
 * Tip:
 *      Fuck you, man
 *      谁关闭了我的 Activity ??
 *
 * Created by A·Cap on 2021/10/13 10:13
 * </pre>
 */
public final class WhoFinishMyActivity {
    //
    public static boolean IsPrintStack = false;
    private static boolean initialized = false;

    /**
     * 启动检测
     */
    public static void start() {
        if (initialized) return;

        try {
            LogUtils.i("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 我的Activity被谁关了 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
            StringBuffer SB = new StringBuffer("SystemInfo");
            SB.append(": ").append("Product: ").append(android.os.Build.PRODUCT);
            SB.append(", ").append("CPU_ABI: ").append(android.os.Build.CPU_ABI);
            SB.append(", ").append("TAGS: ").append(android.os.Build.TAGS);
            SB.append(", ").append("VERSION_CODES.BASE: ").append(android.os.Build.VERSION_CODES.BASE);
            SB.append(", ").append("MODEL: ").append(android.os.Build.MODEL);
            SB.append(", ").append("SDK: ").append(android.os.Build.VERSION.SDK);
            SB.append(", ").append("VERSION.RELEASE: ").append(android.os.Build.VERSION.RELEASE);
            SB.append(", ").append("DEVICE: ").append(android.os.Build.DEVICE);
            SB.append(", ").append("DISPLAY: ").append(android.os.Build.DISPLAY);
            SB.append(", ").append("BRAND: ").append(android.os.Build.BRAND);
            SB.append(", ").append("BOARD: ").append(android.os.Build.BOARD);
            SB.append(", ").append("FINGERPRINT: ").append(android.os.Build.FINGERPRINT);
            SB.append(", ").append("ID: ").append(android.os.Build.ID);
            SB.append(", ").append("MANUFACTURER: ").append(android.os.Build.MANUFACTURER);
            SB.append(", ").append("USER: ").append(android.os.Build.USER);
            LogUtils.i(SB.toString());

            Utils.bypassHiddenAPIEnforcementPolicyIfNeeded();

            hook();
            initialized = true;
            LogUtils.i("↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 我的Activity被谁关了 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
        } catch (Throwable e) {
            LogUtils.e(e);
        }
    }

    private static void hook() throws Exception {
        XposedCompat.classLoader = ClassLoader.getSystemClassLoader();
        HookActivityFinishCall.start();
        HookMessageDispatch.start();

        HookActivityOnDestroy.start(param -> {
            Activity activity = (Activity) param.thisObject;
            MessageEnqueue currentEnqueueMessage = HookMessageDispatch.getCurrentEnqueueMessage();
            HookEvent<Activity> finishEvent = HookActivityFinishCall.findFinishEvent(activity);

            Exception exception;
            if (finishEvent != null) {
                exception = finishEvent.getMethodStack();
            } else if (currentEnqueueMessage != null) {
                exception = currentEnqueueMessage.getMethodStack();
            } else {
                OnUnknownCause onUnknownCause = new OnUnknownCause();
                onUnknownCause.setTargetActivity((Activity) param.thisObject);
                exception = onUnknownCause;
            }

            if (exception instanceof ActivityFinishWhyException) {
                ((ActivityFinishWhyException) exception).setTargetActivity(activity);
            }
            LogUtils.trim(exception);
        });

        new HookModel(Activity.class)
                .setMethod("onCreate", Bundle.class)
                .findAndHookMethod((OnBeforeHookedMethod) param -> {
                    LogUtils.i(MessageFormat.format("Activity.onCreate() ---> {0}", param.thisObject.getClass().getName()));
                });
    }

    private static void other(Message mCurrentDispatchMessage) throws Throwable {
        LogUtils.i(MessageFormat.format("dispatchMessage:{0}", mCurrentDispatchMessage.toString()));

        //android.app.servertransaction.ClientTransaction
        Object mClientTransaction = mCurrentDispatchMessage.obj;


        //android.app.servertransaction.ActivityLifecycleItem
        Object mDestroyActivityItem = XposedHelpers.findField(mClientTransaction.getClass(), "mLifecycleStateRequest").get(mClientTransaction);
        Object intLifcycler = XposedHelpers.findMethodExact(mDestroyActivityItem.getClass(), "getTargetState").invoke(mDestroyActivityItem);
        LogUtils.i("生命周期状态：" + LifecycleStateRequest.format((Integer) intLifcycler));
        LogUtils.i("信息：" + mDestroyActivityItem.toString());
//        Utils.printHierarchy(mDestroyActivityItem.getClass());

//        Method getActivityClientRecord = Utils.exactMethod(mDestroyActivityItem.getClass(), "getActivityClientRecord"
//                , new Class<?>[]{ClassLoader.getSystemClassLoader().loadClass("android.app.ClientTransactionHandler"), IBinder.class});

//        getActivityClientRecord.invoke(mDestroyActivityItem);


        //IApplicationThread
        Object mIApplicationThread = XposedHelpers.findField(mClientTransaction.getClass(), "mClient").get(mClientTransaction);
//        Utils.printHierarchy(mIApplicationThread.getClass());

        int getCallingPid = (int) Utils.exactMethod(mIApplicationThread.getClass(), "getCallingPid").invoke(mIApplicationThread);
        int getCallingUid = (int) Utils.exactMethod(mIApplicationThread.getClass(), "getCallingUid").invoke(mIApplicationThread);
        LogUtils.i(MessageFormat.format("CallingPid:{0,number,0} , CallingUid:{1,number,0}", getCallingPid, getCallingUid));
        PackageManager packageManager = ApplicationUtils.currentApplication().getPackageManager();
        String nameForUid = packageManager.getNameForUid(getCallingUid);
        String[] packagesForUid = packageManager.getPackagesForUid(getCallingUid);
//        LogUtils.i(MessageFormat.format("NameForUid:{0} , PackagesForUid:{1}", nameForUid, new Gson().toJson(packagesForUid)));

    }

    private static void other2(Message mCurrentDispatchMessage) throws Exception {

        HookEvent<Message> currentDispatchMessageEvent = HookMessageDispatch.findCurrentEnqueueMessageEvent(mCurrentDispatchMessage);
        if (currentDispatchMessageEvent != null) {
            LogUtils.i(MessageFormat.format("dispatchMessage:{0}", mCurrentDispatchMessage.toString()));

            //android.app.servertransaction.ClientTransaction
            Object obj = mCurrentDispatchMessage.obj;
            //android.app.servertransaction.ActivityLifecycleItem
            Object mLifecycleStateRequest = XposedHelpers.findField(obj.getClass(), "mLifecycleStateRequest").get(obj);
            Object getTargetState = XposedHelpers.findMethodExact(mLifecycleStateRequest.getClass(), "getTargetState").invoke(mLifecycleStateRequest);
            LogUtils.i("生命周期状态：" + LifecycleStateRequest.format((Integer) getTargetState));

            Utils.printHierarchy(mLifecycleStateRequest.getClass());


//                            obj.getClass().getClassLoader().system
            Object getActivityClientRecord = XposedHelpers.findMethodExact(
                    mLifecycleStateRequest.getClass().getSuperclass().getSuperclass()
                    , "getActivityClientRecord"
                    , Class.forName("android.app.ClientTransactionHandler"), IBinder.class, boolean.class)
                    .invoke(obj);

            LogUtils.i("目标Activity:" + getActivityClientRecord);

            Object activity1 = XposedHelpers.findField(getActivityClientRecord.getClass(), "activity").get(getActivityClientRecord);

            LogUtils.i("目标Activity:" + activity1.getClass().getName());

                            /*
                             mClient:private IApplicationThread - public IApplicationThread getClient() {
                              mActivityToken:private IBinder
                               mLifecycleStateRequest: private ActivityLifecycleItem
                                mActivityCallbacks:private List<ClientTransactionItem>

                            * */


            LogUtils.i("Message的来源", currentDispatchMessageEvent.getMethodStack()); // 来源

        }
    }

    public static void test(Application application) {
        try {


//            if (!ProcessUtils.isMainProcess()) return;
//            Class<?> aClass = Class.forName("android.app.ActivityThread$H").getSuperclass();
//            Method[] declaredMethods = aClass.getDeclaredMethods();
//            for (Method declaredMethod : declaredMethods) {
//                LogOp.i("{0}.{1}", aClass, declaredMethod);
//            }


        } catch (Exception e) {
            LogUtils.e(e);
        }
    }


}
