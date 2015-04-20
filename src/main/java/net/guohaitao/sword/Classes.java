package net.guohaitao.sword;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by i@guohaitao.net on 15-1-7.
 * Description: Class工具类
 */
public final class Classes {

    private Classes() {
    }

    /**
     * 获取系统默认ClassLoader
     *
     * @return the default ClassLoader (only {@code null} if even the system
     * ClassLoader isn't accessible)
     * @see Thread#getContextClassLoader()
     * @see ClassLoader#getSystemClassLoader()
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = Classes.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    /**
     * 获取运行时加载的class
     *
     * @return class列表
     */
    public static List<Class> getClassesInJVM() {
        try {
            Field field = ClassLoader.class.getDeclaredField("classes");
            //设置该成员变量为可访问
            field.setAccessible(true);
            return (List<Class>) field.get(getDefaultClassLoader());
        } catch (Throwable ex) {
        }
        return null;
    }
}
