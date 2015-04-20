package net.guohaitao.sword.jedis;

/**
 * Created by guohaitao on 14-10-16.
 * Description: Redis Db Name对象，用于区分不同的业务使用
 */
public enum RedisDbName {
    DEFAULT(0), DB_1(1), DB_2(2), DB_3(3), DB_4(4), DB_5(5);
    private int index;

    private RedisDbName(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
