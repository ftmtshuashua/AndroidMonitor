package com.acap.wfma.structure;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/11/12 17:54
 * </pre>
 * @author A·Cap
 */
public class LimitList<E> extends CopyOnWriteArrayList<E> {
    int count;

    public LimitList(int count) {
        this.count = count;
    }

    @Override
    public boolean add(E e) {
        boolean add = super.add(e);
        if (size() > count) {
            remove(0);
        }
        return add;
    }

}
