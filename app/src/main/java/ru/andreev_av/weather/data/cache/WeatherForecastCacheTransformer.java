package ru.andreev_av.weather.data.cache;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import ru.andreev_av.weather.data.db.IWeatherForecastDao;
import ru.andreev_av.weather.data.model.WeatherForecastModel;
import ru.andreev_av.weather.domain.model.WeatherForecast;

public class WeatherForecastCacheTransformer implements ObservableTransformer<WeatherForecastModel, List<WeatherForecast>> {

    private IWeatherForecastDao mWeatherForecastDao;
    private int mCityId;
    private int mCountDays;

    private final Function<WeatherForecastModel, Observable<List<WeatherForecast>>> mSaveFunc = weatherForecastModel -> {
        mWeatherForecastDao.insertOrUpdate(weatherForecastModel);
        List<WeatherForecast> weatherForecasts = mWeatherForecastDao.getWeatherForecastsByCityId(weatherForecastModel.getCity().getId(), mCountDays);
        return Observable.just(weatherForecasts);
    };

    private final Function<Throwable, Observable<List<WeatherForecast>>> mCacheErrorHandler = throwable -> {
        List<WeatherForecast> weatherForecasts = mWeatherForecastDao.getWeatherForecastsByCityId(mCityId, mCountDays);
        return Observable.just(weatherForecasts);
    };

    public WeatherForecastCacheTransformer(IWeatherForecastDao weatherCurrentDao, int cityId, int countDays) {
        mWeatherForecastDao = weatherCurrentDao;
        mCityId = cityId;
        mCountDays = countDays;
    }

    @Override
    public ObservableSource<List<WeatherForecast>> apply(Observable<WeatherForecastModel> weatherForecastObservable) {
        return weatherForecastObservable
                .flatMap(mSaveFunc)
                .onErrorResumeNext(mCacheErrorHandler);
    }
}