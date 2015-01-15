package net.guohaitao.sword.date;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Created by guohaitao on 14-9-19.
 * Description: xxx
 */
public class CalendarsTest {

    @Test
    public void test() {
        //通过月日查找星座
        Constellation constellation1 = Calendars.constellationByMonthDay(11, 1);
        assert constellation1.getName().equals("天蝎");
        //通过id查找星座
        Constellation constellation2 = Calendars.constellationById(10);
        assert constellation2.getName().equals("天蝎");
        //通过id查找星座
        Constellation constellation3 = Calendars.constellationByName("天蝎");
        assert constellation3.getName().equals("天蝎");

        int age = Calendars.ageByBirthday(new DateTime(1989, 12, 1, 10, 0, 0));
        assert age == 25;

        Lunar lunar = new Lunar(DateTime.parse("2014-01-22"));
        assert "癸巳[蛇]年十二月廿二".equals(lunar.toString());
        assert "蛇".equals(Lunars.animalsYear(lunar.getYear()));

        Lunar lunar2 = new Lunar(DateTime.parse("2014-02-06"));
        assert "马".equals(Lunars.animalsYear(lunar2.getYear()));
    }

    @Test
    public void testDateTimes() {
        DateTime dateTime = DateTimes.parse("2014-2-14 18:20:58");
        assert "2014-02-14T18:20:58.000+08:00".equals(dateTime.toString());
    }
}
