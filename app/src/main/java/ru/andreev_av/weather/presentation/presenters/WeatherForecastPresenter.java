package ru.andreev_av.weather.presentation.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import javax.inject.Inject;

import ru.andreev_av.weather.domain.model.WeatherForecast;
import ru.andreev_av.weather.domain.usecase.IWeatherForecastUseCase;
import ru.andreev_av.weather.net.ConnectionDetector;
import ru.andreev_av.weather.presentation.views.IWeatherForecastView;
import rx.functions.Action1;

@InjectViewState
public class WeatherForecastPresenter extends MvpPresenter<IWeatherForecastView> implements IWeatherForecastPresenter {

    private IWeatherForecastUseCase mUseCase;
    private ConnectionDetector mConnectionDetector;
    private int mCityId;
    private int mCountDays;

    @Inject
    public WeatherForecastPresenter(IWeatherForecastUseCase useCase, ConnectionDetector connectionDetector) {
        mUseCase = useCase;
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
    public void loadWeatherForecast(int cityId, int countDays, boolean isRefreshing) {
        if (isRefreshing) {
            getViewState().updateButtonState(true);
        }

        mUseCase.loadWeatherForecast(cityId, countDays)
                .doOnSubscribe(getViewState()::showLoading)
                .doAfterTerminate(getViewState()::hideLoading)
                .subscribe(new Action1<List<WeatherForecast>>() {
                    @Override
                    public void call(List<WeatherForecast> weatherForecasts) {
                        getViewState().showWeatherForecasts(weatherForecasts);
                        if (!checkNetworkAvailableAndConnected()) {
                            getViewState().showNotConnection();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        getViewState()
                                .showErrorWeatherForecasts();
                    }
                });
    }

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int cityId) {
        mCityId = cityId;
    }

    public int getCountDays() {
        return mCountDays;
    }

    public void setCountDays(int countDays) {
        mCountDays = countDays;
    }

    private boolean checkNetworkAvailableAndConnected() {
        return mConnectionDetector.isNetworkAvailableAndConnected();
    }
}