package ru.andreev_av.weather.ui.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.db.entry.WeatherCurrentEntry;
import ru.andreev_av.weather.db.entry.WeatherForecastEntry;
import ru.andreev_av.weather.model.Temperature;
import ru.andreev_av.weather.model.Weather;
import ru.andreev_av.weather.model.WeatherForecast;
import ru.andreev_av.weather.net.ConnectionDetector;
import ru.andreev_av.weather.preferences.AppPreference;
import ru.andreev_av.weather.processors.Processor;
import ru.andreev_av.weather.services.ServiceHelper;
import ru.andreev_av.weather.ui.adapters.WeatherForecastAdapter;
import ru.andreev_av.weather.utils.DateUtils;

public class WeatherForecastActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String BROADCAST_ACTION = "ru.andreev_av.weather.WeatherForecastActivity.ActionResult";
    private final static int WEATHER_FORECAST_LOADER = 1;
    private final static int COUNT_DAYS_DEFAULT = 16;
    private final static int COUNT_DAYS_THREE = 3;
    private final static int COUNT_DAYS_SEVEN = 7;
    private final IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
    private List<WeatherForecast> weatherForecastList = new ArrayList<>();
    private RecyclerView rvWeatherForecastList;
    private WeatherForecastAdapter adapter;
    private ConnectionDetector connectionDetector;
    private ServiceHelper serviceHelper;
    private int cityId;
    private boolean isRefreshed;
    private int countDays;
    private BroadcastReceiver weatherBroadcastReceiver;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        findComponents();

        initToolbar();

        initDrawer(toolbar);

        initAdapter();

        initComponents();

        connectionDetector = new ConnectionDetector(this);

        serviceHelper = new ServiceHelper(this, BROADCAST_ACTION);

        cityId = AppPreference.getCurrentCityId(this);

        isRefreshed = true;

        countDays = COUNT_DAYS_THREE;

        initWeatherReceiver();

        getSupportLoaderManager().initLoader(WEATHER_FORECAST_LOADER, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cityId != -1)
            serviceHelper.loadWeatherForecast(cityId, COUNT_DAYS_DEFAULT);
        registerReceiver(weatherBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(weatherBroadcastReceiver);
    }


    protected void findComponents() {
        super.findComponents();
        rvWeatherForecastList = (RecyclerView) findViewById(R.id.rv_weather_forecast_list);
    }

    private void initAdapter() {
        adapter = new WeatherForecastAdapter(this, weatherForecastList);
    }

    private void initComponents() {
        rvWeatherForecastList.setAdapter(adapter);
        rvWeatherForecastList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initWeatherReceiver() {
        weatherBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int status = intent.getIntExtra(PARAM_STATUS, 0);

                if (status == STATUS_CONNECTION_NOT_FOUND) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(WeatherForecastActivity.this,
                            R.string.connection_not_found,
                            Toast.LENGTH_SHORT).show();
                    isRefreshed = false;
                    getSupportLoaderManager().getLoader(WEATHER_FORECAST_LOADER).forceLoad();
                }

                if (status == STATUS_START) {
                    dialog = new ProgressDialog(context);
                    dialog.setMessage(context.getResources().getString(R.string.loading));
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                }

                if (status == STATUS_FINISH) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Bundle extras = intent.getExtras();

                    boolean success = extras.getBoolean(Processor.Extras.RESULT_EXTRA);
                    int method = extras.getInt(Processor.Extras.METHOD_EXTRA);
                    switch (method) {
                        case ServiceHelper.Methods.LOAD_WEATHER_FORECAST:
                            if (success) {
                                getSupportLoaderManager().getLoader(WEATHER_FORECAST_LOADER).forceLoad();
                            }
                            isRefreshed = false;
                            setUpdateButtonState(false);
                            break;
                    }
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_forecast, menu);
        updateItem = menu.findItem(R.id.main_menu_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.main_menu_refresh:
                if (connectionDetector.isNetworkAvailableAndConnected()) {
                    if (cityId != -1) {
                        serviceHelper.loadWeatherForecast(cityId, COUNT_DAYS_DEFAULT);
                        setUpdateButtonState(true);
                    }
                } else {
                    Toast.makeText(WeatherForecastActivity.this,
                            R.string.connection_not_found,
                            Toast.LENGTH_SHORT).show();
                    setUpdateButtonState(false);
                }
                return true;
            case R.id.main_menu_count_days:
                if (countDays == COUNT_DAYS_THREE) {
                    countDays = COUNT_DAYS_SEVEN;
                    item.setIcon(R.drawable.ic_filter_3);
                } else {
                    countDays = COUNT_DAYS_THREE;
                    item.setIcon(R.drawable.ic_filter_7);
                }
                isRefreshed = false;
                getSupportLoaderManager().restartLoader(WEATHER_FORECAST_LOADER, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        switch (id) {
            case WEATHER_FORECAST_LOADER:
                String selection = WeatherCurrentEntry.COLUMN_CITY_ID + " = " + cityId
                        + " and " + WeatherForecastEntry.COLUMN_DATE_TIME + " >= " + DateUtils.getFormatCalendarNowTimeInMillis();
                String sortOrder = WeatherForecastEntry.COLUMN_DATE_TIME + " ASC " + " LIMIT " + countDays;
                cursorLoader = new CursorLoader(this, WeatherForecastEntry.CONTENT_URI, null, selection, null, sortOrder);
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (!isRefreshed) {
            if (cityId != -1) {
                weatherForecastList.clear();
                if (cursor != null) {
                    if (!cursor.isClosed()) {
                        loadWeatherForecastListFromCursor(cursor);
                        cursor.close();
                    } else {
                        getSupportLoaderManager().getLoader(WEATHER_FORECAST_LOADER).forceLoad();
                    }
                }
                adapter.refreshList(weatherForecastList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void loadWeatherForecastListFromCursor(Cursor cursor) {

        if (cursor.moveToFirst()) {
            do {
                WeatherForecast weatherForecast = new WeatherForecast();

                long dateTime = cursor.getLong(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_DATE_TIME));
                weatherForecast.setDateTime(dateTime);

                float temperatureDay = cursor.getFloat(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_TEMPERATURE_DAY));
                float temperatureMin = cursor.getFloat(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_TEMPERATURE_MIN));
                float temperatureMax = cursor.getFloat(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_TEMPERATURE_MAX));
                float temperatureNight = cursor.getFloat(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_TEMPERATURE_NIGHT));
                float temperatureEvening = cursor.getFloat(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_TEMPERATURE_EVENING));
                float temperatureMorning = cursor.getFloat(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_TEMPERATURE_MORNING));
                weatherForecast.setTemperature(new Temperature(temperatureDay, temperatureMin, temperatureMax, temperatureNight, temperatureEvening, temperatureMorning));

                weatherForecast.setPressure(cursor.getFloat(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_PRESSURE)));

                weatherForecast.setHumidity(cursor.getFloat(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_HUMIDITY)));

                int weatherId = cursor.getInt(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_WEATHER_ID));
                String weatherMain = cursor.getString(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_WEATHER_MAIN));
                String weatherDescription = cursor.getString(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_WEATHER_DESCRIPTION));
                String weatherIcon = cursor.getString(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_WEATHER_ICON));
                Weather weather = new Weather(weatherId, weatherMain, weatherDescription, weatherIcon);
                List<Weather> weatherList = new ArrayList<>();
                weatherList.add(weather);
                weatherForecast.setWeather(weatherList);

                weatherForecast.setSpeed(cursor.getFloat(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_WIND_SPEED)));

                weatherForecast.setClouds(cursor.getInt(cursor.getColumnIndex(WeatherForecastEntry.COLUMN_CLOUDINESS)));

                weatherForecastList.add(weatherForecast);
            } while (cursor.moveToNext());
        }
    }
}
