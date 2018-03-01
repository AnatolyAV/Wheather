package ru.andreev_av.weather.ui.presentation;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import ru.andreev_av.weather.domain.model.WeatherForecast;
import ru.andreev_av.weather.domain.usecase.IWeatherForecastUseCase;
import ru.andreev_av.weather.net.ConnectionDetector;
import rx.functions.Action1;

@InjectViewState
public class WeatherForecastPresenter extends MvpPresenter<IWeatherForecastView> implements IWeatherForecastPresenter {

    private IWeatherForecastUseCase mUseCase;
    private ConnectionDetector mConnectionDetector;
    private int mCityId;
    private int mCountDays;

    public WeatherForecastPresenter() {
    }

    //    @Inject
    public WeatherForecastPresenter(IWeatherForecastUseCase useCase, ConnectionDetector connectionDetector) {
        mUseCase = useCase;
        mConnectionDetector = connectionDetector;
    }

    public IWeatherForecastUseCase getUseCase() {
        return mUseCase;
    }

    public void setUseCase(IWeatherForecastUseCase useCase) {
        mUseCase = useCase;
    }

    public ConnectionDetector getConnectionDetector() {
        return mConnectionDetector;
    }

    public void setConnectionDetector(ConnectionDetector connectionDetector) {
        mConnectionDetector = connectionDetector;
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

    private boolean checkNetworkAvailableAndConnected() {
        return mConnectionDetector.isNetworkAvailableAndConnected();
    }
}