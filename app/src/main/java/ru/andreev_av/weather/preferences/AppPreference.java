package ru.andreev_av.weather.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import ru.andreev_av.weather.model.WeatherCurrentModel;
import ru.andreev_av.weather.utils.StringUtils;

public class AppPreference {

    public static final String APP_CONFIG = "AppConfig";
    public static final String PREF_CITY = "PrefCity";
    public static final String PREF_WEATHER_CURRENT = "PrefWeatherCurrent";

    public static final String PREF_FIRST_LAUNCH_APP = "pref_first_launch_app";
    public static final String PREF_DB_CREATED = "pref_db_created";

    public static final String PREF_CITY_IDS = "pref_city_ids";

    public static final String PREF_CITY_ID = "pref_city_id";
    public static final String PREF_CITY_NAME = "pref_city_name";
    public static final String PREF_COUNTRY_CODE = "pref_country_code";
    public static final String LAST_UPDATE_TIME_IN_MS = "last_update";

    public static final String PREF_WEATHER_ID = "pref_weather_id";
    public static final String PREF_WEATHER_MAIN = "pref_weather_main";
    public static final String PREF_WEATHER_DESCRIPTION = "pref_weather_description";
    public static final String PREF_WEATHER_ICON = "pref_weather_icon";
    public static final String PREF_TEMPERATURE_CURRENT = "pref_temperature_current";
    public static final String PREF_PRESSURE = "pref_pressure";
    public static final String PREF_HUMIDITY = "pref_humidity";
    public static final String PREF_TEMPERATURE_MIN = "pref_temperature_min";
    public static final String PREF_TEMPERATURE_MAX = "pref_temperature_max";
    public static final String PREF_SEA_LEVEL = "pref_sea_level";
    public static final String PREF_GRND_LEVEL = "pref_grnd_level";
    public static final String PREF_WIND_SPEED = "pref_wind_speed";
    public static final String PREF_WIND_DEGREE = "pref_wind_degree";
    public static final String PREF_CLOUDINESS = "pref_cloudiness";
    public static final String PREF_RAIN_VOLUME = "pref_rain_volume";
    public static final String PREF_SNOW_VOLUME = "pref_snow_volume";
    public static final String PREF_TIME_MEASUREMENT = "pref_time_of_measurement";
    public static final String PREF_TIME_SUNRISE = "pref_time_sunrise";
    public static final String PREF_TIME_SUNSET = "pref_time_sunset";


    public static void setDbCreated(Context context, boolean created) {
        SharedPreferences preferences = context.getSharedPreferences(APP_CONFIG,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_DB_CREATED, created);
        editor.apply();
    }

    public static boolean isDbCreated(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_CONFIG,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(PREF_DB_CREATED, false);
    }

    public static boolean isFirstLaunchApp(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_CONFIG,
                Context.MODE_PRIVATE);
        boolean firstLaunch = preferences.getBoolean(PREF_FIRST_LAUNCH_APP, true);
        if (firstLaunch) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_FIRST_LAUNCH_APP, false);
            editor.apply();
        }
        return firstLaunch;
    }

    public static void saveWeather(Context context, WeatherCurrentModel weatherCurrent) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_WEATHER_CURRENT,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(PREF_CITY_ID, weatherCurrent.getCityId());
        editor.putString(PREF_CITY_NAME, weatherCurrent.getCityName());
        editor.putString(PREF_COUNTRY_CODE, weatherCurrent.getSys().getCountryCode());
        editor.putLong(LAST_UPDATE_TIME_IN_MS, System.currentTimeMillis());

        editor.putInt(PREF_WEATHER_ID, weatherCurrent.getWeather().get(0).getId());
        editor.putString(PREF_WEATHER_MAIN, weatherCurrent.getWeather().get(0).getMain());
        editor.putString(PREF_WEATHER_DESCRIPTION, weatherCurrent.getWeather().get(0).getDescription());
        editor.putString(PREF_WEATHER_ICON, weatherCurrent.getWeather().get(0).getIcon());

        editor.putFloat(PREF_TEMPERATURE_CURRENT, weatherCurrent.getMain().getTemperature());
        editor.putFloat(PREF_PRESSURE, weatherCurrent.getMain().getPressure());
        editor.putFloat(PREF_HUMIDITY, weatherCurrent.getMain().getHumidity());

        editor.putFloat(PREF_TEMPERATURE_MIN, weatherCurrent.getMain().getTemperatureMin());
        editor.putFloat(PREF_TEMPERATURE_MAX, weatherCurrent.getMain().getTemperatureMax());

        editor.putFloat(PREF_WIND_SPEED, weatherCurrent.getWind().getSpeed());
        editor.putInt(PREF_CLOUDINESS, weatherCurrent.getClouds().getAll());
        editor.putString(PREF_WEATHER_ICON, weatherCurrent.getWeather().get(0).getIcon());
        editor.putLong(PREF_TIME_SUNRISE, weatherCurrent.getSys().getSunrise());
        editor.putLong(PREF_TIME_SUNSET, weatherCurrent.getSys().getSunset());
        editor.putLong(PREF_TIME_MEASUREMENT, weatherCurrent.getDateTime());
        editor.apply();
    }

    public static String[] getCityAndCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_WEATHER_CURRENT,
                Context.MODE_PRIVATE);
        String[] result = new String[2];
        result[0] = preferences.getString(PREF_CITY_NAME, "Moscow");
        result[1] = preferences.getString(PREF_COUNTRY_CODE, "RU");
        return result;
    }

    public static void updateCurrentCityId(Context context, int cityId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_CITY_ID, cityId);
        editor.apply();
    }

    public static int getCurrentCityId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        return preferences.getInt(PREF_CITY_ID, -1);
    }

    public static void saveCityIds(Context context, ArrayList<Integer> cityIds) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_CITY_ID, cityIds.get(0));
        editor.putString(PREF_CITY_IDS, StringUtils.joinIds(cityIds));
        editor.apply();
    }

    public static void addCityId(Context context, Integer cityId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String cityIdsStr = preferences.getString(PREF_CITY_IDS, null);
        if (cityIdsStr != null)
            editor.putString(PREF_CITY_IDS, cityIdsStr.concat("," + cityId));
        else
            editor.putString(PREF_CITY_IDS, String.valueOf(cityId));
        editor.apply();
    }


    public static ArrayList<Integer> getCityIds(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        ArrayList<Integer> cityIdList = null;
        String cityIdsStr = preferences.getString(PREF_CITY_IDS, null);
        if (cityIdsStr != null) {
            cityIdList = StringUtils.convertStringToIntList(cityIdsStr);
        }
        return cityIdList;
    }

}
