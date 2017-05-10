package ru.andreev_av.weather.utils;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UnitUtils {

    private UnitUtils() {

    }

    public static String getFormatTemperature(float temperature) {
        return (temperature > 0 ? String.format(Locale.getDefault(), "+%.0f", temperature) : String.format(Locale.getDefault(), "%.0f", temperature));
    }

    public static String getFormatPressure(float pressure) {
        return String.format(Locale.getDefault(), "%.1f", pressure);
    }

    public static String getFormatWind(float wind) {
        return String.format(Locale.getDefault(), "%.1f", wind);
    }

    public static String getFormatDateTime(Context context, long time) {
        return DateUtils.formatDateTime(context, time, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
    }

    public static String getFormatUnixTime(Context context, long unixTime) {
        long unixTimeToMillis = unixTime * 1000;
        return DateUtils.formatDateTime(context, unixTimeToMillis, DateUtils.FORMAT_SHOW_TIME);
    }

    public static String getFormatDateTimeEEEddMMMM(long unixTime) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMMM", Locale.getDefault());
        long unixTimeToMillis = unixTime * 1000;
        Date date = new Date(unixTimeToMillis);
        return format.format(date);
    }

}
