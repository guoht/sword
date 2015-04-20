package net.guohaitao.sword.concurent;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by guohaitao on 15-3-3.
 * Description: 线程执行器的基础信息
 */
public class ExecutorInfo implements ExecutorInfoMBean {
    private ThreadPoolExecutor threadPoolExecutor;
    private int maxExecTime;

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void setMaxExecTime(int maxExecTime) {
        this.maxExecTime = maxExecTime;
    }

    @Override
    public int getQueueSize() {
        return threadPoolExecutor.getQueue().size();
    }

    @Override
    public int getQueueRemainingCapacity() {
        return threadPoolExecutor.getQueue().remainingCapacity();
    }

    @Override
    public int getMaxExecTime() {
        return maxExecTime;
    }

    @Override
    public int getPoolSize() {
        return threadPoolExecutor.getPoolSize();
    }

    @Override
    public int getLargestPoolSize() {
        return threadPoolExecutor.getLargestPoolSize();
    }

    @Override
    public int getCorePoolSize() {
        return threadPoolExecutor.getCorePoolSize();
    }

    @Override
    public int getMaximumPoolSize() {
        return threadPoolExecutor.getMaximumPoolSize();
    }

    @Override
    public int getActiveCount() {
        return threadPoolExecutor.getActiveCount();
    }

    @Override
    public long getCompletedTaskCount() {
        return threadPoolExecutor.getCompletedTaskCount();
    }

    @Override
    public long getTaskCount() {
        return threadPoolExecutor.getTaskCount();
    }
}
