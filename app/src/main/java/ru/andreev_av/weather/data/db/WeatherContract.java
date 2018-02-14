package ru.andreev_av.weather.data.db;

import android.provider.BaseColumns;

/**
 * Пример - @see <a href="https://developer.android.com/training/basics/data-storage/databases.html"></a>
 */
public final class WeatherContract {

    public static final String SQL_CREATE_CITY_TABLE = "CREATE TABLE " + CityEntry.TABLE_NAME + "(" +
            CityEntry._ID + " INTEGER primary key autoincrement, " +
            CityEntry.COLUMN_CITY_ID + " INTEGER NOT NULL, " +
            CityEntry.COLUMN_NAME + " VARCHAR(100) NOT NULL, " +
            CityEntry.COLUMN_LATITUDE + " FLOAT NOT NULL, " +
            CityEntry.COLUMN_LONGITUDE + " FLOAT NOT NULL, " +
            CityEntry.COLUMN_COUNTRY_CODE + " VARCHAR(3) NOT NULL, " +
            CityEntry.COLUMN_WATCHED + " INT " +
            ")";

    public static final String SQL_CREATE_WEATHER_CURRENT_TABLE = "CREATE TABLE " + WeatherCurrentEntry.TABLE_NAME + "(" +
            WeatherCurrentEntry._ID + " INTEGER primary key autoincrement, " +
            WeatherCurrentEntry.COLUMN_CITY_ID + " INTEGER NOT NULL, " +
            WeatherCurrentEntry.COLUMN_CITY_NAME + " VARCHAR(100) NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WEATHER_MAIN + " VARCHAR(50) NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WEATHER_DESCRIPTION + " VARCHAR(50) NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WEATHER_ICON + " VARCHAR(5) NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TEMPERATURE_CURRENT + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_PRESSURE + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_HUMIDITY + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TEMPERATURE_MIN + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TEMPERATURE_MAX + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_SEA_LEVEL + " FLOAT, " +
            WeatherCurrentEntry.COLUMN_GRND_LEVEL + " FLOAT, " +
            WeatherCurrentEntry.COLUMN_WIND_SPEED + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WIND_DEGREE + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_CLOUDINESS + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_RAIN_VOLUME + " FLOAT, " +
            WeatherCurrentEntry.COLUMN_SNOW_VOLUME + " FLOAT, " +
            WeatherCurrentEntry.COLUMN_TIME_MEASUREMENT + " BIGINT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TIME_SUNRISE + " BIGINT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TIME_SUNSET + " BIGINT NOT NULL " +
            ")";
    public static final String SQL_CREATE_WEATHER_FORECAST_TABLE = "CREATE TABLE " + WeatherForecastEntry.TABLE_NAME + "(" +
            WeatherForecastEntry._ID + " INTEGER primary key autoincrement, " +
            WeatherForecastEntry.COLUMN_CITY_ID + " INTEGER NOT NULL, " +
            WeatherForecastEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
            WeatherForecastEntry.COLUMN_WEATHER_MAIN + " VARCHAR(50) NOT NULL, " +
            WeatherForecastEntry.COLUMN_WEATHER_DESCRIPTION + " VARCHAR(50) NOT NULL, " +
            WeatherForecastEntry.COLUMN_WEATHER_ICON + " VARCHAR(5) NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_DAY + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_MIN + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_MAX + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_NIGHT + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_EVENING + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_MORNING + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_PRESSURE + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_HUMIDITY + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_WIND_SPEED + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_WIND_DEGREE + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_CLOUDINESS + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_RAIN_VOLUME + " FLOAT, " +
            WeatherForecastEntry.COLUMN_SNOW_VOLUME + " FLOAT, " +
            WeatherForecastEntry.COLUMN_DATE_TIME + " BIGINT NOT NULL " +
            ")";

    public static abstract class CityEntry implements BaseColumns {
        public static final String TABLE_NAME = "city";
        public static final String COLUMN_CITY_ID = "city_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_COUNTRY_CODE = "country_code";
        public static final String COLUMN_WATCHED = "watched";
    }

    public static abstract class WeatherCurrentEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather_current";

        public static final String COLUMN_CITY_ID = "city_id";
        public static final String COLUMN_CITY_NAME = "city_name";

        public static final String COLUMN_WEATHER_ID = "weather_id";
        public static final String COLUMN_WEATHER_MAIN = "weather_main";
        public static final String COLUMN_WEATHER_DESCRIPTION = "weather_description";
        public static final String COLUMN_WEATHER_ICON = "weather_icon";

        public static final String COLUMN_TEMPERATURE_CURRENT = "temperature_current";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_TEMPERATURE_MIN = "temperature_min";
        public static final String COLUMN_TEMPERATURE_MAX = "temperature_max";
        public static final String COLUMN_SEA_LEVEL = "sea_level";
        public static final String COLUMN_GRND_LEVEL = "grnd_level";

        public static final String COLUMN_WIND_SPEED = "wind_speed";
        public static final String COLUMN_WIND_DEGREE = "wind_degree";

        public static final String COLUMN_CLOUDINESS = "cloudiness";

        public static final String COLUMN_RAIN_VOLUME = "rain_volume";
        public static final String COLUMN_SNOW_VOLUME = "snow_volume";

        public static final String COLUMN_TIME_MEASUREMENT = "time_of_measurement";

        public static final String COLUMN_TIME_SUNRISE = "time_sunrise";
        public static final String COLUMN_TIME_SUNSET = "time_sunset";
    }

    public static abstract class WeatherForecastEntry implements BaseColumns {
        public static final String TABLE_NAME = "weather_forecast";

        public static final String COLUMN_CITY_ID = "city_id";

        public static final String COLUMN_DATE_TIME = "date_time";

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
}
