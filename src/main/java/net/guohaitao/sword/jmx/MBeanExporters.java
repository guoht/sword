package net.guohaitao.sword.jmx;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by guohaitao on 15-3-2.
 * Description: JMX工具类
 */
public class MBeanExporters {

    private static final Logger logger = Logger.getLogger(MBeanExporters.class.getName());

    public static final String SWORD_JMX_ENABLED = "sword.jmx.enabled";
    public static final String SWORD_JMX_DEFAULT = "false";

    /**
     * 注册bean
     *
     * @param bean
     */
    public static void registerBean(@Nonnull Object bean) {
        Preconditions.checkNotNull(bean);
        try {
            MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
            Class<?> clazz = bean.getClass();
            ObjectName objectName = new ObjectName(clazz.getPackage().getName() + ":name=" + clazz.getSimpleName());
            if (!platformMBeanServer.isRegistered(objectName)) {
                platformMBeanServer.registerMBean(bean, objectName);
            } else {
                logger.log(Level.WARNING, String.format("The bean[%s] has bean registered in JMX Server.", bean.getClass().getName()));
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "MBeanExporters ERROR", e);
        }
    }
}
