package ru.andreev_av.weather.data.cache;

import java.util.List;

import ru.andreev_av.weather.data.db.IWeatherCurrentDao;
import ru.andreev_av.weather.data.model.WeatherCurrentModel;
import ru.andreev_av.weather.domain.model.WeatherCurrent;
import rx.Observable;
import rx.functions.Func1;

public class WeatherCurrentsCacheTransformer implements Observable.Transformer<List<WeatherCurrentModel>, List<WeatherCurrent>> {

    private IWeatherCurrentDao mWeatherCurrentDao;
    private final Func1<List<WeatherCurrentModel>, Observable<List<WeatherCurrent>>> mSaveFunc = weatherCurrentModels -> {
        mWeatherCurrentDao.insertOrUpdate(weatherCurrentModels);
        List<WeatherCurrent> weatherCurrents = mWeatherCurrentDao.getAll();
        return Observable.just(weatherCurrents);
    };
    private final Func1<Throwable, Observable<List<WeatherCurrent>>> mCacheErrorHandler = throwable -> {
        List<WeatherCurrent> weatherCurrents = mWeatherCurrentDao.getAll();
        return Observable.just(weatherCurrents);
    };

    public WeatherCurrentsCacheTransformer(IWeatherCurrentDao weatherCurrentDao) {
        mWeatherCurrentDao = weatherCurrentDao;
    }

    @Override
    public Observable<List<WeatherCurrent>> call(Observable<List<WeatherCurrentModel>> weatherCurrentsObservable) {
        return weatherCurrentsObservable
                .flatMap(mSaveFunc)
                .onErrorResumeNext(mCacheErrorHandler);
    }
}
