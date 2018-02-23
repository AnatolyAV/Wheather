package ru.andreev_av.weather.domain.usecase;

import java.util.List;

import ru.andreev_av.weather.domain.model.City;
import rx.Observable;

public interface ICitiesUseCase {

    Observable<List<City>> getCitiesByToWatch(boolean isToWatch);

    Observable<List<City>> findCities(String cityNameFirstLetters);

    Observable<Boolean> loadCityToWatch(City city);
}
