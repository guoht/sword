package net.guohaitao.sword;

import org.junit.Test;

/**
 * Created by guohaitao on 14-9-11.
 * Description: xxx
 */
public class EmailCheckerTest {

    @Test
    public void testEmail() {
        boolean test1 = Emails.isValid("i@guohaitao.net");
        boolean test2 = Emails.isValid("www.you.c@om");
        assert test1;
        assert !test2;
    }

}
