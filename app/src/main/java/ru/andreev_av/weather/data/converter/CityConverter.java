package ru.andreev_av.weather.data.converter;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.domain.model.Coordinate;

import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_CITY_ID;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_COUNTRY_CODE;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_LATITUDE;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_LONGITUDE;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_NAME;

public class CityConverter implements ICityConverter {
    @Override
    public List<City> convertAll(Cursor cursor) {
        List<City> cities;
        if (cursor != null && !cursor.isClosed()) {
            cities = new ArrayList<>();
            while (cursor.moveToNext()) {
                City city = convert(cursor);
                cities.add(city);
            }
        } else {
            cities = Collections.emptyList();
        }
        return cities;
    }

    private City convert(Cursor cursor) {
        if (cursor == null || cursor.isClosed()) {
            throw new IllegalArgumentException("Cursor must be initialized and not closed");
        }

        int cityId = cursor.getInt(cursor.getColumnIndex(COLUMN_CITY_ID));
        String cityName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));

        float latitude = cursor.getInt(cursor.getColumnIndex(COLUMN_LATITUDE));
        float longitude = cursor.getInt(cursor.getColumnIndex(COLUMN_LONGITUDE));
        String countryCode = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY_CODE));
        Coordinate coordinate = Coordinate.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();

        return new City(cityId, cityName, coordinate, countryCode);
    }
}
