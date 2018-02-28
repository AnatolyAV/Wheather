package ru.andreev_av.weather.ui.presentation;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.domain.usecase.IWeatherCurrentUseCase;
import ru.andreev_av.weather.net.ConnectionDetector;
import rx.functions.Action1;

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
        mWeatherCurrentUseCase.loadWeather(cityId)
                .doOnSubscribe(getViewState()::showLoading)
                .doAfterTerminate(getViewState()::hideLoading)
                .subscribe(new Action1<List<WeatherCurrent>>() {
                    @Override
                    public void call(List<WeatherCurrent> weatherCurrents) {
                        getViewState().showWeatherCurrent(weatherCurrents);
                        if (!checkNetworkAvailableAndConnected()) {
                            getViewState().showNotConnection();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getViewState()
                                .showErrorWeatherCurrent();
                    }
                });
    }

    @Override
    public void loadWeather(ArrayList<Integer> cityIds, boolean isRefreshing) {
        if (isRefreshing) {
            getViewState().updateButtonState(true);
        }

        mWeatherCurrentUseCase.loadWeather(cityIds)
                .doOnSubscribe(getViewState()::showLoading)
                .doAfterTerminate(getViewState()::hideLoading)
                .subscribe(new Action1<List<WeatherCurrent>>() {
                    @Override
                    public void call(List<WeatherCurrent> weatherCurrents) {
                        getViewState().showWeatherCurrents(weatherCurrents);
                        if (!checkNetworkAvailableAndConnected()) {
                            getViewState().showNotConnection();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getViewState()
                                .showErrorWeatherCurrents();
                    }
                });
    }

    private boolean checkNetworkAvailableAndConnected() {
        return mConnectionDetector.isNetworkAvailableAndConnected();
    }
}
