package com.acap.fix.jg;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <pre>
 * Tip:
 *
 *
 * Created by A·Cap on 2021/10/13 10:20
 * </pre>
 */
public class CacheList<E> extends CopyOnWriteArrayList<E> {
    int count;

    public CacheList(int count) {
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
//
//    public CacheList<E> toSynchronized() {
//        return (CacheList<E>) Collections.synchronizedList(this);
//    }
}
