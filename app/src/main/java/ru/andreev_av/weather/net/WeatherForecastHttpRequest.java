package ru.andreev_av.weather.net;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import ru.andreev_av.weather.App;
import ru.andreev_av.weather.model.WeatherForecastModel;
import ru.andreev_av.weather.ownapi.OwmApi;
import ru.andreev_av.weather.processors.WeatherForecastProcessor;

public class WeatherForecastHttpRequest {

    private Context context;
    private WeatherForecastProcessor processor;

    public WeatherForecastHttpRequest(Context context, final WeatherForecastProcessor processor) {
        this.context = context;
        this.processor = processor;
    }

    public void getWeatherForecast(int cityId, int countDays) {
        App.getApi().getWeatherForecasts(cityId, countDays, "metric", "ru", OwmApi.API_KEY).enqueue(new Callback<WeatherForecastModel>() {
            @Override
            public void onResponse(Call<WeatherForecastModel> call, retrofit2.Response<WeatherForecastModel> response) {
                processor.runProcessSuccessResponse(response.body());
            }

            @Override
            public void onFailure(Call<WeatherForecastModel> call, Throwable t) {
                processor.runProcessErrorResponse(t);
            }
        });
    }


}
