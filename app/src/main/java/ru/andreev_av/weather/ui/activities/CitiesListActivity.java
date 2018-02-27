package ru.andreev_av.weather.ui.activities;

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

import ru.andreev_av.weather.App;
import ru.andreev_av.weather.R;
import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.listeners.RecyclerItemClickListener;
import ru.andreev_av.weather.preferences.AppPreference;
import ru.andreev_av.weather.ui.adapters.WeatherCurrentCitiesAdapter;
import ru.andreev_av.weather.ui.fragments.AddCityFragment;
import ru.andreev_av.weather.ui.presentation.CitiesPresenter;
import ru.andreev_av.weather.ui.presentation.ICitiesView;
import ru.andreev_av.weather.ui.presentation.IWeatherCurrentView;
import ru.andreev_av.weather.ui.presentation.WeatherCurrentPresenter;

public class CitiesListActivity extends BaseActivity implements AddCityFragment.OnAddCityFragmentInteractionListener, IWeatherCurrentView, ICitiesView {

    @Inject
    @InjectPresenter
    WeatherCurrentPresenter mWeatherCurrentPresenter;

    @Inject
    @InjectPresenter
    CitiesPresenter mCitiesPresenter;

    private RecyclerView mWeatherCurrentCitiesRecyclerView;
    private WeatherCurrentCitiesAdapter mWeatherCurrentCitiesAdapter;
    private FloatingActionButton mAddCityFloatingActionButton;
    // TODO Заменить на ProgressBar
    private ProgressDialog dialog;

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

        dialog = new ProgressDialog(this);

        // TODO возможно при пересоздании некорректные mCityIds будут, надо проверить
        mWeatherCurrentPresenter.setCityIds(mCityIds);
    }

    @ProvidePresenter
    WeatherCurrentPresenter provideWeatherCurrentPresenter() {
        return mWeatherCurrentPresenter;
    }

    @ProvidePresenter
    CitiesPresenter provideCitiesPresenter() {
        return mCitiesPresenter;
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
//                AppPreference.updateCurrentCityId(CitiesListActivity.this, weatherCurrents.get(position).getCityId());
//                AppPreference.saveWeather(CitiesListActivity.this, weatherCurrents.get(position));
                Intent intent = new Intent(CitiesListActivity.this, WeatherCurrentActivity.class);
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
                    mWeatherCurrentPresenter.loadWeather(mCityIds, true);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddCityFragmentInteraction(ru.andreev_av.weather.domain.model.City city) {
        if (city != null) {
            AppPreference.addCityId(this, city.getId());
            mCitiesPresenter.loadCityToWatch(city);
        }
    }

    @Override
    public void showLoading() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void updateButtonState(boolean isUpdate) {
        setUpdateButtonState(isUpdate);
    }

    @Override
    public void showWeatherCurrents(List<WeatherCurrent> weatherCurrents) {
        mWeatherCurrents = weatherCurrents;
        mWeatherCurrentCitiesAdapter.refreshList(mWeatherCurrents);
        // TODO удалить
        Toast.makeText(this,
                "Загрузка погод успешно завершена",
                Toast.LENGTH_SHORT).show();
        updateButtonState(false);
    }

    @Override
    public void showWeatherCurrent(List<WeatherCurrent> weatherCurrent) {
        mWeatherCurrents.add(weatherCurrent.get(0));
        mWeatherCurrentCitiesAdapter.refreshList(mWeatherCurrents);
        // TODO удалить
        Toast.makeText(this,
                "Загрузка погоды успешно завершена",
                Toast.LENGTH_SHORT).show();
        updateButtonState(false);
    }

    @Override
    public void showErrorWeatherCurrent() {
        Toast.makeText(this,
                R.string.error_weather_current,
                Toast.LENGTH_SHORT).show();
        updateButtonState(false);
    }

    @Override
    public void showErrorWeatherCurrents() {
        Toast.makeText(this,
                R.string.error_weather_currents,
                Toast.LENGTH_SHORT).show();
        updateButtonState(false);
    }

    @Override
    public void showNotConnection() {
        Toast.makeText(this,
                R.string.connection_not_found,
                Toast.LENGTH_SHORT).show();
        updateButtonState(false);
    }

    @Override
    public void processAddedCity(City city) {
        mWeatherCurrentPresenter.loadWeather(city.getId());
    }

    @Override
    public void showCities(List<City> cities) {

    }
}
