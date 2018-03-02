package ru.andreev_av.weather.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.atomic.AtomicInteger;

import ru.andreev_av.weather.data.listeners.IDbTablesCreatedListener;

import static ru.andreev_av.weather.data.db.WeatherContract.SQL_CREATE_CITY_TABLE;
import static ru.andreev_av.weather.data.db.WeatherContract.SQL_CREATE_WEATHER_CURRENT_TABLE;
import static ru.andreev_av.weather.data.db.WeatherContract.SQL_CREATE_WEATHER_FORECAST_TABLE;

public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "weather.db";
    private static final String TAG = "WeatherDbHelper";
    private static WeatherDbHelper mInstance;
    private AtomicInteger mGetDbRequestCounter = new AtomicInteger();
    private IDbTablesCreatedListener mDbTablesCreatedListener;

    private WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static WeatherDbHelper getInstance(Context context) {
        if (mInstance == null) {
            synchronized (WeatherDbHelper.class) {
                if (mInstance == null) {
                    mInstance = new WeatherDbHelper(context.getApplicationContext());
                }
            }
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
        fireDbTablesCreatedListener(db);
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

    public void setDbTablesCreatedListener(IDbTablesCreatedListener dbTablesCreatedListener) {
        mDbTablesCreatedListener = dbTablesCreatedListener;
    }

    public void fireDbTablesCreatedListener(SQLiteDatabase db) {
        if (mDbTablesCreatedListener != null) {
            mDbTablesCreatedListener.onDbTablesCreated(db);
        }
    }

    public void removeDbTablesCreatedListener() {
        mDbTablesCreatedListener = null;
    }
}
