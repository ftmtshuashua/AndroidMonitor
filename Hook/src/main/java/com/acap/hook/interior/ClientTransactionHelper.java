package com.acap.hook.interior;

import com.acap.hook.runtime.LifecycleStateRequest;

import de.robv.android.xposed.XposedHelpers;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/11/15 14:29
 * </pre>
 */
public class ClientTransactionHelper {

    public static Object getLifecycleStateRequest(Object clientTransaction) {
        return XposedHelpers.callMethod(clientTransaction, "getLifecycleStateRequest");
    }

    public static String getLifecycleType(Object lifecycleStateRequest) {
        if (lifecycleStateRequest == null) {
            return "null";
        }
        return lifecycleStateRequest.getClass().getName();
    }

    public static boolean isFinish(Object destroyActivityItem) {
        return (boolean) XposedHelpers.getObjectField(destroyActivityItem, "mFinished");
    }

    /**
     * 客户端活动在事务执行后的最终生命周期状态
     */
    public static String getLifecycleState(Object clientTransaction) {
        try {
            Object mDestroyActivityItem = XposedHelpers.callMethod(clientTransaction, "getLifecycleStateRequest");
            Object mLifecycleState = XposedHelpers.callMethod(mDestroyActivityItem, "getTargetState");
            return LifecycleStateRequest.format((Integer) mLifecycleState);
        } catch (Throwable e) {
            return "GET-ERROR";
        }
    }


}
