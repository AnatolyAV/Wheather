package ru.andreev_av.weather.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.andreev_av.weather.data.converter.IWeatherForecastConverter;
import ru.andreev_av.weather.data.converter.WeatherForecastConverter;
import ru.andreev_av.weather.data.model.City;
import ru.andreev_av.weather.data.model.WeatherForecastModel;
import ru.andreev_av.weather.domain.model.WeatherForecast;
import ru.andreev_av.weather.utils.DateUtils;

import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_CITY_ID;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_CLOUDINESS;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_DATE_TIME;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_HUMIDITY;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_PRESSURE;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_RAIN_VOLUME;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_SNOW_VOLUME;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_DAY;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_EVENING;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_MAX;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_MIN;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_MORNING;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_TEMPERATURE_NIGHT;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_WEATHER_DESCRIPTION;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_WEATHER_ICON;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_WEATHER_ID;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_WEATHER_MAIN;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_WIND_DEGREE;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.COLUMN_WIND_SPEED;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherForecastEntry.TABLE_NAME;

public class WeatherForecastDao extends AbstractDao implements IWeatherForecastDao {

    private static final String TAG = "WeatherForecastDao";

    private static WeatherForecastDao mInstance;

    private IWeatherForecastConverter converter;

    private WeatherForecastDao(Context context) {
        super(WeatherDbHelper.getInstance(context));
        converter = new WeatherForecastConverter();
    }

    public static WeatherForecastDao getInstance(Context context) {
        if (mInstance == null) {
            synchronized (WeatherForecastDao.class) {
                if (mInstance == null) {
                    mInstance = new WeatherForecastDao(context);
                }
            }
        }
        return mInstance;
    }

    @Override
    public void insertOrUpdate(WeatherForecastModel weatherForecastModel) {
        List<ru.andreev_av.weather.data.model.WeatherForecast> weatherForecasts = weatherForecastModel.getList();
        for (ru.andreev_av.weather.data.model.WeatherForecast weatherForecast : weatherForecasts) {
            insertOrUpdate(weatherForecast, weatherForecastModel.getCity());
        }
    }

    @Override
    public void insertOrUpdate(ru.andreev_av.weather.data.model.WeatherForecast weatherForecast, City city) {
        SQLiteDatabase db = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = create(weatherForecast, city);

            String selection = COLUMN_CITY_ID + " = ?" + " and " + COLUMN_DATE_TIME + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(city.getId()), String.valueOf(weatherForecast.getDateTime())};

            int updCount = db.update(TABLE_NAME, contentValues, selection, selectionArgs);

            if (updCount == 0) {
                db.insert(TABLE_NAME, null, contentValues);
            }

            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, e);
        } finally {
            if (db != null) {
                db.endTransaction();
                mDbHelper.close();
            }
        }
    }

    @Override
    public List<WeatherForecast> getWeatherForecastsByCityId(int cityId, int countDays) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<WeatherForecast> weatherForecasts = Collections.emptyList();
        try {
            db = mDbHelper.getReadableDatabase();
            db.beginTransaction();


            String selection = COLUMN_CITY_ID + " = " + cityId
                    + " and " + COLUMN_DATE_TIME + " >= " + DateUtils.getFormatCalendarNowTimeInMillis();
            String sortOrder = COLUMN_DATE_TIME + " ASC " + " LIMIT " + countDays;

            cursor = db.query(TABLE_NAME, null, selection, null, null, null, sortOrder);
            db.setTransactionSuccessful();
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                weatherForecasts = converter.convertAll(cursor);
            }
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.endTransaction();
                mDbHelper.close();
            }
        }

        return weatherForecasts;
    }

    private ContentValues create(ru.andreev_av.weather.data.model.WeatherForecast weatherForecast, City city) {
        // TODO вынести в отдельный Creator
        ContentValues contentValues = new ContentValues();

        if (city != null) {
            contentValues.put(COLUMN_CITY_ID, city.getId());
        }
        contentValues.put(COLUMN_DATE_TIME, weatherForecast.getDateTime());
        if (weatherForecast.getTemperature() != null) {
            contentValues.put(COLUMN_TEMPERATURE_DAY, weatherForecast.getTemperature().getDay());
            contentValues.put(COLUMN_TEMPERATURE_MIN, weatherForecast.getTemperature().getMin());
            contentValues.put(COLUMN_TEMPERATURE_MAX, weatherForecast.getTemperature().getMax());
            contentValues.put(COLUMN_TEMPERATURE_NIGHT, weatherForecast.getTemperature().getNight());
            contentValues.put(COLUMN_TEMPERATURE_EVENING, weatherForecast.getTemperature().getEvening());
            contentValues.put(COLUMN_TEMPERATURE_MORNING, weatherForecast.getTemperature().getMorning());
        }
        contentValues.put(COLUMN_PRESSURE, weatherForecast.getPressure());
        contentValues.put(COLUMN_HUMIDITY, weatherForecast.getHumidity());
        if (weatherForecast.getWeather() != null) {
            contentValues.put(COLUMN_WEATHER_ID, weatherForecast.getWeather().get(0).getId());
            contentValues.put(COLUMN_WEATHER_MAIN, weatherForecast.getWeather().get(0).getMain());
            contentValues.put(COLUMN_WEATHER_DESCRIPTION, weatherForecast.getWeather().get(0).getDescription());
            contentValues.put(COLUMN_WEATHER_ICON, weatherForecast.getWeather().get(0).getIcon());
        }
        contentValues.put(COLUMN_WIND_SPEED, weatherForecast.getSpeed());
        contentValues.put(COLUMN_WIND_DEGREE, weatherForecast.getDeg());
        contentValues.put(COLUMN_CLOUDINESS, weatherForecast.getClouds());
        contentValues.put(COLUMN_RAIN_VOLUME, weatherForecast.getRain());
        contentValues.put(COLUMN_SNOW_VOLUME, weatherForecast.getSnow());


        return contentValues;
    }
}