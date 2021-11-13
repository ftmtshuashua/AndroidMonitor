package com.acap.hook.runtime;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/13 18:45
 * </pre>
 *
 * @author A·Cap
 */
public class LifecycleStateRequest {
    public static final int UNDEFINED = -1;
    public static final int PRE_ON_CREATE = 0;
    public static final int ON_CREATE = 1;
    public static final int ON_START = 2;
    public static final int ON_RESUME = 3;
    public static final int ON_PAUSE = 4;
    public static final int ON_STOP = 5;
    public static final int ON_DESTROY = 6;
    public static final int ON_RESTART = 7;

    public static final String STR_UNDEFINED = "UNDEFINED";
    public static final String STR_PRE_ON_CREATE = "PRE_ON_CREATE";
    public static final String STR_ON_CREATE = "ON_CREATE";
    public static final String STR_ON_START = "ON_START";
    public static final String STR_ON_RESUME = "ON_RESUME";
    public static final String STR_ON_PAUSE = "ON_PAUSE";
    public static final String STR_ON_STOP = "ON_STOP";
    public static final String STR_ON_DESTROY = "ON_DESTROY";
    public static final String STR_ON_RESTART = "ON_RESTART";

    public static String format(int type) {
        switch (type) {
            case PRE_ON_CREATE:
                return STR_PRE_ON_CREATE;
            case ON_CREATE:
                return STR_ON_CREATE;
            case ON_START:
                return STR_ON_START;
            case ON_RESUME:
                return STR_ON_RESUME;
            case ON_PAUSE:
                return STR_ON_PAUSE;
            case ON_STOP:
                return STR_ON_STOP;
            case ON_DESTROY:
                return STR_ON_DESTROY;
            case ON_RESTART:
                return STR_ON_RESTART;
            case UNDEFINED:
            default:
                return STR_UNDEFINED;
        }
    }
}
