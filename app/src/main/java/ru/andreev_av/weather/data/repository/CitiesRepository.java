package ru.andreev_av.weather.data.repository;

import java.util.Collections;
import java.util.List;

import ru.andreev_av.weather.data.cache.CitiesToWatchCacheTransformer;
import ru.andreev_av.weather.data.db.ICityDao;
import ru.andreev_av.weather.domain.model.City;
import rx.Observable;

public class CitiesRepository implements ICitiesRepository {

    private ICityDao mCityDao;

    public CitiesRepository(ICityDao cityDao) {
        mCityDao = cityDao;
    }

    @Override
    public Observable<List<City>> getCitiesByToWatch(boolean isToWatch) {
        return Observable.just(Collections.<City>emptyList()).compose(new CitiesToWatchCacheTransformer(mCityDao, isToWatch));
    }

    @Override
    public Observable<List<City>> findCities(String cityNameFirstLetters) {
        List<City> cities = mCityDao.findCities(cityNameFirstLetters);
        return Observable.just(cities);
    }

    @Override
    public Observable<Boolean> loadCityToWatch(City city) {
        boolean updated = mCityDao.updateCityWatched(city);
        return Observable.just(updated);
    }

}
