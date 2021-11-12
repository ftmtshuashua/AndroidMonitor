package com.acap.fix.why;

import android.app.Activity;

import java.text.MessageFormat;

/**
 * <pre>
 * Tip:
 *      Activity 结束异常
 *
 * Created by A·Cap on 2021/10/14 18:37
 * </pre>
 */
public class ActivityFinishWhyException extends Exception {
    private String mMessage;
    private String mTargetActivity;

    public ActivityFinishWhyException(String message) {
        super(message);
        mMessage = message;
    }

    public ActivityFinishWhyException(String message, Throwable cause) {
        super(message, cause);
        mMessage = message;
    }


    @Override
    public String getMessage() {
        return MessageFormat.format("{0} end -> Because the {1}", mTargetActivity, mMessage);
    }

    public void setTargetActivity(Activity activity) {
        mTargetActivity = activity.getClass().getName();
    }

}
