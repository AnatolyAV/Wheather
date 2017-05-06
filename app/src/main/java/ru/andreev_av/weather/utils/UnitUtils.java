package ru.andreev_av.weather.utils;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Locale;

public class UnitUtils {

    private UnitUtils() {

    }

    public static String getFormatTemperature(float temperature) {
        return String.format(Locale.getDefault(), "%.0f", temperature);
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
}
