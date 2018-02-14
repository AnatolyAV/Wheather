package ru.andreev_av.weather.net;

import android.content.Context;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.andreev_av.weather.App;
import ru.andreev_av.weather.data.model.WeatherCurrentListModel;
import ru.andreev_av.weather.data.model.WeatherCurrentModel;
import ru.andreev_av.weather.ownapi.OwmApi;
import ru.andreev_av.weather.processors.WeatherCurrentProcessor;
import ru.andreev_av.weather.utils.StringUtils;

public class WeatherCurrentHttpRequest {

    private Context context;
    private WeatherCurrentProcessor processor;

    public WeatherCurrentHttpRequest(Context context, final WeatherCurrentProcessor processor) {
        this.context = context;
        this.processor = processor;
    }

    public void getWeatherCurrent(int cityId) {
        App.getApi().getWeatherCurrent(cityId, "metric", Locale.getDefault().getLanguage(), OwmApi.API_KEY).enqueue(new Callback<WeatherCurrentModel>() {
            @Override
            public void onResponse(Call<WeatherCurrentModel> call, Response<WeatherCurrentModel> response) {
                processor.runProcessSuccessResponse(response.body());
            }

            @Override
            public void onFailure(Call<WeatherCurrentModel> call, Throwable t) {
                processor.runProcessErrorResponse(t);
            }
        });
    }

    public void getWeatherCurrent(ArrayList<Integer> cityIds) {

        App.getApi().getWeatherCurrent(StringUtils.joinIds(cityIds), "metric", Locale.getDefault().getLanguage(), OwmApi.API_KEY).enqueue(new Callback<WeatherCurrentListModel>() {
            @Override
            public void onResponse(Call<WeatherCurrentListModel> call, Response<WeatherCurrentListModel> response) {
                processor.runProcessSuccessResponse(response.body());
            }

            @Override
            public void onFailure(Call<WeatherCurrentListModel> call, Throwable t) {
                processor.runProcessErrorResponse(t);
            }
        });
    }

}
