package ru.andreev_av.weather.presentation.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.domain.usecase.IWeatherCurrentUseCase;
import ru.andreev_av.weather.presentation.enums.RefreshingType;
import ru.andreev_av.weather.presentation.views.IWeatherCurrentView;
import ru.andreev_av.weather.utils.ConnectionDetector;
import rx.functions.Action1;

@InjectViewState
public class WeatherCurrentPresenter extends MvpPresenter<IWeatherCurrentView> implements IWeatherCurrentPresenter {

    private final IWeatherCurrentUseCase mWeatherCurrentUseCase;
    private final ConnectionDetector mConnectionDetector;

    @Inject
    public WeatherCurrentPresenter(IWeatherCurrentUseCase weatherCurrentUseCase, ConnectionDetector connectionDetector) {
        mWeatherCurrentUseCase = weatherCurrentUseCase;
        mConnectionDetector = connectionDetector;
    }

    /**
     * Вызывается тогда, когда к конкретному экземпляру Presenter первый раз будет привязана любая View
     */
    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
    }

    @Override
    public void loadWeather(int cityId) {
        loadWeather(cityId, RefreshingType.STANDARD);
    }

    @Override
    public void loadWeather(int cityId, RefreshingType refreshingType) {
        mWeatherCurrentUseCase.loadWeather(cityId)
                .doOnSubscribe(() -> showProgress(refreshingType))
                .doAfterTerminate(() -> hideProgress(refreshingType))
                .subscribe(new Action1<WeatherCurrent>() {
                    @Override
                    public void call(WeatherCurrent weatherCurrent) {
                        getViewState().showWeatherCurrent(weatherCurrent);
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

    // TODO подумать над разделением на два презентора
    @Override
    public void loadWeather(ArrayList<Integer> cityIds) {
        loadWeather(cityIds, RefreshingType.STANDARD);
    }

    @Override
    public void loadWeather(ArrayList<Integer> cityIds, RefreshingType refreshingType) {
        mWeatherCurrentUseCase.loadWeather(cityIds)
                .doOnSubscribe(() -> showProgress(refreshingType))
                .doAfterTerminate(() -> hideProgress(refreshingType))
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

    private void showProgress(RefreshingType refreshingType) {
        switch (refreshingType) {
            case STANDARD:
                getViewState().showLoading();
                break;
            case UPDATE_BUTTON:
                getViewState().showButtonRefreshing();
                break;
            case SWIPE:
                getViewState().showSwipeRefreshing();
                break;
        }
    }

    private void hideProgress(RefreshingType refreshingType) {
        switch (refreshingType) {
            case STANDARD:
                getViewState().hideLoading();
                break;
            case UPDATE_BUTTON:
                getViewState().hideButtonRefreshing();
                break;
            case SWIPE:
                getViewState().hideSwipeRefreshing();
                break;
        }
    }

    private boolean checkNetworkAvailableAndConnected() {
        return mConnectionDetector.isNetworkAvailableAndConnected();
    }
}
