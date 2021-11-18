package com.acap.wfma.record;

import android.app.Activity;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;

import com.acap.hook.bean.ActivityThread;
import com.acap.hook.interior.ClientTransactionHelper;
import com.acap.hook.runtime.LifecycleStateRequest;
import com.acap.wfma.interior.Logs;
import com.swift.sandhook.xposedcompat.utils.ApplicationUtils;

import java.text.MessageFormat;

import de.robv.android.xposed.XposedHelpers;
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
public class MessageDestroyRecord extends BaseRecord<Message> {
    public Activity mActivity;

    public MessageDestroyRecord(Message obj) {
        super(obj);
    }


    @Override
    protected void onInit() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Object mActivityToken = XposedHelpers.getObjectField(obj.obj, "mActivityToken");
                mActivity = ActivityThread.getInstance().getActivity((IBinder) mActivityToken);
            } else {
                mActivity = ActivityThread.getInstance().getActivity((IBinder) obj.obj);
            }
        } catch (Exception e) {
            Logs.e(e);
        }
    }

    @Override
    protected Throwable generate(Message message) {
        String nameForUid = ApplicationUtils.currentApplication().getPackageManager().getNameForUid(Binder.getCallingUid());
        return new ActivityFinishStack(MessageFormat.format("{0} receives an {2} signal from {1} >> {3}", getActivity(), nameForUid, LifecycleStateRequest.STR_ON_DESTROY, message));
    }

    private String getActivity() {
        if (mActivity != null) {
            return mActivity.getClass().getName();
        }
        return "a activity";
    }

    @Override
    public String toString() {
        return "MessageDestroyRecord{" +
                "obj=" + obj +
                ", mActivity=" + mActivity +
                '}';
    }
}
