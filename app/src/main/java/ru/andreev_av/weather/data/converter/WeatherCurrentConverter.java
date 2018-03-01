package ru.andreev_av.weather.data.converter;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.andreev_av.weather.domain.model.Clouds;
import ru.andreev_av.weather.domain.model.Main;
import ru.andreev_av.weather.domain.model.Sys;
import ru.andreev_av.weather.domain.model.Weather;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.domain.model.Wind;

import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_CITY_ID;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_CITY_NAME;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_CLOUDINESS;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_COUNTRY_CODE;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_HUMIDITY;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_PRESSURE;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_TEMPERATURE_CURRENT;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_TEMPERATURE_MAX;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_TEMPERATURE_MIN;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_TIME_MEASUREMENT;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_TIME_SUNRISE;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_TIME_SUNSET;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WEATHER_DESCRIPTION;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WEATHER_ICON;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WEATHER_ID;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WEATHER_MAIN;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WIND_SPEED;

public class WeatherCurrentConverter implements IWeatherCurrentConverter {


    @Override
    public List<WeatherCurrent> convertAll(Cursor cursor) {

        List<WeatherCurrent> weatherCurrents;
        try {
            if (cursor != null && !cursor.isClosed()) {
                weatherCurrents = new ArrayList<>();
                while (cursor.moveToNext()) {
                    WeatherCurrent weatherCurrent = convert(cursor);
                    weatherCurrents.add(weatherCurrent);
                }
            } else {
                weatherCurrents = Collections.emptyList();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return weatherCurrents;
    }

    @Override
    public WeatherCurrent convert(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            throw new IllegalArgumentException("Cursor must be initialized and not closed");
        }

        int cityId = cursor.getInt(cursor.getColumnIndex(COLUMN_CITY_ID));
        String cityName = cursor.getString(cursor.getColumnIndex(COLUMN_CITY_NAME));

        float temperature = cursor.getFloat(cursor.getColumnIndex(COLUMN_TEMPERATURE_CURRENT));
        float pressure = cursor.getFloat(cursor.getColumnIndex(COLUMN_PRESSURE));
        float humidity = cursor.getFloat(cursor.getColumnIndex(COLUMN_HUMIDITY));
        float temperatureMin = cursor.getFloat(cursor.getColumnIndex(COLUMN_TEMPERATURE_MIN));
        float temperatureMax = cursor.getFloat(cursor.getColumnIndex(COLUMN_TEMPERATURE_MAX));
        Main main = new Main(temperature, pressure, humidity, temperatureMin, temperatureMax);

        int weatherId = cursor.getInt(cursor.getColumnIndex(COLUMN_WEATHER_ID));
        String weatherMain = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_MAIN));
        String weatherDescription = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_DESCRIPTION));
        String weatherIcon = cursor.getString(cursor.getColumnIndex(COLUMN_WEATHER_ICON));
        Weather weather = new Weather(weatherId, weatherMain, weatherDescription, weatherIcon);

        List<Weather> weathers = new ArrayList<>();
        weathers.add(weather);

        float windSpeed = cursor.getFloat(cursor.getColumnIndex(COLUMN_WIND_SPEED));
        Wind wind = new Wind(windSpeed);

        String countryCode = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_CODE));
        int sysSunrise = cursor.getInt(cursor.getColumnIndex(COLUMN_TIME_SUNRISE));
        int sysSunset = cursor.getInt(cursor.getColumnIndex(COLUMN_TIME_SUNSET));
        Sys sys = new Sys(countryCode, sysSunrise, sysSunset);

        int cloudsAll = cursor.getInt(cursor.getColumnIndex(COLUMN_CLOUDINESS));
        Clouds clouds = new Clouds(cloudsAll);

        long dateTime = cursor.getLong(cursor.getColumnIndex(COLUMN_TIME_MEASUREMENT));
        return WeatherCurrent.builder()
                .cityId(cityId)
                .cityName(cityName)
                .main(main)
                .weathers(weathers)
                .wind(wind)
                .sys(sys)
                .clouds(clouds)
                .dateTime(dateTime)
                .build();
    }
}
