package ru.andreev_av.weather.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import ru.andreev_av.weather.model.City;
import ru.andreev_av.weather.processors.Processor;

import static ru.andreev_av.weather.services.ServiceHelper.Methods.CREATE_DATABASE;
import static ru.andreev_av.weather.services.ServiceHelper.Methods.LOAD_CITY_TO_WATCH;
import static ru.andreev_av.weather.services.ServiceHelper.Methods.LOAD_WEATHER_CITIES;
import static ru.andreev_av.weather.services.ServiceHelper.Methods.LOAD_WEATHER_CITY;
import static ru.andreev_av.weather.services.ServiceHelper.Methods.LOAD_WEATHER_FORECAST;
import static ru.andreev_av.weather.services.ServiceHelper.Methods.PARAMETER_CITY;
import static ru.andreev_av.weather.services.ServiceHelper.Methods.PARAMETER_CITY_ID;
import static ru.andreev_av.weather.services.ServiceHelper.Methods.PARAMETER_CITY_IDS;
import static ru.andreev_av.weather.services.ServiceHelper.Methods.PARAMETER_COUNT_DAYS;

public class ServiceHelper {

    private Context context;
    private String resultAction;

    public ServiceHelper(Context context, String resultAction) {
        this.context = context;
        this.resultAction = resultAction;
    }

    public void createDb() {
        initAndStartService(CREATE_DATABASE, null);
    }

    public void loadWeather(int cityId) {
        Bundle extras = new Bundle();
        extras.putInt(PARAMETER_CITY_ID, cityId);
        initAndStartService(LOAD_WEATHER_CITY, extras);
    }

    public void loadWeather(ArrayList<Integer> cityIds) {
        Bundle extras = new Bundle();
        extras.putIntegerArrayList(PARAMETER_CITY_IDS, cityIds);
        initAndStartService(LOAD_WEATHER_CITIES, extras);
    }

    public void loadWeatherForecast(City city, int countDays) {
        Bundle extras = new Bundle();
        extras.putInt(PARAMETER_CITY_ID, city.getId());
        extras.putInt(PARAMETER_COUNT_DAYS, countDays);
        initAndStartService(LOAD_WEATHER_FORECAST, extras);
    }

    public void loadCityToWatch(City city) {
        Bundle extras = new Bundle();
        extras.putParcelable(PARAMETER_CITY, city);

        initAndStartService(LOAD_CITY_TO_WATCH, extras);
    }

    private void initAndStartService(int methodId, Bundle bundle) {
        Intent intent;
        switch (methodId) {
            case CREATE_DATABASE:
                intent = new Intent(context, CreateDatabaseService.class);
                break;
            case LOAD_WEATHER_CITY:
            case LOAD_WEATHER_CITIES:
                intent = new Intent(context, WeatherCurrentService.class);
                break;
            case LOAD_WEATHER_FORECAST:
                intent = new Intent(context, WeatherForecastService.class);
                break;
            case LOAD_CITY_TO_WATCH:
                intent = new Intent(context, CityService.class);
                break;
            default:
                intent = new Intent(context, WeatherCurrentService.class);
                break;

        }
        intent.putExtra(Processor.Extras.METHOD_EXTRA, methodId);
        intent.putExtra(Processor.Extras.RESULT_ACTION_EXTRA, resultAction);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        context.startService(intent);
    }

    public static class Methods {
        public static final int CREATE_DATABASE = 1;
        public static final int LOAD_WEATHER_CITY = 2;
        public static final int LOAD_WEATHER_CITIES = 3;
        public static final int LOAD_WEATHER_FORECAST = 4;
        public static final int LOAD_CITY_TO_WATCH = 5;

        public static final String PARAMETER_CITY = "city";
        public static final String PARAMETER_CITY_ID = "cityId";
        public static final String PARAMETER_CITY_IDS = "cityIds";
        public static final String PARAMETER_COUNT_DAYS = "countDays";
    }
}
