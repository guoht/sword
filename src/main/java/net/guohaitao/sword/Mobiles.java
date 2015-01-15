package net.guohaitao.sword;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by i@guohaitao.net on 14-10-10.
 * Description: 手机相关工具类
 */
public final class Mobiles {
    private final static Pattern PHONE_PATTERN = Pattern.compile("^(130|131|132|145|155|156|184|185|186|134|135|136|137|138|139|147|150|151|152|153|157|158|159|181|182|183|187|188|133|153|189|180|170)\\d{8}$");

    private Mobiles() {
    }

    /**
     * 是否手机号
     *
     * @param phone
     * @return
     */
    public static boolean isValid(@Nullable String phone) {
        if (phone == null || phone.length() != 11) {
            return false;
        }
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        return matcher.matches();
    }

}
