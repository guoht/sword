package net.guohaitao.sword.concurent;

import com.google.common.eventbus.AsyncEventBus;

import javax.annotation.Nonnull;

/**
 * Created by i@guohaitao.net on 14-9-23.
 * Description: 事件触发工具类
 * listener 需要有@Subscribe注释
 */
public final class Events {


    private final static AsyncEventBus ASYNC_EVENT_BUS = new AsyncEventBus("Events.THREAD_POOL", AsyncExecutors.getExecutorService());

    private Events() {
    }

    /**
     * 注册消息订阅者
     *
     * @param listener
     */
    public static void register(@Nonnull Object listener) {
        ASYNC_EVENT_BUS.register(listener);
    }

    /**
     * 发送消息
     *
     * @param msg
     */
    public static void post(@Nonnull Object msg) {
        ASYNC_EVENT_BUS.post(msg);
    }


}
