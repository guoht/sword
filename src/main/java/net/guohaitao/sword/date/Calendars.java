package net.guohaitao.sword.date;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import org.joda.time.DateTime;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static net.guohaitao.sword.date.Constellation.*;

/**
 * Created by i@guohaitao.net on 14-9-19.
 * Description: 公历相关的工具类
 */
public final class Calendars {

    /**
     * 星座的id map
     */
    private static final ImmutableMap<Integer, Constellation> ID_CONSTELLATION_MAP = ImmutableMap.<Integer, Constellation>builder()
            .put(AQUARIUS.getId(), AQUARIUS)
            .put(PISCES.getId(), PISCES)
            .put(ARIES.getId(), ARIES)
            .put(TAURUS.getId(), TAURUS)
            .put(GEMINI.getId(), GEMINI)
            .put(CANCER.getId(), CANCER)
            .put(LEO.getId(), LEO)
            .put(VIRGO.getId(), VIRGO)
            .put(LIBRA.getId(), LIBRA)
            .put(SCORPIO.getId(), SCORPIO)
            .put(SAGITTARIUS.getId(), SAGITTARIUS)
            .put(CAPRICORN.getId(), CAPRICORN)
            .build();
    /**
     * 星座的name map
     */
    private static final ImmutableMap<String, Constellation> NAME_CONSTELLATION_MAP = ImmutableMap.<String, Constellation>builder()
            .put(AQUARIUS.getName(), AQUARIUS)
            .put(PISCES.getName(), PISCES)
            .put(ARIES.getName(), ARIES)
            .put(TAURUS.getName(), TAURUS)
            .put(GEMINI.getName(), GEMINI)
            .put(CANCER.getName(), CANCER)
            .put(LEO.getName(), LEO)
            .put(VIRGO.getName(), VIRGO)
            .put(LIBRA.getName(), LIBRA)
            .put(SCORPIO.getName(), SCORPIO)
            .put(SAGITTARIUS.getName(), SAGITTARIUS)
            .put(CAPRICORN.getName(), CAPRICORN)
            .build();

    private final static Constellation CONSTELLATIONS[] = {
            AQUARIUS,
            PISCES,
            ARIES,
            TAURUS,
            GEMINI,
            CANCER,
            LEO,
            VIRGO,
            LIBRA,
            SCORPIO,
            SAGITTARIUS,
            CAPRICORN
    };

    private final static int[] CONSTELLATION_BEGIN_INDEX = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

    private Calendars() {
    }


    /**
     * 星座 by month,day
     *
     * @param month
     *         月
     * @param day
     *         日
     * @return
     */
    public static Constellation constellationByMonthDay(@Nonnegative int month, @Nonnegative int day) {
        Preconditions.checkArgument(month >= 1 && month <= 12, "The month[1-12] argument error.");
        Preconditions.checkArgument(day >= 1 && day <= 31, "The day[1-31] argument error.");
        return (CONSTELLATION_BEGIN_INDEX[month - 1] <= day) ?
                CONSTELLATIONS[month - 1] :
                (month == 1) ? CONSTELLATIONS[11] : CONSTELLATIONS[month - 2];
    }

    /**
     * 星座 by id
     *
     * @param id
     * @return
     */
    public static Constellation constellationById(@Nonnegative int id) {
        Preconditions.checkArgument(id > 0, "The id argument error.");
        return ID_CONSTELLATION_MAP.get(id);
    }

    /**
     * 星座 by name
     *
     * @param name
     * @return
     */
    public static Constellation constellationByName(@Nonnull String name) {
        Preconditions.checkNotNull(name, "The name should not be null.");
        return NAME_CONSTELLATION_MAP.get(name);
    }


    /**
     * 根据生日计算年龄
     */
    public static int ageByBirthday(@Nonnull DateTime birthday) {
        Preconditions.checkNotNull(birthday, "The birthday should be null.");
        DateTime now = DateTime.now();
        Preconditions.checkArgument(birthday.isBefore(now), "The birthDay is after Now.It's unbelievable!");


        int yearNow = now.getYear();
        int monthNow = now.getMonthOfYear();
        int dayOfMonthNow = now.getDayOfMonth();

        int yearBirth = birthday.getYear();
        int monthBirth = birthday.getMonthOfYear();
        int dayOfMonthBirth = birthday.getDayOfMonth();

        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

}
