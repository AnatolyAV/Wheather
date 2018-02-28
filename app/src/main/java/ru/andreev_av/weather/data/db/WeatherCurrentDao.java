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

import ru.andreev_av.weather.data.converter.IWeatherCurrentConverter;
import ru.andreev_av.weather.data.converter.WeatherCurrentConverter;
import ru.andreev_av.weather.data.model.WeatherCurrentModel;
import ru.andreev_av.weather.domain.model.WeatherCurrent;

import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_CITY_ID;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_CITY_NAME;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_CLOUDINESS;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_COUNTRY_CODE;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_GRND_LEVEL;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_HUMIDITY;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_PRESSURE;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_RAIN_VOLUME;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_SEA_LEVEL;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_SNOW_VOLUME;
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
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WIND_DEGREE;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.COLUMN_WIND_SPEED;
import static ru.andreev_av.weather.data.db.WeatherContract.WeatherCurrentEntry.TABLE_NAME;

public class WeatherCurrentDao extends AbstractDao implements IWeatherCurrentDao {

    private static final String TAG = "WeatherCurrentDao";

    private static WeatherCurrentDao mInstance;

    private IWeatherCurrentConverter converter;

    private WeatherCurrentDao(Context context) {
        super(WeatherDbHelper.getInstance(context));
        converter = new WeatherCurrentConverter();
    }

    public static WeatherCurrentDao getInstance(Context context) {
        if (mInstance == null) {
            synchronized (WeatherCurrentDao.class) {
                if (mInstance == null) {
                    mInstance = new WeatherCurrentDao(context);
                }
            }
        }
        return mInstance;
    }

    @Override
    public List<WeatherCurrent> getAll() {
        SQLiteDatabase db = null;
        Cursor cursor;
        List<WeatherCurrent> weatherCurrents = Collections.emptyList();
        try {
            db = mDbHelper.getReadableDatabase();
            db.beginTransaction();
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            db.setTransactionSuccessful();
            if (cursor.getCount() > 0) {
                weatherCurrents = converter.convertAll(cursor);
            }
        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, e);
        } finally {
            if (db != null) {
                db.endTransaction();
                mDbHelper.close();
            }
        }

        return weatherCurrents;
    }

    @Override
    public WeatherCurrent getWeatherCurrentByCityId(int cityId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        WeatherCurrent weatherCurrent = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();
            String selection = COLUMN_CITY_ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf(cityId),};
            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
            db.setTransactionSuccessful();
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                weatherCurrent = converter.convert(cursor);
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

        return weatherCurrent;
    }

    @Override
    public void insertOrUpdate(List<WeatherCurrentModel> weatherCurrentModels) {
        for (WeatherCurrentModel weatherCurrentModel : weatherCurrentModels) {
            insertOrUpdate(weatherCurrentModel);
        }
    }

    @Override
    public void insertOrUpdate(WeatherCurrentModel weatherCurrentModel) {
        SQLiteDatabase db = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();
            ContentValues contentValues = create(weatherCurrentModel);

            String selection = COLUMN_CITY_ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf((int) weatherCurrentModel.getCityId()),};
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
    public WeatherCurrent get(long id) {
        return null;
    }

    @Override
    public boolean add(WeatherCurrent weatherCurrent) throws SQLException {
//        SQLiteDatabase db = null;
//        try {
//            db = mDbHelper.getWritableDatabase();
//            db.beginTransaction();
//            ContentValues contentValues = create(weatherCurrent);
//
//            db.insert(TABLE_NAME, null, contentValues);
//            db.setTransactionSuccessful();
//            return true;
//        } catch (SQLException e) {
//            Logger.getLogger(TAG).log(Level.SEVERE, null, e);
//        } finally {
//            if (db != null) {
//                db.endTransaction();
//                mDbHelper.close();
//            }
//        }
        return false;
    }

    @Override
    public boolean update(WeatherCurrent weatherCurrent) throws SQLException {
//        SQLiteDatabase db = null;
//        try {
//            db = mDbHelper.getWritableDatabase();
//            db.beginTransaction();
//            ContentValues contentValues = create(weatherCurrent);
//
//            String selection = COLUMN_CITY_ID + " = ?";
//            String[] selectionArgs = new String[]{String.valueOf((int) weatherCurrent.getCityId()),};
//            int updCount = db.update(TABLE_NAME, contentValues, selection, selectionArgs);
//
//            db.setTransactionSuccessful();
//            if (updCount == 0) {
////                db.insert(TABLE_NAME, null, contentValues);
//                return false;
//            } else {
//                return true;
//            }
//
//        } catch (SQLException e) {
//            Logger.getLogger(TAG).log(Level.SEVERE, null, e);
//        } finally {
//            if (db != null) {
//                db.endTransaction();
//                mDbHelper.close();
//            }
//        }
        return false;
    }

    @Override
    public boolean delete(WeatherCurrent weatherCurrent) throws SQLException {
        return false;
    }

    private ContentValues create(WeatherCurrentModel weatherCurrentModel) {
        // TODO вынести в отдельный Creator
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_CITY_ID, weatherCurrentModel.getCityId());
        contentValues.put(COLUMN_CITY_NAME, weatherCurrentModel.getCityName());

        List<ru.andreev_av.weather.data.model.Weather> weathers = weatherCurrentModel.getWeather();
        if (weathers != null) {
            contentValues.put(COLUMN_WEATHER_ID, weathers.get(0).getId());
            contentValues.put(COLUMN_WEATHER_MAIN, weathers.get(0).getMain());
            contentValues.put(COLUMN_WEATHER_DESCRIPTION, weathers.get(0).getDescription());
            contentValues.put(COLUMN_WEATHER_ICON, weathers.get(0).getIcon());
        }

        ru.andreev_av.weather.data.model.Main main = weatherCurrentModel.getMain();
        if (main != null) {
            contentValues.put(COLUMN_TEMPERATURE_CURRENT, main.getTemperature());
            contentValues.put(COLUMN_PRESSURE, main.getPressure());
            contentValues.put(COLUMN_HUMIDITY, main.getHumidity());
            contentValues.put(COLUMN_TEMPERATURE_MIN, main.getTemperatureMin());
            contentValues.put(COLUMN_TEMPERATURE_MAX, main.getTemperatureMax());
            contentValues.put(COLUMN_SEA_LEVEL, main.getSeaLevel());
            contentValues.put(COLUMN_GRND_LEVEL, main.getGrndLevel());
        }

        ru.andreev_av.weather.data.model.Wind wind = weatherCurrentModel.getWind();
        if (wind != null) {
            contentValues.put(COLUMN_WIND_SPEED, wind.getSpeed());
            contentValues.put(COLUMN_WIND_DEGREE, wind.getDeg());
        }

        ru.andreev_av.weather.data.model.Clouds clouds = weatherCurrentModel.getClouds();
        if (clouds != null)
            contentValues.put(COLUMN_CLOUDINESS, clouds.getAll());

        ru.andreev_av.weather.data.model.Rain rain = weatherCurrentModel.getRain();
        if (rain != null)
            contentValues.put(COLUMN_RAIN_VOLUME, rain.get3h());

        ru.andreev_av.weather.data.model.Snow snow = weatherCurrentModel.getSnow();
        if (snow != null)
            contentValues.put(COLUMN_SNOW_VOLUME, snow.get3h());

        contentValues.put(COLUMN_TIME_MEASUREMENT, weatherCurrentModel.getDateTime());

        ru.andreev_av.weather.data.model.Sys sys = weatherCurrentModel.getSys();
        if (sys != null) {
            contentValues.put(COLUMN_COUNTRY_CODE, sys.getCountryCode());
            contentValues.put(COLUMN_TIME_SUNRISE, sys.getSunrise());
            contentValues.put(COLUMN_TIME_SUNSET, sys.getSunset());
        }

        return contentValues;
    }
}
