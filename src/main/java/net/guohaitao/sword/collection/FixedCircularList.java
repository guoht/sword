package net.guohaitao.sword.collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by i@guohaitao.net on 14-9-17.
 * Description: 固定长度环形列表
 */
@ThreadSafe
public class FixedCircularList<T> extends AbstractList<T>
        implements List<T>, RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = -2057670888638539584L;

    private Lock lock = new ReentrantLock();
    private ImmutableList<T> list;
    private int index;


    private FixedCircularList(@Nonnull List<T> list) {
        this.list = ImmutableList.<T>builder().addAll(list).build();
    }

    public static <T> FixedCircularList<T> of(@Nonnull List<T> list) {
        Preconditions.checkNotNull(list, "The list should not be null.");
        return new FixedCircularList<>(list);
    }

    @SafeVarargs
    public static <T> FixedCircularList<T> of(@Nonnull T... objects) {
        Preconditions.checkNotNull(objects, "The objects should not be null.");
        return new FixedCircularList<>(Arrays.asList(objects));
    }

    /**
     * 选举
     *
     * @return
     */
    @Nonnull
    public T pick() {
        lock.lock();
        try {
            if (index == list.size()) {
                index = 0;
            }
            return list.get(index++);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public T get(int index) {
        return list.get(index % list.size());
    }

    @Override
    public int size() {
        return list.size();
    }
}
