package ru.andreev_av.weather.data.db;

abstract class AbstractDao {
    protected WeatherDbHelper mDbHelper;

    public AbstractDao(WeatherDbHelper dbHelper) {
        mDbHelper = dbHelper;
    }
}
