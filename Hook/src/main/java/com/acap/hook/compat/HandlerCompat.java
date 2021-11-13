package com.acap.hook.compat;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by ACap on 2021/11/13 0:24
 * </pre>
 *
 * @author A·Cap
 */
public interface HandlerCompat {

    interface P {
        int WHAT_EXECUTE_TRANSACTION = 159;
    }

    interface Default {
         int LAUNCH_ACTIVITY         = 100; // to onResume
         int PAUSE_ACTIVITY          = 101;
         int PAUSE_ACTIVITY_FINISHING= 102;
         int STOP_ACTIVITY_SHOW      = 103;
         int STOP_ACTIVITY_HIDE      = 104; // onStop
         int RESUME_ACTIVITY         = 107;
         int DESTROY_ACTIVITY        = 109; // onDestroy
    }
}
