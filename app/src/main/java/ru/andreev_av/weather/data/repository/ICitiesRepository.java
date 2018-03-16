package ru.andreev_av.weather.data.repository;

import java.util.List;

import io.reactivex.Observable;
import ru.andreev_av.weather.domain.model.City;

public interface ICitiesRepository {

    Observable<List<City>> getCitiesByToWatch(boolean isToWatch);

    Observable<List<City>> findCities(String cityNameFirstLetters);

    Observable<Boolean> loadCityToWatch(City city);
}
