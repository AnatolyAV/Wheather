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
import ru.andreev_av.weather.data.db.CityDao;
import ru.andreev_av.weather.data.repository.CityRepository;
import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.domain.usecase.CityUseCase;
import ru.andreev_av.weather.domain.usecase.ICityUseCase;
import ru.andreev_av.weather.listeners.RecyclerItemClickListener;
import ru.andreev_av.weather.preferences.AppPreference;
import ru.andreev_av.weather.ui.adapters.CityListAdapter;
import ru.andreev_av.weather.ui.fragments.AddCityFragment;
import ru.andreev_av.weather.ui.presentation.CityPresenter;
import ru.andreev_av.weather.ui.presentation.ICityView;
import ru.andreev_av.weather.ui.presentation.IWeatherCurrentView;
import ru.andreev_av.weather.ui.presentation.WeatherCurrentPresenter;

public class CityListActivity extends BaseActivity implements AddCityFragment.OnAddCityFragmentInteractionListener, IWeatherCurrentView, ICityView {

    @Inject
    @InjectPresenter
    WeatherCurrentPresenter mWeatherCurrentPresenter;

    @InjectPresenter
    CityPresenter mCitiesPresenter;

    private RecyclerView rvCityList;
    private FloatingActionButton fabAddCity;
    private CityListAdapter adapter;
    private ArrayList<Integer> cityIds;
    private List<WeatherCurrent> mWeatherCurrents = new ArrayList<>();
    // TODO Заменить на ProgressBar
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        findComponents();

        initListeners();

        initToolbar();

        initDrawer(toolbar);

        initAdapter();

        initComponents();

        cityIds = AppPreference.getCityIds(this);

        dialog = new ProgressDialog(this);

        // TODO возможно при пересоздании некорректные cityIds будут, надо проверить
        mWeatherCurrentPresenter.setCityIds(cityIds);

        // TODO Заменить на Dagger
        ICityUseCase cityUseCase = new CityUseCase(new CityRepository(CityDao.getInstance(getApplicationContext())));
        mCitiesPresenter.setCitiesUseCase(cityUseCase);
    }

    @ProvidePresenter
    WeatherCurrentPresenter providePresenter() {
        return mWeatherCurrentPresenter;
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
//                AppPreference.updateCurrentCityId(CityListActivity.this, weatherCurrents.get(position).getCityId());
//                AppPreference.saveWeather(CityListActivity.this, weatherCurrents.get(position));
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
        final FragmentManager fragmentManager = getFragmentManager();
        AddCityFragment.newInstance().show(fragmentManager, "AddCityFragmentDialog");
    }

    private void initAdapter() {
        adapter = new CityListAdapter(this, mWeatherCurrents);
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
                if (cityIds != null && !cityIds.isEmpty()) {
                    mWeatherCurrentPresenter.loadWeather(cityIds, true);
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
//            dialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
        }
    }

    @Override
    public void updateButtonState(boolean isUpdate) {
        setUpdateButtonState(isUpdate);
    }

    @Override
    public void showWeatherCurrents(List<WeatherCurrent> weatherCurrents) {
        mWeatherCurrents = weatherCurrents;
        adapter.refreshList(mWeatherCurrents);
        // TODO удалить
        Toast.makeText(this,
                "Загрузка погод успешно завершена",
                Toast.LENGTH_SHORT).show();
        updateButtonState(false);
    }

    @Override
    public void showWeatherCurrent(List<WeatherCurrent> weatherCurrent) {
        mWeatherCurrents.add(weatherCurrent.get(0));
        adapter.refreshList(mWeatherCurrents);
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
