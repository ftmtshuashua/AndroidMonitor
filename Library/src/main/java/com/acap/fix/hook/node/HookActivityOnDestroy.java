package com.acap.fix.hook.node;

import android.app.Activity;

import com.acap.fix.hook.HookModel;
import com.acap.fix.hook.OnBeforeHookedMethod;

/**
 * <pre>
 * Tip:
 *      当 Activity 的 onDestroy() 被调用时
 *
 * Created by A·Cap on 2021/10/13 15:47
 * </pre>
 */
public class HookActivityOnDestroy {

    public static void start(OnBeforeHookedMethod call) {
        new HookModel(Activity.class)
                .setMethod("onDestroy")
                .findAndHookMethod(call);
    }

}
