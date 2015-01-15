package net.guohaitao.sword;

import org.junit.Test;

/**
 * Created by guohaitao on 14-11-5.
 * Description: xxx
 */
public class NetsTest {

    @Test
    public void test() {
        boolean test1 = Nets.isValidInet4Address("198.2.3.255");
        boolean test2 = Nets.isValidInet4Address("1098.2.3.255");
        boolean test3 = Nets.isValidInet4Address(null);
        assert test1;
        assert !test2;
        assert !test3;
    }
}
