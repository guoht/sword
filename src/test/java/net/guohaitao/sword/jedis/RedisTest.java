package net.guohaitao.sword.jedis;

import org.junit.Test;

/**
 * Created by guohaitao on 14-10-28.
 * Description: xxx
 */
public class RedisTest {

    /**
     * 当设置本机host后，打开测试
     * redis.master.local,redis.slave1.local,redis.slave2.local
     *
     * @throws Exception
     */
    @Test
    public void testWriteAndRead() throws Exception {
        /*String key = "test";
        String value = "a";
        Jedis jedis = null;
        try {
            jedis = RedisPool.getMasterJedis(RedisDbName.HULU);
            jedis.set(key, value);
            RedisPool.closeMasterJedis(jedis, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            RedisPool.closeMasterJedis(jedis, false);
        }
        Jedis slaveJedis = RedisPool.getSlaveJedis(RedisDbName.HULU);
        assert value.equals(slaveJedis.get(key));
        RedisPool.closeSlaveJedis(slaveJedis, true);*/
    }

}
