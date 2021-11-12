package com.acap.fix.why;

/**
 * <pre>
 * Tip:
 *      未知情况导致的
 *
 * Created by A·Cap on 2021/10/13 15:36
 * </pre>
 */
public class OnUnknownCause extends ActivityFinishWhyException {
    public OnUnknownCause() {
        super("A unknown cause");
    }
}
