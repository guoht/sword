package net.guohaitao.sword.concurent;

import com.google.common.eventbus.Subscribe;

/**
 * Created by guohaitao on 14-9-23.
 * Description: xxx
 */
public class MsgListener {

    @Subscribe
    public void doMsg1(String msg) {
        assert  msg.startsWith("hello world");
    }

    @Subscribe
    public void doMsg2(String msg) {
        assert  msg.startsWith("hello world");
    }

}
