package com.grade.plugin;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.text.ParseException;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description:
 * @Date: Created in 20:05 2018/9/13
 */
public class FirstServiceTest {

    @Test
    public void firstPart() {

        /*new FirstService().firstPart("1", "2", "1", "7");
        new FirstService().firstPart("1", "2", "1", "8");*/

        new FirstService().secondPart("1", "2", "1");
        //System.out.println(new FirstService().getTestTime("2018-09-05 19:21:34", "2018-09-05 19:26:34"));
    }

    @Test
    public void getTime() throws ParseException {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date start = simpleDateFormat.parse("2018-09-05 19:21:34");
        //Date end = simpleDateFormat.parse("2018-09-05 19:26:34");
        //Date end = dateFormat.parse("2018-09-05 19:25:34");
        //Period period =Period.between(start, end);
        //long bew = end.getTime() - start.getTime();
        //Period period = Period.between(start, end);
        //System.out.println(ChronoUnit.MINUTES.between(start, end));

        //Interval interval = new Interval(start.getTime(), end.getTime());
        //Period period = interval.toPeriod();
        //System.out.println(period.getMinutes());

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        DateTime start1 = dateTimeFormatter.parseDateTime("2018-09-05 19:21:34");
        DateTime end1 = dateTimeFormatter.parseDateTime("2018-09-05 19:26:34");

        Interval interval1 = new Interval(start1, end1);
        System.out.println(interval1.toPeriod().getMinutes());
        System.out.println(Minutes.minutesBetween(start1, end1).getMinutes());
    }
}