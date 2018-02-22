package ru.andreev_av.weather.data.repository;

import java.util.List;

import ru.andreev_av.weather.domain.model.City;
import rx.Observable;

public interface ICitiesRepository {

    Observable<List<City>> findCities(String cityNameFirstLetters);

    Observable<Boolean> loadCityToWatch(City city);
}
