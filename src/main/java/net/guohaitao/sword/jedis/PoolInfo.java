package net.guohaitao.sword.jedis;

import redis.clients.jedis.JedisPool;

/**
 * Created by guohaitao on 15-3-2.
 * Description: Redis Pool JMX信息实现
 */
public class PoolInfo implements PoolInfoMBean {

    private JedisPool masterJedisPool;
    private JedisPool slave1JedisPool;
    private JedisPool slave2JedisPool;

    public PoolInfo(JedisPool masterJedisPool, JedisPool slave1JedisPool, JedisPool slave2JedisPool) {
        this.masterJedisPool = masterJedisPool;
        this.slave1JedisPool = slave1JedisPool;
        this.slave2JedisPool = slave2JedisPool;
    }

    @Override
    public int getMasterActive() {
        return masterJedisPool.getNumActive();
    }

    @Override
    public int getSlave1Active() {
        return slave1JedisPool.getNumActive();
    }

    @Override
    public int getSlave2Active() {
        return slave2JedisPool.getNumActive();
    }

}
