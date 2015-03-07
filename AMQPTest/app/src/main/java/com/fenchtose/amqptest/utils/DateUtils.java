package com.fenchtose.amqptest.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jay Rambhia on 17/02/15.
 */
public class DateUtils {

    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";

    static DateFormat commonFormatter = new SimpleDateFormat(DATE_FORMAT_STRING);

    public static Date getDate(String strDate) {
        try {
            return commonFormatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static long getCurrentDiff(Date date, TimeUnit timeUnit) {
        return getDateDiff(date, new Date(), timeUnit);
    }

    public static String getDateString(Date date, String format) {
        DateFormat customFormat = new SimpleDateFormat(format);
        return customFormat.format(date);
    }
}
