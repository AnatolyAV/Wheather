package ru.andreev_av.weather.db.entry;

import android.net.Uri;
import android.provider.BaseColumns;

import static ru.andreev_av.weather.db.provider.WeatherContentProviderData.BASE_CONTENT_URI;
import static ru.andreev_av.weather.db.provider.WeatherContentProviderData.CONTENT_AUTHORITY;

public class WeatherForecastEntry implements BaseColumns {

    public static final String PATH = "weatherForecast";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + CONTENT_AUTHORITY + "." + PATH;

    public static final String TABLE_WEATHER_FORECAST = "weather_forecast";

    public static final String COLUMN_CITY_ID = "city_id";

    public static final String COLUMN_TIME_MEASUREMENT = "time_of_measurement";

    public static final String COLUMN_TEMPERATURE_DAY = "temperature_day";
    public static final String COLUMN_TEMPERATURE_MIN = "temperature_min";
    public static final String COLUMN_TEMPERATURE_MAX = "temperature_max";
    public static final String COLUMN_TEMPERATURE_NIGHT = "temperature_night";
    public static final String COLUMN_TEMPERATURE_EVENING = "temperature_evening";
    public static final String COLUMN_TEMPERATURE_MORNING = "temperature_morning";

    public static final String COLUMN_HUMIDITY = "humidity";
    public static final String COLUMN_PRESSURE = "pressure";

    public static final String COLUMN_WEATHER_ID = "weather_id";
    public static final String COLUMN_WEATHER_MAIN = "weather_main";
    public static final String COLUMN_WEATHER_DESCRIPTION = "weather_description";
    public static final String COLUMN_WEATHER_ICON = "weather_icon";

    public static final String COLUMN_WIND_SPEED = "wind_speed";
    public static final String COLUMN_WIND_DEGREE = "wind_degree";

    public static final String COLUMN_CLOUDINESS = "cloudiness";

    public static final String COLUMN_RAIN_VOLUME = "rain_volume";
    public static final String COLUMN_SNOW_VOLUME = "snow_volume";
}
