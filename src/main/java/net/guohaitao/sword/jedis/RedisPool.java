package net.guohaitao.sword.jedis;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import net.guohaitao.sword.Retrys;
import net.guohaitao.sword.collection.FixedCircularList;
import net.guohaitao.sword.concurent.Events;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by i@guohaitao.net on 14-10-16.
 * Description: Jedis客户端工具
 * 需要分别设置本地host来明确服务地址 redis.master.local,redis.slave1.local,redis.slave2.local
 */
public final class RedisPool {
    private static final Logger logger = Logger.getLogger(RedisPool.class.getName());
    /**
     * Jedis连接池配置
     */
    private static final JedisPoolConfig JEDIS_POOL_CONFIG;
    /**
     * Master服务器连接池
     */
    private static final JedisPool MASTER_JEDIS_POOL;
    /**
     * Slave 集群服务器连接池
     */
    private static final FixedCircularList<RedisPoolState> SLAVE_POOL_LIST;
    private static final ImmutableMap<String, RedisPoolState> SLAVE_POOL_MAP;
    /**
     * 支持的Host
     */
    private static final String MASTER_HOST = "redis.master.local";
    private static final String SLAVE1_HOST = "redis.slave1.local";
    private static final String SLAVE2_HOST = "redis.slave2.local";

    /**
     * #最大能够保持idle状态的对象数
     * redis.pool.maxIdle=128
     * #最大分配的对象数
     * redis.pool.maxTotal=1024
     * #多长时间检查一次连接池中空闲的连接
     * redis.pool.timeBetweenEvictionRunsMillis=30000
     * #空闲连接多长时间后会被收回
     * redis.pool.minEvictableIdleTimeMillis=-1
     * redis.pool.softMinEvictableIdleTimeMillis=10000
     * #最大等待间隔时间
     * redis.pool.maxWaitMillis=2000
     * #当调用borrow Object方法时，是否进行有效性检查
     * redis.pool.testOnBorrow=true
     * redis.pool.testWhileIdle=true
     * redis.pool.testOnReturn=false
     * #最多驱逐对象的个数
     * redis.pool.numTestsPerEvictionRun=1024
     */
    static {
        JEDIS_POOL_CONFIG = new JedisPoolConfig();
        JEDIS_POOL_CONFIG.setMaxIdle(128);
        JEDIS_POOL_CONFIG.setMaxTotal(1024);
        JEDIS_POOL_CONFIG.setTimeBetweenEvictionRunsMillis(30000L);
        JEDIS_POOL_CONFIG.setMinEvictableIdleTimeMillis(-1);
        JEDIS_POOL_CONFIG.setSoftMinEvictableIdleTimeMillis(10000L);
        JEDIS_POOL_CONFIG.setMaxWaitMillis(2000L);
        JEDIS_POOL_CONFIG.setTestOnBorrow(true);
        JEDIS_POOL_CONFIG.setTestWhileIdle(true);
        JEDIS_POOL_CONFIG.setTestOnReturn(false);
        JEDIS_POOL_CONFIG.setNumTestsPerEvictionRun(1024);

        MASTER_JEDIS_POOL = new JedisPool(JEDIS_POOL_CONFIG, MASTER_HOST);
        //Slave 多连接池
        JedisPool slave1JedisPool = new JedisPool(JEDIS_POOL_CONFIG, SLAVE1_HOST);
        JedisPool slave2JedisPool = new JedisPool(JEDIS_POOL_CONFIG, SLAVE2_HOST);
        RedisPoolState slave1PoolState = new RedisPoolState(slave1JedisPool);
        slave1PoolState.setHost(SLAVE1_HOST);
        RedisPoolState slave2PoolState = new RedisPoolState(slave2JedisPool);
        slave2PoolState.setHost(SLAVE2_HOST);
        SLAVE_POOL_LIST = FixedCircularList.of(slave1PoolState, slave2PoolState);
        //host to RedisPoolState 映射
        SLAVE_POOL_MAP = ImmutableMap.of(SLAVE1_HOST, slave1PoolState, SLAVE2_HOST, slave2PoolState);
        //注册Jedis关闭事件
        Events.register(new RedisCloseableListener());


    }

    private RedisPool() {
    }

    /**
     * 获得可写的Jedis对象
     *
     * @param redisDbName
     * @return
     */
    public static Jedis getMasterJedis(@Nonnull RedisDbName redisDbName) {
        Preconditions.checkNotNull(redisDbName, "The RedisDbName should not be null.");
        Jedis jedis = MASTER_JEDIS_POOL.getResource();
        jedis.select(redisDbName.getIndex());
        return jedis;
    }

    /**
     * 关闭Jedis对象
     *
     * @param jedis
     * @param isNormalClosed
     */
    public static void closeMasterJedis(@Nullable Jedis jedis, boolean isNormalClosed) {
        if (jedis != null) {
            Events.post(new RedisCloseable(jedis, isNormalClosed, true));
        }
    }


    /**
     * 获得可写的Jedis对象
     *
     * @param redisDbName
     * @return
     */
    public static Jedis getSlaveJedis(@Nonnull RedisDbName redisDbName) {
        Preconditions.checkNotNull(redisDbName, "The RedisDbName should not be null.");
        Jedis jedis = getRunningSlaveJedis();
        Preconditions.checkNotNull(jedis, "The Redis Slave Service ALL ERROR.");
        jedis.select(redisDbName.getIndex());
        return jedis;
    }

    private static Jedis getRunningSlaveJedis() {
        Jedis jedis = null;
        int count = 0;
        while (count < SLAVE_POOL_LIST.size() && jedis == null) {
            final RedisPoolState redisPoolState = SLAVE_POOL_LIST.pick();
            count++;

            boolean canTry = true;
            if (!redisPoolState.isRunning()) {
                DateTime failTime = redisPoolState.getFailTime();
                Duration duration = new Duration(failTime, DateTime.now());
                long minutes = Math.abs(duration.getStandardMinutes());
                //一分钟服务暂停，不需要重试
                if (minutes < 1) {
                    canTry = false;
                }
            }

            if (canTry) {
                final JedisPool jedisPool = redisPoolState.getJedisPool();
                //重复试错3次
                try {
                    jedis = Retrys.retry(new Callable<Jedis>() {
                        @Override
                        public Jedis call() throws Exception {
                            return jedisPool.getResource();
                        }
                    }, new Predicate<Throwable>() {
                        @Override
                        public boolean apply(Throwable input) {
                            return input instanceof redis.clients.jedis.exceptions.JedisConnectionException;
                        }
                    }, 3);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "The Slave Jedis Server ERROR. Host:" + redisPoolState.getHost(), ex);
                }
                //jedis为空，说明连接池异常
                if (jedis == null) {
                    redisPoolState.setRunning(false);
                    redisPoolState.setFailTime(DateTime.now());
                } else {
                    //从异常中恢复过来的话，重置状态
                    if (!redisPoolState.isRunning()) {
                        redisPoolState.setRunning(true);
                        redisPoolState.setFailTime(null);
                    }
                }

            }

        }
        return jedis;
    }

    /**
     * 关闭Jedis对象
     *
     * @param jedis
     * @param isNormalClosed
     */
    public static void closeSlaveJedis(@Nullable Jedis jedis, boolean isNormalClosed) {
        if (jedis != null) {
            Events.post(new RedisCloseable(jedis, isNormalClosed, false));
        }
    }


    /**
     * 处理Redis关闭
     */
    static class RedisCloseableListener {
        @Subscribe
        public void closeForPool(RedisCloseable redisCloseable) {
            try {
                if (redisCloseable.isMaster) {
                    if (redisCloseable.isNormalClosed) {
                        MASTER_JEDIS_POOL.returnResource(redisCloseable.getJedis());
                    } else {
                        MASTER_JEDIS_POOL.returnBrokenResource(redisCloseable.getJedis());
                    }
                } else {
                    String host = redisCloseable.getHost();
                    RedisPoolState redisPoolState = SLAVE_POOL_MAP.get(host);
                    Preconditions.checkNotNull(redisPoolState, "The redisPoolState should not be null.");
                    if (redisCloseable.isNormalClosed) {
                        redisPoolState.getJedisPool().returnResource(redisCloseable.getJedis());
                    } else {
                        redisPoolState.getJedisPool().returnBrokenResource(redisCloseable.getJedis());
                    }
                }
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "RedisCloseableListener.close ERROR.", ex);
            }
        }
    }

    /**
     * Jedis关闭对象
     */
    static class RedisCloseable {
        /**
         * Jedis对象
         */
        private Jedis jedis;
        /**
         * 是否正常关闭
         */
        private boolean isNormalClosed;
        /**
         * 是否master服务器
         */
        private boolean isMaster;

        /**
         * host
         */
        private String host;

        RedisCloseable(Jedis jedis, boolean isNormalClosed, boolean isMaster) {
            this.jedis = jedis;
            this.isNormalClosed = isNormalClosed;
            this.isMaster = isMaster;
            this.host = jedis.getClient().getHost();
        }

        public Jedis getJedis() {
            return jedis;
        }

        public boolean isNormalClosed() {
            return isNormalClosed;
        }

        public boolean isMaster() {
            return isMaster;
        }

        public String getHost() {
            return host;
        }
    }


    static class RedisPoolState {
        /**
         * JedisPool
         */
        private JedisPool jedisPool;
        /**
         * 正在运行
         */
        private boolean running = true;
        /**
         * 失败发送的时间
         */
        private DateTime failTime;
        /**
         * 连接池host
         */
        private String host;

        RedisPoolState(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
        }

        public JedisPool getJedisPool() {
            return jedisPool;
        }

        public void setJedisPool(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
        }

        public boolean isRunning() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public DateTime getFailTime() {
            return failTime;
        }

        public void setFailTime(DateTime failTime) {
            this.failTime = failTime;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

}
