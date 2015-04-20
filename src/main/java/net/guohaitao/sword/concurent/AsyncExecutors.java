package net.guohaitao.sword.concurent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.guohaitao.sword.jmx.MBeanExporters;

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
    private final static int DEFAULT_THREADS = Runtime.getRuntime().availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() + 1 : 2;
    private final static ThreadPoolExecutor EXECUTOR_SERVICE;
    private final static int MAX_EXEC_TIME = 60;
    private final static int MAX_QUEUE_SIZE = 128;

    static {
        ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
        EXECUTOR_SERVICE = new ThreadPoolExecutor(DEFAULT_THREADS, DEFAULT_THREADS, MAX_EXEC_TIME, TimeUnit.SECONDS, arrayBlockingQueue);
        EXECUTOR_SERVICE.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        //JMX监控
        boolean jmxEnabled = Boolean.valueOf(System.getProperty(MBeanExporters.SWORD_JMX_ENABLED, MBeanExporters.SWORD_JMX_DEFAULT));
        if (jmxEnabled) {
            net.guohaitao.sword.concurent.ExecutorInfo executorInfo = new net.guohaitao.sword.concurent.ExecutorInfo();
            executorInfo.setMaxExecTime(MAX_EXEC_TIME);
            executorInfo.setThreadPoolExecutor(EXECUTOR_SERVICE);
            MBeanExporters.registerBean(executorInfo);
        }
    }

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

    public static void shutdown() {
        EXECUTOR_SERVICE.shutdown();
    }
}

