package ru.andreev_av.weather.presentation.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import ru.andreev_av.weather.domain.usecase.IWeatherCurrentUseCase;
import ru.andreev_av.weather.presentation.enums.RefreshingType;
import ru.andreev_av.weather.presentation.views.IWeatherCurrentsView;
import ru.andreev_av.weather.utils.ConnectionDetector;

@InjectViewState
public class WeatherCurrentsPresenter extends MvpPresenter<IWeatherCurrentsView> implements IWeatherCurrentsPresenter {

    private final IWeatherCurrentUseCase mWeatherCurrentUseCase;
    private final ConnectionDetector mConnectionDetector;

    @Inject
    public WeatherCurrentsPresenter(IWeatherCurrentUseCase weatherCurrentUseCase, ConnectionDetector connectionDetector) {
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
    public void loadWeather(ArrayList<Integer> cityIds) {
        loadWeather(cityIds, RefreshingType.STANDARD);
    }

    @Override
    public void loadWeather(ArrayList<Integer> cityIds, RefreshingType refreshingType) {
        mWeatherCurrentUseCase.loadWeather(cityIds)
                .doOnSubscribe(disposable -> showProgress(refreshingType))
                .doAfterTerminate(() -> hideProgress(refreshingType))
                .subscribe(new Consumer<List<WeatherCurrent>>() {
                    @Override
                    public void accept(List<WeatherCurrent> weatherCurrents) {
                        getViewState().showWeatherCurrents(weatherCurrents);
                        if (!checkNetworkAvailableAndConnected()) {
                            getViewState().showNotConnection();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
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
        }
    }

    private boolean checkNetworkAvailableAndConnected() {
        return mConnectionDetector.isNetworkAvailableAndConnected();
    }
}
