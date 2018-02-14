package ru.andreev_av.weather.data.network;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.andreev_av.weather.data.model.WeatherCurrentListModel;
import ru.andreev_av.weather.data.model.WeatherCurrentModel;
import ru.andreev_av.weather.data.model.WeatherForecastModel;
import rx.Observable;

public interface OwmService {

    String API_KEY = "9162388bd4e78a8f4b5748cc11597c24";
    String BASE_URL = "http://api.openweathermap.org/";

    // TODO вынести в настройки units,lang
    @GET("/data/2.5/group")
    Observable<WeatherCurrentListModel> getWeatherCurrent(@Query("id") String cityIds, @Query("units") String units, @Query("lang") String lang, @Query("appid") String apiKey);

    @GET("/data/2.5/weather")
    Observable<WeatherCurrentModel> getWeatherCurrent(@Query("id") int cityId, @Query("units") String units, @Query("lang") String lang, @Query("appid") String apiKey);

    @GET("/data/2.5/forecast/daily")
    Observable<WeatherForecastModel> getWeatherForecasts(@Query("id") int cityId, @Query("cnt") int count, @Query("units") String units, @Query("lang") String lang, @Query("appid") String apiKey);
}
