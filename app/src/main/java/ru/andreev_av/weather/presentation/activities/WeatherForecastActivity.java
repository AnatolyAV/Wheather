package ru.andreev_av.weather.presentation.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

    @Inject
    @InjectPresenter
    WeatherForecastPresenter mWeatherForecastPresenter;
    private List<WeatherForecast> mWeatherForecasts = new ArrayList<>();
    private RecyclerView mWeatherForecastsRecycleView;
    private WeatherForecastAdapter mAdapter;
    private ImageView mCountDaysImageView;
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

        initListeners();

        initToolbar();

        initDrawer(toolbar);

        initAdapter();

        initComponents();

        mCityId = AppPreference.getCurrentCityId(this);

        mProgressDialog = new ProgressDialog(this);

        if (savedInstanceState == null) {
            mCountDays = WeatherForecastPresenter.COUNT_DAYS_THREE;
            mWeatherForecastPresenter.setCityId(mCityId);
            mWeatherForecastPresenter.setCountDays(mCountDays);
            mWeatherForecastPresenter.loadWeatherForecast(mCityId, mCountDays);
        } else {
            mWeatherForecastPresenter.updateCountDaysAndItsImage(false);
        }
    }

    @ProvidePresenter
    WeatherForecastPresenter provideWeatherForecastPresenter() {
        return mWeatherForecastPresenter;
    }

    protected void findComponents() {
        super.findComponents();
        mWeatherForecastsRecycleView = (RecyclerView) findViewById(R.id.rv_weather_forecast_list);
        mCountDaysImageView = (ImageView) findViewById(R.id.img_count_days);
    }

    private void initAdapter() {
        mAdapter = new WeatherForecastAdapter(this, mWeatherForecasts);
    }

    private void initComponents() {
        mWeatherForecastsRecycleView.setAdapter(mAdapter);
        mWeatherForecastsRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void initListeners() {
        mCountDaysImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeatherForecastPresenter.updateCountDaysAndItsImage(true);
                mWeatherForecastPresenter.loadWeatherForecast(mCityId, mCountDays);
            }
        });
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
                mWeatherForecastPresenter.loadWeatherForecast(mCityId, mCountDays, RefreshingType.UPDATE_BUTTON);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
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

    @Override
    public void setCountDays(int countDays) {
        mCountDays = countDays;
    }

    @Override
    public void showCountDaysImageThree() {
        mCountDaysImageView.setImageResource(R.drawable.ic_filter_3);
    }

    @Override
    public void showCountDaysImageSeven() {
        mCountDaysImageView.setImageResource(R.drawable.ic_filter_7);
    }
}
