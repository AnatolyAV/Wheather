package ru.andreev_av.weather.data.converter;

import android.database.Cursor;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherForecast;

public interface IWeatherForecastConverter {

    List<WeatherForecast> convertAll(Cursor cursor);

    WeatherForecast convert(Cursor cursor);
}