package net.guohaitao.sword.http;

import com.google.common.base.Preconditions;
import net.guohaitao.sword.Strings;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by guohaitao on 15-3-19.
 * Description: HttpRequest.getAttribute工具类
 */
public class RequestAttributes {

    /**
     * request返回int
     *
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static int toInt(HttpServletRequest request, String name, int defaultValue) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(name);
        String value = Strings.toString(request.getAttribute(name));
        return (Strings.isEmpty(value)) ? defaultValue : Integer.parseInt(value);
    }

    /**
     * request返回float
     *
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static float toFloat(HttpServletRequest request, String name, float defaultValue) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(name);
        String value = Strings.toString(request.getAttribute(name));
        return (Strings.isEmpty(value)) ? defaultValue : Float.parseFloat(value);
    }

    /**
     * request返回double
     *
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static double toDouble(HttpServletRequest request, String name, double defaultValue) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(name);
        String value = Strings.toString(request.getAttribute(name));
        return (Strings.isEmpty(value)) ? defaultValue : Double.parseDouble(value);
    }

    /**
     * request返回long
     *
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static long toLong(HttpServletRequest request, String name, long defaultValue) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(name);
        String value = Strings.toString(request.getAttribute(name));
        return (Strings.isEmpty(value)) ? defaultValue : Long.parseLong(value);
    }

    /**
     * request返回String
     *
     * @param request
     * @param name
     * @param defaultValue
     * @return
     */
    public static String toString(HttpServletRequest request, String name, String defaultValue) {
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(name);
        String value = Strings.toString(request.getAttribute(name));
        return (Strings.isEmpty(value)) ? defaultValue : value;
    }

}
