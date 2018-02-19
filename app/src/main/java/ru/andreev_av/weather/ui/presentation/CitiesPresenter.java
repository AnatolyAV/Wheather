package ru.andreev_av.weather.ui.presentation;

import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.andreev_av.weather.data.model.City;
import ru.andreev_av.weather.domain.usecase.ICitiesUseCase;
import ru.andreev_av.weather.net.ConnectionDetector;
import ru.andreev_av.weather.utils.RxUtils;

@InjectViewState
public class CitiesPresenter extends MvpPresenter<ICitiesView> implements ICitiesPresenter {

    private final ICitiesUseCase mCitiesUseCase;
    private final ConnectionDetector mConnectionDetector;
    private ArrayList<Integer> mCityIds;

    @Inject
    public CitiesPresenter(ICitiesUseCase citiesUseCase, ConnectionDetector connectionDetector) {
        mCitiesUseCase = citiesUseCase;
        mConnectionDetector = connectionDetector;
    }

    public void setCityIds(ArrayList<Integer> cityIds) {
        mCityIds = cityIds;
    }

    /**
     * Вызывается тогда, когда к конкретному экземпляру Presenter первый раз будет привязана любая View
     */
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadWeather(mCityIds, false);
    }

    @Override
    public void loadWeather(int cityId) {
        if (checkNetworkAvailableAndConnected()) {
            mCitiesUseCase.loadWeather(cityId)
                    .doOnSubscribe(getViewState()::showLoading)
                    .doAfterTerminate(getViewState()::hideLoading)
                    .compose(RxUtils.async())
                    .subscribe(getViewState()::showWeatherCurrent, throwable -> getViewState()
                            .showErrorWeatherCurrent());
        } else {
            getViewState().showNotConnection();
        }
    }

    @Override
    public void loadWeather(ArrayList<Integer> cityIds, boolean isCallByPressingUpdateButton) {
        if (isCallByPressingUpdateButton) {
            getViewState().updateButtonState(true);
        }

        if (checkNetworkAvailableAndConnected()) {
            mCitiesUseCase.loadWeather(cityIds)
                    .doOnSubscribe(getViewState()::showLoading)
                    .doAfterTerminate(getViewState()::hideLoading)
                    .compose(RxUtils.async())
                    .subscribe(getViewState()::showWeatherCurrents, throwable -> getViewState().showErrorWeatherCurrents());
        } else {
            getViewState().showNotConnection();
        }
    }

    @Override
    public void loadCityToWatch(City city) {
        Bundle extras = new Bundle();
//        extras.putParcelable(PARAMETER_CITY, city);
//        initAndStartService(LOAD_CITY_TO_WATCH, extras);
    }

    private boolean checkNetworkAvailableAndConnected() {
        return mConnectionDetector.isNetworkAvailableAndConnected();
    }
}