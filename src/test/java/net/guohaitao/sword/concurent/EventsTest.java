package net.guohaitao.sword.concurent;

import net.guohaitao.sword.Instances;
import org.junit.Test;

/**
 * Created by guohaitao on 14-9-23.
 * Description: xxx
 */
public class EventsTest {

    @Test
    public void test() {
        MsgListener msgListener = Instances.get(MsgListener.class);
        Events.register(msgListener);
        for (int i = 0; i < 5; i++) {
            Events.post("hello world " + i);
        }
    }

}
