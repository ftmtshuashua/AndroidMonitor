package com.acap.wfma.record;

import android.app.Activity;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;

import com.acap.wfma.compat.HandlerCompat;
import com.acap.wfma.hook.bean.ActivityThread;
import com.acap.wfma.interior.Logs;
import com.acap.wfma.interior.Utils;
import com.acap.wfma.runtime.LifecycleStateRequest;
import com.swift.sandhook.xposedcompat.utils.ApplicationUtils;

import java.text.MessageFormat;

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
public class MessageEnqueueRecord extends BaseRecord<Message> {
    public Activity mActivity;

    public MessageEnqueueRecord(Message obj) {
        super(obj);
    }

    @Override
    protected void onInit() {
        try {
            mActivity = ActivityThread.getInstance().getActivity((IBinder) obj.obj);
        } catch (Exception e) {
            Logs.e(e);
        }
    }

    @Override
    protected Throwable generate(Message obj) {
        String nameForUid = ApplicationUtils.currentApplication().getPackageManager().getNameForUid(Binder.getCallingUid());
        String messageFromXx = MessageFormat.format("To receive {0} from {1}", LifecycleStateRequest.STR_ON_DESTROY, nameForUid);

        switch (obj.what) {
            case HandlerCompat.P.WHAT_EXECUTE_TRANSACTION:
                return new ActivityFinishStack(MessageFormat.format("{0}:Change lifecycle to[{1}]", messageFromXx, Utils.getLifecycleState(obj.obj)));
            default:
                return new ActivityFinishStack(MessageFormat.format("{0} receives an {2} signal from {1}", getActivity(), nameForUid, LifecycleStateRequest.STR_ON_DESTROY));
        }
    }

    private String getActivity() {
        if (mActivity != null) {
            return mActivity.getClass().getName();
        }
        return "a activity";
    }

    @Override
    public String toString() {
        return "MessageEnqueueRecord{" +
                "obj=" + obj +
                ", mActivity=" + mActivity +
                '}';
    }
}
