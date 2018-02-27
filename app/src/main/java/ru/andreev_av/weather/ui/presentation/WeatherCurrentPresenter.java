package ru.andreev_av.weather.ui.presentation;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;

import javax.inject.Inject;

import ru.andreev_av.weather.domain.usecase.IWeatherCurrentUseCase;
import ru.andreev_av.weather.net.ConnectionDetector;

@InjectViewState
public class WeatherCurrentPresenter extends MvpPresenter<IWeatherCurrentView> implements IWeatherCurrentPresenter {

    private final IWeatherCurrentUseCase mWeatherCurrentUseCase;
    private final ConnectionDetector mConnectionDetector;
    private ArrayList<Integer> mCityIds;

    @Inject
    public WeatherCurrentPresenter(IWeatherCurrentUseCase weatherCurrentUseCase, ConnectionDetector connectionDetector) {
        mWeatherCurrentUseCase = weatherCurrentUseCase;
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
            mWeatherCurrentUseCase.loadWeather(cityId)
                    .doOnSubscribe(getViewState()::showLoading)
                    .doAfterTerminate(getViewState()::hideLoading)
                    .subscribe(getViewState()::showWeatherCurrent, throwable -> getViewState()
                            .showErrorWeatherCurrent());
        } else {
            getViewState().showNotConnection();
        }
    }

    @Override
    public void loadWeather(ArrayList<Integer> cityIds, boolean isRefreshing) {
        if (isRefreshing) {
            getViewState().updateButtonState(true);
        }

        if (checkNetworkAvailableAndConnected()) {
            mWeatherCurrentUseCase.loadWeather(cityIds)
                    .doOnSubscribe(getViewState()::showLoading)
                    .doAfterTerminate(getViewState()::hideLoading)
                    .subscribe(getViewState()::showWeatherCurrents, throwable -> getViewState().showErrorWeatherCurrents());
        } else {
            getViewState().showNotConnection();
        }
    }

    private boolean checkNetworkAvailableAndConnected() {
        return mConnectionDetector.isNetworkAvailableAndConnected();
    }
}
