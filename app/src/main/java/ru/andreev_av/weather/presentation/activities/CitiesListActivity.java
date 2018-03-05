package ru.andreev_av.weather.presentation.activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.presentation.App;
import ru.andreev_av.weather.presentation.adapters.WeatherCurrentCitiesAdapter;
import ru.andreev_av.weather.presentation.enums.RefreshingType;
import ru.andreev_av.weather.presentation.fragments.AddCityFragment;
import ru.andreev_av.weather.presentation.listeners.RecyclerItemClickListener;
import ru.andreev_av.weather.presentation.preferences.AppPreference;
import ru.andreev_av.weather.presentation.presenters.WeatherCurrentPresenter;
import ru.andreev_av.weather.presentation.views.IWeatherCurrentView;

public class CitiesListActivity extends BaseActivity implements AddCityFragment.OnAddCityFragmentInteractionListener, IWeatherCurrentView {

    @Inject
    @InjectPresenter
    WeatherCurrentPresenter mWeatherCurrentPresenter;

    private RecyclerView mWeatherCurrentCitiesRecyclerView;
    private WeatherCurrentCitiesAdapter mWeatherCurrentCitiesAdapter;
    private FloatingActionButton mAddCityFloatingActionButton;
    // TODO Заменить на ProgressBar
    private ProgressDialog mProgressDialog;

    private ArrayList<Integer> mCityIds;
    private List<WeatherCurrent> mWeatherCurrents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInstance().getAppComponent().plusWeatherCurrentComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        findComponents();

        initListeners();

        initToolbar();

        initDrawer(toolbar);

        initAdapter();

        initComponents();

        mCityIds = AppPreference.getCityIds(this);

        mProgressDialog = new ProgressDialog(this);

        if (savedInstanceState == null) {
            mWeatherCurrentPresenter.loadWeather(mCityIds);
        }
    }

    @ProvidePresenter
    WeatherCurrentPresenter provideWeatherCurrentPresenter() {
        return mWeatherCurrentPresenter;
    }

    protected void findComponents() {
        super.findComponents();
        mWeatherCurrentCitiesRecyclerView = (RecyclerView) findViewById(R.id.rv_city_list);
        mAddCityFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add_city);
    }

    protected void initListeners() {
        mWeatherCurrentCitiesRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mWeatherCurrentCitiesRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO заменить на intent.putExtra()
                AppPreference.saveCurrentCityId(CitiesListActivity.this, mWeatherCurrents.get(position).getCityId());
                AppPreference.saveCityNameAndCountryCode(CitiesListActivity.this, mWeatherCurrents.get(position).getCityName(), mWeatherCurrents.get(position).getSys().getCountryCode());
                Intent intent = new Intent(CitiesListActivity.this, WeatherCurrentDetailsActivity.class);
                startActivity(intent);
            }
        }));

        mAddCityFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAddCityFragment();
            }
        });
    }

    private void initAddCityFragment() {
        final FragmentManager fragmentManager = getFragmentManager();
        AddCityFragment.newInstance().show(fragmentManager, "AddCityFragmentDialog");
    }

    private void initAdapter() {
        mWeatherCurrentCitiesAdapter = new WeatherCurrentCitiesAdapter(this, mWeatherCurrents);
    }

    private void initComponents() {
        mWeatherCurrentCitiesRecyclerView.setAdapter(mWeatherCurrentCitiesAdapter);
        mWeatherCurrentCitiesRecyclerView.setHasFixedSize(true);
        mWeatherCurrentCitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.main_menu_refresh:
                if (mCityIds != null && !mCityIds.isEmpty()) {
                    mWeatherCurrentPresenter.loadWeather(mCityIds, RefreshingType.UPDATE_BUTTON);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddCityFragmentInteraction(City city) {
        if (city != null) {
            AppPreference.addCityId(this, city.getId());
            mWeatherCurrentPresenter.loadWeather(city.getId());
        }
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
    public void showSwipeRefreshing() {

    }

    @Override
    public void hideSwipeRefreshing() {

    }

    @Override
    public void showWeatherCurrents(List<WeatherCurrent> weatherCurrents) {
        mWeatherCurrents = weatherCurrents;
        mWeatherCurrentCitiesAdapter.refreshList(mWeatherCurrents);
        // TODO удалить
        Toast.makeText(this,
                "Загрузка погод успешно завершена",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWeatherCurrent(WeatherCurrent weatherCurrent) {
        if (weatherCurrent != null) {
            mWeatherCurrents.add(weatherCurrent);
            mWeatherCurrentCitiesAdapter.refreshList(mWeatherCurrents);
            // TODO удалить
            Toast.makeText(this,
                    "Загрузка погоды успешно завершена",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showErrorWeatherCurrent() {
        Toast.makeText(this,
                R.string.error_weather_current,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorWeatherCurrents() {
        Toast.makeText(this,
                R.string.error_weather_currents,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotConnection() {
        Toast.makeText(this,
                R.string.connection_not_found,
                Toast.LENGTH_SHORT).show();
    }
}
