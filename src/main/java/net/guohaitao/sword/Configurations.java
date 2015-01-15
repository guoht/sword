package net.guohaitao.sword;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import net.guohaitao.sword.collection.Pair;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by i@guohaitao.net on 14-12-31.
 * Description: 配置properties文件工具类，变更配置文件不需要服务重启
 */
public final class Configurations {

    /**
     * 内部属性Table
     * String1 filename ,String2 hashcode ,Properties value
     */
    private static final Map<String, Pair<String, Properties>> propertiesMap = Maps.newConcurrentMap();

    private Configurations() {
    }

    /**
     * 读取配置信息
     *
     * @param filename
     *         绝对路径和文件名
     * @param name
     *         属性名称
     * @return
     */
    public static String get(String filename, String name) {
        Preconditions.checkNotNull(filename);
        Preconditions.checkNotNull(name);
        return getProperties(filename).getProperty(name);
    }

    /**
     * 读取配置信息-带默认值
     *
     * @param filename
     *         绝对路径和文件名
     * @param name
     * @return
     */
    public static String get(String filename, String name, String defaultValue) {
        Preconditions.checkNotNull(filename);
        Preconditions.checkNotNull(name);
        return getProperties(filename).getProperty(name, defaultValue);
    }

    /**
     * 加载文件锁
     */
    private static final Lock LOAD_LOCK = new ReentrantLock();

    /**
     * 根据文件名获取properties
     *
     * @param filename
     * @return
     */
    private static Properties getProperties(String filename) {
        Pair<String, Properties> pair = propertiesMap.get(filename);
        if (pair == null) {
            //获得锁,防止多次load
            LOAD_LOCK.lock();
            try {
                if (propertiesMap.get(filename) == null) {
                    loadProperties(filename);
                }
            } finally {
                LOAD_LOCK.unlock();
            }
            pair = propertiesMap.get(filename);
        }
        return pair.getSecond();
    }

    /**
     * 加载配置文件
     *
     * @param filename
     */
    private static void loadProperties(String filename) {
        try (FileReader fileReader = new FileReader(Resources.getFile(Resources.CLASSPATH_URL_PREFIX + filename))) {
            Properties properties = new Properties();
            properties.load(fileReader);
            String hash = hashFile(filename);
            propertiesMap.put(filename, Pair.of(hash, properties));
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }
    }

    /**
     * 观察配置文件变更并及时更新
     */
    private static void watchProperties() {
        Timer timer = new Timer();
        //10s后启动，每隔60s检查文件是否修改
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (String filename : propertiesMap.keySet()) {
                    Pair<String, Properties> pair = propertiesMap.get(filename);
                    String hash = hashFile(filename);
                    if (!hash.equals(pair.getFirst())) {
                        loadProperties(filename);
                    }
                }
            }
        }, 10_000L, 60_000L);
    }

    private static String hashFile(String filename) {
        try {
            File file = Resources.getFile(Resources.CLASSPATH_URL_PREFIX + filename);
            HashCode hashCode = Files.hash(file, Hashing.sha1());
            return hashCode.toString();
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }
    }

    static {
        watchProperties();
    }

}
