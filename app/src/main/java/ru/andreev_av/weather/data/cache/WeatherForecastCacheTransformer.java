package ru.andreev_av.weather.data.cache;

import java.util.List;

import ru.andreev_av.weather.data.db.IWeatherForecastDao;
import ru.andreev_av.weather.data.model.WeatherForecastModel;
import ru.andreev_av.weather.domain.model.WeatherForecast;
import rx.Observable;
import rx.functions.Func1;

public class WeatherForecastCacheTransformer implements Observable.Transformer<WeatherForecastModel, List<WeatherForecast>> {

    private IWeatherForecastDao mWeatherForecastDao;
    private int mCityId;
    private int mCountDays;
    private final Func1<WeatherForecastModel, Observable<List<WeatherForecast>>> mSaveFunc = weatherForecastModel -> {
        mWeatherForecastDao.insertOrUpdate(weatherForecastModel);
        List<WeatherForecast> weatherForecasts = mWeatherForecastDao.getWeatherForecastsByCityId(weatherForecastModel.getCity().getId(), mCountDays);
        return Observable.just(weatherForecasts);
    };
    private final Func1<Throwable, Observable<List<WeatherForecast>>> mCacheErrorHandler = throwable -> {
        List<WeatherForecast> weatherForecasts = mWeatherForecastDao.getWeatherForecastsByCityId(mCityId, mCountDays);
        return Observable.just(weatherForecasts);
    };

    public WeatherForecastCacheTransformer(IWeatherForecastDao weatherCurrentDao, int cityId, int countDays) {
        mWeatherForecastDao = weatherCurrentDao;
        mCityId = cityId;
        mCountDays = countDays;
    }

    @Override
    public Observable<List<WeatherForecast>> call(Observable<WeatherForecastModel> weatherForecastObservable) {
        return weatherForecastObservable
                .flatMap(mSaveFunc)
                .onErrorResumeNext(mCacheErrorHandler);
    }
}