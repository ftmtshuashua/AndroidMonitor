package com.acap.fix.runtime;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/13 18:45
 * </pre>
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


    public static final String format(int type) {
        switch (type) {
            case PRE_ON_CREATE:
                return "PRE_ON_CREATE";
            case ON_CREATE:
                return "ON_CREATE";
            case ON_START:
                return "ON_START";
            case ON_RESUME:
                return "ON_RESUME";
            case ON_PAUSE:
                return "ON_PAUSE";
            case ON_STOP:
                return "ON_STOP";
            case ON_DESTROY:
                return "ON_DESTROY";
            case ON_RESTART:
                return "ON_RESTART";
            case UNDEFINED:
                return "UNDEFINED";
        }
        return "UNDEFINED";
    }
}
