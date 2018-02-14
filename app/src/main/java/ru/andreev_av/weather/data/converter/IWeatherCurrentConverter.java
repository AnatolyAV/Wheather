package ru.andreev_av.weather.data.converter;

import android.database.Cursor;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherCurrent;

public interface IWeatherCurrentConverter {

    List<WeatherCurrent> convertAll(Cursor cursor);

    WeatherCurrent convert(Cursor cursor);
}
