package ru.andreev_av.weather.ui.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.db.entry.WeatherCurrentEntry;
import ru.andreev_av.weather.listeners.RecyclerItemClickListener;
import ru.andreev_av.weather.model.City;
import ru.andreev_av.weather.model.Clouds;
import ru.andreev_av.weather.model.Main;
import ru.andreev_av.weather.model.Sys;
import ru.andreev_av.weather.model.Weather;
import ru.andreev_av.weather.model.WeatherCurrentModel;
import ru.andreev_av.weather.model.Wind;
import ru.andreev_av.weather.net.ConnectionDetector;
import ru.andreev_av.weather.preferences.AppPreference;
import ru.andreev_av.weather.processors.Processor;
import ru.andreev_av.weather.services.ServiceHelper;
import ru.andreev_av.weather.ui.adapters.CityListAdapter;
import ru.andreev_av.weather.ui.fragments.AddCityFragment;

public class CityListActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, AddCityFragment.OnAddCityFragmentInteractionListener {

    public final static String BROADCAST_ACTION = "ru.andreev_av.weather.CityListActivity.ActionResult";
    private final static int WEATHER_CURRENT_LOADER = 1;
    private final IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
    private RecyclerView rvCityList;
    private CityListAdapter adapter;
    private ArrayList<Integer> cityIds;
    private List<WeatherCurrentModel> weatherCurrentList = new ArrayList<>();
    private ConnectionDetector connectionDetector;
    private ServiceHelper serviceHelper;
    private FloatingActionButton fabAddCity;
    private BroadcastReceiver weatherBroadcastReceiver;
    // TODO Заменить на ProgressBar
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        findComponents();

        initListeners();

        initToolbar();

        initDrawer(toolbar);

        initAdapter();

        initComponents();

        cityIds = AppPreference.getCityIds(this);

        connectionDetector = new ConnectionDetector(this);

        serviceHelper = new ServiceHelper(this, BROADCAST_ACTION);

        initWeatherReceiver();

        getSupportLoaderManager().initLoader(WEATHER_CURRENT_LOADER, null, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(weatherBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(weatherBroadcastReceiver);
    }

    protected void findComponents() {
        super.findComponents();

        rvCityList = (RecyclerView) findViewById(R.id.rv_city_list);
        fabAddCity = (FloatingActionButton) findViewById(R.id.fab_add_city);
    }

    protected void initListeners() {
        rvCityList.addOnItemTouchListener(new RecyclerItemClickListener(this, rvCityList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AppPreference.updateCurrentCityId(CityListActivity.this, weatherCurrentList.get(position).getCityId());
                AppPreference.saveWeather(CityListActivity.this, weatherCurrentList.get(position));
                Intent intent = new Intent(CityListActivity.this, WeatherCurrentActivity.class);
                startActivity(intent);
            }
        }));

        fabAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAddCityFragment();
            }
        });
    }

    private void initAddCityFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        AddCityFragment.newInstance().show(fragmentManager, "AddCityFragmentDialog");
    }

    private void initWeatherReceiver() {
        weatherBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int status = intent.getIntExtra(PARAM_STATUS, 0);

                if (status == STATUS_CONNECTION_NOT_FOUND) {
                    hideLoading();
                    Toast.makeText(CityListActivity.this,
                            R.string.connection_not_found,
                            Toast.LENGTH_SHORT).show();
                }

                if (status == STATUS_START) {
                    dialog = new ProgressDialog(context);
                    dialog.setMessage(context.getResources().getString(R.string.loading));
                    showLoading();
                }

                if (status == STATUS_FINISH) {
                    hideLoading();
                    Bundle extras = intent.getExtras();

                    boolean success = extras.getBoolean(Processor.Extras.RESULT_EXTRA);

                    int method = extras.getInt(Processor.Extras.METHOD_EXTRA);

                    switch (method) {
                        case ServiceHelper.Methods.LOAD_WEATHER_CITY:
                            if (success) {
                                getSupportLoaderManager().restartLoader(WEATHER_CURRENT_LOADER, null, CityListActivity.this);
                            }
                            break;
                        case ServiceHelper.Methods.LOAD_WEATHER_CITIES:
                            if (success) {
                                getSupportLoaderManager().getLoader(WEATHER_CURRENT_LOADER).forceLoad();
                            }
                            setUpdateButtonState(false);
                            break;
                        case ServiceHelper.Methods.LOAD_CITY_TO_WATCH:
                            if (success) {
                                int cityId = extras.getInt(Processor.CITY_ID);
                                if (cityId != Processor.NOT_CITY)
                                    serviceHelper.loadWeather(cityId);
                            }
                            break;
                    }
                }
            }
        };
    }

    private void initAdapter() {
        adapter = new CityListAdapter(this, weatherCurrentList);
    }

    private void initComponents() {
        rvCityList.setAdapter(adapter);
        rvCityList.setHasFixedSize(true);
        rvCityList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.main_menu_refresh:
                if (connectionDetector.isNetworkAvailableAndConnected()) {
                    if (cityIds != null && !cityIds.isEmpty()) {
                        serviceHelper.loadWeather(cityIds);
                        setUpdateButtonState(true);
                    }
                } else {
                    Toast.makeText(CityListActivity.this,
                            R.string.connection_not_found,
                            Toast.LENGTH_SHORT).show();
                    setUpdateButtonState(false);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        switch (id) {
            case WEATHER_CURRENT_LOADER:
                cursorLoader = new CursorLoader(this, WeatherCurrentEntry.CONTENT_URI, null, null, null, null);
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cityIds != null && !cityIds.isEmpty()) {
            weatherCurrentList.clear();
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    loadWeatherCurrentListFromCursor(cursor);
                    cursor.close();
                } else {
                    getSupportLoaderManager().getLoader(WEATHER_CURRENT_LOADER).forceLoad();
                }
            }

            adapter.refreshList(weatherCurrentList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onAddCityFragmentInteraction(City city) {
        if (city != null) {
            AppPreference.addCityId(this, city.getId());
            serviceHelper.loadCityToWatch(city);
        }
    }

    private void loadWeatherCurrentListFromCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                WeatherCurrentModel weatherCurrentModel = new WeatherCurrentModel();
                weatherCurrentModel.setCityId(cursor.getInt(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_CITY_ID)));
                weatherCurrentModel.setCityName(cursor.getString(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_CITY_NAME)));

                float temperature = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TEMPERATURE_CURRENT));
                float pressure = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_PRESSURE));
                float humidity = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_HUMIDITY));
                float temperatureMin = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TEMPERATURE_MIN));
                float temperatureMax = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TEMPERATURE_MAX));
                weatherCurrentModel.setMain(new Main(temperature, pressure, humidity, temperatureMin, temperatureMax));

                int weatherId = cursor.getInt(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_WEATHER_ID));
                String weatherMain = cursor.getString(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_WEATHER_MAIN));
                String weatherDescription = cursor.getString(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_WEATHER_DESCRIPTION));
                String weatherIcon = cursor.getString(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_WEATHER_ICON));
                Weather weather = new Weather(weatherId, weatherMain, weatherDescription, weatherIcon);
                List<Weather> weatherList = new ArrayList<>();
                weatherList.add(weather);
                weatherCurrentModel.setWeather(weatherList);

                float windSpeed = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_WIND_SPEED));
                Wind wind = new Wind(windSpeed);
                weatherCurrentModel.setWind(wind);

                int sysSunrise = cursor.getInt(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TIME_SUNRISE));
                int sysSunset = cursor.getInt(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TIME_SUNSET));
                Sys sys = new Sys(sysSunrise, sysSunset);
                weatherCurrentModel.setSys(sys);

                int cloudsAll = cursor.getInt(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_CLOUDINESS));
                Clouds clouds = new Clouds(cloudsAll);
                weatherCurrentModel.setClouds(clouds);

                long dateTime = cursor.getLong(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TIME_MEASUREMENT));
                weatherCurrentModel.setDateTime(dateTime);

                weatherCurrentList.add(weatherCurrentModel);

                if (AppPreference.getCurrentCityId(this) == weatherCurrentModel.getCityId()) {
                    AppPreference.saveWeather(this, weatherCurrentModel);
                }

            } while (cursor.moveToNext());
        }
    }

    private void showLoading() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
