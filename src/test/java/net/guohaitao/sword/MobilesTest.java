package net.guohaitao.sword;

import org.junit.Test;

/**
 * Created by guohaitao on 14-10-10.
 * Description: xxx
 */
public class MobilesTest {

    @Test
    public void test() {
        boolean test1 = Mobiles.isValid("1311898882q");
        boolean test2 = Mobiles.isValid("13118988821");
        boolean test3 = Mobiles.isValid("");
        assert !test1;
        assert test2;
        assert !test3;
    }
}
