package ru.andreev_av.weather.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.data.files.CityFileReader;
import ru.andreev_av.weather.domain.model.City;

import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_CITY_ID;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_COUNTRY_CODE;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_LATITUDE;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_LONGITUDE;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_NAME;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.COLUMN_WATCHED;
import static ru.andreev_av.weather.data.db.WeatherContract.CityEntry.TABLE_NAME;
import static ru.andreev_av.weather.data.db.WeatherContract.SQL_CREATE_CITY_TABLE;
import static ru.andreev_av.weather.data.db.WeatherContract.SQL_CREATE_WEATHER_CURRENT_TABLE;
import static ru.andreev_av.weather.data.db.WeatherContract.SQL_CREATE_WEATHER_FORECAST_TABLE;

public class WeatherDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "weather.db";
    private static final String TAG = "WeatherDbHelper";
    private static WeatherDbHelper mInstance;
    private Context mContext;
    private AtomicInteger mGetDbRequestCounter = new AtomicInteger();

    private WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static WeatherDbHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new WeatherDbHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CITY_TABLE);
        db.execSQL(SQL_CREATE_WEATHER_CURRENT_TABLE);
        db.execSQL(SQL_CREATE_WEATHER_FORECAST_TABLE);

        // т.к. поиск по названию города через http://api.openweathermap.org/data/2.5/find работает криво,
        // то приходится сначала грузить все необходимые города в БД (из подготовленного файла) и уже искать нужный город из БД
        fillCityTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        mGetDbRequestCounter.incrementAndGet();
        return super.getReadableDatabase();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        mGetDbRequestCounter.incrementAndGet();
        return super.getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        if (mGetDbRequestCounter.decrementAndGet() == 0) {
            super.close();
        }
    }

    // TODO подумать, может вынести и отрефакторить
    private void fillCityTable(SQLiteDatabase db) {
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
            if (db != null) {
                db.endTransaction();
            }
        }
    }
}
