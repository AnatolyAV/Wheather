package ru.andreev_av.weather.db.sqloperation;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

import ru.andreev_av.weather.data.model.WeatherCurrentListModel;
import ru.andreev_av.weather.data.model.WeatherCurrentModel;
import ru.andreev_av.weather.db.entry.WeatherCurrentEntry;

public class WeatherCurrentSqlOperation {

    private Context context;

    public WeatherCurrentSqlOperation(Context context) {
        this.context = context;
    }

    public void insertOrUpdate(WeatherCurrentModel weatherCurrentModel) throws IllegalArgumentException {

        ContentValues cv = new ContentValues();
        cv.put(WeatherCurrentEntry.COLUMN_CITY_ID, weatherCurrentModel.getCityId());
        cv.put(WeatherCurrentEntry.COLUMN_CITY_NAME, weatherCurrentModel.getCityName());
        if (weatherCurrentModel.getWeather() != null) {
            cv.put(WeatherCurrentEntry.COLUMN_WEATHER_ID, weatherCurrentModel.getWeather().get(0).getId());
            cv.put(WeatherCurrentEntry.COLUMN_WEATHER_MAIN, weatherCurrentModel.getWeather().get(0).getMain());
            cv.put(WeatherCurrentEntry.COLUMN_WEATHER_DESCRIPTION, weatherCurrentModel.getWeather().get(0).getDescription());
            cv.put(WeatherCurrentEntry.COLUMN_WEATHER_ICON, weatherCurrentModel.getWeather().get(0).getIcon());
        }
        if (weatherCurrentModel.getMain() != null) {
            cv.put(WeatherCurrentEntry.COLUMN_TEMPERATURE_CURRENT, weatherCurrentModel.getMain().getTemperature());
            cv.put(WeatherCurrentEntry.COLUMN_PRESSURE, weatherCurrentModel.getMain().getPressure());
            cv.put(WeatherCurrentEntry.COLUMN_HUMIDITY, weatherCurrentModel.getMain().getHumidity());
            cv.put(WeatherCurrentEntry.COLUMN_TEMPERATURE_MIN, weatherCurrentModel.getMain().getTemperatureMin());
            cv.put(WeatherCurrentEntry.COLUMN_TEMPERATURE_MAX, weatherCurrentModel.getMain().getTemperatureMax());
            cv.put(WeatherCurrentEntry.COLUMN_SEA_LEVEL, weatherCurrentModel.getMain().getSeaLevel());
            cv.put(WeatherCurrentEntry.COLUMN_GRND_LEVEL, weatherCurrentModel.getMain().getGrndLevel());
        }
        if (weatherCurrentModel.getWind() != null) {
            cv.put(WeatherCurrentEntry.COLUMN_WIND_SPEED, weatherCurrentModel.getWind().getSpeed());
            cv.put(WeatherCurrentEntry.COLUMN_WIND_DEGREE, weatherCurrentModel.getWind().getDeg());
        }
        if (weatherCurrentModel.getClouds() != null)
            cv.put(WeatherCurrentEntry.COLUMN_CLOUDINESS, weatherCurrentModel.getClouds().getAll());
        if (weatherCurrentModel.getRain() != null)
            cv.put(WeatherCurrentEntry.COLUMN_RAIN_VOLUME, weatherCurrentModel.getRain().get3h());
        if (weatherCurrentModel.getSnow() != null)
            cv.put(WeatherCurrentEntry.COLUMN_SNOW_VOLUME, weatherCurrentModel.getSnow().get3h());
        cv.put(WeatherCurrentEntry.COLUMN_TIME_MEASUREMENT, weatherCurrentModel.getDateTime());
        if (weatherCurrentModel.getSys() != null) {
            cv.put(WeatherCurrentEntry.COLUMN_TIME_SUNRISE, weatherCurrentModel.getSys().getSunrise());
            cv.put(WeatherCurrentEntry.COLUMN_TIME_SUNSET, weatherCurrentModel.getSys().getSunset());
        }

        String selection = WeatherCurrentEntry.COLUMN_CITY_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf((int) weatherCurrentModel.getCityId()),};
        int updCount = context.getContentResolver().update(WeatherCurrentEntry.CONTENT_URI, cv, selection, selectionArgs);
        if (updCount == 0) {
            context.getContentResolver().insert(WeatherCurrentEntry.CONTENT_URI, cv);
        }
    }

    public void insertOrUpdate(WeatherCurrentListModel weatherCurrentListModel) throws IllegalArgumentException {
        List<WeatherCurrentModel> weatherCurrentModelList = weatherCurrentListModel.getWeatherCurrentModelList();
        for (WeatherCurrentModel weatherCurrentModel : weatherCurrentModelList) {
            insertOrUpdate(weatherCurrentModel);
        }
    }
}
