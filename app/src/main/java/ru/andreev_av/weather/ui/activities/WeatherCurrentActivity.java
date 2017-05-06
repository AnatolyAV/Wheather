package ru.andreev_av.weather.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.db.entry.WeatherCurrentEntry;
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
import ru.andreev_av.weather.utils.ImageUtils;
import ru.andreev_av.weather.utils.StringUtils;
import ru.andreev_av.weather.utils.UnitUtils;

import static ru.andreev_av.weather.preferences.AppPreference.LAST_UPDATE_TIME_IN_MS;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_CITY_ID;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_CITY_NAME;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_CLOUDINESS;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_HUMIDITY;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_PRESSURE;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_TEMPERATURE_CURRENT;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_TIME_SUNRISE;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_TIME_SUNSET;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_WEATHER_CURRENT;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_WEATHER_DESCRIPTION;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_WEATHER_ICON;
import static ru.andreev_av.weather.preferences.AppPreference.PREF_WIND_SPEED;

public class WeatherCurrentActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private final static String BROADCAST_ACTION = "ru.andreev_av.weather.WeatherCurrentActivity.ActionResult";
    private final static int WEATHER_CURRENT_LOADER = 1;
    private final IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
    private SwipeRefreshLayout srLayout;
    private AppBarLayout abLayout;
    private TextView tvIconWeather;
    private TextView tvTemperatureCurrent;
    private TextView tvDescription;
    private TextView tvLastUpdate;
    private TextView tvIconPressure;
    private TextView tvPressure;
    private TextView tvIconHumidity;
    private TextView tvHumidity;
    private TextView tvIconWind;
    private TextView tvWindSpeed;
    private TextView tvIconCloudiness;
    private TextView tvCloudiness;
    private TextView tvIconSunrise;
    private TextView tvSunrise;
    private TextView tvIconSunset;
    private TextView tvSunset;
    private ConnectionDetector connectionDetector;
    private ServiceHelper serviceHelper;
    private SharedPreferences prefWeather;
    private int cityId;
    private boolean isRefreshed;
    private BroadcastReceiver weatherBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_current);

        findComponents();
        initComponents();
        initToolbar();

        initListeners();
        initDrawer(toolbar);

        connectionDetector = new ConnectionDetector(this);

        serviceHelper = new ServiceHelper(this, BROADCAST_ACTION);

        prefWeather = getSharedPreferences(PREF_WEATHER_CURRENT, MODE_PRIVATE);
        cityId = prefWeather.getInt(PREF_CITY_ID, -1);

        isRefreshed = true;

        initWeatherReceiver();

        getSupportLoaderManager().initLoader(WEATHER_CURRENT_LOADER, null, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadWeatherFromPref();
        abLayout.addOnOffsetChangedListener(this);
        registerReceiver(weatherBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        abLayout.removeOnOffsetChangedListener(this);
        unregisterReceiver(weatherBroadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_refresh:
                if (connectionDetector.isNetworkAvailableAndConnected()) {
                    serviceHelper.loadWeather(cityId);
                    isRefreshed = false;
                    setUpdateButtonState(true);
                } else {
                    Toast.makeText(this,
                            R.string.connection_not_found,
                            Toast.LENGTH_SHORT).show();
                    setUpdateButtonState(false);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void findComponents() {
        super.findComponents();
        srLayout = (SwipeRefreshLayout) findViewById(R.id.layout_weather_current);
        abLayout = (AppBarLayout) findViewById(R.id.app_bar_weather_current);

        tvIconWeather = (TextView) findViewById(R.id.ic_weather);
        tvTemperatureCurrent = (TextView) findViewById(R.id.tv_temperature);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvLastUpdate = (TextView) findViewById(R.id.tv_last_update);

        tvIconPressure = (TextView) findViewById(R.id.ic_pressure);
        tvPressure = (TextView) findViewById(R.id.tv_pressure);
        tvIconHumidity = (TextView) findViewById(R.id.ic_humidity);
        tvHumidity = (TextView) findViewById(R.id.tv_humidity);
        tvIconWind = (TextView) findViewById(R.id.ic_wind);
        tvWindSpeed = (TextView) findViewById(R.id.tv_wind_speed);
        tvIconCloudiness = (TextView) findViewById(R.id.ic_cloudiness);
        tvCloudiness = (TextView) findViewById(R.id.tv_cloudiness);

        tvIconSunrise = (TextView) findViewById(R.id.ic_sunrise);
        tvSunrise = (TextView) findViewById(R.id.tv_sunrise);
        tvIconSunset = (TextView) findViewById(R.id.ic_sunset);
        tvSunset = (TextView) findViewById(R.id.tv_sunset);
    }

    protected void initComponents() {
        Typeface weatherFontIcon = Typeface.createFromAsset(this.getAssets(),
                "fonts/weathericons-regular-webfont.ttf");
        Typeface robotoThin = Typeface.createFromAsset(this.getAssets(),
                "fonts/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(this.getAssets(),
                "fonts/Roboto-Light.ttf");

        tvIconWeather.setTypeface(weatherFontIcon);
        tvTemperatureCurrent.setTypeface(robotoThin);

        tvIconWind.setTypeface(weatherFontIcon);
        tvIconWind.setText(getString(R.string.icon_wind));
        tvWindSpeed.setTypeface(robotoLight);
        tvIconHumidity.setTypeface(weatherFontIcon);
        tvIconHumidity.setText(getString(R.string.icon_humidity));
        tvHumidity.setTypeface(robotoLight);
        tvIconPressure.setTypeface(weatherFontIcon);
        tvIconPressure.setText(getString(R.string.icon_barometer));
        tvPressure.setTypeface(robotoLight);
        tvIconCloudiness.setTypeface(weatherFontIcon);
        tvIconCloudiness.setText(getString(R.string.icon_cloudiness));
        tvCloudiness.setTypeface(robotoLight);

        tvIconSunrise.setTypeface(weatherFontIcon);
        tvIconSunrise.setText(getString(R.string.icon_sunrise));
        tvSunrise.setTypeface(robotoLight);
        tvIconSunset.setTypeface(weatherFontIcon);
        tvIconSunset.setText(getString(R.string.icon_sunset));
        tvSunset.setTypeface(robotoLight);
    }

    protected void initListeners() {
        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (connectionDetector.isNetworkAvailableAndConnected()) {
                    serviceHelper.loadWeather(cityId);
                    isRefreshed = false;
                } else {
                    Toast.makeText(WeatherCurrentActivity.this,
                            R.string.connection_not_found,
                            Toast.LENGTH_SHORT).show();
                    srLayout.setRefreshing(false);
                }
            }
        });
    }

    private void initWeatherReceiver() {
        weatherBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                int status = intent.getIntExtra(PARAM_STATUS, 0);

                if (status == STATUS_FINISH) {
                    Bundle extras = intent.getExtras();

                    boolean success = extras.getBoolean(Processor.Extras.RESULT_EXTRA);

                    int method = extras.getInt(Processor.Extras.METHOD_EXTRA);

                    switch (method) {
                        case ServiceHelper.Methods.LOAD_WEATHER_CITY:
                            srLayout.setRefreshing(false);
                            setUpdateButtonState(false);
                            if (success)
                                getSupportLoaderManager().getLoader(WEATHER_CURRENT_LOADER).forceLoad();
                            break;
                    }
                }
            }
        };
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        srLayout.setEnabled(verticalOffset == 0);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        switch (id) {
            case WEATHER_CURRENT_LOADER:
                String selection = WeatherCurrentEntry.COLUMN_CITY_ID + " = " + cityId;
                cursorLoader = new CursorLoader(this, WeatherCurrentEntry.CONTENT_URI, null, selection, null, null);
                break;
        }

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (!isRefreshed) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    AppPreference.saveWeather(this, loadWeatherFromCursor(cursor));
                    loadWeatherFromPref();
                    isRefreshed = true;
                }
                cursor.close();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void loadWeatherFromPref() {
        String titlePref = prefWeather.getString(PREF_CITY_NAME, getString(R.string.default_city_name));
        setTitle(titlePref);

        String iconIdPref = prefWeather.getString(PREF_WEATHER_ICON, getString(R.string.default_icon_weather));
        float temperaturePref = prefWeather.getFloat(PREF_TEMPERATURE_CURRENT, 0);

        String descriptionPref = prefWeather.getString(PREF_WEATHER_DESCRIPTION, getString(R.string.default_weather_description));
        long lastUpdatePref = prefWeather.getLong(LAST_UPDATE_TIME_IN_MS, 0);

        float windPref = prefWeather.getFloat(PREF_WIND_SPEED, 0);
        float pressurePref = prefWeather.getFloat(PREF_PRESSURE, 0);
        float humidityPref = prefWeather.getFloat(PREF_HUMIDITY, 0);
        int clouds = prefWeather.getInt(PREF_CLOUDINESS, 0);
        long sunrisePref = prefWeather.getLong(PREF_TIME_SUNRISE, -1);
        long sunsetPref = prefWeather.getLong(PREF_TIME_SUNSET, -1);

        // TODO когда добавлю "Настройки" поменять для работы и с °F
        tvIconWeather.setText(ImageUtils.getStrIcon(this, iconIdPref));
        tvTemperatureCurrent.setText(getString(R.string.temperature_with_degree, UnitUtils.getFormatTemperature(temperaturePref)));
        tvDescription.setText(StringUtils.firstUpperCase(descriptionPref));
        tvLastUpdate.setText(getString(R.string.label_last_update, UnitUtils.getFormatDateTime(this, lastUpdatePref)));

        tvWindSpeed.setText(getString(R.string.label_wind, UnitUtils.getFormatWind(windPref), getString(R.string.wind_speed_meters)));
        tvPressure.setText(getString(R.string.label_pressure, UnitUtils.getFormatPressure(pressurePref), getString(R.string.pressure_measurement)));
        tvHumidity.setText(getString(R.string.label_humidity, String.valueOf(humidityPref), getString(R.string.percent_sign)));
        tvCloudiness.setText(getString(R.string.label_cloudiness, String.valueOf(clouds), getString(R.string.percent_sign)));
        tvSunrise.setText(getString(R.string.label_sunrise, UnitUtils.getFormatUnixTime(this, sunrisePref)));
        tvSunset.setText(getString(R.string.label_sunset, UnitUtils.getFormatUnixTime(this, sunsetPref)));

    }

    private WeatherCurrentModel loadWeatherFromCursor(Cursor cursor) {

        WeatherCurrentModel weatherCurrentModel = new WeatherCurrentModel();

        weatherCurrentModel.setCityId(cursor.getInt(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_CITY_ID)));
        weatherCurrentModel.setCityName(cursor.getString(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_CITY_NAME)));

        float temperatureCurrent = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TEMPERATURE_CURRENT));
        float pressure = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_PRESSURE));
        float humidity = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_HUMIDITY));
        float temperatureMin = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TEMPERATURE_MIN));
        float temperatureMax = cursor.getFloat(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TEMPERATURE_MAX));
        weatherCurrentModel.setMain(new Main(temperatureCurrent, pressure, humidity, temperatureMin, temperatureMax));

        long dateTime = cursor.getLong(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TIME_MEASUREMENT));
        weatherCurrentModel.setDateTime(dateTime);

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

        int cloudsAll = cursor.getInt(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_CLOUDINESS));
        Clouds clouds = new Clouds(cloudsAll);
        weatherCurrentModel.setClouds(clouds);

        int sysSunrise = cursor.getInt(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TIME_SUNRISE));
        int sysSunset = cursor.getInt(cursor.getColumnIndex(WeatherCurrentEntry.COLUMN_TIME_SUNSET));
        Sys sys = new Sys(sysSunrise, sysSunset);
        weatherCurrentModel.setSys(sys);

        return weatherCurrentModel;
    }
}
