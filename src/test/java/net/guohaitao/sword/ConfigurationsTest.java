package net.guohaitao.sword;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by guohaitao on 14-12-31.
 * Description: xxx
 */
public class ConfigurationsTest {

    private String filename = "a.properties";
    private String key = "abc";
    private String testValue = "116699";

    @Before
    public void init() throws IOException {
        File file = Resources.getFile(Resources.CLASSPATH_URL_PREFIX + filename);
        FileWriter fw = new FileWriter(file);
        fw.write(key + "=" + testValue);
        fw.close();
    }

    @Test
    public void test() throws IOException, InterruptedException {
        String test1 = Configurations.get(filename, key);
        assert testValue.equals(test1) : test1;
        File file = Resources.getFile(Resources.CLASSPATH_URL_PREFIX + filename);
        FileWriter fw = new FileWriter(file);
        fw.write(key + "=test");
        fw.close();
        //等待重新加载
        Thread.sleep(15_000L);
        String test2 = Configurations.get(filename, key);
        assert "test".equals(test2) : test2;

    }
}
