package ru.andreev_av.weather.data.cache;

import ru.andreev_av.weather.data.db.IWeatherCurrentDao;
import ru.andreev_av.weather.data.model.WeatherCurrentModel;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import rx.Observable;
import rx.functions.Func1;

public class WeatherCurrentCacheTransformer implements Observable.Transformer<WeatherCurrentModel, WeatherCurrent> {

    private IWeatherCurrentDao mWeatherCurrentDao;
    private final Func1<WeatherCurrentModel, Observable<WeatherCurrent>> mSaveFunc = weatherCurrentModel -> {
        mWeatherCurrentDao.insertOrUpdate(weatherCurrentModel);
        WeatherCurrent weatherCurrent = mWeatherCurrentDao.getWeatherCurrentByCityId(weatherCurrentModel.getCityId());
        return Observable.just(weatherCurrent);
    };
    private int mCityId;
    private final Func1<Throwable, Observable<WeatherCurrent>> mCacheErrorHandler = throwable -> {
        WeatherCurrent weatherCurrent = mWeatherCurrentDao.getWeatherCurrentByCityId(mCityId);
        return Observable.just(weatherCurrent);
    };

    public WeatherCurrentCacheTransformer(IWeatherCurrentDao weatherCurrentDao, int cityId) {
        mWeatherCurrentDao = weatherCurrentDao;
        mCityId = cityId;
    }

    @Override
    public Observable<WeatherCurrent> call(Observable<WeatherCurrentModel> weatherCurrentObservable) {
        return weatherCurrentObservable
                .flatMap(mSaveFunc)
                .onErrorResumeNext(mCacheErrorHandler);
    }
}
