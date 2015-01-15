package net.guohaitao.sword;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by i@guohaitao.net on 14-9-15.
 * Description: 字符串工具类
 */
public final class Strings {
    /**
     * 空字符串
     */
    public static final String EMPTY = "";

    private Strings() {
    }

    /**
     * 通配符匹配
     * 支持 '*' '?' '\'
     *
     * @param string
     * @param pattern
     * @return
     */
    public static boolean match(@Nonnull String string, @Nonnull String pattern) {
        Preconditions.checkNotNull(string, "The string should not be null.");
        Preconditions.checkNotNull(pattern, "The pattern should not be null.");
        return match(string, pattern, 0, 0);
    }

    /**
     * 转换String类型
     *
     * @param object
     * @return
     */
    @Nonnull
    public static String toString(@Nullable Object object) {
        return object == null ? EMPTY : object.toString();
    }

    /**
     * 清理HTML标签
     *
     * @param content
     * @return
     */
    @Nonnull
    public static String trimHtml(@Nullable String content) {
        return content == null ? EMPTY : content.replaceAll("<[/!\\?]?[a-zA-Z]+[1-9]?[^><]*>", EMPTY).trim();
    }

    /**
     * 字符串替换
     *
     * @param inString
     * @param oldPattern
     * @param newPattern
     * @return
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (isEmpty(inString) || isEmpty(oldPattern) || newPattern == null) {
            return inString;
        }
        StringBuilder sb = new StringBuilder();
        int pos = 0; // our position in the old string
        int index = inString.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }

    /**
     * 空字符串判断
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Internal matching recursive function.
     */
    private static boolean match(String string, String pattern, int stringStartNdx, int patternStartNdx) {
        int pNdx = patternStartNdx;
        int sNdx = stringStartNdx;
        int pLen = pattern.length();
        if (pLen == 1 && pattern.charAt(0) == '*') {
            return true;
        }
        int sLen = string.length();
        boolean nextIsNotWildcard = false;

        while (true) {
            // check if end of string and/or pattern occurred
            if (sNdx >= sLen) {
                // end of string still may have pending '*' in pattern
                while ((pNdx < pLen) && (pattern.charAt(pNdx) == '*')) {
                    pNdx++;
                }
                return pNdx >= pLen;
            }
            if (pNdx >= pLen) {
                // end of pattern, but not end of the string
                return false;
            }
            // pattern char
            char p = pattern.charAt(pNdx);
            // perform logic
            if (!nextIsNotWildcard) {
                if (p == '\\') {
                    pNdx++;
                    nextIsNotWildcard = true;
                    continue;
                } else if (p == '?') {
                    sNdx++;
                    pNdx++;
                    continue;
                } else if (p == '*') {
                    char pnext = 0;
                    // next pattern char
                    if (pNdx + 1 < pLen) {
                        pnext = pattern.charAt(pNdx + 1);
                    }
                    // double '*' have the same effect as one '*'
                    if (pnext == '*') {
                        pNdx++;
                        continue;
                    }
                    int i;
                    pNdx++;
                    // find recursively if there is any substring from the end of the
                    // line that matches the rest of the pattern !!!
                    for (i = string.length(); i >= sNdx; i--) {
                        if (match(string, pattern, i, pNdx)) {
                            return true;
                        }
                    }
                    return false;
                }
            } else {
                nextIsNotWildcard = false;
            }
            // check if pattern char and string char are equals
            if (p != string.charAt(sNdx)) {
                return false;
            }
            // everything matches for now, continue
            sNdx++;
            pNdx++;
        }
    }
}
