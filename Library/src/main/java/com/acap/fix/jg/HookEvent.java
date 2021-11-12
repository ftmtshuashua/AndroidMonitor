package com.acap.fix.jg;

import java.util.Objects;

/**
 * <pre>
 * Tip:
 *      储存Hook到的事件
 *
 * Created by A·Cap on 2021/10/13 10:39
 * </pre>
 */
public class HookEvent<K> {

    //用于表示同一事件的值
    private K mParams;

    public HookEvent(K mParams) {
        this.mParams = mParams;
    }


    // 调用堆栈
    private Exception mMethodStack;


    public HookEvent setMethodStack(Exception mMethodStack) {
        this.mMethodStack = mMethodStack;
        return this;
    }

    public Exception getMethodStack() {
        return mMethodStack;
    }

    protected void setParams(K mParams) {
        this.mParams = mParams;
    }

    public K getParams() {
        return mParams;
    }

    public boolean equalsParams(K o) {
        return Objects.equals(mParams, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HookEvent<?> hookEvent = (HookEvent<?>) o;
        return Objects.equals(mParams, hookEvent.mParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mParams);
    }
}
