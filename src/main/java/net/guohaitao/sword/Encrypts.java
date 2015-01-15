package net.guohaitao.sword;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;

import javax.annotation.Nonnull;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by i@guohaitao.net on 14-9-18.
 * Description: 加密算法工具类
 */
public final class Encrypts {

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private Encrypts() {
    }

    /**
     * md5编码
     *
     * @param source
     *         需要计算md5的byte数组
     * @return MD5
     */
    public static String md5(@Nonnull byte[] source) {
        Preconditions.checkNotNull(source, "The source should not be null.");
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Throwables.propagate(e);
        }
        assert messageDigest != null;
        messageDigest.update(source);
        byte digest[] = messageDigest.digest();
        char[] str = new char[32];
        for (int i = 0, k = 0; i < 16; i++) {
            // 取第 i 个字节
            byte byte0 = digest[i];
            // 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移
            str[k++] = HEX_DIGITS[byte0 >>> 4 & 0xf];
            // 取字节中低 4 位的数字转换
            str[k++] = HEX_DIGITS[byte0 & 0xf];
        }
        return new String(str);

    }

    /**
     * md5编码
     *
     * @param source
     * @return md5
     */
    public static String md5(@Nonnull String source) {
        Preconditions.checkNotNull(source, "The source should not be null.");
        return md5(source.getBytes(Charset.forName("UTF-8")));
    }
}
