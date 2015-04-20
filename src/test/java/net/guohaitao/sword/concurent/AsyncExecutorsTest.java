package net.guohaitao.sword.concurent;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by guohaitao on 14-12-11.
 * Description: xxx
 */
public class AsyncExecutorsTest {

    @Test
    public void test() {
        List<Callable<String>> callables = getList();
        //串行
        System.out.print("串行");
        Stopwatch stopwatch = Stopwatch.createStarted();
        List<String> list = Lists.newArrayList();
        for (Callable<String> c : callables) {
            String s = AsyncExecutors.exec(c);
            if (s != null) {
                list.add(s);
            }
        }
        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");

        //并行
        System.out.print("并行");
        stopwatch = Stopwatch.createStarted();
        list = AsyncExecutors.execAll(getList());
        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.MILLISECONDS) + "ms");

    }

    private List<Callable<String>> getList() {
        List<Callable<String>> list = Lists.newArrayList();
        //a
        list.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "a";
            }
        });
        //b
        list.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000L);
                return "b";
            }
        });
        //null
        list.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(500L);
                return null;
            }
        });
        //c
        list.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000L);
                return "c";
            }
        });
        //d
        list.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(1000L);
                return "d";
            }
        });
        return list;
    }

    @Test
    public void test2() {
        Map<String, Long> map = Maps.newHashMap();
        map.put("a", System.currentTimeMillis());
        AsyncExecutors.execAll(createMap(map));
    }

    private List<Callable<Boolean>> createMap(final Map<String, Long> map) {
        List<Callable<Boolean>> list = Lists.newArrayList();
        list.add(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Thread.sleep(1000L);
                map.put("b", System.currentTimeMillis());
                return true;
            }
        });
        list.add(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Thread.sleep(500L);
                map.put("c", System.currentTimeMillis());
                return true;
            }
        });
        list.add(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                map.put("d", System.currentTimeMillis());
                return true;
            }
        });
        return list;
    }
}
