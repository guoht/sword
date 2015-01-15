package net.guohaitao.sword.concurent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by i@guohaitao.net on 14-12-11.
 * Description:  异步并发执行的函数
 */
public final class AsyncExecutors {
    private final static Logger logger = Logger.getLogger(AsyncExecutors.class.getName());
    private final static int DEFAULT_THREADS = Runtime.getRuntime().availableProcessors() > 1 ? 2 * Runtime.getRuntime().availableProcessors() : 2;
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(DEFAULT_THREADS);
    private final static int MAX_EXEC_TIME = 10;

    private AsyncExecutors() {
    }

    /**
     * 线程池服务
     *
     * @return
     */
    @Nonnull
    public static ExecutorService getExecutorService() {
        return EXECUTOR_SERVICE;
    }

    /**
     * 异步执行
     *
     * @param runnable
     */
    public static void exec(@Nonnull Runnable runnable) {
        Preconditions.checkNotNull(runnable);
        EXECUTOR_SERVICE.execute(runnable);
    }

    /**
     * 异步执行
     *
     * @param callable
     * @param <E>
     * @return
     */
    public static <E> E exec(@Nonnull Callable<E> callable) {
        Preconditions.checkNotNull(callable);
        Future<E> future = EXECUTOR_SERVICE.submit(callable);
        try {
            return future.get(MAX_EXEC_TIME, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.log(Level.WARNING, "AsyncFunctions.exec error.", e);
        }
        return null;
    }

    /**
     * 批量异步执行
     *
     * @param callableList
     * @param <E>
     * @return
     */
    public static <E> List<E> execAll(@Nonnull List<Callable<E>> callableList) {
        Preconditions.checkNotNull(callableList);
        try {
            List<Future<E>> futureList = EXECUTOR_SERVICE.invokeAll(callableList, MAX_EXEC_TIME, TimeUnit.SECONDS);
            List<E> list = Lists.newArrayListWithCapacity(callableList.size());
            for (Future<E> f : futureList) {
                if (f.isDone() && !f.isCancelled()) {
                    E element = f.get();
                    if (element != null) {
                        list.add(element);
                    }
                }
            }
            return list;
        } catch (Exception e) {
            logger.log(Level.WARNING, "AsyncFunctions.execAll error.", e);
        }
        return null;
    }

    /**
     * 批量异步执行
     *
     * @param callableList
     * @param <E>
     * @return
     */
    public static <E> List<E> execAllSafe(@Nonnull List<Callable<E>> callableList) {
        Preconditions.checkNotNull(callableList);
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            List<Future<E>> futureList = executorService.invokeAll(callableList, MAX_EXEC_TIME, TimeUnit.SECONDS);
            List<E> list = Lists.newArrayListWithCapacity(callableList.size());
            for (Future<E> f : futureList) {
                if (f.isDone() && !f.isCancelled()) {
                    E element = f.get();
                    if (element != null) {
                        list.add(element);
                    }
                }
            }
            return list;
        } catch (Exception e) {
            logger.log(Level.WARNING, "AsyncFunctions.execAllSafe error.", e);
        } finally {
            try {
                executorService.shutdown();
            } catch (Exception ex) {
                logger.log(Level.WARNING, "executorService.shutdown error.", ex);
            }
        }
        return null;
    }
}
