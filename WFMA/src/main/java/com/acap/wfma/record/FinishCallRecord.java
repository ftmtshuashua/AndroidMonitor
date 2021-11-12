package com.acap.wfma.record;

import android.app.Activity;

import stack.ActivityFinishStack;

import java.text.MessageFormat;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/11/12 17:51
 * </pre>
 * @author A·Cap
 */
public class FinishCallRecord extends BaseRecord<Activity> {
    public FinishCallRecord(Activity obj) {
        super(obj);
    }

    @Override
    protected Throwable generate(Activity obj) {
        return new ActivityFinishStack(MessageFormat.format("{0} call finish()", obj.getClass().getName()));
    }
}
