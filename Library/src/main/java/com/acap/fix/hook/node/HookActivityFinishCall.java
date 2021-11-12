package com.acap.fix.hook.node;

import android.app.Activity;

import com.acap.fix.hook.HookModel;
import com.acap.fix.hook.OnBeforeHookedMethod;
import com.acap.fix.jg.CacheList;
import com.acap.fix.jg.HookEvent;
import com.acap.fix.why.OnActivityFinishCalled;

/**
 * <pre>
 * Tip:
 *      当 Activity 的 finish() 被调用时
 *
 * Created by A·Cap on 2021/10/13 15:47
 * </pre>
 */
public class HookActivityFinishCall {
    // 调用Finish
    private static CacheList<HookEvent<Activity>> mClassFinish = new CacheList(1000);

    private HookActivityFinishCall() {
    }

    public static void start() {
        new HookModel(Activity.class)
                .setMethod("finish")
                .findAndHookMethod((OnBeforeHookedMethod) param -> {
                    mClassFinish.add(new HookEvent(param.thisObject).setMethodStack(new OnActivityFinishCalled(param.thisObject)));
                });
    }

    //查找 调用Finish 的目标
    public static HookEvent<Activity> findFinishEvent(Activity cls) {
        for (int i = mClassFinish.size() - 1; i >= 0; i--) {
            HookEvent<Activity> objectHookEvent = mClassFinish.get(i);
            if (objectHookEvent.equalsParams(cls)) {
                return objectHookEvent;
            }
        }
        return null;
    }

}
