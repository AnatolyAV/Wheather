package ru.andreev_av.weather.presentation.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.domain.model.WeatherForecast;
import ru.andreev_av.weather.presentation.App;
import ru.andreev_av.weather.presentation.adapters.WeatherForecastAdapter;
import ru.andreev_av.weather.presentation.enums.RefreshingType;
import ru.andreev_av.weather.presentation.preferences.AppPreference;
import ru.andreev_av.weather.presentation.presenters.WeatherForecastPresenter;
import ru.andreev_av.weather.presentation.views.IWeatherForecastView;

public class WeatherForecastActivity extends BaseActivity implements IWeatherForecastView {

    private final static int COUNT_DAYS_THREE = 3;
    private final static int COUNT_DAYS_SEVEN = 7;
    private final static String COUNT_DAYS = "countDays";

    @Inject
    @InjectPresenter
    WeatherForecastPresenter mWeatherForecastPresenter;
    private List<WeatherForecast> mWeatherForecasts = new ArrayList<>();
    private RecyclerView mWeatherForecastsRecycleView;
    private WeatherForecastAdapter mAdapter;
    // TODO заменить на ProgressBar
    private ProgressDialog mProgressDialog;

    private int mCityId;
    private int mCountDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInstance().getAppComponent().plusWeatherForecastComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        findComponents();

        initToolbar();

        initDrawer(toolbar);

        initAdapter();

        initComponents();

        mCityId = AppPreference.getCurrentCityId(this);

        mProgressDialog = new ProgressDialog(this);

        if (savedInstanceState == null) {
            mCountDays = COUNT_DAYS_THREE;
            mWeatherForecastPresenter.setCityId(mCityId);
            mWeatherForecastPresenter.setCountDays(mCountDays);
            mWeatherForecastPresenter.loadWeatherForecast(mCityId, mCountDays);
        } else {
            mCountDays = savedInstanceState.getInt(COUNT_DAYS);
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNT_DAYS, mCountDays);
    }

    @ProvidePresenter
    WeatherForecastPresenter provideWeatherForecastPresenter() {
        return mWeatherForecastPresenter;
    }

    protected void findComponents() {
        super.findComponents();
        mWeatherForecastsRecycleView = (RecyclerView) findViewById(R.id.rv_weather_forecast_list);
    }

    private void initAdapter() {
        mAdapter = new WeatherForecastAdapter(this, mWeatherForecasts);
    }

    private void initComponents() {
        mWeatherForecastsRecycleView.setAdapter(mAdapter);
        mWeatherForecastsRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather_forecast, menu);
        updateItem = menu.findItem(R.id.main_menu_refresh);
        MenuItem countDaysMenuItem = menu.findItem(R.id.main_menu_count_days);
        if (mCountDays == COUNT_DAYS_THREE) {
            countDaysMenuItem.setIcon(R.drawable.ic_filter_7);
        } else {
            countDaysMenuItem.setIcon(R.drawable.ic_filter_3);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.main_menu_refresh:
                mWeatherForecastPresenter.loadWeatherForecast(mCityId, mCountDays, RefreshingType.UPDATE_BUTTON);
                return true;
            case R.id.main_menu_count_days:
                if (mCountDays == COUNT_DAYS_THREE) {
                    mCountDays = COUNT_DAYS_SEVEN;
                    item.setIcon(R.drawable.ic_filter_3);
                } else {
                    mCountDays = COUNT_DAYS_THREE;
                    item.setIcon(R.drawable.ic_filter_7);
                }
                mWeatherForecastPresenter.loadWeatherForecast(mCityId, mCountDays);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading(RefreshingType refreshingType) {
        switch (refreshingType) {
            case STANDARD:
                if (!mProgressDialog.isShowing()) {
                    mProgressDialog.show();
                }
                break;
            case UPDATE_BUTTON:
                setUpdateButtonState(true);
                break;
        }
    }

    @Override
    public void hideLoading(RefreshingType refreshingType) {
        switch (refreshingType) {
            case STANDARD:
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                break;
            case UPDATE_BUTTON:
                setUpdateButtonState(false);
                break;
        }
    }

    @Override
    public void showWeatherForecasts(List<ru.andreev_av.weather.domain.model.WeatherForecast> weatherForecasts) {
        mWeatherForecasts = weatherForecasts;

        mAdapter.setWeatherForecasts(mWeatherForecasts);
        // TODO удалить
        Toast.makeText(this,
                "Загрузка погод успешно завершена",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorWeatherForecasts() {
        Toast.makeText(this,
                R.string.error_weather_forecasts,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotConnection() {
        Toast.makeText(this,
                R.string.connection_not_found,
                Toast.LENGTH_SHORT).show();
    }
}
