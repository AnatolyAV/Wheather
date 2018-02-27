package ru.andreev_av.weather.data.repository;

import java.util.List;
import java.util.concurrent.Callable;

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
        return Observable.fromCallable(new Callable<List<City>>() {

            @Override
            public List<City> call() throws Exception {
                return mCityDao.getCitiesByToWatch(isToWatch);
            }
        });
    }

    @Override
    public Observable<List<City>> findCities(String cityNameFirstLetters) {
        List<City> cities = mCityDao.findCities(cityNameFirstLetters);
        return Observable.just(cities);
    }

    @Override
    public Observable<Boolean> loadCityToWatch(City city) {
        return Observable.fromCallable(new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return mCityDao.updateCityWatched(city);
            }
        });
    }

}
