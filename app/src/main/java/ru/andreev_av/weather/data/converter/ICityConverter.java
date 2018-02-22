package ru.andreev_av.weather.data.converter;

import android.database.Cursor;

import java.util.List;

import ru.andreev_av.weather.domain.model.City;

public interface ICityConverter {

    List<City> convertAll(Cursor cursor);

}
