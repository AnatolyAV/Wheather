package ru.andreev_av.weather.db.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.data.model.City;
import ru.andreev_av.weather.db.entry.CityEntry;
import ru.andreev_av.weather.db.entry.WeatherCurrentEntry;
import ru.andreev_av.weather.db.entry.WeatherForecastEntry;
import ru.andreev_av.weather.files.CityFileReader;

public class WeatherContentProvider extends ContentProvider {

    private static final String TAG = "WeatherContentProvider";

    private static final String DB_NAME = "weather.db";
    private static final int DB_VERSION = 1;

    private static final String DB_TABLE_CITY_CREATE = "CREATE TABLE " + CityEntry.TABLE_CITY + "(" +
            CityEntry._ID + " INTEGER primary key autoincrement, " +
            CityEntry.COLUMN_CITY_ID + " INTEGER NOT NULL, " +
            CityEntry.COLUMN_NAME + " VARCHAR(100) NOT NULL, " +
            CityEntry.COLUMN_LATITUDE + " FLOAT NOT NULL, " +
            CityEntry.COLUMN_LONGITUDE + " FLOAT NOT NULL, " +
            CityEntry.COLUMN_COUNTRY_CODE + " VARCHAR(3) NOT NULL, " +
            CityEntry.COLUMN_WATCHED + " INT " +
            ")";

    private static final String DB_TABLE_WEATHER_CURRENT = "CREATE TABLE " + WeatherCurrentEntry.TABLE_WEATHER_CURRENT + "(" +
            WeatherCurrentEntry._ID + " INTEGER primary key autoincrement, " +
            WeatherCurrentEntry.COLUMN_CITY_ID + " INTEGER NOT NULL, " +
            WeatherCurrentEntry.COLUMN_CITY_NAME + " VARCHAR(100) NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WEATHER_MAIN + " VARCHAR(50) NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WEATHER_DESCRIPTION + " VARCHAR(50) NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WEATHER_ICON + " VARCHAR(5) NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TEMPERATURE_CURRENT + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_PRESSURE + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_HUMIDITY + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TEMPERATURE_MIN + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TEMPERATURE_MAX + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_SEA_LEVEL + " FLOAT, " +
            WeatherCurrentEntry.COLUMN_GRND_LEVEL + " FLOAT, " +
            WeatherCurrentEntry.COLUMN_WIND_SPEED + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_WIND_DEGREE + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_CLOUDINESS + " FLOAT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_RAIN_VOLUME + " FLOAT, " +
            WeatherCurrentEntry.COLUMN_SNOW_VOLUME + " FLOAT, " +
            WeatherCurrentEntry.COLUMN_TIME_MEASUREMENT + " BIGINT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TIME_SUNRISE + " BIGINT NOT NULL, " +
            WeatherCurrentEntry.COLUMN_TIME_SUNSET + " BIGINT NOT NULL " +
            ")";

    private static final String DB_TABLE_WEATHER_FORECAST = "CREATE TABLE " + WeatherForecastEntry.TABLE_WEATHER_FORECAST + "(" +
            WeatherForecastEntry._ID + " INTEGER primary key autoincrement, " +
            WeatherForecastEntry.COLUMN_CITY_ID + " INTEGER NOT NULL, " +
            WeatherForecastEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL, " +
            WeatherForecastEntry.COLUMN_WEATHER_MAIN + " VARCHAR(50) NOT NULL, " +
            WeatherForecastEntry.COLUMN_WEATHER_DESCRIPTION + " VARCHAR(50) NOT NULL, " +
            WeatherForecastEntry.COLUMN_WEATHER_ICON + " VARCHAR(5) NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_DAY + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_MIN + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_MAX + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_NIGHT + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_EVENING + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_TEMPERATURE_MORNING + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_PRESSURE + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_HUMIDITY + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_WIND_SPEED + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_WIND_DEGREE + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_CLOUDINESS + " FLOAT NOT NULL, " +
            WeatherForecastEntry.COLUMN_RAIN_VOLUME + " FLOAT, " +
            WeatherForecastEntry.COLUMN_SNOW_VOLUME + " FLOAT, " +
            WeatherForecastEntry.COLUMN_DATE_TIME + " BIGINT NOT NULL " +
            ")";

    private static final int URI_CITIES = 1;
    private static final int URI_WEATHERS_CURRENT = 2;
    private static final int URI_WEATHERS_FORECAST = 3;
    private static final int URI_CITIES_ID = 4;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(WeatherContentProviderData.CONTENT_AUTHORITY, CityEntry.PATH, URI_CITIES);
        uriMatcher.addURI(WeatherContentProviderData.CONTENT_AUTHORITY, WeatherCurrentEntry.PATH, URI_WEATHERS_CURRENT);
        uriMatcher.addURI(WeatherContentProviderData.CONTENT_AUTHORITY, WeatherForecastEntry.PATH, URI_WEATHERS_FORECAST);
        uriMatcher.addURI(WeatherContentProviderData.CONTENT_AUTHORITY, CityEntry.PATH + "/#", URI_CITIES_ID);
    }

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private Context context;

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_CITIES:
                return CityEntry.CONTENT_TYPE;
            case URI_WEATHERS_CURRENT:
                return WeatherCurrentEntry.CONTENT_TYPE;
            case URI_WEATHERS_FORECAST:
                return WeatherForecastEntry.CONTENT_TYPE;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate");
        context = getContext();
        dbHelper = new DBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case URI_CITIES:
                db = dbHelper.getWritableDatabase();
                cursor = db.query(CityEntry.TABLE_CITY, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(context.getContentResolver(), CityEntry.CONTENT_URI);
                break;
            case URI_WEATHERS_CURRENT:
                db = dbHelper.getWritableDatabase();
                cursor = db.query(WeatherCurrentEntry.TABLE_WEATHER_CURRENT, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(context.getContentResolver(), WeatherCurrentEntry.CONTENT_URI);
                break;
            case URI_WEATHERS_FORECAST:
                db = dbHelper.getWritableDatabase();
                cursor = db.query(WeatherForecastEntry.TABLE_WEATHER_FORECAST, projection, selection, selectionArgs, null, null, sortOrder);
                cursor.setNotificationUri(context.getContentResolver(), WeatherForecastEntry.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) throws IllegalArgumentException {
        long rowID;
        Uri resultUri;
        switch (uriMatcher.match(uri)) {
            case URI_CITIES:
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(CityEntry.TABLE_CITY, null, values);
                resultUri = ContentUris.withAppendedId(CityEntry.CONTENT_URI, rowID);
                break;
            case URI_WEATHERS_CURRENT:
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(WeatherCurrentEntry.TABLE_WEATHER_CURRENT, null, values);
                resultUri = ContentUris.withAppendedId(WeatherCurrentEntry.CONTENT_URI, rowID);
                break;
            case URI_WEATHERS_FORECAST:
                db = dbHelper.getWritableDatabase();
                rowID = db.insert(WeatherForecastEntry.TABLE_WEATHER_FORECAST, null, values);
                resultUri = ContentUris.withAppendedId(WeatherForecastEntry.CONTENT_URI, rowID);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        context.getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_CITIES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = CityEntry._ID + " = " + id;
                } else {
                    selection = selection + " AND " + CityEntry._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int cnt = db.delete(CityEntry.TABLE_CITY, selection, selectionArgs);
        context.getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int cnt;
        switch (uriMatcher.match(uri)) {
            case URI_CITIES:
                db = dbHelper.getWritableDatabase();
                cnt = db.update(CityEntry.TABLE_CITY, values, selection, selectionArgs);
                break;
            case URI_WEATHERS_CURRENT:
                db = dbHelper.getWritableDatabase();
                cnt = db.update(WeatherCurrentEntry.TABLE_WEATHER_CURRENT, values, selection, selectionArgs);
                break;
            case URI_WEATHERS_FORECAST:
                db = dbHelper.getWritableDatabase();
                cnt = db.update(WeatherForecastEntry.TABLE_WEATHER_FORECAST, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        context.getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    private void fillCityTable(SQLiteDatabase db) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.city_list);
        CityFileReader fileReader = new CityFileReader();
        try {
            final List<City> cities = fileReader.readCityListFromFile(inputStream);
            inputStream.close();
            db.beginTransaction();
            for (City city : cities) {
                ContentValues cv = new ContentValues();
                cv.put(CityEntry.COLUMN_CITY_ID, city.getId());
                cv.put(CityEntry.COLUMN_NAME, city.getName());
                cv.put(CityEntry.COLUMN_LATITUDE, city.getCoordinate().getLat());
                cv.put(CityEntry.COLUMN_LONGITUDE, city.getCoordinate().getLon());
                cv.put(CityEntry.COLUMN_COUNTRY_CODE, city.getCountryCode());
                cv.put(CityEntry.COLUMN_WATCHED, city.getWatch());
                db.insert(CityEntry.TABLE_CITY, null, cv);
            }
            db.setTransactionSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private class DBHelper extends SQLiteOpenHelper {

        private DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_TABLE_CITY_CREATE);
            db.execSQL(DB_TABLE_WEATHER_CURRENT);
            db.execSQL(DB_TABLE_WEATHER_FORECAST);

            // т.к. поиск по названию города через http://api.openweathermap.org/data/2.5/find работает криво,
            // то приходится сначала грузить все необходимые города в БД (из подготовленного файла) и уже искать нужный город из БД
            fillCityTable(db);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
