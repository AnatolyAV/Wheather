package ru.andreev_av.weather.presentation.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import javax.inject.Inject;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.presentation.App;
import ru.andreev_av.weather.presentation.enums.RefreshingType;
import ru.andreev_av.weather.presentation.preferences.AppPreference;
import ru.andreev_av.weather.presentation.presenters.WeatherCurrentPresenter;
import ru.andreev_av.weather.presentation.views.IWeatherCurrentView;
import ru.andreev_av.weather.utils.ImageUtils;
import ru.andreev_av.weather.utils.StringUtils;
import ru.andreev_av.weather.utils.UnitUtils;

public class WeatherCurrentDetailsActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, IWeatherCurrentView {

    @Inject
    @InjectPresenter
    WeatherCurrentPresenter mWeatherCurrentPresenter;

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

    private int cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInstance().getAppComponent().plusWeatherCurrentComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_current_details);

        findComponents();
        initComponents();
        initToolbar();

        initListeners();
        initDrawer(toolbar);

        cityId = AppPreference.getCurrentCityId(this);

        if (savedInstanceState == null) {
            mWeatherCurrentPresenter.loadWeather(cityId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        abLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        abLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_refresh:
                mWeatherCurrentPresenter.loadWeather(cityId, RefreshingType.UPDATE_BUTTON);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @ProvidePresenter
    WeatherCurrentPresenter provideWeatherCurrentPresenter() {
        return mWeatherCurrentPresenter;
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
                mWeatherCurrentPresenter.loadWeather(cityId, RefreshingType.SWIPE);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        srLayout.setEnabled(verticalOffset == 0);
    }

    @Override
    public void showButtonRefreshing() {
        setUpdateButtonState(true);
    }

    @Override
    public void hideButtonRefreshing() {
        setUpdateButtonState(false);
    }

    @Override
    public void showSwipeRefreshing() {
        srLayout.setRefreshing(true);
        srLayout.setEnabled(false);
    }

    @Override
    public void hideSwipeRefreshing() {
        srLayout.setRefreshing(false);
        srLayout.setEnabled(true);
    }

    @Override
    public void showWeatherCurrent(WeatherCurrent weatherCurrent) {
        fillComponentsFromWeatherCurrent(weatherCurrent);
    }

    @Override
    public void showErrorWeatherCurrent() {
        Toast.makeText(this,
                R.string.error_weather_current,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotConnection() {
        Toast.makeText(this,
                R.string.connection_not_found,
                Toast.LENGTH_SHORT).show();
    }

    private void fillComponentsFromWeatherCurrent(WeatherCurrent weatherCurrent) {
        String title = weatherCurrent.getCityName();
        setTitle(title);

        String iconId = weatherCurrent.getWeathers().get(0).getIcon();
        float temperatureCurrent = weatherCurrent.getMain().getTemperature();

        String description = weatherCurrent.getWeathers().get(0).getDescription();
        long lastUpdate = System.currentTimeMillis();

        float wind = weatherCurrent.getWind().getSpeed();
        float pressure = weatherCurrent.getMain().getPressure();
        float humidity = weatherCurrent.getMain().getHumidity();
        int clouds = weatherCurrent.getClouds().getAll();
        long sunrise = weatherCurrent.getSys().getSunrise();
        long sunset = weatherCurrent.getSys().getSunset();

        // TODO когда добавлю "Настройки" поменять для работы и с °F
        tvIconWeather.setText(ImageUtils.getStrIcon(this, iconId));
        tvTemperatureCurrent.setText(getString(R.string.temperature_with_degree, UnitUtils.getFormatTemperature(temperatureCurrent)));
        tvDescription.setText(StringUtils.firstUpperCase(description));
        tvLastUpdate.setText(getString(R.string.label_last_update, UnitUtils.getFormatDateTime(this, lastUpdate)));

        tvWindSpeed.setText(getString(R.string.label_wind, UnitUtils.getFormatWind(wind), getString(R.string.wind_speed_meters)));
        tvPressure.setText(getString(R.string.label_pressure, UnitUtils.getFormatPressure(pressure), getString(R.string.pressure_measurement)));
        tvHumidity.setText(getString(R.string.label_humidity, String.valueOf(humidity), getString(R.string.percent_sign)));
        tvCloudiness.setText(getString(R.string.label_cloudiness, String.valueOf(clouds), getString(R.string.percent_sign)));
        tvSunrise.setText(getString(R.string.label_sunrise, UnitUtils.getFormatUnixTime(this, sunrise)));
        tvSunset.setText(getString(R.string.label_sunset, UnitUtils.getFormatUnixTime(this, sunset)));
    }
}