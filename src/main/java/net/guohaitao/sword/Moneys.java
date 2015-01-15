package net.guohaitao.sword;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by i@guohaitao.net on 14-10-31.
 * Description: 金额转换工具类
 */
public final class Moneys {

    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^(0|[1-9]\\d{0,11})\\.(\\d\\d)$");
    private static final char[] RMB_NUMS = "零壹贰叁肆伍陆柒捌玖".toCharArray();
    private static final String[] UNITS = {"元", "角", "分", "整"};
    private static final String[] U1 = {Strings.EMPTY, "拾", "佰", "仟"};
    private static final String[] U2 = {Strings.EMPTY, "万", "亿"};

    private Moneys() {
    }

    /**
     * 转换为大写中文
     *
     * @param amount
     * @return
     */
    public static String toChinese(@Nullable String amount) {
        String localAmount = moneyFormat(amount);
        if (localAmount.equals("0.00")) {
            return Strings.EMPTY;
        }
        Matcher matcher = AMOUNT_PATTERN.matcher(localAmount);
        if (!matcher.find()) {
            return Strings.EMPTY;
        }
        String integer = matcher.group(1);
        String fraction = matcher.group(2);
        String result = Strings.EMPTY;
        if (!integer.equals("0")) {
            result += integer2rmb(integer) + UNITS[0];
        }
        if (fraction.equals("00")) {
            result += UNITS[3];
        } else if (fraction.startsWith("0") && integer.equals("0")) {
            result += fraction2rmb(fraction).substring(1);
        } else {
            result += fraction2rmb(fraction);
        }
        return result;
    }

    /**
     * 小数转换
     *
     * @param fraction
     * @return
     */
    private static String fraction2rmb(String fraction) {
        char jiao = fraction.charAt(0);
        char fen = fraction.charAt(1);
        return (RMB_NUMS[jiao - '0'] + (jiao > '0' ? UNITS[1] : Strings.EMPTY))
                + (fen > '0' ? RMB_NUMS[fen - '0'] + UNITS[2] : Strings.EMPTY);
    }

    /**
     * 整数转换
     *
     * @param integer
     * @return
     */
    private static String integer2rmb(String integer) {
        StringBuilder builder = new StringBuilder();
        int i, j;
        for (i = integer.length() - 1, j = 0; i >= 0; i--, j++) {
            char n = integer.charAt(i);
            if (n == '0') {
                if (i < integer.length() - 1 && integer.charAt(i + 1) != '0') {
                    builder.append(RMB_NUMS[0]);
                }
                if (j % 4 == 0) {
                    if (i > 0 && integer.charAt(i - 1) != '0'
                            || i > 1 && integer.charAt(i - 2) != '0'
                            || i > 2 && integer.charAt(i - 3) != '0') {
                        builder.append(U2[j / 4]);
                    }
                }
            } else {
                if (j % 4 == 0) {
                    builder.append(U2[j / 4]);
                }
                builder.append(U1[j % 4]);
                builder.append(RMB_NUMS[n - '0']);
            }
        }
        return builder.reverse().toString();
    }


    /**
     * 格式化数字金额
     *
     * @param money
     * @return
     */
    private static String moneyFormat(String money) {
        if (Strings.isEmpty(money)) {
            return "0.00";
        }
        int index = money.indexOf(".");
        if (index == -1) {
            return money + ".00";
        } else {
            //整数
            String integer = money.substring(0, index);
            //小数
            String fraction = money.substring(index + 1);
            if (fraction.length() == 1) {
                fraction = fraction + "0";
            } else if (fraction.length() > 2) {
                fraction = fraction.substring(0, 2);
            }
            return integer + "." + fraction;
        }
    }

}
