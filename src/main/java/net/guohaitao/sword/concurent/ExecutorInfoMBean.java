package net.guohaitao.sword.concurent;

/**
 * Created by guohaitao on 15-3-3.
 * Description: 线程执行器的基础信息
 */
public interface ExecutorInfoMBean {
    int getPoolSize();

    int getQueueSize();

    int getQueueRemainingCapacity();

    int getMaxExecTime();

    int getLargestPoolSize();

    int getCorePoolSize();

    int getMaximumPoolSize();

    int getActiveCount();

    long getCompletedTaskCount();

    long getTaskCount();
}
