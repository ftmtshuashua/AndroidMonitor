package com.acap.fix.jg;

import android.os.Message;

/**
 * <pre>
 * Tip:
 *      Message 入栈
 *
 * Created by A·Cap on 2021/10/13 11:45
 * </pre>
 */
public class MessageEnqueue extends HookEvent<Message> {
    // 发送者ID
    public int mSendPid;
    public int mSendUid;
    public String mLifecycleState;

    public MessageEnqueue(Message mParams, int mSendPid, int mSendUid, String lifecycleState) {
        super(mParams);
        this.mSendPid = mSendPid;
        this.mSendUid = mSendUid;
        this.mLifecycleState = lifecycleState;
    }

}
