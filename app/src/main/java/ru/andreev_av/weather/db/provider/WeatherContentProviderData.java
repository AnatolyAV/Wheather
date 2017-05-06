package ru.andreev_av.weather.db.provider;

import android.net.Uri;

public class WeatherContentProviderData {

    public static final String CONTENT_AUTHORITY = "ru.andreev_av.weather.provider.Weather";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private WeatherContentProviderData() {
    }
}
