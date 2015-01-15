package net.guohaitao.sword.date;

import com.google.common.base.MoreObjects;
import org.joda.time.MonthDay;

/**
 * Created by i@guohaitao.net on 14-9-19.
 * Description: 星座
 */
public enum Constellation {
    AQUARIUS(1, "水瓶", new MonthDay(1, 20), new MonthDay(2, 18)),
    PISCES(2, "双鱼", new MonthDay(2, 19), new MonthDay(3, 20)),
    ARIES(3, "白羊", new MonthDay(3, 21), new MonthDay(4, 20)),
    TAURUS(4, "金牛", new MonthDay(4, 21), new MonthDay(5, 20)),
    GEMINI(5, "双子", new MonthDay(5, 21), new MonthDay(6, 21)),
    CANCER(6, "巨蟹", new MonthDay(6, 22), new MonthDay(7, 22)),
    LEO(7, "狮子", new MonthDay(7, 23), new MonthDay(8, 22)),
    VIRGO(8, "处女", new MonthDay(8, 23), new MonthDay(9, 22)),
    LIBRA(9, "天秤", new MonthDay(9, 23), new MonthDay(10, 22)),
    SCORPIO(10, "天蝎", new MonthDay(10, 23), new MonthDay(11, 21)),
    SAGITTARIUS(11, "射手", new MonthDay(11, 22), new MonthDay(12, 21)),
    CAPRICORN(12, "摩羯", new MonthDay(12, 22), new MonthDay(12, 31), new MonthDay(1, 1), new MonthDay(1, 19)),;

    /**
     * 唯一ID
     */
    private int id;
    /**
     * 名称
     */
    private String name;
    /**
     * 起始日期
     */
    private MonthDay start1;
    private MonthDay end1;
    private MonthDay start2;
    private MonthDay end2;

    Constellation(int id, String name, MonthDay start, MonthDay end) {
        this.id = id;
        this.name = name;
        this.start1 = start;
        this.end1 = end;
    }

    Constellation(int id, String name, MonthDay start1, MonthDay end1, MonthDay start2, MonthDay end2) {
        this.id = id;
        this.name = name;
        this.start1 = start1;
        this.end1 = end1;
        this.start2 = start2;
        this.end2 = end2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MonthDay getStart1() {
        return start1;
    }

    public MonthDay getEnd1() {
        return end1;
    }

    public MonthDay getStart2() {
        return start2;
    }

    public MonthDay getEnd2() {
        return end2;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("start1", start1)
                .add("end1", end1)
                .add("start2", start2)
                .add("end2", end2)
                .toString();
    }
}
