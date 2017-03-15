package com.sleepwalker.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class TimeUtil {

    private static final String DEFAULT_TIME_STAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static Logger       logger                     = Logger.getLogger(TimeUtil.class);

    public static String formatTimeString(String sourceTimePattern, String timeStr) {
        return formatTimeString(sourceTimePattern, DEFAULT_TIME_STAMP_PATTERN, timeStr);
    }

    private static final int WORKING_HOUR_START = 8;

    private static final int WORKING_HOUR_END   = 22;

    public static void main(String[] args) {
        System.out.println(new DateTime(System.currentTimeMillis()).getDayOfWeek());
        Timestamp startTime = Timestamp.valueOf("2015-05-01 00:00:00");

        Timestamp endTime = Timestamp.valueOf("2015-05-25 00:00:00");

        for (int i = 0; i < 100; i++) {
            Timestamp sTimestamp = randomBuyingHours(startTime, endTime);
            System.out.println(sTimestamp);
        }
    }

    private static boolean isWorkingHour(DateTime dateTime) {
        if (dateTime.getHourOfDay() > WORKING_HOUR_END
            || dateTime.getHourOfDay() < WORKING_HOUR_START) {
            return false;
        }
        return true;
    }

    private static boolean isWorkingDate(DateTime dateTime) {
        if (dateTime.getDayOfWeek() >= 6) {
            return false;
        }
        return true;
    }

    private static boolean isWorkingDay(DateTime dateTime) {
        if (isWorkingHour(dateTime) && isWorkingDate(dateTime)) {
            return true;
        }
        return false;
    }

    private static boolean isBuyingHour(DateTime dateTime) {
        if (dateTime.getHourOfDay() <= 8 && dateTime.getHourOfDay() >= 2) {
            return false;
        }
        return true;
    }

    public static Timestamp randomBuyingHours(Timestamp startTime, Timestamp endTime) {
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            return null;
        }

        DateTime randTime = new DateTime(NumberUtil.rangeRandomWithRange(startTime.getTime(),
            endTime.getTime()));

        while (!isBuyingHour(randTime)) {
            randTime = new DateTime(NumberUtil.rangeRandomWithRange(startTime.getTime(),
                endTime.getTime()));
        }

        return new Timestamp(randTime.getMillis());
    }

    public static Timestamp randomWorkingDays(Timestamp startTime, Timestamp endTime) {
        if (startTime == null || endTime == null || startTime.after(endTime)) {
            return null;
        }

        DateTime startJodaTime = new DateTime(startTime.getTime());
        DateTime endJodaTime = new DateTime(endTime.getTime());
        if (startJodaTime.getWeekOfWeekyear() == endJodaTime.getWeekOfWeekyear()
            && startJodaTime.getDayOfWeek() > 5 && endJodaTime.getDayOfWeek() > 5) {
            return null;
        }

        DateTime randTime = new DateTime(NumberUtil.rangeRandomWithRange(startTime.getTime(),
            endTime.getTime()));

        while (!isWorkingDay(randTime)) {
            randTime = new DateTime(NumberUtil.rangeRandomWithRange(startTime.getTime(),
                endTime.getTime()));
        }

        return new Timestamp(randTime.getMillis());
    }

    public static String formatTimeString(String sourceTimePattern, String targetTimePattern,
                                          String timeStr) {
        if (StringUtils.isBlank(sourceTimePattern) || StringUtils.isBlank(targetTimePattern)
            || StringUtils.isBlank(timeStr)) {
            return null;
        }

        SimpleDateFormat sdfSource = new SimpleDateFormat(sourceTimePattern);
        SimpleDateFormat sdfTarget = new SimpleDateFormat(targetTimePattern);
        try {
            Date date = sdfSource.parse(timeStr);

            return sdfTarget.format(date);
        } catch (ParseException e) {
            logger.error("", e);
        }
        return null;
    }
}
