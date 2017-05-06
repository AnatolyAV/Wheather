package ru.andreev_av.weather.db.entry;

import android.net.Uri;
import android.provider.BaseColumns;

import static ru.andreev_av.weather.db.provider.WeatherContentProviderData.BASE_CONTENT_URI;
import static ru.andreev_av.weather.db.provider.WeatherContentProviderData.CONTENT_AUTHORITY;

public class CityEntry implements BaseColumns {

    public static final String PATH = "city";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + CONTENT_AUTHORITY + "." + PATH;

    public static final String TABLE_CITY = "city";

    public static final String COLUMN_CITY_ID = "city_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_COUNTRY_CODE = "country_code";
    public static final String COLUMN_WATCHED = "watched";
}
