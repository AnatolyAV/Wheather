package ru.andreev_av.weather;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.andreev_av.weather.ownapi.OwmApi;

public class App extends Application {

    private static OwmApi owmApi;
    private Retrofit retrofit;

    public static OwmApi getApi() {
        return owmApi;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(OwmApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        owmApi = retrofit.create(OwmApi.class);
    }
}
