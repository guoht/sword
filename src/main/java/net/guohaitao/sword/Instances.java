package net.guohaitao.sword;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by i@guohaitao.net on 14-9-18.
 * Description: 单例对象工具类
 */
@SuppressWarnings("unchecked")
public final class Instances {
    private static final Map<Class<?>, Object> POOL = Maps.newHashMap();
    private static final Lock LOCK = new ReentrantLock();

    private Instances() {
    }

    public static <T> T get(@Nonnull Class<?> clazz) {
        Optional<T> optionalObj = Optional.fromNullable((T) POOL.get(clazz));
        if (optionalObj.isPresent()) {
            return optionalObj.get();
        } else {
            LOCK.lock();
            try {
                Optional<T> optionalLocalObj = Optional.fromNullable((T) POOL.get(clazz));
                if (optionalLocalObj.isPresent()) {
                    return optionalLocalObj.get();
                } else {
                    T obj = (T) clazz.newInstance();
                    POOL.put(clazz, obj);
                    return obj;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw Throwables.propagate(e);
            } finally {
                LOCK.unlock();
            }
        }
    }

    public static <T> T get(@Nonnull String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw Throwables.propagate(e);
        }
        return get(clazz);
    }

    public static <T> T set(@Nonnull Class<?> clazz, @Nonnull T obj) {
        Optional<T> optionalObj = Optional.fromNullable((T) POOL.get(clazz));
        if (optionalObj.isPresent()) {
            return optionalObj.get();
        } else {
            LOCK.lock();
            try {
                Optional<T> optionalLocalObj = Optional.fromNullable((T) POOL.get(clazz));
                if (optionalLocalObj.isPresent()) {
                    return optionalLocalObj.get();
                } else {
                    POOL.put(clazz, obj);
                    return obj;
                }
            } finally {
                LOCK.unlock();
            }
        }
    }
}
