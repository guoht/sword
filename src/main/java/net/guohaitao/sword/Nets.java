package net.guohaitao.sword;

import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by i@guohaitao.net on 14-9-17.
 * Description: 网络工具类
 */
public final class Nets {
    private static final Logger logger = Logger.getLogger(Nets.class.getName());
    //IP地址匹配
    private static final String IPV4_REGEX = "^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$";
    private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);

    private Nets() {
    }

    /**
     * 本机IP地址
     */
    public static String serverIp() {
        try {
            for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
                NetworkInterface nextInterface = networkInterfaces.nextElement();
                if (!nextInterface.isLoopback() && !nextInterface.isVirtual() && !nextInterface.isPointToPoint()) {
                    for (final Enumeration<InetAddress> addresses = nextInterface.getInetAddresses(); addresses.hasMoreElements(); ) {
                        InetAddress inetAddress = addresses.nextElement();
                        byte[] address = inetAddress.getAddress();
                        if ((address.length == 4)
                                && (address[0] != 127 || address[1] != 0)) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }

            }
        } catch (SocketException ex) {
            logger.log(Level.WARNING, "serverIp SocketException error.", ex);
        }

        return null;
    }

    /**
     * 用户真实IP地址
     *
     * @param request
     * @return
     */
    public static String clientIp(@Nonnull HttpServletRequest request) {
        Optional<String> realIp = Optional.fromNullable(request.getHeader("X-Real-IP"));
        String ip = (realIp.isPresent()) ? realIp.get() : request.getRemoteAddr();
        //多个IP情况,获取最后一个Ip地址
        if (ip.contains(",")) {
            Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
            List<String> ipList = splitter.splitToList(ip);
            return ipList.get(ipList.size() - 1);
        }
        return ip;
    }

    /**
     * 用户代理ip列表
     *
     * @param request
     */
    public static List<String> clientForwardedIp(@Nonnull HttpServletRequest request) {
        Optional<String> opForwardIp = Optional.fromNullable(request.getHeader("X-Forwarded-For"));
        if (opForwardIp.isPresent()) {
            Splitter splitter = Splitter.on(',').trimResults().omitEmptyStrings();
            return ImmutableList.<String>builder()
                    .addAll(splitter.split(opForwardIp.get()))
                    .build();
        } else {
            return ImmutableList.<String>builder()
                    .add(clientIp(request)).build();
        }
    }


    /**
     * 验证Ip地址是否合法
     *
     * @param inet4Address
     * @return
     */
    public static boolean isValidInet4Address(@Nullable String inet4Address) {
        if (Strings.isEmpty(inet4Address)) {
            return false;
        }
        Matcher matcher = IPV4_PATTERN.matcher(inet4Address);
        if (matcher.matches()) {
            try {
                for (int i = 1; i <= 4; i++) {
                    if (Integer.parseInt(matcher.group(i)) > 255) {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

}
