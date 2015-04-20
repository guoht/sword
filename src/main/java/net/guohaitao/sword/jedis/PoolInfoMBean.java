package net.guohaitao.sword.jedis;

/**
 * Created by guohaitao on 15-3-2.
 * Description: Redis Pool JMX信息
 */
public interface PoolInfoMBean {
    /**
     * master激活的线程数
     *
     * @return
     */
    int getMasterActive();

    /**
     * Slave1 激活的线程数
     *
     * @return
     */
    int getSlave1Active();

    /**
     * Slave2 激活的线程数
     *
     * @return
     */
    int getSlave2Active();
}
