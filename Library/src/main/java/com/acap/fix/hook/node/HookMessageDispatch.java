package com.acap.fix.hook.node;

import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;

import com.acap.fix.hook.HookModel;
import com.acap.fix.hook.OnAfterHookedMethod;
import com.acap.fix.hook.OnBeforeHookedMethod;
import com.acap.fix.jg.CacheList;
import com.acap.fix.jg.HookEvent;
import com.acap.fix.jg.MessageEnqueue;
import com.acap.fix.runtime.LifecycleStateRequest;
import com.acap.fix.why.OnProcessCause;

import de.robv.android.xposed.XposedHelpers;

/**
 * <pre>
 * Tip:
 *      Hook 消息分发
 *
 * Created by A·Cap on 2021/10/13 15:47
 * </pre>
 */
public class HookMessageDispatch {
    // 执行Message
    private static CacheList<HookEvent<Message>> mDispatchMessage = new CacheList(1000);
    //当前分发的 Message
    private static Message mCurrentDispatchMessage;

    // 创建Message
    private static CacheList<MessageEnqueue> mEnqueueMessage = new CacheList(1000);

    private HookMessageDispatch() {
    }

    public static void start() {
        new HookModel(Handler.class)
                .setMethod("dispatchMessage", Message.class)
                .findAndHookMethod((OnBeforeHookedMethod) param -> {
                    Message message = (Message) param.args[0];
                    mCurrentDispatchMessage = message;
                    mDispatchMessage.add(new HookEvent(param.args[0]).setMethodStack(new Exception()));

//                    LogUtils.i(MessageFormat.format("{0,number,0} -> {1}", message.sendingUid, message.toString()));

                    //Finish -> { when=-1ms what=159 obj=android.app.servertransaction.ClientTransaction@7fc1 target=android.app.ActivityThread$H }
                    //Finish -> { when=-22ms what=159 obj=android.app.servertransaction.ClientTransaction@7fe0 target=android.app.ActivityThread$H }
                });


        new HookModel(MessageQueue.class)
                .setMethod("enqueueMessage", Message.class, long.class)
                .findAndHookMethod((OnAfterHookedMethod) param -> {
                    Message message = (Message) param.args[0];
                    if (message.what == 159) {
                        MessageEnqueue messageEnqueue = new MessageEnqueue(message, Binder.getCallingPid(), Binder.getCallingUid(), getLifecycleState(message.obj));
                        messageEnqueue.setMethodStack(new OnProcessCause(messageEnqueue.mSendUid, messageEnqueue.mSendPid, messageEnqueue.mLifecycleState));
                        mEnqueueMessage.add(messageEnqueue);
                    }
                });
    }

    //客户端活动在事务执行后的最终生命周期状态
    private static String getLifecycleState(Object obj_ClientTransaction) {
        try {
            //android.app.servertransaction.ActivityLifecycleItem
            Object mDestroyActivityItem = XposedHelpers.findField(obj_ClientTransaction.getClass(), "mLifecycleStateRequest").get(obj_ClientTransaction);
            Object mLifecycleState = XposedHelpers.findMethodExact(mDestroyActivityItem.getClass(), "getTargetState").invoke(mDestroyActivityItem);
            String format = LifecycleStateRequest.format((Integer) mLifecycleState);
//            LogUtils.i("客户端活动在事务执行后的最终生命周期状态：" + format);
            return format;
        } catch (Exception e) {
            return "Get state error";
        }
    }

    //查找 当前分发Dispatch的事件
    private static HookEvent<Message> findCurrentDispatchMessageEvent(Message msg) {
        for (int i = mDispatchMessage.size() - 1; i >= 0; i--) {
            HookEvent<Message> messageHookEvent = mDispatchMessage.get(i);
            if (messageHookEvent.equalsParams(msg)) {
                return messageHookEvent;
            }
        }
        return null;
    }

    /**
     * <pre>
     * 获得当前分发的Message
     * 对应同步的逻辑来说他的可用的
     * </pre>
     */
    public static Message getCurrentDispatchMessage() {
        return mCurrentDispatchMessage;
    }

    /**
     * 当前信息的入栈信息
     */
    public static MessageEnqueue getCurrentEnqueueMessage() {
        return findCurrentEnqueueMessageEvent(getCurrentDispatchMessage());
    }

    //查找 Message 的来源
    public static MessageEnqueue findCurrentEnqueueMessageEvent(Message msg) {
        for (int i = mEnqueueMessage.size() - 1; i >= 0; i--) {
            MessageEnqueue messageHookEvent = mEnqueueMessage.get(i);
            if (messageHookEvent.equalsParams(msg)) {
                return messageHookEvent;
            }
        }
        return null;
    }


}
