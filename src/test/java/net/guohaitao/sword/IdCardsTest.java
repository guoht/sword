package net.guohaitao.sword;

import org.junit.Test;

/**
 * Created by guohaitao on 14-9-26.
 * Description: xxx
 */
public class IdCardsTest {

    @Test
    public void test() {
        boolean test1 = IdCards.isValid("210603198809086046",false);
        boolean test2 = IdCards.isValid("640203198012310045");
        assert test1;
        assert test2;
    }

}
