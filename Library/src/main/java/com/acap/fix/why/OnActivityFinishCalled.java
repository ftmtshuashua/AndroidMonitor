package com.acap.fix.why;

import java.text.MessageFormat;

/**
 * <pre>
 * Tip:
 *      某人调用了 finish() 导致的
 *
 * Created by A·Cap on 2021/10/13 15:36
 * </pre>
 */
public class OnActivityFinishCalled extends ActivityFinishWhyException {
    public OnActivityFinishCalled(Object murderer) {
        super(MessageFormat.format("{0} call activity finish() method", murderer.getClass().getName()));
    }
}
