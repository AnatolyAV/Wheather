package ru.andreev_av.weather.presentation.activities;

import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.andreev_av.weather.R;
import ru.andreev_av.weather.domain.model.City;
import ru.andreev_av.weather.presentation.App;
import ru.andreev_av.weather.presentation.preferences.AppPreference;
import ru.andreev_av.weather.presentation.presenters.CitiesPresenter;
import ru.andreev_av.weather.presentation.views.ICitiesView;

public class SplashActivity extends MvpAppCompatActivity implements ICitiesView {

    // TODO подумать над разделением на два презентора
    @Inject
    @InjectPresenter
    CitiesPresenter mCitiesPresenter;

    private ArrayList<Integer> mCityIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getInstance().getAppComponent().plusCitiesComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (savedInstanceState == null) {
            mCitiesPresenter.getCitiesByToWatch(true);
        }
    }


    @ProvidePresenter
    CitiesPresenter provideCitiesPresenter() {
        return mCitiesPresenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showCities(List<City> cities) {
        for (City city : cities) {
            mCityIds.add(city.getId());
        }

        if (AppPreference.getCurrentCityId(this) == AppPreference.NOT_CURRENT_CITY_ID) {
            AppPreference.saveCurrentCityId(this, mCityIds.get(0));
        }

        AppPreference.saveCityIds(this, mCityIds);

        Intent weatherCurrentListIntent = new Intent(this, CitiesListActivity.class);
        startActivity(weatherCurrentListIntent);
    }

    @Override
    public void updateCity(City city) {
    }

    @Override
    public void showCityName(String cityName) {
    }

    @Override
    public void processAddedCity(City city) {
    }

    @Override
    public void showErrorSelectedCity() {
    }
}
