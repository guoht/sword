package net.guohaitao.sword;

import org.junit.Test;

/**
 * Created by guohaitao on 14-10-31.
 * Description: xxx
 */
public class MoneysTest {

    @Test
    public void test() {
        String test1 = Moneys.toChinese("12.34");
        String test2 = Moneys.toChinese("0.12");
        String test3 = Moneys.toChinese("0");
        String test4 = Moneys.toChinese("");
        String test5 = Moneys.toChinese(null);
        assert "壹拾贰元叁角肆分".equals(test1);
        assert "壹角贰分".equals(test2);
        assert test3.isEmpty();
        assert test4.isEmpty();
        assert test5.isEmpty();

    }
}
