package net.guohaitao.sword.collection;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by guohaitao on 14-9-18.
 * Description: xxx
 */
public class FixedCircularListTest {

    private final static int DEFAULT_THREADS = Runtime.getRuntime().availableProcessors() * 2;
    private final static ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREADS);

    @Test
    public void test() throws InterruptedException {
        final FixedCircularList<Integer> list = FixedCircularList.of(1, 2);

        for (int j = 0; j < 100; j++) {
            executorService.submit(new TestThread(list));
        }

        executorService.awaitTermination(10L, TimeUnit.SECONDS);
    }

    class TestThread extends Thread {
        private FixedCircularList<Integer> list;

        TestThread(FixedCircularList<Integer> list) {
            this.list = list;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    assert list.pick() > 0;
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                System.exit(-1);
            }

        }
    }
}
