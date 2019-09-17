package com.ingic.caristocrat.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 */
public class DateFormatHelper {
    public static String changeServerToOurFormatDate(String date) {

        if (date != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                Date newDate = null;
                newDate = format.parse(date);
                format = new SimpleDateFormat(" MMM dd");
                return format.format(newDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return date;

    }

    public static boolean isCurrentDate(String date) {
        DateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
            Date newDate = null;
            newDate = format.parse(date);
            format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(newDate).equals(currentDateFormat.format(currentDate));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public static String changeServerToNotificationsDateFormat(String date) {

        if (date != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                Date newDate = null;
                newDate = format.parse(date);
                format = new SimpleDateFormat("MMM dd, yyyy");
                return format.format(newDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return date;

    }

    public static String getMinutesRemaining(String date) {

        Date currentDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date endDate = format.parse(date);
            return Math.round(-(endDate.getTime() - currentDate.getTime()) / 60000) + " min";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";

    }

    public static String changeServerToOurFormatTime(String date) {

        if (date != null) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                format.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date newDate = null;
                newDate = format.parse(date);
                format = new SimpleDateFormat("hh:mm a");
                return format.format(newDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return date;

    }

    public static String AddOneYearInCurrentDate() {
        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);
        now.add(Calendar.DATE, 365);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return format.format(now.getTime());
    }

}
