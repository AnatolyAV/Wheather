package ru.andreev_av.weather.domain.usecase;

import java.util.List;

import ru.andreev_av.weather.data.repository.ICitiesRepository;
import ru.andreev_av.weather.domain.model.City;
import rx.Observable;

public class CitiesUseCase implements ICitiesUseCase {

    private final ICitiesRepository mRepository;

    public CitiesUseCase(ICitiesRepository repository) {
        mRepository = repository;
    }

    @Override
    public Observable<List<City>> getCitiesByToWatch(boolean isToWatch) {
        return mRepository.getCitiesByToWatch(isToWatch);
    }

    @Override
    public Observable<List<City>> findCities(String cityNameFirstLetters) {
        return mRepository.findCities(cityNameFirstLetters);
    }

    @Override
    public Observable<Boolean> loadCityToWatch(City city) {
        return mRepository.loadCityToWatch(city);
    }
}
