package ru.andreev_av.weather.data.cache;

import java.util.List;

import ru.andreev_av.weather.data.db.ICityDao;
import ru.andreev_av.weather.domain.model.City;
import rx.Observable;
import rx.functions.Func1;

public class CitiesToWatchCacheTransformer implements Observable.Transformer<List<City>, List<City>> {

    private ICityDao mCityDao;
    private boolean mIsCityToWatch;
    private final Func1<List<City>, Observable<List<City>>> mGetCitiesByToWatchFunc = weatherCurrentModels -> {
        List<City> cities = mCityDao.getCitiesByToWatch(mIsCityToWatch);
        return Observable.just(cities);
    };

    public CitiesToWatchCacheTransformer(ICityDao cityDao, boolean isCityToWatch) {
        mCityDao = cityDao;
        mIsCityToWatch = isCityToWatch;
    }

    @Override
    public Observable<List<City>> call(Observable<List<City>> citiesToWatchObservable) {
        return citiesToWatchObservable
                .flatMap(mGetCitiesByToWatchFunc);
    }
}
