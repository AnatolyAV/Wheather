package ru.andreev_av.weather.data.db;

import java.util.List;

import ru.andreev_av.weather.data.listeners.IDbTablesCreatedListener;
import ru.andreev_av.weather.domain.model.City;

public interface ICityDao extends IDbTablesCreatedListener {

    List<City> getCitiesByToWatch(boolean isToWatch);

    List<City> findCities(String cityNameFirstLetters);

    boolean updateCityWatched(City city);

}
