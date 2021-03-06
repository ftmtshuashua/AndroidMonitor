package com.acap.wfma;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.MessageQueue;

import com.acap.hook.HookModel;
import com.acap.hook.OnAfterHookedMethod;
import com.acap.hook.OnBeforeHookedMethod;
import com.acap.hook.compat.HandlerCompat;
import com.acap.hook.interior.ClientTransactionHelper;
import com.acap.hook.runtime.LifecycleStateRequest;
import com.acap.wfma.interior.Logs;
import com.acap.wfma.record.FinishCallRecord;
import com.acap.wfma.record.MessageDestroyRecord;
import com.acap.wfma.record.UnknownRecord;
import com.acap.wfma.structure.LimitList;
import com.swift.sandhook.xposedcompat.XposedCompat;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * Tip:
 *      Who finish my activity !
 *
 * Created by A·Cap on 2021/11/12 16:39
 * </pre>
 *
 * @author A·Cap
 */
public final class WFMA {


    private static boolean mInit = false;

    private static final LimitList<FinishCallRecord> RECORD_FINISH_CALL = new LimitList<>(100);
    private static final LimitList<MessageDestroyRecord> RECORD_MESSAGE_ENQUEUE = new LimitList<>(100);

    private WFMA() {
    }

    /**
     * Start the monitor, It tells you who shut down your activity
     */
    public static void start() {
        if (mInit) {
            throw new IllegalStateException("The monitor is started!");
        }
        mInit = true;
        Logs.i("Monitor starting , Who finish my activity!");

        try {
            init();
        } catch (Throwable e) {
            Logs.e(e);
        }

        collectionFinishCall();
        collectionHandlerMessage();
//        collectionConfigChange();
        lifecycle();
        destroyCall();
    }


    private static void init() throws Throwable {
        XposedCompat.classLoader = ClassLoader.getSystemClassLoader();

    }

    private static void collectionFinishCall() {
        new HookModel(Activity.class).setMethod("finish")
                .findAndHookMethod((OnBeforeHookedMethod) param -> RECORD_FINISH_CALL.add(new FinishCallRecord((Activity) param.thisObject)));
    }

    private static void collectionHandlerMessage() {
        new HookModel(MessageQueue.class).setMethod("enqueueMessage", Message.class, long.class)
                .findAndHookMethod((OnBeforeHookedMethod) param -> {
                    Message message = (Message) param.args[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        if (message.what == HandlerCompat.P.WHAT_EXECUTE_TRANSACTION) {
                            Logs.i(MessageFormat.format("--->>> {0} : {1}", message.toString(), ClientTransactionHelper.getLifecycleState(message.obj)));
                            if (LifecycleStateRequest.STR_ON_DESTROY.equals(ClientTransactionHelper.getLifecycleState(message.obj))) {
                                RECORD_MESSAGE_ENQUEUE.add(new MessageDestroyRecord(message));
                            }
                        }
                    } else {
                        if (message.what == HandlerCompat.Default.DESTROY_ACTIVITY) {
//                            Logs.i(MessageFormat.format("--->>> {0} : {1}", message.toString(), LifecycleStateRequest.ON_DESTROY));
                            RECORD_MESSAGE_ENQUEUE.add(new MessageDestroyRecord(message));
                        }

                    }
                });
    }

    private static void collectionConfigChange() {
        new HookModel("android.app.ActivityThread")
                .setMethod("performActivityConfigurationChanged", Activity.class, Configuration.class, Configuration.class, int.class, boolean.class)
                .findAndHookMethod((OnBeforeHookedMethod) param -> {
                    Activity activity = (Activity) param.args[0];
                    Configuration arg = (Configuration) param.args[1];

                    Logs.i(MessageFormat.format("配置发生改变 ： {0} -> {1}", activity.getClass().getSimpleName(), arg));

                });
    }

    private static void lifecycle() {
        new HookModel(Activity.class).setMethod("onCreate", Bundle.class)
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.onCreate(param.thisObject.getClass()));
        new HookModel(Activity.class).setMethod("onStart")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.onStart(param.thisObject.getClass()));
        new HookModel(Activity.class).setMethod("onResume")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.onResume(param.thisObject.getClass()));
        new HookModel(Activity.class).setMethod("onPause")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.onPause(param.thisObject.getClass()));
        new HookModel(Activity.class).setMethod("onStop")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.onStop(param.thisObject.getClass()));
        new HookModel(Activity.class).setMethod("onDestroy")
                .findAndHookMethod((OnBeforeHookedMethod) param -> Logs.onDestroy(param.thisObject.getClass()));
    }

    private static void destroyCall() {
        new HookModel(Activity.class).setMethod("onDestroy")
                .findAndHookMethod((OnAfterHookedMethod) param -> {
                    Activity activity = (Activity) param.thisObject;

                    FinishCallRecord finishCall = findFinishCall(activity);
                    if (finishCall != null) {
                        Logs.trim(finishCall.stack);
                        return;
                    }

                    MessageDestroyRecord messageEnqueue = findMessageDestroy(activity);
                    if (messageEnqueue != null) {
                        Logs.trim(messageEnqueue.stack);
                        return;
                    }

                    Logs.trim(new UnknownRecord(activity).stack);


                });
    }

    private static FinishCallRecord findFinishCall(Activity activity) {
        List<FinishCallRecord> array = new ArrayList<>(RECORD_FINISH_CALL);
        for (int i = array.size() - 1; i >= 0; i--) {
            FinishCallRecord record = array.get(i);
            if (record.obj == activity) {
                return record;
            }

        }
        return null;
    }

    private static MessageDestroyRecord findMessageDestroy(Activity activity) {
        List<MessageDestroyRecord> array = new ArrayList<>(RECORD_MESSAGE_ENQUEUE);
        for (int i = array.size() - 1; i >= 0; i--) {
            MessageDestroyRecord record = array.get(i);
            if (record.mActivity == activity) {
                return record;
            }

        }
        return null;
    }

}
