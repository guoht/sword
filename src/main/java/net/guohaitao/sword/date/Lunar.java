package net.guohaitao.sword.date;

import com.google.common.base.Preconditions;
import net.guohaitao.sword.Strings;
import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Created by i@guohaitao.net on 14-9-22.
 * Description: 农历对象
 */
public final class Lunar {
    private int year;
    private int month;
    private int day;
    private boolean leap;
    private static final String[] CHINESE_NUMBER = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    private static final String[] CHINESE_TEN = {"初", "十", "廿", "卅"};
    private static final DateTime BASE_DATE = new DateTime(1900, 1, 31, 0, 0, 0);

    private Lunar() {
    }

    /**
     * 返回dateTime对应的农历.
     *
     * @param dateTime
     * @return
     */
    public Lunar(@Nonnull DateTime dateTime) {
        Preconditions.checkNotNull(dateTime, "The dateTime should not be null.");
        Preconditions.checkArgument(dateTime.isAfter(BASE_DATE), "Not allowed to the dateTime is before 1900 .");
        //求出和1900年1月31日相差的天数
        int offset = Days.daysBetween(BASE_DATE, dateTime).getDays();

        //用offset减去每农历年的天数
        // 计算当天是农历第几天
        //i最终结果是农历的年份
        //offset是当年的第几天
        int iYear, daysOfYear = 0;
        for (iYear = 1900; iYear < 2050 && offset > 0; iYear++) {
            daysOfYear = Lunars.yearDays(iYear);
            offset -= daysOfYear;
        }
        if (offset < 0) {
            offset += daysOfYear;
            iYear--;
        }
        //农历年份
        year = iYear;
        //闰哪个月,1-12
        int leapMonth = Lunars.leapMonth(iYear);
        leap = false;

        //用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int iMonth, daysOfMonth = 0;
        for (iMonth = 1; iMonth < 13 && offset > 0; iMonth++) {
            //闰月
            if (leapMonth > 0 && iMonth == (leapMonth + 1) && !leap) {
                --iMonth;
                leap = true;
                daysOfMonth = Lunars.leapDays(year);
            } else {
                daysOfMonth = Lunars.monthDays(year, iMonth);
            }

            offset -= daysOfMonth;
            //解除闰月
            if (leap && iMonth == (leapMonth + 1)) {
                leap = false;
            }
        }
        //offset为0时，并且刚才计算的月份是闰月，要校正
        if (offset == 0 && leapMonth > 0 && iMonth == leapMonth + 1) {
            if (leap) {
                leap = false;
            } else {
                leap = true;
                --iMonth;
            }
        }
        //offset小于0时，也要校正
        if (offset < 0) {
            offset += daysOfMonth;
            --iMonth;
        }
        month = iMonth;
        day = offset + 1;
    }

    private String getChinaDayString(@Nonnegative int day) {
        if (day > 30) {
            return Strings.EMPTY;
        }
        if (day == 10) {
            return "初十";
        } else {
            int n = day % 10 == 0 ? 9 : day % 10 - 1;
            return CHINESE_TEN[day / 10] + CHINESE_NUMBER[n];
        }
    }

    /**
     * 数字纪年
     *
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     * 天干地支纪年
     *
     * @return
     */
    public String getCyclicalYear() {
        return Lunars.cyclical(year);
    }

    /**
     * 属相纪年
     *
     * @return
     */
    public String getAnimal() {
        return Lunars.animalsYear(year);
    }

    /**
     * 月
     *
     * @return
     */
    public String getMonth() {
        return (leap ? "闰" : Strings.EMPTY) + CHINESE_NUMBER[month - 1];
    }

    /**
     * 日
     *
     * @return
     */
    public String getDay() {
        return getChinaDayString(day);
    }


    @Override
    public String toString() {
        return getCyclicalYear() + "[" + getAnimal() + "]年" + getMonth() + "月" + getDay();
    }
}