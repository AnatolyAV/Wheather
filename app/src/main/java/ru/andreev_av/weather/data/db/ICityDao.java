package ru.andreev_av.weather.data.db;

import java.util.List;

import ru.andreev_av.weather.domain.model.City;

public interface ICityDao {

    List<City> findCities(String cityNameFirstLetters);

    boolean updateCityWatched(City city);

}
