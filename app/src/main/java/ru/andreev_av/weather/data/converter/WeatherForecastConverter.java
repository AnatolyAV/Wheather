package ru.andreev_av.weather.data.converter;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.andreev_av.weather.domain.model.Temperature;
import ru.andreev_av.weather.domain.model.Weather;
import ru.andreev_av.weather.domain.model.WeatherForecast;

import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_CLOUDINESS;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_HUMIDITY;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_PRESSURE;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_TEMPERATURE_MAX;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_TEMPERATURE_MIN;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WEATHER_DESCRIPTION;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WEATHER_ICON;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WEATHER_ID;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WEATHER_MAIN;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WIND_SPEED;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_DATE_TIME;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_DAY;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_EVENING;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_MORNING;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_NIGHT;

public class WeatherForecastConverter implements IWeatherForecastConverter {

    @Override
    public List<WeatherForecast> convertAll(Cursor cursor) {

        List<WeatherForecast> weatherForecasts;
        if (cursor != null && !cursor.isClosed()) {
            weatherForecasts = new ArrayList<>();
            // TODO Подумать, как сделать лучше
            // пришлось так сделать т.к. позиция курсора непонятным образом менялась, что приводило к некорректным данным
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                WeatherForecast weatherForecast = convert(cursor);
                weatherForecasts.add(weatherForecast);
            }

        } else {
            weatherForecasts = Collections.emptyList();
        }

        return weatherForecasts;
    }

    @Override
    public WeatherForecast convert(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            throw new IllegalArgumentException("Cursor must be initialized and not closed");
        }

        long dateTime = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE_TIME));

        float temperatureDay = cursor.getFloat(cursor.getColumnIndex(COLUMN_TEMPERATURE_DAY));
        float temperatureMin = cursor.getFloat(cursor.getColumnIndex(COLUMN_TEMPERATURE_MIN));
        float temperatureMax = cursor.getFloat(cursor.getColumnIndex(COLUMN_TEMPERATURE_MAX));
        float temperatureNight = cursor.getFloat(cursor.getColumnIndex(COLUMN_TEMPERATURE_NIGHT));
        float temperatureEvening = cursor.getFloat(cursor.getColumnIndex(COLUMN_TEMPERATURE_EVENING));
        float temperatureMorning = cursor.getFloat(cursor.getColumnIndex(COLUMN_TEMPERATURE_MORNING));
        Temperature temperature = new Temperature(temperatureDay, temperatureMin, temperatureMax, temperatureNight, temperatureEvening, temperatureMorning);

        float pressure = cursor.getFloat(cursor.getColumnIndex(COLUMN_PRESSURE));
        float humidity = cursor.getFloat(cursor.getColumnIndex(COLUMN_HUMIDITY));

        int weatherId = cursor.getInt(cursor.getColumnIndex(COLUMN_WEATHER_ID));
        String weatherMain = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_MAIN));
        String weatherDescription = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_DESCRIPTION));
        String weatherIcon = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_ICON));
        Weather weather = new Weather(weatherId, weatherMain, weatherDescription, weatherIcon);
        List<Weather> weathers = new ArrayList<>();
        weathers.add(weather);

        float speed = cursor.getFloat(cursor.getColumnIndex(COLUMN_WIND_SPEED));
        Integer clouds = cursor.getInt(cursor.getColumnIndex(COLUMN_CLOUDINESS));

        return WeatherForecast.builder().dateTime(dateTime)
                .temperature(temperature)
                .pressure(pressure)
                .humidity(humidity)
                .weathers(weathers)
                .speed(speed)
                .clouds(clouds).build();
    }
}