package com.acap.wfma.record;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/11/12 17:35
 * </pre>
 *
 * @author A·Cap
 */
public abstract class BaseRecord<R> {
    public R obj;
    public Throwable stack;

    public BaseRecord(R obj) {
        this.obj = obj;
        onInit();
        this.stack = generate(obj);
    }

    protected void onInit() {
    }

    /**
     * 生成代码的调用栈
     *
     * @param obj 绑定的对象
     * @return 栈对象
     */
    protected abstract Throwable generate(R obj);

}
