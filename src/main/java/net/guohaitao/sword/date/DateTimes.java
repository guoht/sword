package net.guohaitao.sword.date;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.guohaitao.sword.Strings;
import org.joda.time.DateTime;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by i@guohaitao.net on 14-10-6.
 * 统一转换字符串为DateTime对象
 */
public final class DateTimes {
    private static final String MOS = "(Jan|January|Feb|February|Mar|March|Apr|April|May|Jun|June|Jul|July|Aug|August|Sep|Sept|September|Oct|October|Nov|November|Dec|December)";
    private static final Pattern DATE_PATTERN_1 = Pattern.compile("(\\d{4})[\\./-](\\d{1,2})[\\./-](\\d{1,2})");
    private static final Pattern DATE_PATTERN_2 = Pattern.compile("(\\d{1,2})[\\./-](\\d{1,2})[\\./-](\\d{4})");
    private static final Pattern DATE_PATTERN_3 = Pattern.compile(MOS + "[ ,]+(\\d{1,2})[ ,]+(\\d{4})", Pattern.CASE_INSENSITIVE);
    private static final Pattern DATE_PATTERN_4 = Pattern.compile("(\\d{1,2})[ ,]" + MOS + "[ ,]+(\\d{4})", Pattern.CASE_INSENSITIVE);
    private static final Pattern DATE_PATTERN_5 = Pattern.compile("(\\d{4})[ ,]" + MOS + "[ ,]+(\\d{1,2})", Pattern.CASE_INSENSITIVE);
    private static final Pattern TIME_PATTERN_1 = Pattern.compile("(\\d{2})[:\\.](\\d{2})[:\\.](\\d{2})[\\.](\\d{1,3})");
    private static final Pattern TIME_PATTERN_2 = Pattern.compile("(\\d{2})[:\\.](\\d{2})[:\\.](\\d{2})");
    private static final Pattern TIME_PATTERN_3 = Pattern.compile("(\\d{2})[:\\.](\\d{2})");
    // Month name to number map
    private static final ImmutableMap<String, String> MONTHS = ImmutableMap.<String, String>builder()
            .put("jan", "1")
            .put("january", "1")
            .put("feb", "2")
            .put("february", "2")
            .put("mar", "3")
            .put("march", "3")
            .put("apr", "4")
            .put("april", "4")
            .put("may", "5")
            .put("jun", "6")
            .put("june", "6")
            .put("jul", "7")
            .put("july", "7")
            .put("aug", "8")
            .put("august", "8")
            .put("sep", "9")
            .put("sept", "9")
            .put("september", "9")
            .put("oct", "10")
            .put("october", "10")
            .put("nov", "11")
            .put("november", "11")
            .put("dec", "12")
            .put("december", "12").build();


    private DateTimes() {
    }

    /**
     * 解析日期格式字符串
     *
     * @param dateString
     * @return
     */

    @CheckForNull
    public static DateTime parse(@Nullable String dateString) {
        if (Strings.isEmpty(dateString)) {
            return null;
        }
        // Determine which date pattern (Matcher) to use
        Matcher matcher = DATE_PATTERN_1.matcher(dateString);
        String year, month = null, day, mon = null;
        if (matcher.find()) {
            year = matcher.group(1);
            month = matcher.group(2);
            day = matcher.group(3);
        } else {
            matcher = DATE_PATTERN_2.matcher(dateString);
            if (matcher.find()) {
                month = matcher.group(1);
                day = matcher.group(2);
                year = matcher.group(3);
            } else {
                matcher = DATE_PATTERN_3.matcher(dateString);
                if (matcher.find()) {
                    mon = matcher.group(1);
                    day = matcher.group(2);
                    year = matcher.group(3);
                } else {
                    matcher = DATE_PATTERN_4.matcher(dateString);
                    if (matcher.find()) {
                        day = matcher.group(1);
                        mon = matcher.group(2);
                        year = matcher.group(3);
                    } else {
                        matcher = DATE_PATTERN_5.matcher(dateString);
                        Preconditions.checkState(matcher.find(), "Unable to parse: " + dateString);
                        year = matcher.group(1);
                        mon = matcher.group(2);
                        day = matcher.group(3);
                    }
                }
            }
        }

        if (mon != null) {
            // Month will always be in Map, because regex forces this.
            month = MONTHS.get(mon.trim().toLowerCase());
        }
        // Determine which date pattern (Matcher) to use
        matcher = TIME_PATTERN_1.matcher(dateString);
        if (!matcher.find()) {
            matcher = TIME_PATTERN_2.matcher(dateString);
            if (!matcher.find()) {
                matcher = TIME_PATTERN_3.matcher(dateString);
                if (!matcher.find()) {
                    matcher = null;
                }
            }
        }
        // Regex prevents these from ever failing to parse
        int y = Integer.parseInt(year);
        assert month != null;
        int m = Integer.parseInt(month);
        int d = Integer.parseInt(day);
        Preconditions.checkState(m >= 1 && m <= 12, "Month must be between 1 and 12 inclusive.");
        Preconditions.checkState(d >= 1 && d <= 31, "Day must be between 1 and 31 inclusive.");

        DateTime dateTime;
        if (matcher == null) {
            // no [valid] time portion
            dateTime = new DateTime(y, m, d, 0, 0);
        } else {
            String hour = matcher.group(1);
            String min = matcher.group(2);
            String sec = "0";
            String milli = "0";
            if (matcher.groupCount() > 2) {
                sec = matcher.group(3);
            }
            if (matcher.groupCount() > 3) {
                milli = matcher.group(4);
            }
            // Regex prevents these from ever failing to parse.
            int h = Integer.parseInt(hour);
            int mn = Integer.parseInt(min);
            int s = Integer.parseInt(sec);
            int ms = Integer.parseInt(milli);

            Preconditions.checkState(h >= 0 && h <= 23, "Hour must be between 0 and 23 inclusive.");
            Preconditions.checkState(mn >= 0 && mn <= 59, "Minute must be between 0 and 59 inclusive.");
            Preconditions.checkState(s >= 0 && s <= 59, "Second must be between 0 and 59 inclusive.");
            // regex enforces millis to 000 to 999 or none
            dateTime = new DateTime(y, m, d, h, mn, s, ms);
        }
        return dateTime;
    }
}
