package ru.andreev_av.weather.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.data.converter.CityConverter;
import ru.andreev_av.weather.data.converter.ICityConverter;
import ru.andreev_av.weather.data.files.CityFileReader;
import ru.andreev_av.weather.domain.model.City;

import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_CITY_ID;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_COUNTRY_CODE;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_LATITUDE;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_LONGITUDE;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_NAME;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_WATCHED;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.TABLE_NAME;

public class CityDao extends AbstractDao implements ICityDao {

    private static final String TAG = "CityDao";

    private final static int CITY_LIST_LIMIT = 10;
    private final static int CITY_WATCH = 1;
    private final static int CITY_NOT_WATCH = 0;
    private final static int UPDATED_SUCCESS = 1;
    private static CityDao mInstance;
    private Context mContext;
    private ICityConverter converter;

    private CityDao(Context context) {
        super(WeatherDbHelper.getInstance(context));
        converter = new CityConverter();
        mContext = context;
        mDbHelper.setDbTablesCreatedListener(this);
    }

    public static CityDao getInstance(Context context) {
        if (mInstance == null) {
            synchronized (CityDao.class) {
                if (mInstance == null) {
                    mInstance = new CityDao(context);
                }
            }
        }
        return mInstance;
    }

    @Override
    public List<City> getCitiesByToWatch(boolean isToWatch) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<City> cities = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues contentValues = new ContentValues();
            if (isToWatch) {
                contentValues.put(COLUMN_WATCHED, CITY_WATCH);
            } else {
                contentValues.put(COLUMN_WATCHED, CITY_NOT_WATCH);
            }

            String selection = COLUMN_WATCHED + " = ?";
            String[] selectionArgs;
            if (isToWatch) {
                selectionArgs = new String[]{String.valueOf(CITY_WATCH)};
            } else {
                selectionArgs = new String[]{String.valueOf(CITY_NOT_WATCH)};
            }

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
            db.setTransactionSuccessful();
            cities = converter.convertAll(cursor);
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

        return cities;
    }

    @Override
    public List<City> findCities(String cityNameFirstLetters) {

        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<City> cities = null;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();

            String selection = COLUMN_NAME + " like " + String.format("'%s%%'", cityNameFirstLetters) + " and " + COLUMN_WATCHED + " != " + CITY_WATCH;
            String sortOrder = COLUMN_NAME + " ASC " + " LIMIT " + CITY_LIST_LIMIT;

            cursor = db.query(TABLE_NAME, null, selection, null, null, null, sortOrder);
            db.setTransactionSuccessful();
            cities = converter.convertAll(cursor);
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

        return cities;
    }

    @Override
    public boolean updateCityWatched(City city) throws IllegalArgumentException {
        SQLiteDatabase db = null;
        int updated = 0;
        try {
            db = mDbHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_WATCHED, CITY_WATCH);

            String selection = COLUMN_CITY_ID + " = ?";
            String[] selectionArgs = new String[]{String.valueOf((int) city.getId())};

            updated = db.update(TABLE_NAME, contentValues, selection, selectionArgs);
            db.setTransactionSuccessful();

        } catch (SQLException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, e);
        } finally {
            if (db != null) {
                db.endTransaction();
                mDbHelper.close();
            }
        }
        return (updated == UPDATED_SUCCESS);
    }

    @Override
    public void onDbTablesCreated(SQLiteDatabase db) {
        fillTable(db);
    }

    // TODO подумать, может вынести и отрефакторить
    private void fillTable(SQLiteDatabase db) {
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.city_list);
        CityFileReader fileReader = new CityFileReader();
        try {
            final List<City> cities = fileReader.readCityListFromFile(inputStream);
            inputStream.close();
            db.beginTransaction();
            for (City city : cities) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_CITY_ID, city.getId());
                cv.put(COLUMN_NAME, city.getName());
                cv.put(COLUMN_LATITUDE, city.getCoordinate().getLatitude());
                cv.put(COLUMN_LONGITUDE, city.getCoordinate().getLongitude());
                cv.put(COLUMN_COUNTRY_CODE, city.getCountryCode());
                cv.put(COLUMN_WATCHED, city.getWatch());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            Logger.getLogger(TAG).log(Level.SEVERE, null, e);
        } finally {
            mContext = null;
            mDbHelper.removeDbTablesCreatedListener();
            if (db != null) {
                db.endTransaction();
            }
        }
    }
}
