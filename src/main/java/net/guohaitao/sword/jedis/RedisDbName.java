package net.guohaitao.sword.jedis;

/**
 * Created by i@guohaitao.net on 14-10-16.
 * Description: Redis Db Name对象，用于区分不同的业务使用
 */
public enum RedisDbName {
    DEFAULT(0), INDEX_1(1), INDEX_2(2), INDEX_3(3), INDEX_4(4), INDEX_5(5);
    private int index;

    private RedisDbName(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
