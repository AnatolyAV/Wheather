package ru.andreev_av.weather.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.ArrayList;
import java.util.List;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.data.db.WeatherCurrentDao;
import ru.andreev_av.weather.data.model.City;
import ru.andreev_av.weather.data.repository.CitiesRepository;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.domain.usecase.CitiesUseCase;
import ru.andreev_av.weather.domain.usecase.ICitiesUseCase;
import ru.andreev_av.weather.listeners.RecyclerItemClickListener;
import ru.andreev_av.weather.net.ConnectionDetector;
import ru.andreev_av.weather.preferences.AppPreference;
import ru.andreev_av.weather.ui.adapters.CityListAdapter;
import ru.andreev_av.weather.ui.fragments.AddCityFragment;
import ru.andreev_av.weather.ui.presentation.CitiesPresenter;
import ru.andreev_av.weather.ui.presentation.ICitiesView;

public class CityListActivity extends BaseActivity implements AddCityFragment.OnAddCityFragmentInteractionListener, ICitiesView {

    @InjectPresenter
    CitiesPresenter mCitiesPresenter;
    private RecyclerView rvCityList;
    private FloatingActionButton fabAddCity;
    private CityListAdapter adapter;
    private ArrayList<Integer> cityIds;
    private List<WeatherCurrent> weatherCurrents = new ArrayList<>();
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

        dialog = new ProgressDialog(this);

        // TODO инжектить через Dagger
        ICitiesUseCase citiesUseCase = new CitiesUseCase(new CitiesRepository(WeatherCurrentDao.getInstance(this)));
        // TODO возможно при пересоздании некорректные cityIds будут, надо проверить
        mCitiesPresenter.setCitiesUseCase(citiesUseCase);
        mCitiesPresenter.setCityIds(cityIds);
        mCitiesPresenter.setConnectionDetector(new ConnectionDetector(this));
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
        final FragmentManager fragmentManager = getSupportFragmentManager();
        AddCityFragment.newInstance().show(fragmentManager, "AddCityFragmentDialog");
    }

//    private void initWeatherReceiver() {
//        weatherBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                int status = intent.getIntExtra(PARAM_STATUS, 0);
//
//                if (status == STATUS_CONNECTION_NOT_FOUND) {
//                    hideLoading();
//                    showNotConnection();
//                }
//
//                if (status == STATUS_START) {
//                    dialog = new ProgressDialog(context);
//                    dialog.setMessage(context.getResources().getString(R.string.loading));
//                    showLoading();
//                }
//
//                if (status == STATUS_FINISH) {
//                    hideLoading();
//                    Bundle extras = intent.getExtras();
//
//                    boolean success = extras.getBoolean(Processor.Extras.RESULT_EXTRA);
//
//                    int method = extras.getInt(Processor.Extras.METHOD_EXTRA);
//
//                    switch (method) {
//                        case ServiceHelper.Methods.LOAD_WEATHER_CITY:
//                            if (success) {
//                                getSupportLoaderManager().restartLoader(WEATHER_CURRENT_LOADER, null, CityListActivity.this);
//                            }
//                            break;
//                        case ServiceHelper.Methods.LOAD_WEATHER_CITIES:
//                            if (success) {
//                                getSupportLoaderManager().getLoader(WEATHER_CURRENT_LOADER).forceLoad();
//                            }
//                            setUpdateButtonState(false);
//                            break;
//                        case ServiceHelper.Methods.LOAD_CITY_TO_WATCH:
//                            if (success) {
//                                int cityId = extras.getInt(Processor.CITY_ID);
//                                if (cityId != Processor.NOT_CITY)
//                                    serviceHelper.loadWeather(cityId);
//                            }
//                            break;
//                    }
//                }
//            }
//        };
//    }

    private void initAdapter() {
        adapter = new CityListAdapter(this, weatherCurrents);
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
                    mCitiesPresenter.loadWeather(cityIds);
                    setUpdateButtonState(true);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddCityFragmentInteraction(City city) {
        if (city != null) {
            AppPreference.addCityId(this, city.getId());
//            serviceHelper.loadCityToWatch(city);
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
    public void showWeatherCurrents(List<WeatherCurrent> weatherCurrents) {
        adapter.refreshList(weatherCurrents);
        // TODO удалить
        Toast.makeText(CityListActivity.this,
                "Загрузка погод успешно завершена",
                Toast.LENGTH_SHORT).show();
        setUpdateButtonState(false);
    }

    @Override
    public void showWeatherCurrent(List<WeatherCurrent> weatherCurrent) {
        adapter.refreshList(weatherCurrent);
        // TODO удалить
        Toast.makeText(CityListActivity.this,
                "Загрузка погоды успешно завершена",
                Toast.LENGTH_SHORT).show();
        setUpdateButtonState(false);
    }

    @Override
    public void showErrorWeatherCurrent() {
        Toast.makeText(CityListActivity.this,
                R.string.error_weather_current,
                Toast.LENGTH_SHORT).show();
        setUpdateButtonState(false);
    }

    @Override
    public void showErrorWeatherCurrents() {
        Toast.makeText(CityListActivity.this,
                R.string.error_weather_currents,
                Toast.LENGTH_SHORT).show();
        setUpdateButtonState(false);
    }

    @Override
    public void showNotConnection() {
        Toast.makeText(CityListActivity.this,
                R.string.connection_not_found,
                Toast.LENGTH_SHORT).show();
        setUpdateButtonState(false);
    }
}
