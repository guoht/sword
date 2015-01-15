package net.guohaitao.sword.jedis;

/**
 * Created by i@guohaitao.net on 14-10-16.
 * Description: Redis Db Name对象，用于区分不同的业务使用
 */
public enum RedisDbName {
    SHARE(1), WWW(2), HULU(3);
    private int index;

    private RedisDbName(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
