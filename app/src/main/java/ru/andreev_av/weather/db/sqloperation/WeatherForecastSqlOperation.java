package ru.andreev_av.weather.db.sqloperation;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

import ru.andreev_av.weather.db.entry.WeatherForecastEntry;
import ru.andreev_av.weather.model.WeatherForecast;
import ru.andreev_av.weather.model.WeatherForecastModel;

public class WeatherForecastSqlOperation {

    private Context context;

    public WeatherForecastSqlOperation(Context context) {
        this.context = context;
    }

    public void insertOrUpdate(WeatherForecastModel weatherForecastModel) throws IllegalArgumentException {

        List<WeatherForecast> weatherForecastList = weatherForecastModel.getList();

        for (WeatherForecast weatherForecast : weatherForecastList) {

            ContentValues cv = new ContentValues();
            if (weatherForecastModel.getCity() != null)
                cv.put(WeatherForecastEntry.COLUMN_CITY_ID, weatherForecastModel.getCity().getId());
            cv.put(WeatherForecastEntry.COLUMN_DATE_TIME, weatherForecast.getDateTime());
            if (weatherForecast.getTemperature() != null) {
                cv.put(WeatherForecastEntry.COLUMN_TEMPERATURE_DAY, weatherForecast.getTemperature().getDay());
                cv.put(WeatherForecastEntry.COLUMN_TEMPERATURE_MIN, weatherForecast.getTemperature().getMin());
                cv.put(WeatherForecastEntry.COLUMN_TEMPERATURE_MAX, weatherForecast.getTemperature().getMax());
                cv.put(WeatherForecastEntry.COLUMN_TEMPERATURE_NIGHT, weatherForecast.getTemperature().getNight());
                cv.put(WeatherForecastEntry.COLUMN_TEMPERATURE_EVENING, weatherForecast.getTemperature().getEvening());
                cv.put(WeatherForecastEntry.COLUMN_TEMPERATURE_MORNING, weatherForecast.getTemperature().getMorning());
            }
            cv.put(WeatherForecastEntry.COLUMN_PRESSURE, weatherForecast.getPressure());
            cv.put(WeatherForecastEntry.COLUMN_HUMIDITY, weatherForecast.getHumidity());
            if (weatherForecast.getWeather() != null) {
                cv.put(WeatherForecastEntry.COLUMN_WEATHER_ID, weatherForecast.getWeather().get(0).getId());
                cv.put(WeatherForecastEntry.COLUMN_WEATHER_MAIN, weatherForecast.getWeather().get(0).getMain());
                cv.put(WeatherForecastEntry.COLUMN_WEATHER_DESCRIPTION, weatherForecast.getWeather().get(0).getDescription());
                cv.put(WeatherForecastEntry.COLUMN_WEATHER_ICON, weatherForecast.getWeather().get(0).getIcon());
            }
            cv.put(WeatherForecastEntry.COLUMN_WIND_SPEED, weatherForecast.getSpeed());
            cv.put(WeatherForecastEntry.COLUMN_WIND_DEGREE, weatherForecast.getDeg());
            cv.put(WeatherForecastEntry.COLUMN_CLOUDINESS, weatherForecast.getClouds());
            cv.put(WeatherForecastEntry.COLUMN_RAIN_VOLUME, weatherForecast.getRain());
            cv.put(WeatherForecastEntry.COLUMN_SNOW_VOLUME, weatherForecast.getSnow());

            String selection = WeatherForecastEntry.COLUMN_CITY_ID + " = ?" + " and " + WeatherForecastEntry.COLUMN_DATE_TIME + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(weatherForecastModel.getCity().getId()), String.valueOf(weatherForecast.getDateTime())};
            int updCount = context.getContentResolver().update(WeatherForecastEntry.CONTENT_URI, cv, selection, selectionArgs);
            if (updCount == 0) {
                context.getContentResolver().insert(WeatherForecastEntry.CONTENT_URI, cv);
            }
        }

    }
}
