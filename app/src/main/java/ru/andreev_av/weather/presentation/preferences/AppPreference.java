package ru.andreev_av.weather.presentation.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import ru.andreev_av.weather.utils.StringUtils;

public class AppPreference {

    public static final int NOT_CURRENT_CITY_ID = -1;
    private static final String PREF_CITY = "PrefCity";
    private static final String PREF_CITY_IDS = "pref_city_ids";
    private static final String PREF_CITY_ID = "pref_city_id";
    private static final String PREF_CITY_NAME = "pref_city_name";
    private static final String PREF_COUNTRY_CODE = "pref_country_code";

    public static void saveCityNameAndCountryCode(Context context, String cityName, String countryCode) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_CITY_NAME, cityName);
        editor.putString(PREF_COUNTRY_CODE, countryCode);
        editor.apply();
    }

    public static String[] getCityAndCode(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        String[] result = new String[2];
        result[0] = preferences.getString(PREF_CITY_NAME, "Moscow");
        result[1] = preferences.getString(PREF_COUNTRY_CODE, "RU");
        return result;
    }

    public static void saveCurrentCityId(Context context, int cityId) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_CITY_ID, cityId);
        editor.apply();
    }

    public static int getCurrentCityId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        return preferences.getInt(PREF_CITY_ID, NOT_CURRENT_CITY_ID);
    }

    public static void saveCityIds(Context context, ArrayList<Integer> cityIds) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_CITY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
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
