package com.acap.wfma.record;

import android.os.Build;
import android.os.Message;

import stack.ActivityFinishStack;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/11/12 17:58
 * </pre>
 *
 * @author A·Cap
 */
public class MessageDispatchRecord extends BaseRecord<Message> {
    public MessageDispatchRecord(Message obj) {
        super(obj);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

        } else {
//            Utils.print(obj.obj);
        }

    }

    @Override
    protected Throwable generate(Message obj) {
        return new ActivityFinishStack("At the time of message dispatch");
    }
}
