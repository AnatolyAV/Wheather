package ru.andreev_av.weather.domain.usecase;

import java.util.List;

import ru.andreev_av.weather.data.repository.ICityRepository;
import ru.andreev_av.weather.domain.model.City;
import rx.Observable;

public class CityUseCase implements ICityUseCase {

    private final ICityRepository mRepository;

    public CityUseCase(ICityRepository repository) {
        mRepository = repository;
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
