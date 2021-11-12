package com.acap.fix.why;

import com.swift.sandhook.xposedcompat.utils.ApplicationUtils;

import java.text.MessageFormat;

/**
 * <pre>
 * Tip:
 *      某个进程导致的
 *
 * Created by A·Cap on 2021/10/13 15:36
 * </pre>
 */
public class OnProcessCause extends ActivityFinishWhyException {
    public OnProcessCause(int mSendUid, int mSendPid, String mLifecycleState) {
        super(MessageFormat.format(
                "{0}(pid:{2,number,0}) process send the {3} signal!"
                , ApplicationUtils.currentApplication().getPackageManager().getNameForUid(mSendUid)
                , mSendUid, mSendPid, mLifecycleState
        ));
    }

}
