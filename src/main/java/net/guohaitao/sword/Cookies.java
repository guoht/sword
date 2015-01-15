package net.guohaitao.sword;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Created by i@guohaitao.net on 14-9-17.
 * Description: Cookie处理工具类
 */
public final class Cookies {
    /**
     * 默认一年
     */
    private static final int MAX_AGE = 365 * 24 * 60 * 60;

    private Cookies() {
    }

    /**
     * 获得指定name的cookie
     *
     * @param request
     * @param name
     * @return
     */
    public static String get(@Nonnull HttpServletRequest request, @Nonnull String name) {
        Preconditions.checkNotNull(name, "The name should not be null.");
        Optional<Cookie[]> cookiesOptional = Optional.fromNullable(request.getCookies());
        if (cookiesOptional.isPresent()) {
            for (Cookie c : cookiesOptional.get()) {
                if (Objects.equals(c.getName(), name)) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 设置cookie
     *
     * @param response
     * @param name
     * @param value
     */
    public static void set(@Nonnull HttpServletResponse response, @Nonnull String domain, @Nonnull String name, @Nonnull String value) {
        Preconditions.checkNotNull(domain, "The domain should not be null.");
        Preconditions.checkNotNull(name, "The name should not be null.");
        Preconditions.checkNotNull(value, "The value should not be null.");
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setMaxAge(MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 设置cookie 以及过期时间
     *
     * @param response
     * @param name
     * @param value
     * @param day
     */
    public static void set(@Nonnull HttpServletResponse response, @Nonnull String domain, @Nonnull String name, @Nonnull String value, @Nonnegative int day) {
        Preconditions.checkNotNull(domain, "The domain should not be null.");
        Preconditions.checkNotNull(name, "The name should not be null.");
        Preconditions.checkNotNull(value, "The value should not be null.");
        Preconditions.checkArgument(day > 0, "day > 0");
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setMaxAge(24 * 60 * 60 * day);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 清除cookie
     *
     * @param response
     * @param domain
     * @param name
     */
    public static void clear(@Nonnull HttpServletResponse response, @Nonnull String domain, @Nonnull String name) {
        Preconditions.checkNotNull(domain, "The domain should not be null.");
        Preconditions.checkNotNull(name, "The name should not be null.");
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(-1);
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }

}
