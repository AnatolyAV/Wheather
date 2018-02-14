package ru.andreev_av.weather.data.db;

import java.util.List;

import ru.andreev_av.weather.data.model.WeatherCurrentModel;
import ru.andreev_av.weather.domain.model.WeatherCurrent;

public interface IWeatherCurrentDao extends ICommonDAO<WeatherCurrent> {

    void insertOrUpdate(WeatherCurrentModel weatherCurrentModel);

    void insertOrUpdate(List<WeatherCurrentModel> weatherCurrentModels);

    WeatherCurrent getWeatherCurrentByCityId(int cityId);
}
